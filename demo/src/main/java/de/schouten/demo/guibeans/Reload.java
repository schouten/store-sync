package de.schouten.demo.guibeans;

import de.schouten.store.DomainKey;
import de.schouten.store.SelfDestroyingStoreElement;
import de.schouten.store.Store;

/**
 * Reloads the client on server command.
 */
public class Reload extends SelfDestroyingStoreElement {

    /**
     * Needed for cloning.
     * 
     * @param store the store to store the alert box in.
     * @param domainKey the domain key for this alert box.
     * @param cloning true for cloning.
     */
    private Reload(Store store, DomainKey domainKey, boolean cloning) {
        super(store, domainKey);
        if (!cloning) {
            this.addToStore(1L);
        }
    }

    /**
     * Creates a new reloader.
     * 
     * @param store the store to store the alert box in.
     * @param domainKey the domain key for this alert box.
     */
    public Reload(Store store, DomainKey domainKey) {
        this(store, domainKey, false);
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
    public Reload clone() {
        return new Reload(store, domainKey, true);
    }

}
