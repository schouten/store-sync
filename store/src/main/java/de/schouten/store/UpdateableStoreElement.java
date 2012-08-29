package de.schouten.store;

/**
 * This {@link StoreElement} can update itself in the store.
 */
public abstract class UpdateableStoreElement implements StoreElement {

    /**
     * The {@link Store} to store this element in. All update requests will be send to this store.
     */
    protected transient final Store store;

    /**
     * The key to the domain to store the element for.
     */
    protected transient final DomainKey domainKey;

    /**
     * Constructs a new {@link StoreElement} that can update itself in the given {@link Store}.
     * 
     * @param store the {@link Store} to send the update requests to.
     * @param domainKey the key to the domain to store the element for.
     */
    public UpdateableStoreElement(Store store, DomainKey domainKey) {
        super();
        this.store = store;
        this.domainKey = domainKey;
    }

    /**
     * Now put or update this {@link StoreElement} into the {@link Store}.
     */
    public void update() {
        store.put(domainKey, this);
    }

    @Override
    public abstract UpdateableStoreElement clone();

}
