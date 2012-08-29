package de.schouten.store.jmx;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import de.schouten.store.DomainKey;
import de.schouten.store.ElementKey;
import de.schouten.store.ElementListener;
import de.schouten.store.JMXBeanSupportForStore;
import de.schouten.store.StoreElement;

/**
 * Management bean for one domain in a store.
 */
public class StoreDomainManagement extends NotificationBroadcasterSupport implements StoreDomainManagementMXBean, ElementListener {

    /**
     * The key to the domain to manage.
     */
    private DomainKey domainKey;

    /**
     * The supporting store bean.
     */
    private JMXBeanSupportForStore beanSupportForStore;

    /**
     * Sequence number for messages.
     */
    private AtomicInteger sequenceNumber = new AtomicInteger();

    /**
     * @param domainKey The key to the domain to manage.
     * @param beanSupportForStore The supporting store bean.
     */
    public StoreDomainManagement(DomainKey domainKey, JMXBeanSupportForStore beanSupportForStore) {
        super();
        this.domainKey = domainKey;
        this.beanSupportForStore = beanSupportForStore;
    }

    /**
     * @see de.schouten.store.jmx.StoreDomainManagementMXBean#getSize()
     */
    @Override
    public int getSize() {
        return beanSupportForStore.getStore().size(domainKey);
    }

    /**
     * @see de.schouten.store.jmx.StoreDomainManagementMXBean#getKeys()
     */
    @Override
    public List<String> getKeys() {
        List<String> keys = new ArrayList<String>();
        for (ElementKey elementKey : beanSupportForStore.getStore().keySet(domainKey)) {
            keys.add(elementKey.toString());
        }
        return keys;
    }

    /**
     * @see de.schouten.store.jmx.StoreDomainManagementMXBean#clear()
     */
    @Override
    public void clear() {
        beanSupportForStore.getStore().clear(domainKey);
    }

    /**
     * @see StoreDomainManagementMXBean#getDomainKey()
     */
    @Override
    public String getDomainKey() {
        return domainKey.toString();
    }

    /**
     * @see StoreDomainManagementMXBean#view()
     */
    @Override
    public String view(String key) {
        return beanSupportForStore.getStore().get(domainKey, new ElementKey(key)) + "";
    }

    /**
     * @see StoreDomainManagementMXBean#remove()
     */
    @Override
    public void remove(String key) {
        beanSupportForStore.getStore().remove(domainKey, new ElementKey(key));
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[] {"elementnotifications"};
        String name = Notification.class.getName();
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, "element notifications");
        return new MBeanNotificationInfo[] {info};
    }

    /**
     * @see de.schouten.store.ElementListener#elementUpdated(de.schouten.store.DomainKey, de.schouten.store.ElementKey, de.schouten.store.StoreElement)
     */
    @Override
    public void elementUpdated(DomainKey aDomainKey, ElementKey elementKey, StoreElement storeElement) {
        Notification notification = new Notification("elementnotifications", this, sequenceNumber.incrementAndGet(), "element updated");
        notification.setUserData(String.format("%s : %s", aDomainKey, elementKey));
        sendNotification(notification);
    }

    /**
     * @see de.schouten.store.ElementListener#elementRemoved(de.schouten.store.DomainKey, de.schouten.store.ElementKey, de.schouten.store.StoreElement)
     */
    @Override
    public void elementRemoved(DomainKey aDomainKey, ElementKey elementKey, StoreElement storeElement) {
        Notification notification = new Notification("elementnotifications", this, sequenceNumber.incrementAndGet(), "element removed");
        notification.setUserData(String.format("%s : %s", aDomainKey, elementKey));
        sendNotification(notification);
    }

    /**
     * @see de.schouten.store.ElementListener#exceptionDuringCallback(de.schouten.store.DomainKey, de.schouten.store.ElementKey, java.lang.Exception)
     */
    @Override
    public void exceptionDuringCallback(DomainKey aDomainKey, ElementKey elementKey, Exception e) {
        e.printStackTrace();
    }

}
