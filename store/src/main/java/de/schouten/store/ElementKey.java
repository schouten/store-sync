package de.schouten.store;

import java.io.Serializable;

/**
 * A immutable key for an element.
 */
public final class ElementKey extends KeyPath {

    /**
     * Constructs a new key to an element. The first part of this key is the type of the element. The parts are not allowed to be null.
     * 
     * @param elementType the type of the element to generate the key for.
     * @param parts the parts of the key. they are not allowed to be null. For each part the to string method is beeing called to generate the key.
     * @throws NullPointerException for all null parts.
     */
    public ElementKey(Class<? extends Serializable> elementType, Object... parts) {
        super(partsAsStrings(elementType, parts));
    }

    /**
     * Constructs a new key to an element. The key parts are separated by '/'.
     * 
     * @param key the key separated by '/'.
     */
    public ElementKey(String key) {
        super(key.substring(1).split("/"));
    }

    /**
     * Converts all parts to strings.
     * 
     * @param elementType the type of the element.
     * @param parts all key parts.
     * @return all parts as strings.
     */
    protected static String[] partsAsStrings(Class<? extends Serializable> elementType, Object... parts) {
        String[] partsAsStrings = new String[parts.length + 1];
        int i = 0;
        partsAsStrings[i++] = elementType.getName();
        for (Object part : parts) {
            partsAsStrings[i++] = part.toString();
        }
        return partsAsStrings;
    }

}
