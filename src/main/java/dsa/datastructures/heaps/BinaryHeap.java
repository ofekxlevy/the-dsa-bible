package dsa.datastructures.heaps;

import dsa.datastructures.lists.DynamicArray;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * A binary heap implementation of the Heap interface.
 * The heap is represented using a dynamic array and maintains the
 * complete binary tree property.
 * <p>
 * For an element at index i:
 * - Parent: (i - 1) / 2
 * - Left child: 2 * i + 1
 * - Right child: 2 * i + 2
 * <p>
 * The ordering of the heap is determined by the provided Comparator.
 * Therefore, the same implementation can be used as either a min-heap
 * or a max-heap.
 * <p>
 * Examples:
 * Min-heap: new BinaryHeap<>(Comparator.naturalOrder());
 * Max-heap: new BinaryHeap<>(Comparator.reverseOrder());
 *
 * @param <T> the type of elements stored in the heap
 */
public class BinaryHeap<T> implements Heap<T> {

    // -------- fields --------

    private final DynamicArray<T> elements;
    private final Comparator<T> comparator;


    // -------- constructor --------

    /**
     * Creates an empty binary heap using the given comparator.
     * <p>
     * Time Complexity: O(1)
     *
     * @param comparator determines the priority ordering of elements
     */
    public BinaryHeap(Comparator<T> comparator) {
        if (comparator == null)
            throw new IllegalArgumentException("Comparator cannot be null");

        this.elements = new DynamicArray<>();
        this.comparator = comparator;
    }


    // -------- methods --------

    /**
     * Inserts a value into the heap while maintaining the heap property.
     * The value is first inserted at the end of the array and then moved
     * upward until it reaches its correct position.
     * <p>
     * Time Complexity: O(log n)
     *
     * @param value the value to insert
     */
    @Override
    public void insert(T value) {
        elements.add(value);
        heapifyUp(elements.size() - 1);
    }

    /**
     * Returns the highest-priority element without removing it.
     * The root of the heap is always stored at index 0.
     * <p>
     * Time Complexity: O(1)
     *
     * @return the highest-priority element
     * @throws NoSuchElementException if the heap is empty
     */
    @Override
    public T peek() {
        if (isEmpty())
            throw new NoSuchElementException("Heap is empty");

        return elements.get(0);
    }

    /**
     * Removes and returns the highest-priority element.
     * The root is replaced by the last element in the heap.
     * The new root is then moved downward until the heap property
     * is restored.
     * <p>
     * Time Complexity: O(log n)
     *
     * @return the removed highest-priority element
     * @throws NoSuchElementException if the heap is empty
     */
    @Override
    public T extract() {
        if (isEmpty())
            throw new NoSuchElementException("Heap is empty");

        T root = elements.get(0);
        int lastIndex = elements.size() - 1;

        if (lastIndex == 0) {
            elements.remove(0);
            return root;
        }

        T last = elements.get(lastIndex);

        elements.set(0, last);
        elements.remove(lastIndex);

        heapifyDown(0);

        return root;
    }

    /**
     * Returns the number of elements stored in the heap.
     * <p>
     * Time Complexity: O(1)
     *
     * @return the number of elements
     */
    @Override
    public int size() {
        return elements.size();
    }

    /**
     * Checks whether the heap is empty.
     * <p>
     * Time Complexity: O(1)
     *
     * @return true if the heap is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Restores the heap property by moving an element upward.
     * The element is repeatedly compared with its parent.
     * If it has higher priority than its parent, the two are swapped.
     * <p>
     * Time Complexity: O(log n)
     *
     * @param index the starting index
     */
    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = parent(index);

            if (!hasHigherPriority(index, parentIndex))
                break;

            swap(index, parentIndex);
            index = parentIndex;
        }
    }

    /**
     * Restores the heap property by moving an element downward.
     * At each step, the element is compared with its children.
     * If one of the children has higher priority, the element is
     * swapped with the highest-priority child.
     * <p>
     * Time Complexity: O(log n)
     *
     * @param index the starting index
     */
    private void heapifyDown(int index) {
        while (hasLeftChild(index)) {

            int higherPriorityChild = leftChild(index);
            int rightIndex = rightChild(index);

            if (rightIndex < elements.size() && hasHigherPriority(rightIndex, higherPriorityChild))
                higherPriorityChild = rightIndex;

            if (!hasHigherPriority(higherPriorityChild, index))
                break;

            swap(index, higherPriorityChild);
            index = higherPriorityChild;
        }
    }

    /**
     * Checks whether the element at the first index has higher priority
     * than the element at the second index.
     *
     * @param firstIndex the first index
     * @param secondIndex the second index
     * @return true if the first element has higher priority
     */
    private boolean hasHigherPriority(int firstIndex, int secondIndex) {
        return comparator.compare(elements.get(firstIndex), elements.get(secondIndex)) < 0;
    }

    /**
     * Returns the index of the parent of the given element.
     *
     * @param index the index of the element
     * @return the parent's index
     */
    private int parent(int index) {
        return (index - 1) / 2;
    }

    /**
     * Returns the index of the left child.
     *
     * @param index the index of the parent
     * @return the left child's index
     */
    private int leftChild(int index) {
        return 2 * index + 1;
    }

    /**
     * Returns the index of the right child.
     *
     * @param index the index of the parent
     * @return the right child's index
     */
    private int rightChild(int index) {
        return 2 * index + 2;
    }

    /**
     * Checks whether the element has a left child.
     * Since a complete binary tree cannot have a right child without
     * a left child, this is also sufficient to determine whether the
     * element has any children.
     *
     * @param index the index of the element
     * @return true if a left child exists
     */
    private boolean hasLeftChild(int index) {
        return leftChild(index) < elements.size();
    }

    /**
     * Swaps two elements in the underlying array.
     * <p>
     * Time Complexity: O(1)
     *
     * @param firstIndex the first index
     * @param secondIndex the second index
     */
    private void swap(int firstIndex, int secondIndex) {
        T temp = elements.get(firstIndex);

        elements.set(firstIndex, elements.get(secondIndex));
        elements.set(secondIndex, temp);
    }
}