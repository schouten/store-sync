package de.schouten.store.web;

import de.schouten.store.DomainKey;
import de.schouten.store.ElementListener;
import de.schouten.store.Store;

/**
 * Manages {@link Store} subscriptions.
 */
public class Subscriptions {

    /**
     * The store to manage the subscriptions for.
     */
    private Store store;

    /**
     * The element listener to manage subscriptions for.
     */
    private ElementListener elementListener;

    /**
     * @param store The store to manage the subscriptions for.
     * @param elementListener The element listener to manage subscriptions for.
     */
    Subscriptions(Store store, ElementListener elementListener) {
        super();
        this.store = store;
        this.elementListener = elementListener;
    }

    /**
     * Adds subscriptions for the given keys.
     * 
     * @param domainKeys the keys to subscribe.
     */
    public void subscribe(DomainKey... domainKeys) {
        store.addElementListener(true, elementListener, domainKeys);
    }
}
