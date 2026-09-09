package dsa.datastructures.stacksqueues;

/**
 * Represents a generic queue data structure.
 * A queue follows the FIFO principle:
 * First In, First Out.
 *
 * @param <T> the type of elements stored in the queue
 */
public interface Queue<T> {

    /**
     * Adds an element to the end of the queue.
     *
     * @param element the element to add
     */
    void enqueue(T element);

    /**
     * Removes and returns the element at the front of the queue.
     *
     * @return the removed front element
     * @throws java.util.NoSuchElementException if the queue is empty
     */
    T dequeue();

    /**
     * Returns the element at the front without removing it.
     *
     * @return the front element
     * @throws java.util.NoSuchElementException if the queue is empty
     */
    T peek();

    /**
     * Returns the number of elements in the queue.
     *
     * @return the queue size
     */
    int size();

    /**
     * Checks whether the queue is empty.
     *
     * @return true if the queue is empty, otherwise false
     */
    boolean isEmpty();
}