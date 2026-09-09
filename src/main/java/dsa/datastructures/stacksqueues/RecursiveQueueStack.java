package dsa.datastructures.stacksqueues;

import java.util.NoSuchElementException;

/**
 * Stack implementation using a single queue
 * and the recursion call stack.
 *
 * @param <T> the type of elements stored in the stack
 */
public class RecursiveQueueStack<T> implements Stack<T> {

    // -------- fields --------

    private final Queue<T> queue;


    // -------- constructor --------

    /**
     * Creates an empty stack.
     * <p>
     * Time Complexity: O(1)
     */
    public RecursiveQueueStack() {
        queue = new LinkedQueue<>();
    }


    // -------- methods --------

    /**
     * Adds an element to the stack.
     * <p>
     * Time Complexity: O(1)
     */
    @Override
    public void push(T element) {
        queue.enqueue(element);
    }

    /**
     * Removes and returns the most recently inserted element.
     * <p>
     * Time Complexity: O(n)
     * Space Complexity: O(n) because of recursion
     */
    @Override
    public T pop() {
        if (isEmpty())
            throw new NoSuchElementException("Stack is empty");

        return removeLast(queue.size());
    }

    private T removeLast(int remaining) {
        T current = queue.dequeue();

        if (remaining == 1)
            return current;

        T result = removeLast(remaining - 1);
        queue.enqueue(current);
        return result;
    }

    /**
     * Returns the top element without removing it.
     * <p>
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    @Override
    public T peek() {
        if (isEmpty())
            throw new NoSuchElementException("Stack is empty");

        T top = removeLast(queue.size());
        queue.enqueue(top);
        return top;
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}