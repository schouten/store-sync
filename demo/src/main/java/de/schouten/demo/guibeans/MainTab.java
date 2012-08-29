package de.schouten.demo.guibeans;

import de.schouten.store.DomainKey;
import de.schouten.store.ElementKey;
import de.schouten.store.Store;
import de.schouten.store.UpdateableStoreElement;

/**
 * Contains the name of the current main tab.
 */
public class MainTab extends UpdateableStoreElement {

    /**
     * The currently displayed tab.
     */
    private String tab;

    /**
     * @see UpdateableStoreElement#UpdateableStoreElement(Store, DomainKey)
     */
    private MainTab(Store store, DomainKey domainKey, String tab) {
        super(store, domainKey);
        this.tab = tab;
    }

    /**
     * @param store the store to get the element from.
     * @param domainKey the domain key of the element.
     * @return the {@link MainTab} from store, or a new created one.
     */
    public static MainTab getOrCreate(Store store, DomainKey domainKey) {
        ElementKey elementKey = new ElementKey(MainTab.class, "SINGLETON");
        MainTab mainTab = (MainTab) store.get(domainKey, elementKey);
        if (mainTab == null) {
            mainTab = new MainTab(store, domainKey, "");
        }
        return mainTab;
    }

    /**
     * @see de.schouten.store.StoreElement#getElementKeyParts()
     */
    @Override
    public Object[] getElementKeyParts() {
        return new Object[] {"SINGLETON"};
    }

    /**
     * @param tab the tab to set
     */
    public void setTab(String tab) {
        this.tab = tab;
        update();
    }

    /**
     * @see de.schouten.store.UpdateableStoreElement#clone()
     */
    @Override
    public MainTab clone() {
        return new MainTab(store, domainKey, tab);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return tab;
    }

}
