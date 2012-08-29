package de.schouten.store.web.jmx;

/**
 * Management for the store sync websocket.
 */
public interface WebSocketManagementMXBean {

    /**
     * 
     * @see org.eclipse.jetty.websocket.WebSocket.Connection#close()
     */
    void close();

    /**
     * 
     * @see org.eclipse.jetty.websocket.WebSocket.Connection#close(int, String))
     */
    public void close(int reasonCode, String reasonText);

    /**
     * @see org.eclipse.jetty.websocket.WebSocket.Connection#getMaxBinaryMessageSize()
     */
    int getMaxBinaryMessageSize();

    /**
     * @see org.eclipse.jetty.websocket.WebSocket.Connection#getMaxIdleTime()
     */
    int getMaxIdleTime();

    /**
     * @see org.eclipse.jetty.websocket.WebSocket.Connection#getMaxTextMessageSize()
     */
    int getMaxTextMessageSize();

    /**
     * @see org.eclipse.jetty.websocket.WebSocket.Connection#getProtocol()
     */
    String getProtocol();

    /**
     * @see org.eclipse.jetty.websocket.WebSocket.Connection#isOpen()
     */
    boolean isOpen();

    /**
     * 
     * @return session information.
     */
    String getSessionInfo();

}
