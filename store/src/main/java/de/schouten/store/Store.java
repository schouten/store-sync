package de.schouten.store;

import java.util.Set;

/**
 * A store contains domains in which elements can be stored under element keys. Listeners can be registered to get change notifications.
 */
public interface Store {

    /**
     * Clears the complete store.
     */
    void clear();

    /**
     * @param domainKey the domain to clear.
     */
    void clear(DomainKey domainKey);

    /**
     * @param domainKey the domain to get the key set for.
     * @return all {@link ElementKey}s in a set.
     */
    Set<ElementKey> keySet(DomainKey domainKey);

    /**
     * @return all {@link DomainKey}s in a set.
     */
    Set<DomainKey> keySet();

    /**
     * @param domainKey the domain to get the size for.
     * @return the amount of the elements in the given domain.
     */
    int size(DomainKey domainKey);

    /**
     * @return the amount of all elements in the store.
     */
    int size();

    /**
     * Removes the element from the store.
     * 
     * @param domainKey the domain of the element to remove from store.
     * @param elementKey the key of the element to remove from store.
     */
    void remove(DomainKey domainKey, ElementKey elementKey);

    /**
     * Removes the element from the store.
     * 
     * @param domainKey the domain of the element to remove from store.
     * @param element the element to remove from store.
     */
    void remove(DomainKey domainKey, StoreElement element);

    /**
     * Gets the store element.
     * 
     * @param <T>
     * 
     * @param domainKey the domain of the element to get from store.
     * @param elementKey the key of the element to get from store.
     * @return the element for the key, or null if not present.
     */
    StoreElement get(DomainKey domainKey, ElementKey elementKey);

    /**
     * Puts the element in the store.
     * 
     * @param domainKey the domain of the element to put in store.
     * @param element the element to put in store.
     */
    void put(DomainKey domainKey, StoreElement element);

    /**
     * @param domainKey the key to the domain to test.
     * @return true, if this store contains the domain. The domain is only present, if there is at least one element in that domain.
     */
    boolean contains(DomainKey domainKey);

    /**
     * Adds a domain listener to the store.
     * 
     * @param domainListener this listener should be informed about domain changes.
     */
    void addDomainListener(DomainListener domainListener);

    /**
     * Removes a domain listener from the store.
     * 
     * @param domainListener this listener should be removed.
     */
    void removeDomainListener(DomainListener domainListener);

    /**
     * Adds a element listener to the store.
     * 
     * @param elementListener this listener should be informed about element changes.
     * @param domainKeys the listener should be informed about element changes in the given domain.
     */
    void addElementListener(ElementListener elementListener, DomainKey... domainKeys);

    /**
     * Adds a element listener to the store.
     * 
     * @param resendAll set to true, if all stored elements should be resend.
     * @param elementListener this listener should be informed about element changes.
     * @param domainKeys the listener should be informed about element changes in the given domain.
     */
    void addElementListener(boolean resendAll, ElementListener elementListener, DomainKey... domainKeys);

    /**
     * Removes an element listener from the store.
     * 
     * @param elementListener this listener should be removed.
     */
    void removeElementListener(ElementListener elementListener);

}
