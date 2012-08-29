package de.schouten.demo.web.actions;

import de.schouten.demo.guibeans.Message;
import de.schouten.demo.web.DemoSession;
import de.schouten.demo.web.MessageManager;
import de.schouten.store.DomainKey;
import de.schouten.store.web.Form;
import de.schouten.store.web.StoreSyncContext;
import de.schouten.store.web.Subscriptions;

/**
 * This form represents the message data that can be send on submit.
 */
public class SendMessageForm extends Form<DemoSession> {

    /**
     * The message text to send.
     */
    private String message;

    /**
     * The urgence of the message.
     */
    private boolean urgent;

    /**
     * Clone constructor.
     * 
     * @param message The message text to send.
     * @param urgent The urgence of the message.
     */
    private SendMessageForm(String message, boolean urgent) {
        super();
        this.message = message;
        this.urgent = urgent;
    }

    @Override
    public Object[] getElementKeyParts() {
        return new Object[] {"SINGLETON"};
    }

    /**
     * @see de.schouten.store.web.Form#getDomainKey(de.schouten.store.web.StoreSyncContext, de.schouten.store.web.StoreSyncWebSocketSession)
     */
    @Override
    public DomainKey getDomainKey(StoreSyncContext<DemoSession> context, DemoSession session) {
        return new DomainKey("root", session.getMandator(), session.getUser());
    }

    @Override
    public Form<DemoSession> clone() {
        return new SendMessageForm(message, urgent);
    }

    @Override
    public void submit(StoreSyncContext<DemoSession> context, Subscriptions subscriptions, DemoSession session) throws Exception {
        MessageManager messageManager = MessageManager.getManager(session.getMandator());
        messageManager.addMessage(new Message(System.currentTimeMillis(), message, urgent));
    }

    /**
     * @see de.schouten.store.web.Form#reset(de.schouten.store.web.StoreSyncContext, de.schouten.store.web.Subscriptions,
     *      de.schouten.store.web.StoreSyncWebSocketSession)
     */
    @Override
    public void reset(StoreSyncContext<DemoSession> context, Subscriptions subscriptions, DemoSession session) throws Exception {
        message = "";
        urgent = false;
    }

}
