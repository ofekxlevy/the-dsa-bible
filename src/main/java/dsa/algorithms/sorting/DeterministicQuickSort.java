package dsa.algorithms.sorting;

import java.util.Arrays;

/**
 * Implements a deterministic version of Quick Sort whose pivot
 * is selected using the Median of Medians technique.
 * Unlike standard Quick Sort, which simply uses the last element,
 * and Randomized Quick Sort, which chooses a random element,
 * this implementation deliberately chooses a pivot that guarantees
 * sufficiently balanced partitions.
 * <p>
 * Pivot selection follows the Median of Medians strategy:
 * 1. Divide the elements into groups of at most 5.
 * 2. Sort each group.
 * 3. Find the median of each group.
 * 4. Collect the medians into a new array.
 * 5. Recursively find the median of those medians.
 * <p>
 * The Median of Medians technique guarantees that the chosen
 * pivot is sufficiently far from both extremes.
 * As a result, the larger recursive Quick Sort subproblem contains
 * at most approximately 7n/10 elements (up to additive constants).
 * <p>
 * Properties:
 * - Comparison-based
 * - Deterministic
 * - Not stable
 * - Worst-case asymptotically optimal among comparison sorts
 * <p>
 * Time Complexity:
 * - Best case:  Theta(n log n)
 * - Worst case: Theta(n log n)
 * Pivot selection itself takes Theta(n) worst-case time.
 * <p>
 * Space Complexity:
 * - O(n) additional temporary storage may be used by pivot selection.
 * - O(log n) recursion depth because partitions are guaranteed
 *   to be sufficiently balanced.
 *
 * @param <T> the type of elements to sort
 */
public class DeterministicQuickSort<T extends Comparable<T>> extends QuickSort<T> {

    private static final int GROUP_SIZE = 5;

    /**
     * Sorts the entire array using Quick Sort with deterministic
     * Median of Medians pivot selection.
     *
     * @param array the array to sort
     */
    @Override
    public void sort(T[] array) {
        deterministicQuickSort(array, 0, array.length - 1);
    }

    /**
     * Recursively sorts array[left...right].
     * Instead of directly using array[right] as the pivot,
     * a pivot is first selected using Median of Medians.
     * The selected pivot is moved to array[right], allowing us
     * to reuse the standard partition procedure from QuickSort.
     *
     * @param array the array being sorted
     * @param left  the first index of the current subarray
     * @param right the last index of the current subarray
     */
    private void deterministicQuickSort(T[] array, int left, int right) {
        if (left >= right)
            return;

        int pivotIndex = deterministicPartition(array, left, right);

        deterministicQuickSort(array, left, pivotIndex - 1);
        deterministicQuickSort(array, pivotIndex + 1, right);
    }

    /**
     * Chooses a pivot using Median of Medians and then applies
     * the standard partition procedure.
     *
     * @param array the array being partitioned
     * @param left  the first index of the current subarray
     * @param right the last index of the current subarray
     * @return the final index of the pivot
     */
    private int deterministicPartition(T[] array, int left, int right) {
        T pivot = selectPivot(array, left, right);
        int pivotIndex = findIndex(array, left, right, pivot);

        // The standard partition method expects the pivot to be located at array[right].
        swap(array, pivotIndex, right);
        return partition(array, left, right);
    }

    /**
     * Selects a pivot using the Median of Medians technique.
     * If the current range contains at most 5 elements,
     * the range is sorted and its median is returned.
     * Otherwise:
     * - Divide the range into groups of at most 5.
     * - Find the median of each group.
     * - Store the medians in a separate array.
     * - Recursively select the median of the medians.
     *
     * @param array the array containing the elements
     * @param left  the first index of the current range
     * @param right the last index of the current range
     * @return the selected pivot value
     */
    private T selectPivot(T[] array, int left, int right) {
        int size = right - left + 1;

        // Base case:
        // For a small range, simply sort a copy and return its median.
        if (size <= GROUP_SIZE) {
            T[] group = copyRange(array, left, right);
            insertionSort(group);
            return group[(group.length - 1) / 2];
        }

        int numberOfGroups = (size + GROUP_SIZE - 1) / GROUP_SIZE;
        T[] medians = createArray(array, numberOfGroups);
        int medianIndex = 0;

        for (int groupStart = left; groupStart <= right; groupStart += GROUP_SIZE) {
            int groupEnd = Math.min(groupStart + GROUP_SIZE - 1, right);
            T[] group = copyRange(array, groupStart, groupEnd);
            insertionSort(group);
            medians[medianIndex] = group[(group.length - 1) / 2];
            medianIndex++;
        }

        // Recursively find the median of the medians.
        return select(medians, 0, medians.length - 1, (medians.length + 1) / 2);
    }

    /**
     * Returns the i-th smallest element in array[left...right],
     * where i is 1-based relative to the current range.
     * This is the deterministic Select algorithm using
     * Median of Medians for pivot selection.
     * For example:
     * i = 1 returns the smallest element.
     * i = 2 returns the second-smallest element.
     *
     * @param array the array containing the elements
     * @param left  the first index of the current range
     * @param right the last index of the current range
     * @param i     the desired rank, using 1-based indexing
     * @return the i-th smallest element
     */
    private T select(T[] array, int left, int right, int i) {
        if (left == right)
            return array[left];

        T pivot = selectPivot(array, left, right);
        int pivotIndex = findIndex(array, left, right, pivot);
        swap(array, pivotIndex, right);
        int p = partition(array, left, right);

        // Number of elements from left through the pivot, including the pivot itself.
        int k = p - left + 1;

        if (i == k)
            return array[p];

        if (i < k)
            return select(array, left, p - 1, i);

        return select(array, p + 1, right, i - k);
    }

    /**
     * Finds an occurrence of the given value inside
     * array[left...right].
     *
     * @param array the array to search
     * @param left  the first index of the search range
     * @param right the last index of the search range
     * @param value the value to find
     * @return an index containing the value
     */
    private int findIndex(T[] array, int left, int right, T value) {
        for (int i = left; i <= right; i++)
            if (array[i].compareTo(value) == 0)
                return i;

        throw new IllegalStateException("Pivot must exist in the current range.");
    }

    /**
     * Sorts a small array using Insertion Sort.
     * Median of Medians sorts groups of constant size,
     * so using Insertion Sort here takes constant time
     * per group.
     *
     * @param array the small array to sort
     */
    private void insertionSort(T[] array) {
        for (int i = 1; i < array.length; i++) {
            T key = array[i];
            int j = i - 1;

            while (j >= 0 && array[j].compareTo(key) > 0) {
                array[j + 1] = array[j];
                j--;
            }

            array[j + 1] = key;
        }
    }

    /**
     * Creates a copy of array[left...right].
     *
     * @param array the source array
     * @param left  the first index to copy
     * @param right the last index to copy
     * @return a new array containing the requested range
     */
    private T[] copyRange(T[] array, int left, int right) {
        return Arrays.copyOfRange(array, left, right + 1);
    }

    /**
     * Creates a generic array with the same runtime component
     * type as the given array.
     *
     * @param array  an array whose component type should be used
     * @param length the desired length
     * @return the newly created array
     */
    @SuppressWarnings("unchecked")
    private T[] createArray(T[] array, int length) {
        return (T[]) java.lang.reflect.Array.newInstance(
                array.getClass().getComponentType(),
                length
        );
    }
}