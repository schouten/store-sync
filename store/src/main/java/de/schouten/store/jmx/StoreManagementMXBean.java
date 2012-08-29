package de.schouten.store.jmx;

import java.util.List;
import javax.management.MXBean;

/**
 * Management root for a store.
 */
@MXBean
public interface StoreManagementMXBean {

    int getSize();

    List<String> getKeys();

    void clear();

}
