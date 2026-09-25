package dsa.datastructures.iterators;

/**
 * Represents an object that allows sequential traversal
 * over a collection of elements.
 *
 * @param <T> the type of elements returned by the iterator
 */
public interface Iterator<T> {

    /**
     * Checks whether another element is available.
     *
     * @return true if another element exists, false otherwise
     */
    boolean hasNext();

    /**
     * Returns the next element in the traversal.
     *
     * @return the next element
     */
    T next();
}