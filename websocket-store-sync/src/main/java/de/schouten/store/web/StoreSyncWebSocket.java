package de.schouten.store.web;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.jetty.websocket.WebSocket;
import de.schouten.store.DomainKey;
import de.schouten.store.ElementKey;
import de.schouten.store.ElementListener;
import de.schouten.store.StoreElement;
import de.schouten.store.web.jmx.WebSocketManagement;

/**
 * Syncs the store with the client. Receives all actions.
 */
public class StoreSyncWebSocket<S extends StoreSyncWebSocketSession> implements WebSocket.OnTextMessage, ElementListener {

    /**
     * The connection to send messages with.
     */
    private Connection connection;

    /**
     * The session for this connection.
     */
    private S session;

    /**
     * The management bean for this web socket.
     */
    private WebSocketManagement wsManagement;

    /**
     * To access context information.
     */
    private StoreSyncContext<S> context;

    /**
     * The manager for subscriptions.
     */
    private Subscriptions subscriptions;

    /**
     * This thread sends the messages to the client.
     */
    private MessageSenderThread messageSenderThread;

    /**
     * Counts up the started threads.
     */
    private static final AtomicLong THREAD_COUNTER = new AtomicLong();

    /**
     * New websocket with management integration.
     * 
     * @param context to access context information.
     */
    public StoreSyncWebSocket(StoreSyncContext<S> context) {
        this.context = context;
        this.subscriptions = new Subscriptions(context.getStore(), this);
        this.messageSenderThread = new MessageSenderThread(THREAD_COUNTER.incrementAndGet());
    }

    /**
     * The session is set during creation of the web socket.
     * 
     * @param session the session to set.
     */
    public void setSession(S session) {
        if (this.session != null) {
            throw new IllegalStateException("Session was already set.");
        }
        this.session = session;
    }

    /**
     * @return the connection
     */
    Connection getConnection() {
        return connection;
    }

    @Override
    public void onClose(int code, String message) {
        wsManagement.unregister();
        context.getStore().removeElementListener(this);
        messageSenderThread.interrupt(); // stops the sender
    }

    @Override
    public void onOpen(Connection conn) {
        wsManagement = new WebSocketManagement(context.getJmxDomain(), conn, session);
        this.connection = conn;
    }

    @Override
    public void onMessage(String message) {
        wsManagement.messageReceived(message);
        try {
            int idx = message.indexOf(' ');
            String actionName = message.substring(0, idx);
            String json = message.substring(idx + 1);
            Action<S> action = context.createAction(actionName, json);
            if (action != null) {
                try {
                    action.perform(context, subscriptions, session);
                } catch (Exception e) { // exception during perform
                    context.reportErrorPerformingTheAction(session, action, e);
                }
            } else { // no action found for message
                context.reportActionNotFound(session, message);
            }
        } catch (Exception e) { // general error creating the action
            context.reportErrorCreatingTheAction(session, message, e);
        }
    }

    /**
     * @see de.schouten.store.ElementListener#elementUpdated(de.schouten.store.DomainKey, de.schouten.store.ElementKey, de.schouten.store.StoreElement)
     */
    @Override
    public void elementUpdated(DomainKey domainKey, ElementKey elementKey, StoreElement storeElement) {
        Message message = new Message.UpdateMessage(domainKey, elementKey, storeElement);
        send(message);
    }

    /**
     * @see de.schouten.store.ElementListener#elementRemoved(de.schouten.store.DomainKey, de.schouten.store.ElementKey, de.schouten.store.StoreElement)
     */
    @Override
    public void elementRemoved(DomainKey domainKey, ElementKey elementKey, StoreElement storeElement) {
        Message message = new Message.RemoveMessage(domainKey, elementKey);
        send(message);
    }

    /**
     * Sends the message to the client.
     * 
     * @param message the message to send.
     */
    void send(Message message) {
        messageSenderThread.offer(message);
    }

    /**
     * @see de.schouten.store.ElementListener#exceptionDuringCallback(de.schouten.store.DomainKey, de.schouten.store.ElementKey, java.lang.Exception)
     */
    @Override
    public void exceptionDuringCallback(DomainKey domainKey, ElementKey elementKey, Exception e) {
        context.reportGeneralError(session, e);
    }

    /**
     * Sends the messages asynchronous to the client.
     */
    private final class MessageSenderThread extends Thread {

        /**
         * A queue to send the messages asynchronous.
         */
        private BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message>();

        /**
         * Constructs the new sender thread.
         */
        private MessageSenderThread(long number) {
            super(String.format("MessageSenderThread-%s", number));
            this.setDaemon(true);
            this.start();
        }

        /**
         * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object)
         */
        public boolean offer(Message e) {
            return messageQueue.offer(e);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Message message = messageQueue.take();
                    try {
                        String json = message.toJson();
                        wsManagement.messageSend(json);
                        connection.sendMessage(json);
                    } catch (IOException e) {
                        context.reportGeneralError(session, e);
                    }
                }
            } catch (InterruptedException e) {
                // thats all - end the queue
            }
        }
    }
}
