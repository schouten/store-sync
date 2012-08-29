package de.schouten.demo.web.actions;

import de.schouten.demo.guibeans.MainTab;
import de.schouten.demo.web.DemoSession;
import de.schouten.store.DomainKey;
import de.schouten.store.web.Action;
import de.schouten.store.web.StoreSyncContext;
import de.schouten.store.web.Subscriptions;

/**
 * SwicthTab
 */
public class SwitchTabAction implements Action<DemoSession> {

    /**
     * Switch to this tab.
     */
    private String tab;

    /**
     * @see de.schouten.store.web.Action#perform(StoreSyncContext, Subscriptions, de.schouten.store.web.StoreSyncWebSocketSession)
     */
    @Override
    public void perform(StoreSyncContext<DemoSession> context, Subscriptions subscriptions, DemoSession session) throws Exception {
        MainTab.getOrCreate(context.getStore(), new DomainKey("root", session.getMandator(), session.getUser())).setTab(tab);
    }
}
