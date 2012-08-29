// $Id:$

package de.schouten.demo.web;

import de.schouten.store.web.StoreSyncWebSocket;
import de.schouten.store.web.StoreSyncWebSocketSession;

/**
 * The session for the demo webapplication.
 */
public class DemoSession extends StoreSyncWebSocketSession {

    /**
     * The logged in user.
     */
    private String user;

    /**
     * The mandator of the logged in user.
     */
    private String mandator;

    /**
     * @see StoreSyncWebSocketSession#WebSocketSession(de.schouten.store.web.StoreSyncWebSocket)
     */
    public DemoSession(StoreSyncWebSocket<DemoSession> storeSyncWebSocket) {
        super(storeSyncWebSocket);
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the mandator
     */
    public String getMandator() {
        return mandator;
    }

    /**
     * @param mandator the mandator to set
     */
    public void setMandator(String mandator) {
        this.mandator = mandator;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DemoSession [user=" + user + ", mandator=" + mandator + "]";
    }

}
