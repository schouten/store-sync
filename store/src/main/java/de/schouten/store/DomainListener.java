package de.schouten.store;

/**
 * Callback methods to inform about adding and removing domains to and from the store.
 */
public interface DomainListener {

    /**
     * A domain was added.
     * 
     * @param domainKey the key to the domain that was added.
     */
    void domainAdded(DomainKey domainKey);

    /**
     * A domain was removed.
     * 
     * @param domainKey the key to the domain that was removed.
     */
    void domainRemoved(DomainKey domainKey);

    /**
     * In your {@link #domainAdded(DomainKey)} or {@link #domainRemoved(DomainKey)} implementation has been an exception. Please check your code!
     * 
     * @param domainKey the domain key the exception occurred.
     * @param e the exception that was thrown.
     */
    void exceptionDuringCallback(DomainKey domainKey, Exception e);
}
