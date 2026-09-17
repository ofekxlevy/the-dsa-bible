package dsa.algorithms.sorting;

/**
 * Implements the standard deterministic Quick Sort algorithm.
 * Quick Sort is a divide-and-conquer sorting algorithm.
 * The algorithm consists of three steps:
 * 1. Divide:
 *    Partition the current subarray around a pivot.
 *    This implementation uses the last element of the current
 *    subarray as the pivot.
 * 2. Conquer:
 *    Recursively sort the elements before and after the pivot.
 * 3. Combine:
 *    No combine step is required because the partitioning is
 *    performed directly on the original array.
 * <p>
 * After partitioning array[left...right] around the pivot:
 *     array[left...p-1] <= array[p]
 *     array[p+1...right] > array[p]
 * where p is the final position of the pivot.
 * <p>
 * Properties:
 * - Comparison-based
 * - In-place
 * - Deterministic
 * - Not stable
 * <p>
 * Time Complexity:
 * - Best case:  Theta(n log n)
 * - Worst case: Theta(n^2)
 * The worst case occurs when the pivot repeatedly produces
 * extremely unbalanced partitions, such as partitions of
 * sizes 0 and n - 1.
 * Space Complexity:
 * - Best/balanced case: O(log n) recursion stack
 * - Worst case: O(n) recursion stack
 *
 * @param <T> the type of elements to sort
 */
public class QuickSort<T extends Comparable<T>> implements Sort<T> {

    /**
     * Sorts the entire array in ascending order.
     *
     * @param array the array to sort
     */
    @Override
    public void sort(T[] array) {
        quickSort(array, 0, array.length - 1);
    }

    /**
     * Recursively sorts array[left...right].
     * If the subarray contains fewer than two elements,
     * no work is required.
     * Otherwise, the subarray is partitioned around a pivot.
     * The pivot is then in its final sorted position, so only
     * the two sides need to be sorted recursively.
     *
     * @param array the array being sorted
     * @param left  the first index of the current subarray
     * @param right the last index of the current subarray
     */
    protected void quickSort(T[] array, int left, int right) {
        if (left >= right)
            return;

        int pivotIndex = partition(array, left, right);

        quickSort(array, left, pivotIndex - 1);
        quickSort(array, pivotIndex + 1, right);
    }

    /**
     * Partitions array[left...right] using array[right] as the pivot.
     * During the loop, the subarray is divided into four zones:
     * 1. array[left...i] - Elements that are <= pivot.
     * 2. array[i+1...j-1] - Elements that are > pivot.
     * 3. array[j...right-1] - Elements that have not been examined yet.
     * 4. array[right] - The pivot.
     * Whenever array[j] <= pivot, it is moved into the first zone.
     * At the end, the pivot is swapped with array[i + 1],
     * placing it in its final sorted position.
     * <p>
     * Time Complexity:
     * - Theta(right - left + 1)
     *
     * @param array the array being partitioned
     * @param left  the first index of the current subarray
     * @param right the last index of the current subarray
     * @return the final index of the pivot
     */
    protected int partition(T[] array, int left, int right) {
        T pivot = array[right];
        int i = left - 1;

        for (int j = left; j < right; j++) {
            if (array[j].compareTo(pivot) <= 0) {
                i++;
                swap(array, i, j);
            }
        }

        swap(array, i + 1, right);
        return i + 1;
    }

    /**
     * Swaps two elements in the array.
     * <p>
     * Time Complexity: O(1)
     *
     * @param array the array containing the elements
     * @param i     the index of the first element
     * @param j     the index of the second element
     */
    protected void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}