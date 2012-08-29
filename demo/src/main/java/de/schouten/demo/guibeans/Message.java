package de.schouten.demo.guibeans;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import de.schouten.store.StoreElement;

/**
 * A message that should be displayed in the gui.
 */
public class Message implements StoreElement, Comparable<Message> {

    /**
     * The timestamp (and key) for the message.
     */
    private long time;

    /**
     * The date as string.
     */
    private String dateString;

    /**
     * Is this a urgent message.
     */
    private boolean urgent;

    /**
     * The message text.
     */
    private String message;

    /**
     * @param time The timestamp (and key) for the message.
     * @param message The message text.
     */
    public Message(long time, String message, boolean urgent) {
        super();
        this.time = time;
        this.dateString = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, new Locale("de")).format(new Date(time));
        this.message = message;
        this.urgent = urgent;
    }

    /**
     * @see de.schouten.store.StoreElement#getElementKeyParts()
     */
    @Override
    public Object[] getElementKeyParts() {
        return new Object[] {time};
    }

    @Override
    public Message clone() {
        return new Message(time, message, urgent);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + (int) (time ^ (time >>> 32));
        result = prime * result + (urgent ? 1231 : 1237);
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Message other = (Message) obj;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (time != other.time)
            return false;
        if (urgent != other.urgent)
            return false;
        return true;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Message [dateString=" + dateString + ", urgent=" + urgent + ", message=" + message + "]";
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Message o) {
        return Long.valueOf(this.time).compareTo(o.time);
    }

}
