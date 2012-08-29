package de.schouten.demo.web.actions;

import de.schouten.demo.guibeans.Message;
import de.schouten.demo.guibeans.PhoneNumberDisplay;
import de.schouten.demo.web.DemoSession;
import de.schouten.demo.web.MessageManager;
import de.schouten.store.DomainKey;
import de.schouten.store.web.Action;
import de.schouten.store.web.StoreSyncContext;
import de.schouten.store.web.Subscriptions;

/**
 * Dial
 */
public class DialAction implements Action<DemoSession> {

    /**
     * @see de.schouten.store.web.Action#perform(StoreSyncContext, Subscriptions, de.schouten.store.web.StoreSyncWebSocketSession)
     */
    @Override
    public void perform(StoreSyncContext<DemoSession> context, Subscriptions subscriptions, DemoSession session) throws Exception {
        DomainKey domainKey = new DomainKey("root", session.getMandator(), session.getUser());
        PhoneNumberDisplay display = PhoneNumberDisplay.getOrCreate(context.getStore(), domainKey);
        MessageManager messageManager = MessageManager.getManager(session.getMandator());
        messageManager.addMessage(new Message(System.currentTimeMillis(), String.format("Nummer %s gewählt.", display), false));
    }
}
