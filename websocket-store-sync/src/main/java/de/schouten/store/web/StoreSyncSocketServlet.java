package de.schouten.store.web;

import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

/**
 * Servlet that starts the web socket communication.
 */
public class StoreSyncSocketServlet<S extends StoreSyncWebSocketSession> extends WebSocketServlet {

    /**
     * The {@link StoreSyncContext} for this servlet instance.
     */
    private StoreSyncContext<S> context;

    /**
     * @param context the context to set
     */
    public void setContext(StoreSyncContext<S> context) {
        this.context = context;
    }

    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest request, String protocolString) {
        WebSocket webSocket = context.createWebSocket(protocolString);
        if (webSocket == null) {
            webSocket = context.createDefaultWebSocket();
        }
        return webSocket;
    }
}
