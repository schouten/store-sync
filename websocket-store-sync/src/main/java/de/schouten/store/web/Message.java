package de.schouten.store.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import com.google.gson.Gson;
import de.schouten.store.DomainKey;
import de.schouten.store.ElementKey;
import de.schouten.store.StoreElement;

/**
 * A message to send to the client.
 */
@SuppressWarnings("unused")
public abstract class Message {

    /**
     * To create json strings.
     */
    private transient static final Gson GSON = new Gson();

    /**
     * All possible message types.
     */
    private enum Type {
        update, remove, status, error;
    }

    /**
     * The message type to send.
     */
    private final Type type;

    /**
     * Creates a new message.
     * 
     * @param type The message type to send.
     */
    private Message(Type type) {
        super();
        this.type = type;
    }

    /**
     * @return a json representation of this object.
     */
    public String toJson() {
        return GSON.toJson(this);
    }

    /**
     * Message about the new status text for the client.
     */
    public static final class ClientStatusMessage extends Message {

        /**
         * All possible cinet states.
         */
        public enum ClientStatus {
            /**
             * The state if initializing was not possible. The client has to take action to send a valid start action. Maybe the user has to be asced for proper
             * login information.
             */
            uninitialized,

            /**
             * A user is authenticated, the client session is connected to a store.
             */
            initialized;
        }

        /**
         * The next client state.
         */
        private ClientStatus clientStatus;

        /**
         * The text to send.
         */
        private String statusText;

        /**
         * Creates a new message.
         * 
         * @param clientStatus the client status to send to the client.
         * @param statusText The text to send to the client.
         */
        public ClientStatusMessage(ClientStatus clientStatus, String statusText) {
            super(Type.status);
            this.statusText = statusText;
            this.clientStatus = clientStatus;
        }

    }

    /**
     * Message about an error to inform the client.
     */
    public static final class ErrorMessage extends Message {

        /**
         * The message about the error.
         */
        private String message;

        /**
         * The trace about the error.
         */
        private String trace;

        /**
         * Creates a new message.
         * 
         * @param message the message about the error.
         * @param trace the trace about the error.
         */
        public ErrorMessage(String message, String trace) {
            super(Type.error);
            this.message = message;
            this.trace = trace;
        }

        /**
         * Creates a new message.
         * 
         * @param exception the exception to extract the message and trace information from.
         */
        public ErrorMessage(Exception exception) {
            this(exception.getMessage(), createTrace(exception));
        }

        /**
         * Creates the trace as string.
         * 
         * @param exception the exception to create the trace from.
         * @return the trace as string.
         */
        private static String createTrace(Exception exception) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            try {
                exception.printStackTrace(printWriter);
                return stringWriter.toString();
            } finally {
                printWriter.close();
                try {
                    stringWriter.close();
                } catch (IOException e) {
                    // close silently
                }
            }
        }

    }

    /**
     * A store sync message about updates and removes of elements.
     */
    public abstract static class StoreSyncMessage extends Message {
        /**
         * The type of the element.
         */
        private final String elementType;

        /**
         * The key parts of the element.
         */
        private final String[] elementKeyParts;

        /**
         * The domain key of the element.
         */
        private final List<String> domainKeyParts;

        /**
         * Creates a new message.
         * 
         * @param type The message type to send.
         * @param domainKey The domain key of the element.
         * @param elementKey The key of the element.
         */
        private StoreSyncMessage(Type type, DomainKey domainKey, ElementKey elementKey) {
            super(type);
            this.domainKeyParts = domainKey.asList();
            int length = elementKey.length();
            Iterator<String> iter = elementKey.iterator();
            if (length < 2) {
                throw new IllegalArgumentException("elementKey is not valid");
            } else {
                this.elementKeyParts = new String[length - 1];
                this.elementType = iter.next();
                for (int i = 0; i < length - 1; i++) {
                    this.elementKeyParts[i] = iter.next();
                }
            }
        }
    }

    /**
     * A remove message for the client.
     */
    public static final class RemoveMessage extends StoreSyncMessage {

        /**
         * Creates a new message.
         * 
         * @param domainKey The domain key of the element.
         * @param elementKey The key of the element.
         */
        public RemoveMessage(DomainKey domainKey, ElementKey elementKey) {
            super(Type.remove, domainKey, elementKey);
        }
    }

    /**
     * A update message for the client.
     */
    public static final class UpdateMessage extends StoreSyncMessage {

        /**
         * The element to send.
         */
        private final StoreElement element;

        /**
         * Creates a new message.
         * 
         * @param domainKey The domain key of the element.
         * @param elementKey The key of the element.
         * @param element The element to send.
         */
        public UpdateMessage(DomainKey domainKey, ElementKey elementKey, StoreElement element) {
            super(Type.update, domainKey, elementKey);
            this.element = element;
        }
    }
}
