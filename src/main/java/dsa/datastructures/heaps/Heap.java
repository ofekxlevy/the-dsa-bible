package dsa.datastructures.heaps;

/**
 * Represents the Heap abstract data type.
 * A heap stores elements according to a priority ordering and allows
 * access to the element with the highest priority.
 * The exact ordering and implementation are determined by the concrete
 * heap implementation.
 *
 * @param <T> the type of elements stored in the heap
 */
public interface Heap<T> {

    /**
     * Inserts a value into the heap.
     *
     * @param value the value to insert
     */
    void insert(T value);

    /**
     * Returns the highest-priority element without removing it.
     *
     * @return the highest-priority element
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    T peek();

    /**
     * Removes and returns the highest-priority element.
     *
     * @return the removed highest-priority element
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    T extract();

    /**
     * Returns the number of elements stored in the heap.
     *
     * @return the number of elements in the heap
     */
    int size();

    /**
     * Checks whether the heap is empty.
     *
     * @return true if the heap contains no elements, false otherwise
     */
    boolean isEmpty();
}