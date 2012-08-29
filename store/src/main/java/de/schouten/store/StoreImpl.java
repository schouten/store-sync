package de.schouten.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * The implementation of a store. A store contains domains in which elements can be stored under element keys. Listeners can be registered to get change
 * notifications.
 */
public final class StoreImpl implements Store {

    /**
     * A map of domains.
     */
    private SortedMap<DomainKey, SortedMap<ElementKey, StoreElement>> domains = new TreeMap<DomainKey, SortedMap<ElementKey, StoreElement>>();

    /**
     * These listeners will be informed about domain changes.
     */
    private List<DomainListener> domainListeners = new LinkedList<DomainListener>();

    /**
     * These listeners will be informed about element changes.
     */
    private Map<DomainKey, List<ElementListener>> elementListeners = new HashMap<DomainKey, List<ElementListener>>();

    /**
     * @param domainKey the key to the domain to test.
     * @return true, if this store contains the domain. The domain is only present, if there is at least one element in that domain.
     */
    @Override
    public synchronized boolean contains(DomainKey domainKey) {
        return domains.containsKey(domainKey);
    }

    /**
     * Puts the element in the store.
     * 
     * @param domainKey the domain of the element to put in store.
     * @param element the element to put in store.
     */
    @Override
    public synchronized void put(DomainKey domainKey, StoreElement element) {
        SortedMap<ElementKey, StoreElement> domain;
        if (!contains(domainKey)) {
            domain = new TreeMap<ElementKey, StoreElement>();
            domains.put(domainKey, domain);
            sendAddMessages(domainKey);
        } else {
            domain = domains.get(domainKey);
        }
        ElementKey elementKey = createElementKey(element);
        domain.put(elementKey, element.clone());
        sendUpdateMessages(domainKey, Collections.singletonMap(elementKey, element));
    }

    /**
     * Gets the store element.
     * 
     * @param domainKey the domain of the element to get from store.
     * @param elementKey the key of the element to get from store.
     * @return the element for the key, or null if not present.
     */
    @Override
    public synchronized StoreElement get(DomainKey domainKey, ElementKey elementKey) {
        if (contains(domainKey)) {
            return domains.get(domainKey).get(elementKey);
        } else { // the domain wasn't found
            return null;
        }
    }

    /**
     * Removes the element from the store.
     * 
     * @param domainKey the domain of the element to remove from store.
     * @param element the element to remove from store.
     */
    @Override
    public synchronized void remove(DomainKey domainKey, StoreElement element) {
        remove(domainKey, createElementKey(element));
    }

    /**
     * Removes the element from the store.
     * 
     * @param domainKey the domain of the element to remove from store.
     * @param elementKey the key of the element to remove from store.
     */
    @Override
    public synchronized void remove(DomainKey domainKey, ElementKey elementKey) {
        if (contains(domainKey)) {
            SortedMap<ElementKey, StoreElement> domain = domains.get(domainKey);
            StoreElement element = domain.remove(elementKey);
            if (element != null) {
                sendRemoveMessages(domainKey, Collections.singletonMap(elementKey, element));
            }
            if (domain.isEmpty()) { // domain is empty - can be removed
                domains.remove(domainKey);
                sendRemoveMessages(domainKey);
            }
        }
    }

    /**
     * @return the amount of all elements in the store.
     */
    @Override
    public synchronized int size() {
        int size = 0;
        for (SortedMap<ElementKey, StoreElement> domain : domains.values()) {
            size += domain.size();
        }
        return size;
    }

    /**
     * @param domainKey the domain to get the size for.
     * @return the amount of the elements in the given domain.
     */
    @Override
    public synchronized int size(DomainKey domainKey) {
        if (contains(domainKey)) {
            return domains.get(domainKey).size();
        } else { // no domain found
            return 0;
        }
    }

    /**
     * @return all {@link DomainKey}s in a set.
     */
    @Override
    public synchronized Set<DomainKey> keySet() {
        return Collections.unmodifiableSet(domains.keySet());
    }

