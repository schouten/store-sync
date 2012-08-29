package de.schouten.store.web;

import java.io.IOException;
import org.eclipse.jetty.websocket.WebSocket;
import de.schouten.store.web.jmx.WebSocketManagement;

/**
 * The echo socket is for testing purposes. It simply echos the submitted messages.
 */
public class EchoWebSocket<S extends StoreSyncWebSocketSession> implements WebSocket.OnTextMessage {

    /**
     * The connection to send messages with.
     */
    private Connection connection;

    /**
     * The management bean for this web socket.
     */
    private WebSocketManagement wsManagement;

    /**
     * The domain all management jmx beans should reside in. e.g. org.example.
     */
    private String jmxDomain;

    /**
     * @param context access context information.
     */
    public EchoWebSocket(StoreSyncContext<S> context) {
        super();
        this.jmxDomain = context.getJmxDomain();
    }

    @Override
    public void onClose(int code, String message) {
        wsManagement.unregister();
    }

    @Override
    public void onOpen(Connection conn) {
        this.connection = conn;
        wsManagement = new WebSocketManagement(jmxDomain, conn, null);
    }

    @Override
    public void onMessage(String message) {
        // simply reply the message
        try {
            wsManagement.messageReceived(message);
            connection.sendMessage(message);
            wsManagement.messageSend(message);
        } catch (IOException e) {
            throw new RuntimeException("Unchecked exception occurs: " + e.toString(), e);
        }
    }
}
