package dsa.datastructures.lists;

import java.util.Objects;
import dsa.datastructures.iterators.Iterator;

public class SinglyLinkedList<T> implements List<T> {

    // -------- nested class: Node --------

    /**
     * Represents a single node in the singly linked list.
     * Each node stores an element and a reference to the next node.
     * The last node points to null.
     */
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }


    // -------- fields --------

    private Node<T> head;
    private Node<T> tail;
    private int size;


    // -------- constructor --------

    /**
     * Creates an empty singly linked list.
     * <p>
     * Time Complexity: O(1)
     */
    public SinglyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }


    // -------- methods --------

    /**
     * Adds an element to the end of the list.
     * <p>
     * Time Complexity: O(1)
     * because we maintain a reference to the tail.
     */
    @Override
    public void add(T element) {
        Node<T> newNode = new Node<>(element);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        }
        else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    /**
     * Inserts an element at the specified index.
     * Valid indices: 0 <= index <= size.
     * <p>
     * Time Complexity: O(n)
     * because we may need to traverse the list to find
     * the node before the insertion position.
     */
    @Override
    public void add(int index, T element) {
        checkPositionIndex(index);

        if (index == size) {
            add(element);
            return;
        }

        Node<T> newNode = new Node<>(element);

        if (index == 0) {
            newNode.next = head;
            head = newNode;
            size++;
            return;
        }

        Node<T> previous = getNode(index - 1);

        newNode.next = previous.next;
        previous.next = newNode;

        size++;
    }

    /**
     * Returns the element at the specified index.
     * <p>
     * Time Complexity: O(n)
     * because linked lists do not support direct random access.
     */
    @Override
    public T get(int index) {
        checkElementIndex(index);

        return getNode(index).data;
    }

    /**
     * Replaces the element at the specified index
     * and returns the previous element.
     * <p>
     * Time Complexity: O(n)
     * because we must traverse the list to reach the node.
     */
    @Override
    public T set(int index, T element) {
        checkElementIndex(index);

        Node<T> node = getNode(index);

        T oldElement = node.data;
        node.data = element;

        return oldElement;
    }

    /**
     * Removes and returns the element at the specified index.
     * <p>
     * Time Complexity: O(n)
     */
    @Override
    public T remove(int index) {
        checkElementIndex(index);

        if (index == 0) {
            T removedElement = head.data;

            head = head.next;
            size--;

            if (size == 0) {
                tail = null;
            }

            return removedElement;
        }

        Node<T> previous = getNode(index - 1);
        Node<T> removedNode = previous.next;

        previous.next = removedNode.next;

        if (removedNode == tail) {
            tail = previous;
        }

        size--;

        return removedNode.data;
    }

    /**
     * Removes the first occurrence of the specified element.
     * <p>
     * Time Complexity: O(n)
     * because the list may need to be scanned completely.
     */
    @Override
    public boolean remove(T element) {
        Node<T> current = head;
        Node<T> previous = null;

        while (current != null) {
            if (Objects.equals(current.data, element)) {

                if (previous == null)
                    head = current.next;
                else
                    previous.next = current.next;

                if (current == tail)
                    tail = previous;

                size--;
                return true;
            }

            previous = current;
            current = current.next;
        }

        return false;
    }

    /**
     * Returns the number of elements in the list.
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
     */
    @Override
    public boolean contains(T element) {
        return indexOf(element) != -1;
    }

    /**
     * Returns the index of the first occurrence of the element.
     * Returns -1 if it does not exist.
     * <p>
     * Time Complexity: O(n)
     */
    @Override
    public int indexOf(T element) {
        Node<T> current = head;
        int index = 0;

        while (current != null) {
            if (Objects.equals(current.data, element))
                return index;

            current = current.next;
            index++;
        }

        return -1;
    }

    /**
     * Removes all elements from the list.
     * <p>
     * Time Complexity: O(1)
     * Once head and tail no longer reference the nodes,
     * the nodes become unreachable and can be collected
     * by Java's garbage collector.
     */
    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns the first element in the list.
     * <p>
     * Time Complexity: O(1)
     */
    public T getFirst() {
        if (isEmpty())
            throw new IllegalStateException("List is empty");

        return head.data;
    }

    /**
     * Adds an element to the beginning of the list.
     * <p>
     * Time Complexity: O(1)
     */
    public void addFirst(T element) {
        Node<T> newNode = new Node<>(element);
        newNode.next = head;
        head = newNode;

        if (size == 0)
            tail = newNode;

        size++;
    }

    /**
     * Removes and returns the first element in the list.
     * <p>
     * Time Complexity: O(1)
     */
    public T removeFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }

        T removedElement = head.data;

        head = head.next;
        size--;

        if (size == 0) {
            tail = null;
        }

        return removedElement;
    }


    // -------- helpers --------

    /**
     * Returns the node at the specified index.
     * <p>
     * Time Complexity: O(n)
     */
    private Node<T> getNode(int index) {
        Node<T> current = head;

        for (int i = 0; i < index; i++)
            current = current.next;

        return current;
    }

    /**
     * Checks whether the index refers to an existing element.
     * Valid range: 0 <= index < size.
     * <p>
     * Time Complexity: O(1)
     */
    private void checkElementIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    /**
     * Checks whether the index is a valid insertion position.
     * Valid range: 0 <= index <= size.
     * <p>
     * Time Complexity: O(1)
     */
    private void checkPositionIndex(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }


    // -------- Iterator --------

    /**
     * Returns an iterator that traverses the list from head to tail.
     * The iterator starts at the first node and advances by following
     * the next reference of each node.
     * Creating the iterator: O(1)
     *
     * @return an iterator over the elements of the list
     */
    @Override
    public Iterator<T> iterator() {
        return new SinglyLinkedListIterator();
    }

    /**
     * Iterator for the singly linked list.
     * The iterator keeps a reference to the next node whose value
     * should be returned.
     * Since moving to the next element only requires following
     * one next reference, both hasNext() and next() run in O(1).
     * Traversing the entire list therefore takes O(n).
     */
    private class SinglyLinkedListIterator implements Iterator<T> {

        /**
         * The next node to be returned by the iterator.
         */
        private Node<T> current;

        /**
         * Creates an iterator positioned at the beginning of the list.
         */
        public SinglyLinkedListIterator() {
            current = head;
        }

        /**
         * Checks whether another element remains in the traversal.
         *
         * @return true if another element exists, false otherwise
         *
         * Time Complexity: O(1)
         */
        @Override
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the current element and advances the iterator
         * to the next node.
         *
         * @return the next element in the list
         *
         * @throws IllegalStateException if no elements remain
         *
         * Time Complexity: O(1)
         */
        @Override
        public T next() {
            if (!hasNext())
                throw new IllegalStateException("No more elements in the iterator.");

            T value = current.data;
            current = current.next;

            return value;
        }
    }
}