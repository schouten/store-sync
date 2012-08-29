package de.schouten.store.web.jmx;

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicInteger;
import javax.management.JMException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import org.eclipse.jetty.websocket.WebSocket.Connection;

/**
 * Management for the store sync websocket.
 */
public class WebSocketManagement extends NotificationBroadcasterSupport implements WebSocketManagementMXBean {

    /**
     * The management object name.
     */
    private ObjectName objectName;

    /**
     * The {@link Connection} to manage.
     */
    private Connection connection;

    /**
     * A object with session info.
     */
    private Object sessionInfo;

    /**
     * Sequence number for messages.
     */
    private AtomicInteger sequenceNumber = new AtomicInteger();

    /**
     * Counts up the INSTANCES.
     */
    private static final AtomicInteger INSTANCES = new AtomicInteger();

    /**
     * @param jmxDomain The domain all management jmx beans should reside in. e.g. org.example.
     * @param connection the {@link Connection} for this websocket.
     * @param a object with session information.
     */
    public WebSocketManagement(String jmxDomain, Connection connection, Object sessionInfo) {
        this.connection = connection;
        this.sessionInfo = sessionInfo;
        try {
            objectName = new ObjectName(jmxDomain + ":type=WebSockets,instance=WS_" + INSTANCES.incrementAndGet());
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            server.registerMBean(this, objectName);
        } catch (JMException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unregister this management bean.
     */
    public void unregister() {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try {
            server.unregisterMBean(objectName);
        } catch (JMException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @see org.eclipse.jetty.websocket.WebSocket.Connection#close()
     */
    public void close() {
        connection.close();
    }

    /**
     * 
     * @see org.eclipse.jetty.websocket.WebSocket.Connection#close(int, String)
     */
    public void close(int reasonCode, String reasonText) {
        connection.close(reasonCode, reasonText);
    }

    /**
     * @see org.eclipse.jetty.websocket.WebSocket.Connection#getMaxBinaryMessageSize()
     */
    public int getMaxBinaryMessageSize() {
        return connection.getMaxBinaryMessageSize();
    }

    /**
     * @see org.eclipse.jetty.websocket.WebSocket.Connection#getMaxIdleTime()
     */
    public int getMaxIdleTime() {
        return connection.getMaxIdleTime();
    }

    /**
     * @see org.eclipse.jetty.websocket.WebSocket.Connection#getMaxTextMessageSize()
     */
    public int getMaxTextMessageSize() {
        return connection.getMaxTextMessageSize();
    }

    /**
     * @see org.eclipse.jetty.websocket.WebSocket.Connection#getProtocol()
     */
    public String getProtocol() {
        return connection.getProtocol();
    }

    /**
     * @see org.eclipse.jetty.websocket.WebSocket.Connection#isOpen()
     */
    public boolean isOpen() {
        return connection.isOpen();
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[] {"websocketnotifications"};
        String name = Notification.class.getName();
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, "websocket notifications");
        return new MBeanNotificationInfo[] {info};
    }

    /**
     * 
     * A message was send.
     * 
     * @param text the message content.
     */
    public void messageSend(String text) {
        Notification notification = new Notification("websocketnotifications", this, sequenceNumber.incrementAndGet(), "message send");
        notification.setUserData(text);
        sendNotification(notification);
    }

    /**
     * 
     * A message was received.
     * 
     * @param text the message content.
     */
    public void messageReceived(String text) {
        Notification notification = new Notification("websocketnotifications", this, sequenceNumber.incrementAndGet(), "message received");
        notification.setUserData(text);
        sendNotification(notification);
    }

    /**
     * @return the sessionInfo
     */
    public String getSessionInfo() {
        return sessionInfo + "";
    }

}
