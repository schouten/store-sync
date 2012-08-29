package de.schouten.store.web;

import org.eclipse.jetty.websocket.WebSocket;
import de.schouten.store.Store;

/**
 * The cotext is used by {@link StoreSyncSocketServlet} and contains the {@link Store} and factories for actions and websockets.
 */
public abstract class StoreSyncContext<S extends StoreSyncWebSocketSession> {

    /**
     * The name of the store sync protocol.
     */
    public static final String STORESYNC_PROTOCOL = "storesync";

    /**
     * The store contains the data that should be synchronized to the clients via {@link StoreSyncWebSocket}.
     */
    private Store store;

    /**
     * The domain all management jmx beans should reside in. e.g. org.example.
     */
    private String jmxDomain;

    /**
     * @param store puts the store into the context.
     * @param jmxDomain The domain all management jmx beans should reside in. e.g. org.example.
     */
    public StoreSyncContext(Store store, String jmxDomain) {
        super();
        this.store = store;
        this.jmxDomain = jmxDomain;
    }

    /**
     * Override this factory method, if another than the {@link EchoWebSocket} should be the default one.
     * 
     * @return the default web socket, if no socket was found for the protocol.
     */
    public WebSocket createDefaultWebSocket() {
        return new EchoWebSocket<S>(this);
    }

    /**
     * Override this factory method to create {@link WebSocket}s. The one for the protocol "storesync" should be a {@link StoreSyncWebSocket}.
     * 
     * @param protocol the protocol the {@link WebSocket} should be created for.
     * @return the {@link WebSocket} for the requested protocol, or null if not found.
     */
    public abstract WebSocket createWebSocket(String protocol);

    /**
     * @return the store
     */
    public Store getStore() {
        return store;
    }

    /**
     * @return the jmxDomain
     */
    public String getJmxDomain() {
        return jmxDomain;
    }

    /**
     * 
     * Creates a action for the given action name.
     * 
     * @param actionName the name of the action to create.
     * @param json a json String containing the data for the action.
     * @return the created action, or null if not found.
     * @throws Exception on all possible errors.
     */
    public abstract Action<S> createAction(String actionName, String json) throws Exception;

    /**
     * 
     * Reports an error during the performing of an action.
     * 
     * @param session the current session.
     * @param action the action that was performed and caused the error.
     * @param e the exception that was catched.
     */
    public abstract void reportErrorPerformingTheAction(S session, Action<S> action, Exception e);

    /**
     * A action for the given name couldn't be created.
     * 
     * @param session the current session.
     * @param message the message for which the action couldn't be created.
     */
    public abstract void reportActionNotFound(S session, String message);

    /**
     * Reports an error creating the action.
     * 
     * @param session the current session.
     * @param message the message for which the action couldn't be created.
     * @param e the exception that was catched during creation process.
     */
    public abstract void reportErrorCreatingTheAction(S session, String message, Exception e);

    /**
     * Reports an general error.
     * 
     * @param session the current session.
     * @param e the error to report.
     */
    public abstract void reportGeneralError(S session, Exception e);
}
