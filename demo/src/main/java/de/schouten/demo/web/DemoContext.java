package de.schouten.demo.web;

import org.eclipse.jetty.websocket.WebSocket;
import de.schouten.demo.web.actions.Actions;
import de.schouten.store.Store;
import de.schouten.store.web.Action;
import de.schouten.store.web.StoreSyncContext;
import de.schouten.store.web.StoreSyncWebSocket;

/**
 * The context of the demo application.
 */
public class DemoContext extends StoreSyncContext<DemoSession> {

    public DemoContext(Store store) {
        super(store, "Demo");
    }

    /**
     * @see de.schouten.store.web.StoreSyncContext#createAction(java.lang.String, java.lang.String)
     */
    @Override
    public Action<DemoSession> createAction(String actionName, String json) throws Exception {
        return Actions.valueOf(actionName).create(json);
    }

    /**
     * @see de.schouten.store.web.StoreSyncContext#createWebSocket(java.lang.String)
     */
    @Override
    public WebSocket createWebSocket(String protocol) {
        if (StoreSyncContext.STORESYNC_PROTOCOL.equals(protocol)) {
            StoreSyncWebSocket<DemoSession> webSocket = new StoreSyncWebSocket<>(this);
            DemoSession session = new DemoSession(webSocket);
            webSocket.setSession(session);
            return webSocket;
        } else { // nothing found for the given protocol
            return null;
        }
    }

    /**
     * @see de.schouten.store.web.StoreSyncContext#reportErrorPerformingTheAction(de.schouten.store.web.StoreSyncWebSocketSession, Action, Exception)
     */
    @Override
    public void reportErrorPerformingTheAction(DemoSession session, Action<DemoSession> action, Exception e) {
        e.printStackTrace();
        session.sendError(e);
    }

    /**
     * @see de.schouten.store.web.StoreSyncContext#reportActionNotFound(de.schouten.store.web.StoreSyncWebSocketSession, String)
     */
    @Override
    public void reportActionNotFound(DemoSession session, String message) {
        System.err.println("No action found for " + message);
        session.sendError("No action found for " + message);
    }

    /**
     * @see de.schouten.store.web.StoreSyncContext#reportErrorCreatingTheAction(de.schouten.store.web.StoreSyncWebSocketSession, String, Exception)
     */
    @Override
    public void reportErrorCreatingTheAction(DemoSession session, String message, Exception e) {
        e.printStackTrace();
        session.sendError(e);
    }

    /**
     * @see de.schouten.store.web.StoreSyncContext#reportGeneralError(de.schouten.store.web.StoreSyncWebSocketSession, Exception)
     */
    @Override
    public void reportGeneralError(DemoSession session, Exception e) {
        e.printStackTrace();
        session.sendError(e);
    }

}
