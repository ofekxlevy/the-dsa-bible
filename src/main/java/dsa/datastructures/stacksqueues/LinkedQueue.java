package dsa.datastructures.stacksqueues;

import dsa.datastructures.lists.SinglyLinkedList;
import java.util.NoSuchElementException;

/**
 * Queue implementation backed by a SinglyLinkedList.
 * Elements are added to the end of the list and removed from the beginning.
 *
 * @param <T> the type of elements stored in the queue
 */
public class LinkedQueue<T> implements Queue<T> {

    // -------- fields --------

    private final SinglyLinkedList<T> elements;


    // -------- constructor --------

    /**
     * Creates an empty queue.
     *
     * Time Complexity: O(1)
     */
    public LinkedQueue() {
        elements = new SinglyLinkedList<>();
    }

    /**
     * Adds an element to the end of the queue.
     *
     * Time Complexity: O(1)
     */
    @Override
    public void enqueue(T element) {
        elements.add(element);
    }

    /**
     * Removes and returns the element at the front of the queue.
     *
     * Time Complexity: O(1)
     */
    @Override
    public T dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue is empty");

        return elements.removeFirst();
    }

    /**
     * Returns the element at the front without removing it.
     *
     * Time Complexity: O(1)
     */
    @Override
    public T peek() {
        if (isEmpty())
            throw new NoSuchElementException("Queue is empty");

        return elements.getFirst();
    }

    /**
     * Returns the number of elements in the queue.
     *
     * Time Complexity: O(1)
     */
    @Override
    public int size() {
        return elements.size();
    }

    /**
     * Checks whether the queue is empty.
     *
     * Time Complexity: O(1)
     */
    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }
}