package de.schouten.store;

import java.io.Serializable;

/**
 * A store element must be serializable and cloneable, so they can be send over the network and only clones are stored.
 */
public interface StoreElement extends Serializable, Cloneable {

    /**
     * @return the element key parts for this element without type information {@link ElementKey}.
     */
    Object[] getElementKeyParts();

    /**
     * @return a clone of the store element.
     */
    StoreElement clone();

}
