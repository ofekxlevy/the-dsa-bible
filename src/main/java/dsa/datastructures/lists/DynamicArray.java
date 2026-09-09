package dsa.datastructures.lists;

import java.util.Objects;

public class DynamicArray<T> implements List<T> {


    // -------- fields --------

    private static final int DEFAULT_CAPACITY = 10;
    private T[] elements;
    private int size;


    // -------- constructors --------
    /**
     * Creates an empty DynamicArray with the default capacity.
     * <p>
     * Time Complexity: O(1)
     */
    @SuppressWarnings("unchecked")
    public DynamicArray() {
        elements = (T[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /**
     * Creates an empty DynamicArray with the specified initial capacity.
     * <p>
     * Time Complexity: O(n), where n is the requested capacity.
     */
    @SuppressWarnings("unchecked")
    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Capacity cannot be negative");

        elements = (T[]) new Object[initialCapacity];
        size = 0;
    }


    // -------- methods --------

    /**
     * Adds an element to the end of the list.
     * The internal array is expanded if necessary.
     * <p>
     * Time Complexity:
     * O(1) amortized.
     * O(n) worst case when the internal array must be resized.
     */
    @Override
    public void add(T element) {
        ensureCapacity();

        elements[size] = element;
        size++;
    }

    /**
     * Inserts an element at the specified index.
     * Elements from that index onward are shifted one position to the right.
     * Valid indices are from 0 to size, inclusive.
     * <p>
     * Time Complexity: O(n)
     * because elements may need to be shifted.
     */
    @Override
    public void add(int index, T element) {
        checkPositionIndex(index);
        ensureCapacity();

        for (int i = size; i > index; i--)
            elements[i] = elements[i - 1];

        elements[index] = element;
        size++;
    }

    /**
     * Returns the element at the specified index.
     * <p>
     * Time Complexity: O(1)
     * because arrays support direct random access.
     */
    @Override
    public T get(int index) {
        checkElementIndex(index);
        return elements[index];
    }

    /**
     * Replaces the element at the specified index and returns the previous element.
     * <p>
     * Time Complexity: O(1)
     */
    @Override
    public T set(int index, T element) {
        checkElementIndex(index);

        T oldElement = elements[index];
        elements[index] = element;

        return oldElement;
    }

    /**
     * Removes and returns the element at the specified index.
     * All elements after it are shifted one position to the left.
     * <p>
     * Time Complexity: O(n)
     * because elements may need to be shifted.
     */
    @Override
    public T remove(int index) {
        checkElementIndex(index);

        T removedElement = elements[index];

        for (int i = index; i < size - 1; i++)
            elements[i] = elements[i + 1];

        elements[size - 1] = null;
        size--;

        return removedElement;
    }

    /**
     * Removes the first occurrence of the specified element.
     * Returns true if an element was removed, otherwise false.
     * <p>
     * Time Complexity: O(n),
     * because the element must first be searched for and
     * elements may then need to be shifted.
     */
    @Override
    public boolean remove(T element) {
        int index = indexOf(element);
        if (index == -1)
            return false;

        remove(index);
        return true;
    }

    /**
     * Returns the number of elements currently stored in the list.
     * <p>
     * Time Complexity: O(1)
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns true if the list contains no elements.
     * <p>
     * Time Complexity: O(1)
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns true if the specified element exists in the list.
     * <p>
     * Time Complexity: O(n)
     * because in the worst case every element must be checked.
     */
    @Override
    public boolean contains(T element) {
        return indexOf(element) != -1;
    }

    /**
     * Returns the index of the first occurrence of the specified element.
     * Returns -1 if the element does not exist in the list.
     * <p>
     * Time Complexity: O(n)
     * because the array may need to be scanned completely.
     */
    @Override
    public int indexOf(T element) {
        for (int i = 0; i < size; i++)
            if (Objects.equals(elements[i], element))
                return i;

        return -1;
    }

    /**
     * Removes all elements from the list.
     * <p>
     * Time Complexity: O(n)
     */
    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            elements[i] = null;

        size = 0;
    }


    // -------- helpers --------

    /**
     * Ensures that there is enough space for at least one more element.
     * If the internal array is full, its capacity is doubled.
     * <p>
     * Time Complexity:
     * O(1) if no resize is required.
     * O(n) if the array must be resized and copied.
     */
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size < elements.length)
            return;

        int newCapacity;

        if (elements.length == 0)
            newCapacity = 1;
         else
            newCapacity = elements.length * 2;

        T[] newElements = (T[]) new Object[newCapacity];

        for (int i = 0; i < size; i++)
            newElements[i] = elements[i];

        elements = newElements;
    }

    /**
     * Verifies that an index refers to an existing element.
     * Valid range: 0 <= index < size
     * <p>
     * Time Complexity: O(1)
     */
    private void checkElementIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    /**
     * Verifies that an index is a valid insertion position.
     * Valid range: 0 <= index <= size
     * Notice that index == size is valid because it represents
     * inserting an element at the end of the list.
     * <p>
     * Time Complexity: O(1)
     */
    private void checkPositionIndex(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
}