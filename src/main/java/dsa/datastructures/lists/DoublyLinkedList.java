package dsa.datastructures.lists;

import java.util.Objects;

public class DoublyLinkedList<T> implements List<T> {

    // -------- nested class: Node --------

    /**
     * Represents a single node in the doubly linked list.
     * Each node stores an element and references to both
     * the next and previous nodes.
     * The first node has prev == null,
     * and the last node has next == null.
     */
    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;

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
     * Creates an empty doubly linked list.
     * <p>
     * Time Complexity: O(1)
     */
    public DoublyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }


    // -------- methods --------

    /**
     * Adds an element to the end of the list.
     * <p>
     * Time Complexity: O(1)
     */
    @Override
    public void add(T element) {
        Node<T> newNode = new Node<>(element);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        }
        else {
            newNode.prev = tail;
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
     * because we may need to traverse the list.
     */
    @Override
    public void add(int index, T element) {
        checkPositionIndex(index);

        if (index == size) {
            add(element);
            return;
        }

        Node<T> current = getNode(index);
        Node<T> newNode = new Node<>(element);

        newNode.next = current;
        newNode.prev = current.prev;

        if (current.prev == null)
            head = newNode;
        else
            current.prev.next = newNode;

        current.prev = newNode;
        size++;
    }

    /**
     * Returns the element at the specified index.
     * <p>
     * Time Complexity: O(n)
     * Traversal begins from either head or tail,
     * depending on which is closer to the requested index.
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
     * because locating the node may require traversal.
     * Once the node is found, unlinking it is O(1).
     */
    @Override
    public T remove(int index) {
        checkElementIndex(index);

        Node<T> node = getNode(index);
        T removedElement = node.data;

        unlink(node);

        return removedElement;
    }

    /**
     * Removes the first occurrence of the specified element.
     * <p>
     * Time Complexity: O(n)
     */
    @Override
    public boolean remove(T element) {
        Node<T> current = head;

        while (current != null) {
            if (Objects.equals(current.data, element)) {
                unlink(current);
                return true;
            }

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
     * Returns -1 if the element does not exist.
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
     * Removing the references from head and tail makes the chain unreachable from the list,
     * allowing Java's garbage collector to reclaim the nodes.
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
     * Returns the last element in the list.
     * <p>
     * Time Complexity: O(1)
     */
    public T getLast() {
        if (isEmpty())
            throw new IllegalStateException("List is empty");

        return tail.data;
    }

    /**
     * Adds an element to the beginning of the list.
     * <p>
     * Time Complexity: O(1)
     */
    public void addFirst(T element) {
        Node<T> newNode = new Node<>(element);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        }
        else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }

        size++;
    }

    /**
     * Adds an element to the end of the list.
     * <p>
     * Time Complexity: O(1)
     */
    public void addLast(T element) {
        Node<T> newNode = new Node<>(element);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        }
        else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    /**
     * Removes and returns the first element in the list.
     * <p>
     * Time Complexity: O(1)
     */
    public T removeFirst() {
        if (isEmpty())
            throw new IllegalStateException("List is empty");

        T removedElement = head.data;

        if (size == 1) {
            head = null;
            tail = null;
        }
        else {
            head = head.next;
            head.prev = null;
        }

        size--;
        return removedElement;
    }

    /**
     * Removes and returns the last element in the list.
     * <p>
     * Time Complexity: O(1)
     */
    public T removeLast() {
        if (isEmpty())
            throw new IllegalStateException("List is empty");

        T removedElement = tail.data;

        if (size == 1) {
            head = null;
            tail = null;
        }
        else {
            tail = tail.prev;
            tail.next = null;
        }

        size--;
        return removedElement;
    }


    // -------- helpers --------

    /**
     * Returns the node at the specified index.
     * If the index is in the first half, traversal starts from the head.
     * Otherwise, it starts from the tail.
     * <p>
     * Time Complexity: O(n).
     */
    private Node<T> getNode(int index) {
        if (index < size / 2) {
            Node<T> current = head;

            for (int i = 0; i < index; i++)
                current = current.next;

            return current;
        }

        Node<T> current = tail;

        for (int i = size - 1; i > index; i--)
            current = current.prev;

        return current;
    }

    /**
     * Disconnects a node from the list.
     * <p>
     * Time Complexity: O(1)
     * because the node already contains references to both its predecessor and successor.
     */
    private void unlink(Node<T> node) {
        if (node.prev == null)
            head = node.next;
        else
            node.prev.next = node.next;

        if (node.next == null)
            tail = node.prev;
        else
            node.next.prev = node.prev;

        size--;
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
}