package de.schouten.store;

import javax.management.ObjectName;
import org.junit.Test;

public class ObjectNameTest {

    @Test
    public void test() throws Exception {
        printObjectNameInfo(new ObjectName("de.ics:type=Lala"));
        printObjectNameInfo(new ObjectName("de.ics:a=b,b=c"));
    }

    private void printObjectNameInfo(ObjectName on) {
        System.out.println(on);
        System.out.println("CanonicalName                   " + on.getCanonicalName());
        System.out.println("CanonicalKeyPropertyListString  " + on.getCanonicalKeyPropertyListString());
        System.out.println("Domain                          " + on.getDomain());
        System.out.println("KeyPropertyListString           " + on.getKeyPropertyList());
        System.out.println();
    }

}
