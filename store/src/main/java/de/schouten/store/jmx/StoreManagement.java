package de.schouten.store.jmx;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import de.schouten.store.DomainKey;
import de.schouten.store.DomainListener;
import de.schouten.store.JMXBeanSupportForStore;

/**
 * The implementation of the store management.
 */
public class StoreManagement extends NotificationBroadcasterSupport implements StoreManagementMXBean, DomainListener {

    /**
     * The supporting store bean.
     */
    private JMXBeanSupportForStore beanSupportForStore;

    /**
     * Sequence number for messages.
     */
    private AtomicInteger sequenceNumber = new AtomicInteger();

    /**
     * Creates a new management bean.
     * 
     * @param beanSupportForStore the support for the store.
     */
    public StoreManagement(JMXBeanSupportForStore beanSupportForStore) {
        super();
        this.beanSupportForStore = beanSupportForStore;
        beanSupportForStore.getStore().addDomainListener(this);
    }

    public int getSize() {
        return beanSupportForStore.getStore().size();
    }

    public List<String> getKeys() {
        List<String> keys = new ArrayList<String>();
        for (DomainKey domainKey : beanSupportForStore.getStore().keySet()) {
            keys.add(domainKey.toString());
        }
        return keys;
    }

    public void clear() {
        beanSupportForStore.getStore().clear();
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[] {"domainnotifications"};
        String name = Notification.class.getName();
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, "domain notifications");
        return new MBeanNotificationInfo[] {info};
    }

    /**
     * @see de.schouten.store.DomainListener#domainAdded(de.schouten.store.DomainKey)
     */
    @Override
    public void domainAdded(DomainKey domainKey) {
        Notification notification = new Notification("domainnotifications", this, sequenceNumber.incrementAndGet(), "domain added");
        notification.setUserData(domainKey.toString());
        sendNotification(notification);
    }

    /**
     * @see de.schouten.store.DomainListener#domainRemoved(de.schouten.store.DomainKey)
     */
    @Override
    public void domainRemoved(DomainKey domainKey) {
        Notification notification = new Notification("domainnotifications", this, sequenceNumber.incrementAndGet(), "domain removed");
        notification.setUserData(domainKey.toString());
        sendNotification(notification);
    }

    /**
     * @see de.schouten.store.DomainListener#exceptionDuringCallback(de.schouten.store.DomainKey, java.lang.Exception)
     */
    @Override
    public void exceptionDuringCallback(DomainKey domainKey, Exception e) {
        e.printStackTrace();
    }

}
