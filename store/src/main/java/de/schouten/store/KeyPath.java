package de.schouten.store;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A path like structure that is combined to an immutable key.
 */
abstract class KeyPath implements Comparable<KeyPath>, Iterable<String>, Serializable {

    /**
     * The path parts that describe the path of the key.
     */
    protected String[] pathParts;

    /**
     * Construct an immutable key path out of its parts.
     * 
     * @param parts the path parts to the key. A null key is not allowed!
     * @throws NullPointerException if a key is null.
     */
    public KeyPath(String... parts) {
        pathParts = parts;
        for (String string : this) {
            if (string == null) {
                throw new NullPointerException("Parts must not contain a null.");
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return asList().iterator();
    }

    /**
     * @return all the key path elements as list.
     */
    public List<String> asList() {
        return Arrays.asList(pathParts);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : this) {
            stringBuilder.append('/').append(key);
        }
        return stringBuilder.toString();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KeyPath) {
            return Arrays.equals(pathParts, ((KeyPath) obj).pathParts);
        } else {
            return false;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(pathParts);
    }

    /**
     * @return the length of the key path.
     */
    public int length() {
        return pathParts.length;
    }

    /**
     * @param other the other key path to check.
     * @return true, if the other key path is contained by the path of this key path, or if they are equals.
     */
    public boolean isChild(KeyPath other) {
        Iterator<String> thisIter = this.iterator();
        Iterator<String> otherIter = other.iterator();
        while (thisIter.hasNext()) {
            if (!otherIter.hasNext()) {
                // other iter is too short!
                return false;
            }
            if (!thisIter.next().equals(otherIter.next())) {
                // path is not the same!
                return false;
            }
        }
        return true; // yes - all sub path elements are the same!
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(KeyPath o) {
        return toString().compareTo(o.toString());
    }

}
