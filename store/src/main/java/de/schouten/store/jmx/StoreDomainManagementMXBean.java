package de.schouten.store.jmx;

import java.util.List;
import javax.management.MXBean;

/**
 * Management root for a store.
 */
@MXBean
public interface StoreDomainManagementMXBean {

    String getDomainKey();

    int getSize();

    List<String> getKeys();

    void clear();

    String view(String key);

    void remove(String key);

}
