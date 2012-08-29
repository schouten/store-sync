package de.schouten.demo.web.actions;

import de.schouten.demo.web.DemoSession;
import de.schouten.store.DomainKey;
import de.schouten.store.web.Action;
import de.schouten.store.web.StoreSyncContext;
import de.schouten.store.web.Subscriptions;

/**
 * Login
 */
public class LoginAction implements Action<DemoSession> {

    /**
     * The user to login.
     */
    private String user;

    /**
     * @see de.schouten.store.web.Action#perform(StoreSyncContext, Subscriptions, de.schouten.store.web.StoreSyncWebSocketSession)
     */
    @Override
    public void perform(StoreSyncContext<DemoSession> context, Subscriptions subscriptions, DemoSession session) throws Exception {
        boolean loggedIn = false;
        if ("TOM_AP1".equals(user)) {
            subscriptions.subscribe(new DomainKey("root"));
            subscriptions.subscribe(new DomainKey("root", "tom"));
            subscriptions.subscribe(new DomainKey("root", "tom", "TOM_AP1"));
            session.setMandator("tom");
            loggedIn = true;
        } else if ("TOM_AP2".equals(user)) {
            subscriptions.subscribe(new DomainKey("root"));
            subscriptions.subscribe(new DomainKey("root", "tom"));
            subscriptions.subscribe(new DomainKey("root", "tom", "TOM_AP2"));
            session.setMandator("tom");
            loggedIn = true;
        } else if ("JERRY_AP1".equals(user)) {
            subscriptions.subscribe(new DomainKey("root"));
            subscriptions.subscribe(new DomainKey("root", "jerry"));
            subscriptions.subscribe(new DomainKey("root", "jerry", "JERRY_AP1"));
            session.setMandator("jerry");
            loggedIn = true;
        }
        if (loggedIn) {
            session.setUser(user);
            session.setClientStatusInitialized();
        } else {
            session.setClientStatusUninitialized(String.format("User '%s' not found.", user));
        }
    }
}
