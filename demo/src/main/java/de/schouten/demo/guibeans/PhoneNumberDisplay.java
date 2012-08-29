package de.schouten.demo.guibeans;

import de.schouten.store.DomainKey;
import de.schouten.store.ElementKey;
import de.schouten.store.Store;
import de.schouten.store.UpdateableStoreElement;

/**
 * Contains the phone display.
 */
public class PhoneNumberDisplay extends UpdateableStoreElement {

    /**
     * The phone number display content.
     */
    private String display;

    /**
     * @see UpdateableStoreElement#UpdateableStoreElement(Store, DomainKey)
     */
    public PhoneNumberDisplay(Store store, DomainKey domainKey) {
        super(store, domainKey);
        clear();
    }

    /**
     * @see UpdateableStoreElement#UpdateableStoreElement(Store, DomainKey)
     */
    private PhoneNumberDisplay(Store store, DomainKey domainKey, String display) {
        super(store, domainKey);
        this.display = display;
    }

    /**
     * @param store the store to get the element from.
     * @param domainKey the domain key of the element.
     * @return the {@link PhoneNumberDisplay} from store, or a new created one.
     */
    public static PhoneNumberDisplay getOrCreate(Store store, DomainKey domainKey) {
        ElementKey elementKey = new ElementKey(PhoneNumberDisplay.class, "SINGLETON");
        PhoneNumberDisplay phoneNumberDisplay = (PhoneNumberDisplay) store.get(domainKey, elementKey);
        if (phoneNumberDisplay == null) {
            phoneNumberDisplay = new PhoneNumberDisplay(store, domainKey);
        }
        return phoneNumberDisplay;
    }

    /**
     * Clears the display.
     */
    public void clear() {
        display = "";
        update();
    }

    /**
     * Appends the digit to the display.
     * 
     * @param i the digit to add.
     */
    public void appendDigit(int i) {
        if (i >= 0 && i <= 9) {
            display += i;
        }
        update();
    }

    /**
     * @see de.schouten.store.StoreElement#getElementKeyParts()
     */
    @Override
    public Object[] getElementKeyParts() {
        return new Object[] {"SINGLETON"};
    }

    /**
     * @see de.schouten.store.UpdateableStoreElement#clone()
     */
    @Override
    public PhoneNumberDisplay clone() {
        return new PhoneNumberDisplay(store, domainKey, display);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return display;
    }

}
