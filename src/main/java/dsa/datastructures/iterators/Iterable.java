package dsa.datastructures.iterators;

/**
 * Represents a collection whose elements can be traversed
 * using our custom Iterator.
 *
 * @param <T> the type of elements in the collection
 */
public interface Iterable<T> {

    /**
     * Creates an iterator for traversing the elements
     * of this collection.
     *
     * @return an iterator over the elements
     */
    Iterator<T> iterator();
}