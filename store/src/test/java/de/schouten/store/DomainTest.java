package de.schouten.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.SortedSet;
import java.util.TreeSet;
import org.junit.Test;

public class DomainTest {

    private DomainKey d = new DomainKey("a", "b", "c", "d");

    @Test(expected = NullPointerException.class)
    public void testDomain() {
        new DomainKey("a", null, "b");
    }

    @Test
    public void testIterator() {
        String[] values = new String[] {"a", "b", "c", "d"};
        int i = 0;
        for (String keypart : d) {
            assertEquals(values[i++], keypart);
        }
    }

    @Test
    public void testToString() {
        assertEquals("/a/b/c/d", d.toString());
    }

    @Test
    public void testEquals() {
        assertEquals(new DomainKey("a", "b", "c", "d"), d);
        assertFalse(d.equals(new DomainKey()));
    }

    @Test
    public void testHashCode() {
        assertNotNull(d.hashCode());
    }

    @Test
    public void testLength() {
        assertEquals(4, d.length());
    }

    @Test
    public void testIsChild() {
        assertTrue(new DomainKey().isChild(new DomainKey()));
        assertTrue(new DomainKey().isChild(d));
        assertTrue(new DomainKey("a").isChild(d));
        assertTrue(new DomainKey("a", "b").isChild(d));
        assertTrue(new DomainKey("a", "b", "c").isChild(d));
        assertTrue(new DomainKey("a", "b", "c", "d").isChild(d));
        assertFalse(new DomainKey("a", "b", "c", "d", "e").isChild(d));
        assertFalse(new DomainKey("x").isChild(d));
    }

    @Test
    public void testCompareable() {
        SortedSet<DomainKey> set = new TreeSet<DomainKey>();
        set.add(new DomainKey("b", "a"));
        set.add(new DomainKey());
        set.add(new DomainKey("z"));
        set.add(new DomainKey("a", "b"));
        assertEquals("[, /a/b, /b/a, /z]", set.toString());
    }
}
