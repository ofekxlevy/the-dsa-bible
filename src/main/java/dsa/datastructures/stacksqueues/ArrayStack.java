package dsa.datastructures.stacksqueues;

import dsa.datastructures.lists.DynamicArray;
import java.util.NoSuchElementException;

/**
 * Stack implementation backed by a DynamicArray.
 * The top of the stack is stored at the end of the array.
 *
 * @param <T> the type of elements stored in the stack
 */
public class ArrayStack<T> implements Stack<T> {

    // -------- fields --------

    private final DynamicArray<T> elements;


    // -------- constructor --------

    /**
     * Creates an empty stack.
     * <p>
     * Time Complexity: O(1)
     */
    public ArrayStack() {
        elements = new DynamicArray<>();
    }

    // -------- methods --------

    /**
     * Adds an element to the top of the stack.
     * <p>
     * Time Complexity: O(1) amortized
     */
    @Override
    public void push(T element) {
        elements.add(element);
    }

    /**
     * Removes and returns the top element of the stack.
     * <p>
     * Time Complexity: O(1)
     */
    @Override
    public T pop() {
        if (isEmpty())
            throw new NoSuchElementException("Stack is empty");

        return elements.remove(elements.size() - 1);
    }

    /**
     * Returns the top element without removing it.
     * <p>
     * Time Complexity: O(1)
     */
    @Override
    public T peek() {
        if (isEmpty())
            throw new NoSuchElementException("Stack is empty");

        return elements.get(elements.size() - 1);
    }

    /**
     * Returns the number of elements in the stack.
     * <p>
     * Time Complexity: O(1)
     */
    @Override
    public int size() {
        return elements.size();
    }

    /**
     * Checks whether the stack is empty.
     * <p>
     * Time Complexity: O(1)
     */
    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }
}