    /**
     * @param domainKey the domain to get the key set for.
     * @return all {@link ElementKey}s in a set.
     */
    @Override
    public synchronized Set<ElementKey> keySet(DomainKey domainKey) {
        if (contains(domainKey)) {
            return Collections.unmodifiableSet(domains.get(domainKey).keySet());
        } else { // no domain found
            return Collections.emptySet();
        }
    }

    /**
     * @param domainKey the domain to clear.
     */
    @Override
    public synchronized void clear(DomainKey domainKey) {
        SortedMap<ElementKey, StoreElement> elementsMap = domains.remove(domainKey);
        if (elementsMap != null) {
            sendRemoveMessages(domainKey, elementsMap);
        }
        sendRemoveMessages(domainKey);
    }

    /**
     * Clears the complete store.
     */
    @Override
    public synchronized void clear() {
        List<DomainKey> keys = new ArrayList<DomainKey>(keySet());
        for (DomainKey domainKey : keys) {
            clear(domainKey);
        }
        domains.clear();
    }

    /**
     * Adds a domain listener to the store.
     * 
     * @param domainListener this listener should be informed about domain changes.
     */
    public synchronized void addDomainListener(DomainListener domainListener) {
        domainListeners.add(domainListener);
    }

    /**
     * Removes a domain listener from the store.
     * 
     * @param domainListener this listener should be removed.
     */
    public synchronized void removeDomainListener(DomainListener domainListener) {
        domainListeners.remove(domainListener);
    }

    /**
     * Adds a element listener to the store.
     * 
     * @param elementListener this listener should be informed about element changes.
     * @param domainKeys the listener should be informed about element changes in the given domain.
     */
    public synchronized void addElementListener(ElementListener elementListener, DomainKey... domainKeys) {
        addElementListener(false, elementListener, domainKeys);
    }

    /**
     * Adds a element listener to the store.
     * 
     * @param resendAll set to true, if all stored elements should be resend.
     * @param elementListener this listener should be informed about element changes.
     * @param domainKeys the listener should be informed about element changes in the given domain.
     */
    public synchronized void addElementListener(boolean resendAll, ElementListener elementListener, DomainKey... domainKeys) {
        for (DomainKey domainKey : domainKeys) {
            getElementListeners(domainKey).add(elementListener);
            if (resendAll && domains.containsKey(domainKey)) {
                sendUpdateMessages(domainKey, elementListener, domains.get(domainKey));
            }
        }
    }

    /**
     * @param domainKey the domain to get the listeners for.
     * @return a list of element listeners for the given domain.
     */
    private List<ElementListener> getElementListeners(DomainKey domainKey) {
        if (!elementListeners.containsKey(domainKey)) {
            elementListeners.put(domainKey, new LinkedList<ElementListener>());
        }
        return elementListeners.get(domainKey);
    }

    /**
     * Removes an element listener from the store.
     * 
     * @param elementListener this listener should be removed.
     */
    public synchronized void removeElementListener(ElementListener elementListener) {
        for (List<ElementListener> elementListenerList : elementListeners.values()) {
            while (elementListenerList.remove(elementListener))
                ;
        }
    }

    /**
     * Creates the element key.
     * 
     * @param element the element to create the key for.
     * @return the created element key.
     */
    private ElementKey createElementKey(StoreElement element) {
        return new ElementKey(element.getClass(), element.getElementKeyParts());
    }

    /**
     * Informs all listeners.
     * 
     * @param domainKeys the domains that have been removed.
     */
    private void sendRemoveMessages(DomainKey... domainKeys) {
        sendDomainMessages(DomainMessageSender.REMOVE, domainKeys);
    }

    /**
     * Informs all listeners.
     * 
     * @param domainKeys the domains that have been added.
     */
    private void sendAddMessages(DomainKey... domainKeys) {
        sendDomainMessages(DomainMessageSender.ADD, domainKeys);
    }

