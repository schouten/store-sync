package de.schouten.store.web;

/**
 * A gui action that is performed on the server and maybe changes some state in the store.
 */
public interface Action<S extends StoreSyncWebSocketSession> {

    /**
     * Performs the action.
     * 
     * @param context the current context.
     * @param subscriptions the manager for store subscriptions.
     * @param session the session for the web socket connection.
     * 
     * @throws Exception on errors.
     */
    public void perform(StoreSyncContext<S> context, Subscriptions subscriptions, S session) throws Exception;

}
