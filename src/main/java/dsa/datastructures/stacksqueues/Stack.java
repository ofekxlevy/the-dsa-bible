package dsa.datastructures.stacksqueues;

/**
 * Represents a generic stack data structure.
 * A stack follows the LIFO principle:
 * Last In, First Out.
 *
 * @param <T> the type of elements stored in the stack
 */
public interface Stack<T> {

    /**
     * Adds an element to the top of the stack.
     *
     * @param element the element to add
     */
    void push(T element);

    /**
     * Removes and returns the top element of the stack.
     *
     * @return the removed top element
     * @throws java.util.NoSuchElementException if the stack is empty
     */
    T pop();

    /**
     * Returns the top element without removing it.
     *
     * @return the top element
     * @throws java.util.NoSuchElementException if the stack is empty
     */
    T peek();

    /**
     * Returns the number of elements in the stack.
     *
     * @return the stack size
     */
    int size();

    /**
     * Checks whether the stack is empty.
     *
     * @return true if the stack is empty, otherwise false
     */
    boolean isEmpty();
}