package dsa.datastructures.stacksqueues;

import java.util.NoSuchElementException;

/**
 * Stack implementation using two queues.
 * The queues are arranged so that the front of the main queue
 * always represents the top of the stack.
 *
 * @param <T> the type of elements stored in the stack
 */
public class QueueStack<T> implements Stack<T> {

    // -------- fields --------

    private Queue<T> mainQueue;
    private Queue<T> helperQueue;


    // -------- constructor --------

    /**
     * Creates an empty stack.
     *
     * Time Complexity: O(1)
     */
    public QueueStack() {
        mainQueue = new LinkedQueue<>();
        helperQueue = new LinkedQueue<>();
    }


    // -------- methods --------

    /**
     * Adds an element to the top of the stack.
     *
     * Time Complexity: O(n)
     */
    @Override
    public void push(T element) {

        // Add the new element to the empty helper queue.
        helperQueue.enqueue(element);

        // Move all existing elements after the new element.
        while (!mainQueue.isEmpty())
            helperQueue.enqueue(mainQueue.dequeue());

        // Swap the queues.
        Queue<T> temp = mainQueue;
        mainQueue = helperQueue;
        helperQueue = temp;
    }

    /**
     * Removes and returns the top element of the stack.
     *
     * Time Complexity: O(1)
     */
    @Override
    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }

        return mainQueue.dequeue();
    }

    /**
     * Returns the top element without removing it.
     *
     * Time Complexity: O(1)
     */
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }

        return mainQueue.peek();
    }

    /**
     * Returns the number of elements in the stack.
     *
     * Time Complexity: O(1)
     */
    @Override
    public int size() {
        return mainQueue.size();
    }

    /**
     * Checks whether the stack is empty.
     *
     * Time Complexity: O(1)
     */
    @Override
    public boolean isEmpty() {
        return mainQueue.isEmpty();
    }
}