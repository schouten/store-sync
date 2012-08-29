package de.schouten.store;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import de.schouten.store.jmx.StoreDomainManagement;
import de.schouten.store.jmx.StoreManagement;
import de.schouten.store.jmx.StoreManagementMXBean;

/**
 * Adds JMX management capabilities to a store. The given store is then manageable via JMX.
 */
public class JMXBeanSupportForStore implements DomainListener {

    /**
     * The store that is then supported by JMX.
     */
    private Store store;

    /**
     * The root object name.
     */
    private ObjectName objectName;

    /**
     * All object objectNames.
     */
    private Map<ObjectName, StoreDomainManagement> objectNames = Collections.synchronizedMap(new HashMap<ObjectName, StoreDomainManagement>());

    /**
     * Adds JMX support to the given store.
     * 
     * @param store the store to add support to.
     */
    public JMXBeanSupportForStore(Store store) {
        super();
        this.store = store;
    }

    /**
     * Registers the store management in JMX with the {@link ObjectName} following this pattern.
     * 
     * @param managementObjectName the name of the store management to register as JMX bean.
     * @throws MalformedObjectNameException
     * @throws JMXxception on management errors.
     */
    public void register(String managementObjectName) throws JMException {
        objectName = new ObjectName(managementObjectName);
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        StoreManagementMXBean management = new StoreManagement(this);
        server.registerMBean(management, objectName);
        for (DomainKey key : store.keySet()) {
            domainAdded(key);
        }
        store.addDomainListener(this);
    }

    /**
     * Unregisters the store management.
     * 
     * @throws JMException on management errors.
     */
    public void unregister() throws JMException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        server.unregisterMBean(objectName);
        unregisterAllDomains();
    }

    /**
     * 
     * Unregisters all domain manage beans.
     * 
     * @throws JMException on all m beans
     */
    private void unregisterAllDomains() throws JMException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        for (Map.Entry<ObjectName, StoreDomainManagement> entry : objectNames.entrySet()) {
            store.removeElementListener(entry.getValue());
            server.unregisterMBean(entry.getKey());
        }
    }

    /**
     * @return the store
     */
    public Store getStore() {
        return store;
    }

    /**
     * @see de.schouten.store.DomainListener#domainAdded(de.schouten.store.DomainKey)
     */
    @Override
    public void domainAdded(DomainKey domainKey) {
        try {
            ObjectName name = createObjectName(domainKey);
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            StoreDomainManagement management = new StoreDomainManagement(domainKey, this);
            server.registerMBean(management, name);
            store.addElementListener(management, domainKey);
            objectNames.put(name, management);
        } catch (JMException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see de.schouten.store.DomainListener#domainRemoved(de.schouten.store.DomainKey)
     */
    @Override
    public void domainRemoved(DomainKey domainKey) {
        try {
            ObjectName domainObjectName = createObjectName(domainKey);
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            store.removeElementListener(objectNames.get(domainObjectName));
            server.unregisterMBean(domainObjectName);
            objectNames.remove(domainObjectName);
        } catch (JMException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see de.schouten.store.DomainListener#exceptionDuringCallback(de.schouten.store.DomainKey, java.lang.Exception)
     */
    @Override
    public void exceptionDuringCallback(DomainKey domainKey, Exception e) {
        e.printStackTrace();
    }

    /**
     * Creates a object name for the domainkey.
     * 
     * @param domainKey the domainKey to create the object name for.
     * @return the created object name.
     * @throws MalformedObjectNameException on management errors.
     */
    private ObjectName createObjectName(DomainKey domainKey) throws MalformedObjectNameException {
        StringBuilder stringBuilder = new StringBuilder(objectName.getCanonicalName()).append(",root=domains");
        int level = 1;
        for (String key : domainKey) {
            stringBuilder.append(",dl").append(level++).append("=").append(key);
        }
        return new ObjectName(stringBuilder.toString());
    }
}
