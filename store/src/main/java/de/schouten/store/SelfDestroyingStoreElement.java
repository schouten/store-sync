package de.schouten.store;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The store element removes itself from the {@link Store} after the given time.
 */
public abstract class SelfDestroyingStoreElement extends UpdateableStoreElement {

    /**
     * Timer to timeout this element..
     */
    private transient static final Timer TIMER = new Timer("SelfDestroyingStoreElement Timer", true);

    /**
     * @see UpdateableStoreElement#UpdateableStoreElement(Store, DomainKey)
     */
    public SelfDestroyingStoreElement(Store store, DomainKey domainKey) {
        super(store, domainKey);
    }

    /**
     * Adds the element to the store for the given time to live. After ttl the element removes itself.
     * 
     * @param timeToLive the ttl for the element in the store.
     */
    public void addToStore(long timeToLive) {
        update();
        TIMER.schedule(new TimerTask() {

            @Override
            public void run() {
                store.remove(domainKey, SelfDestroyingStoreElement.this);
            }
        }, timeToLive);
    }

    /**
     * @see de.schouten.store.UpdateableStoreElement#clone()
     */
    @Override
    public abstract SelfDestroyingStoreElement clone();

}
