package de.schouten.store;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import org.junit.Test;

public class ElementKeyTest {

    @Test
    public void testElementKeyString() {
        assertEquals("/a/b/c", new ElementKey("/a/b/c").toString());
    }

    @Test
    public void testElementKey() {
        assertEquals("/java.lang.Integer/1/3.5/lala", new ElementKey(Integer.class, 1, 3.5, "lala").toString());
    }

    @Test
    public void testPartsAsStrings() {
        assertEquals("[java.lang.String, 1, 3.5, lala]", Arrays.toString(ElementKey.partsAsStrings(String.class, 1, 3.5, "lala")));
    }

}
