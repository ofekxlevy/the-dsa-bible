package dsa.algorithms.sorting;

/**
 * Implements the Insertion Sort algorithm.
 * Insertion Sort builds the sorted portion of the array one element
 * at a time. At the beginning of each iteration, the prefix
 * array[0...i-1] is already sorted.
 * The current element is removed conceptually and inserted into
 * its correct position within the sorted prefix by shifting larger
 * elements one position to the right.
 * <p>
 * Properties:
 * - Comparison-based
 * - In-place
 * - Stable
 * - Adaptive: performs well on nearly sorted arrays
 * <p>
 * Time Complexity:
 * - Best case:    O(n)
 * - Average case: O(n^2)
 * - Worst case:   O(n^2)
 * Space Complexity: O(1)
 *
 * @param <T> the type of elements to sort
 */
public class InsertionSort<T extends Comparable<T>> implements Sort<T> {

    /**
     * Sorts the given array in ascending order using Insertion Sort.
     * Loop invariant:
     * At the beginning of each iteration of the outer loop,
     * the subarray array[0...i-1] is sorted.
     *
     * @param array the array to sort
     */
    @Override
    public void sort(T[] array) {
        for (int i = 1; i < array.length; i++) {
            T key = array[i];
            int j = i - 1;

            // Shift elements greater than key one position to the right.
            while (j >= 0 && array[j].compareTo(key) > 0) {
                array[j + 1] = array[j];
                j--;
            }

            // Insert key into its correct position.
            array[j + 1] = key;
        }
    }
}