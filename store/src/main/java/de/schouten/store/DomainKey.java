package de.schouten.store;

/**
 * The domain is a hierarchical immutable key that describes an access path to the store. It is a path like structure.
 */
public final class DomainKey extends KeyPath {

    /**
     * Construct an immutable domain out of its keys.
     * 
     * @param parts the path parts to the domain. A null key is not allowed!
     * @throws NullPointerException if a key is null.
     */
    public DomainKey(String... parts) {
        super(parts);
    }

}
