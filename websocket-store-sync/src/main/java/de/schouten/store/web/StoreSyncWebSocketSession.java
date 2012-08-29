package de.schouten.store.web;

import de.schouten.store.web.Message.ClientStatusMessage.ClientStatus;

/**
 * A session for one store sync web socket connection.
 */
public abstract class StoreSyncWebSocketSession {

    /**
     * The web socket for this session.
     */
    private StoreSyncWebSocket<? extends StoreSyncWebSocketSession> storeSyncWebSocket;

    /**
     * A new session for the given web socket.
     * 
     * @param storeSyncWebSocket The web socket for this session.
     */
    public StoreSyncWebSocketSession(StoreSyncWebSocket<? extends StoreSyncWebSocketSession> storeSyncWebSocket) {
        super();
        this.storeSyncWebSocket = storeSyncWebSocket;
    }

    /**
     * Invalidates the session and closes the web socket connection.
     */
    public void invalidate() {
        storeSyncWebSocket.getConnection().close(4000, "session invalidated.");
    }

    /**
     * @return if this session (web socket connection) is still open.
     * @see org.eclipse.jetty.websocket.WebSocket.Connection#isOpen()
     */
    public boolean isOpen() {
        return storeSyncWebSocket.getConnection().isOpen();
    }

    /**
     * The client is initialized - inform him!
     */
    public void setClientStatusInitialized() {
        storeSyncWebSocket.send(new Message.ClientStatusMessage(ClientStatus.initialized, null));
    }

    /**
     * The client is still initializing - inform him with a status text!
     * 
     * @param statusText the text should be displayed to the user, so he can react and reach the state {@link ClientStatus#initialized}. Most often this happens
     *            if the user is not logged in correctly.
     */
    public void setClientStatusUninitialized(String statusText) {
        storeSyncWebSocket.send(new Message.ClientStatusMessage(ClientStatus.uninitialized, statusText));
    }

    /**
     * Sends the error to the client.
     * 
     * @param message the message text to send.
     */
    public void sendError(String message) {
        sendError(message, null);
    }

    /**
     * Sends the error to the client.
     * 
     * @param message the message text to send.
     * @param trace a optional trace to send.
     */
    public void sendError(String message, String trace) {
        storeSyncWebSocket.send(new Message.ErrorMessage(message, trace));
    }

    /**
     * Sends the exception to the client.
     * 
     * @param exception the exception to send.
     */
    public void sendError(Exception exception) {
        storeSyncWebSocket.send(new Message.ErrorMessage(exception));
    }
}