    /**
     * Informs all listeners.
     * 
     * @param sender to send the message with.
     * @param domainKeys the domains that have been modified.
     */
    private void sendDomainMessages(DomainMessageSender sender, DomainKey... domainKeys) {
        for (DomainListener domainListener : new LinkedList<DomainListener>(domainListeners)) {
            for (DomainKey domainKey : domainKeys) {
                try {
                    sender.send(domainListener, domainKey);
                } catch (Exception e) {
                    try {
                        domainListener.exceptionDuringCallback(domainKey, e);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Informs all listeners.
     * 
     * @param domainKey the domain that has been modified.
     * @param elementsMap the elements with their keys that are modified.
     * 
     */
    private void sendUpdateMessages(DomainKey domainKey, Map<ElementKey, StoreElement> elementsMap) {
        sendElementMessages(ElementMessageSender.UPDATE, domainKey, elementsMap);
    }

    /**
     * Informs one listeners.
     * 
     * @param listener the listener to inform.
     * @param domainKey the domain that has been modified.
     * @param elementsMap the elements with their keys that are modified.
     * 
     */
    private void sendUpdateMessages(DomainKey domainKey, ElementListener listener, Map<ElementKey, StoreElement> elementsMap) {
        sendElementMessages(ElementMessageSender.UPDATE, listener, domainKey, elementsMap);
    }

    /**
     * Informs all listeners.
     * 
     * @param domainKey the domain that has been modified.
     * @param elementsMap the elements with their keys that are modified.
     * 
     */
    private void sendRemoveMessages(DomainKey domainKey, Map<ElementKey, StoreElement> elementsMap) {
        sendElementMessages(ElementMessageSender.REMOVE, domainKey, elementsMap);
    }

    /**
     * 
     * Informs all listeners.
     * 
     * @param sender to send the message with.
     * @param domainKey the domain that has been modified.
     * @param elementsMap the elements with their keys that are modified.
     * 
     */
    private void sendElementMessages(ElementMessageSender sender, DomainKey domainKey, Map<ElementKey, StoreElement> elementsMap) {
        for (ElementListener elementListener : getElementListeners(domainKey)) {
            sendElementMessages(sender, elementListener, domainKey, elementsMap);
        }
    }

    /**
     * Informs one listener.
     * 
     * @param elementListener the listener to inform.
     * @param sender to send the message with.
     * @param domainKey the domain that has been modified.
     * @param elementsMap the elements with their keys that are modified.
     * 
     */
    private void sendElementMessages(ElementMessageSender sender, ElementListener elementListener, DomainKey domainKey,
                                     Map<ElementKey, StoreElement> elementsMap) {
        for (Map.Entry<ElementKey, StoreElement> elementEntry : elementsMap.entrySet()) {
            try {
                sender.send(elementListener, domainKey, elementEntry.getKey(), elementEntry.getValue().clone());
            } catch (Exception e) {
                try {
                    elementListener.exceptionDuringCallback(domainKey, elementEntry.getKey(), e);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    /**
     * Enumeration to hold functionality for sending messages.
     */
    private enum DomainMessageSender {

        ADD {

            @Override
            public void send(DomainListener domainListener, DomainKey domainKey) {
                domainListener.domainAdded(domainKey);
            }

        },
        REMOVE {

            @Override
            public void send(DomainListener domainListener, DomainKey domainKey) {
                domainListener.domainRemoved(domainKey);
            }
        };

        /**
         * sends the message to the domain listener. The implementation decide if add or remove is send.
         * 
         * @param domainListener the listener to inform.
         * @param domainKey the key to the domain.
         */
        public abstract void send(DomainListener domainListener, DomainKey domainKey);
    }

    /**
     * Enumeration to hold functionality for sending messages.
     */
    private enum ElementMessageSender {

        UPDATE {

            @Override
            public void send(ElementListener elementListener, DomainKey domainKey, ElementKey elementKey, StoreElement storeElement) {
                elementListener.elementUpdated(domainKey, elementKey, storeElement);
            }

        },
        REMOVE {

            @Override
            public void send(ElementListener elementListener, DomainKey domainKey, ElementKey elementKey, StoreElement storeElement) {
                elementListener.elementRemoved(domainKey, elementKey, storeElement);
            }
        };

        /**
         * Sends the message to the element listener. The implementation decide if update or remove is send.
         * 
         * @param elementListener the listener to inform.
         * @param domainKey the key to the domain.
         * @param elementKey the key to the element.
         * @param storeElement the element itself.
         */
        public abstract void send(ElementListener elementListener, DomainKey domainKey, ElementKey elementKey, StoreElement storeElement);
    }
}
