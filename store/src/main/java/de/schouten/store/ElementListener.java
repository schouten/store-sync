package de.schouten.store;

/**
 * Callback methods to inform about adding and removing elements to and from the store.
 */
public interface ElementListener {

    /**
     * An element was updated or added.
     * 
     * @param domainKey the key for the domain the element was updated.
     * @param elementKey the key of the updated element.
     * @param storeElement the added element.
     */
    void elementUpdated(DomainKey domainKey, ElementKey elementKey, StoreElement storeElement);

    /**
     * An element was removed.
     * 
     * @param domainKey the key for the domain the element was removed.
     * @param elementKey the key of the removed element.
     * @param storeElement the removed element.
     */
    void elementRemoved(DomainKey domainKey, ElementKey elementKey, StoreElement storeElement);

    /**
     * In your {@link #elementAdded(DomainKey, ElementKey, StoreElement)} or {@link #elementRemoved(DomainKey, ElementKey, StoreElement)} implementation has
     * been an exception. Please check your code!
     * 
     * @param domainKey the domain key the exception occurred.
     * @param elementKey the element tkey the exception occurred.
     * @param e the exception that was thrown.
     */
    void exceptionDuringCallback(DomainKey domainKey, ElementKey elementKey, Exception e);
}
