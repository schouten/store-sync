package de.schouten.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class StoreImplTest {

    @Test
    public void testContains() {
        StoreImpl store = new StoreImpl();
        assertFalse(store.contains(new DomainKey()));
    }

    @Test
    public void testPutAndGetAndRemoveAndClear() {
        StoreImpl store = new StoreImpl();
        TheDomainListener domainListener = new TheDomainListener();
        store.addDomainListener(domainListener);
        TheElementListener elementListener = new TheElementListener();
        store.addElementListener(elementListener, new DomainKey("root"), new DomainKey("root", "tom"));

        // put
        assertEquals(0, store.size());
        SimpleStoreElement storeElement = new SimpleStoreElement(17L, "Hello Store!");
        store.put(new DomainKey("root"), storeElement);
        assertEquals(1, domainListener.added);
        assertEquals(1, elementListener.updated);
        assertEquals("[/de.schouten.store.SimpleStoreElement/17]", store.keySet(new DomainKey("root")).toString());
        assertTrue(store.contains(new DomainKey("root")));
        assertEquals(1, store.size());
        assertEquals(1, store.size(new DomainKey("root")));
        store.put(new DomainKey("root", "tom"), new SimpleStoreElement(1, "one"));
        assertEquals(2, domainListener.added);
        assertEquals(2, elementListener.updated);
        assertEquals("[/de.schouten.store.SimpleStoreElement/1]", store.keySet(new DomainKey("root", "tom")).toString());
        assertEquals(2, store.size());
        assertEquals(1, store.size(new DomainKey("root", "tom")));
        store.put(new DomainKey("root", "tom"), new SimpleStoreElement(2, "two"));
        assertEquals(2, domainListener.added);
        assertEquals(3, elementListener.updated);
        assertEquals("[/de.schouten.store.SimpleStoreElement/1, /de.schouten.store.SimpleStoreElement/2]", store.keySet(new DomainKey("root", "tom"))
            .toString());
        assertEquals(3, store.size());
        assertEquals(2, store.size(new DomainKey("root", "tom")));
        store.put(new DomainKey("root", "jerry"), new SimpleStoreElement(3, "three"));
        assertEquals(3, domainListener.added);
        assertEquals(3, elementListener.updated);
        assertEquals(4, store.size());
        assertEquals(2, store.size(new DomainKey("root", "tom")));
        assertEquals("[/root, /root/jerry, /root/tom]", store.keySet().toString());

        // get
        SimpleStoreElement clonedElement = (SimpleStoreElement) store.get(new DomainKey("root"), new ElementKey(SimpleStoreElement.class, 17L));
        assertNotNull(clonedElement);
        assertFalse(storeElement == clonedElement);
        assertEquals(storeElement.getValue(), clonedElement.getValue());

        // remove
        store.remove(new DomainKey("root"), storeElement);
        assertEquals(1, domainListener.removed);
        assertEquals(1, elementListener.removed);
        assertFalse(store.contains(new DomainKey("root")));
        assertEquals(3, store.size());
        assertEquals("[/root/jerry, /root/tom]", store.keySet().toString());

        // clear
        store.clear(new DomainKey("root", "jerry"));
        assertEquals(2, domainListener.removed);
        assertEquals(1, elementListener.removed);
        assertFalse(store.contains(new DomainKey("root", "jerry")));
        assertFalse(store.contains(new DomainKey("root")));
        assertTrue(store.contains(new DomainKey("root", "tom")));
        assertEquals(2, store.size());
        store.clear();
        assertEquals(3, domainListener.removed);
        assertEquals(3, elementListener.removed);
        assertEquals(0, store.size());
        assertFalse(store.contains(new DomainKey("root", "jerry")));
        assertFalse(store.contains(new DomainKey("root")));
        assertFalse(store.contains(new DomainKey("root", "tom")));
        assertEquals("[]", store.keySet().toString());
    }

    private static final class TheDomainListener implements DomainListener {

        public int added = 0;
        public int removed = 0;

        @Override
        public void domainAdded(DomainKey domainKey) {
            System.out.println("added " + domainKey);
            added++;
        }

        @Override
        public void domainRemoved(DomainKey domainKey) {
            System.out.println("removed " + domainKey);
            removed++;
        }

        @Override
        public void exceptionDuringCallback(DomainKey domainKey, Exception e) {
            e.printStackTrace();
        }

    }

    private static final class TheElementListener implements ElementListener {
        public int updated = 0;
        public int removed = 0;

        @Override
        public void elementUpdated(DomainKey domainKey, ElementKey elementKey, StoreElement storeElement) {
            updated++;
            System.out.println("updated " + domainKey + " " + elementKey);
        }

        @Override
        public void elementRemoved(DomainKey domainKey, ElementKey elementKey, StoreElement storeElement) {
            removed++;
            System.out.println("removed " + domainKey + " " + elementKey);
        }

        @Override
        public void exceptionDuringCallback(DomainKey domainKey, ElementKey elementKey, Exception e) {
            e.printStackTrace();
        }

    }

}
