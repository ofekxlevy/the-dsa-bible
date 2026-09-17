package dsa.algorithms.sorting;

import dsa.datastructures.heaps.BinaryHeap;

/**
 * Implements the Heap Sort algorithm using the BinaryHeap
 * data structure implemented in this project.
 * The algorithm works in two phases:
 * 1. Build a min-heap:
 *    Insert all elements of the input array into a BinaryHeap.
 *    The heap uses the natural ordering of T, so the smallest
 *    element always has the highest priority.
 * 2. Extract the elements:
 *    Repeatedly extract the minimum element from the heap
 *    and place it back into the original array from left to right.
 * <p>
 * Properties:
 * - Comparison-based
 * - Uses the BinaryHeap data structure
 * - Not stable
 * - Not in-place in this implementation
 * <p>
 * Time Complexity:
 * Building the heap:
 * - n insertions
 * - Each insertion takes O(log n)
 * - Total: O(n log n)
 * Extracting all elements:
 * - n extractions
 * - Each extraction takes O(log n)
 * - Total: O(n log n)
 * Therefore:
 * - Best case:    O(n log n)
 * - Average case: O(n log n)
 * - Worst case:   O(n log n)
 * Space Complexity: O(n)
 * <p>
 * Note:
 * The classical Heap Sort algorithm can be implemented in-place
 * with O(1) additional space and can build the initial heap in O(n)
 * time using bottom-up heap construction.
 * This implementation intentionally uses the BinaryHeap class
 * already implemented in this project. Therefore, it requires
 * O(n) additional space and builds the heap using n insertions.
 *
 * @param <T> the type of elements to sort
 */
public class HeapSort<T extends Comparable<T>> implements Sort<T> {

    /**
     * Sorts the given array in ascending order using a min-heap.
     * A BinaryHeap is created with a comparator that gives
     * smaller elements higher priority.
     * All elements are inserted into the heap and are then
     * extracted from smallest to largest.
     * <p>
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     *
     * @param array the array to sort
     */
    @Override
    public void sort(T[] array) {
        // Create a min-heap.
        // If a < b according to compareTo, then a receives higher priority than b.
        BinaryHeap<T> heap = new BinaryHeap<>((a, b) -> a.compareTo(b));

        // Phase 1:
        // Insert every element into the heap.
        // n insertions, each taking O(log n): O(n log n)
        for (T element : array)
            heap.insert(element);

        // Phase 2:
        // Repeatedly extract the minimum element.
        // Because this is a min-heap, the extracted elements appear from smallest to largest.
        // n extractions, each taking O(log n): O(n log n)
        for (int i = 0; i < array.length; i++)
            array[i] = heap.extract();
    }
}