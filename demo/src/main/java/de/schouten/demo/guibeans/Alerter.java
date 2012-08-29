package de.schouten.demo.guibeans;

import de.schouten.store.DomainKey;
import de.schouten.store.SelfDestroyingStoreElement;
import de.schouten.store.Store;

/**
 * Displays an alert box on the client.
 */
public class Alerter extends SelfDestroyingStoreElement {

    /**
     * The message to display.
     */
    private String alertMessage;

    /**
     * Needed for cloning.
     * 
     * @param store the store to store the alert box in.
     * @param domainKey the domain key for this alert box.
     * @param alertMessage the message to display.
     * @param cloning true for cloning.
     */
    private Alerter(Store store, DomainKey domainKey, String alertMessage, boolean cloning) {
        super(store, domainKey);
        this.alertMessage = alertMessage;
        if (!cloning) {
            this.addToStore(1000L);
        }
    }

    /**
     * Creates a new alert box.
     * 
     * @param store the store to store the alert box in.
     * @param domainKey the domain key for this alert box.
     * @param alertMessage the message to display.
     */
    public Alerter(Store store, DomainKey domainKey, String alertMessage) {
        this(store, domainKey, alertMessage, false);
    }

    /**
     * @see de.schouten.store.StoreElement#getElementKeyParts()
     */
    @Override
    public Object[] getElementKeyParts() {
        return new Object[] {"SINGLETON"};
    }

    /**
     * @see de.schouten.store.SelfDestroyingStoreElement#clone()
     */
    @Override
    public Alerter clone() {
        return new Alerter(store, domainKey, alertMessage, true);
    }

}
