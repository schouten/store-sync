package de.schouten.demo.web;

import java.util.SortedSet;
import java.util.TreeSet;
import de.schouten.demo.guibeans.Message;
import de.schouten.store.DomainKey;
import de.schouten.store.Store;

/**
 * Manages messages. If there are more than 4 messages, the oldest ones will be deleted.
 */
public class MessageManager {

    /** Manager for tom. */
    private static MessageManager managerTom;
    /** Manager for jerry. */
    private static MessageManager managerJerry;

    private static final DomainKey TOM_KEY = new DomainKey("root", "tom");
    private static final DomainKey JERRY_KEY = new DomainKey("root", "jerry");

    /**
     * Inits at application startup.
     * 
     * @param store the store for all data.
     */
    public static void init(Store store) {
        managerTom = new MessageManager(store, TOM_KEY);
        managerJerry = new MessageManager(store, JERRY_KEY);
    }

    /**
     * @return the managerTom
     */
    public static MessageManager getManagerTom() {
        return managerTom;
    }

    /**
     * @return the managerJerry
     */
    public static MessageManager getManagerJerry() {
        return managerJerry;
    }

    /**
     * @return the manager for the mandator.
     * @param mandator the mandator to get the manager for.
     */
    public static MessageManager getManager(String mandator) {
        if (mandator.equals("tom")) {
            return getManagerTom();
        } else {
            return getManagerJerry();
        }
    }

    /**
     * The latest messages.
     */
    private SortedSet<Message> messages = new TreeSet<Message>();

    /**
     * The current store.
     */
    private Store store;

    /**
     * The domain key for the manager.
     */
    private DomainKey domainKey;

    public MessageManager(Store store, DomainKey domainKey) {
        super();
        this.store = store;
        this.domainKey = domainKey;
    }

    /**
     * Adds the new message. Deletes old ones.
     * 
     * @param message the message to add.
     */
    public void addMessage(Message message) {
        if (messages.size() > 6) {
            Message toRemove = messages.first();
            store.remove(domainKey, toRemove);
            messages.remove(toRemove);
        }
        messages.add(message);
        store.put(domainKey, message);
    }
}