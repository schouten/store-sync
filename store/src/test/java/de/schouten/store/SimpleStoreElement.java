package de.schouten.store;

public class SimpleStoreElement implements StoreElement {

    private String value;

    private long key;

    public SimpleStoreElement(long key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SimpleStoreElement [value=" + value + "]";
    }

    /**
     * @see de.schouten.store.StoreElement#getElementKeyParts()
     */
    @Override
    public Object[] getElementKeyParts() {
        return new Object[] {key};
    }

    /**
     * @see java.lang.Object#clone()
     */
    @Override
    public StoreElement clone() {
        return new SimpleStoreElement(key, value);
    }

}
