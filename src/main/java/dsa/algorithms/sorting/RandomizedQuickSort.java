package dsa.algorithms.sorting;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Implements Randomized Quick Sort.
 * Randomized Quick Sort uses the same partition procedure as
 * standard Quick Sort, but chooses the pivot randomly.
 * For each recursive call:
 * 1. Choose a random index from the current subarray.
 * 2. Swap that element with the last element.
 * 3. Apply the standard partition procedure.
 * 4. Recursively sort the two resulting subarrays.
 * Randomization prevents the algorithm's behavior from depending
 * directly on the original ordering of the input.
 * <p>
 * Properties:
 * - Comparison-based
 * - In-place
 * - Randomized
 * - Not stable
 * <p>
 * Time Complexity:
 * - Expected:   Theta(n log n)
 * - Worst case: Theta(n^2)
 * Randomization does NOT eliminate the theoretical worst case.
 * It makes consistently bad pivot choices unlikely.
 * <p>
 * Space Complexity:
 * - Expected recursion stack: O(log n)
 * - Worst-case recursion stack: O(n)
 *
 * @param <T> the type of elements to sort
 */
public class RandomizedQuickSort<T extends Comparable<T>> extends QuickSort<T> {

    /**
     * Sorts the entire array using Randomized Quick Sort.
     *
     * @param array the array to sort
     */
    @Override
    public void sort(T[] array) {
        randomizedQuickSort(array, 0, array.length - 1);
    }

    /**
     * Recursively sorts array[left...right].
     * The only fundamental difference from standard Quick Sort
     * is that the pivot is selected randomly.
     *
     * @param array the array being sorted
     * @param left  the first index of the current subarray
     * @param right the last index of the current subarray
     */
    private void randomizedQuickSort(T[] array, int left, int right) {
        if (left >= right)
            return;

        int pivotIndex = randomizedPartition(array, left, right);

        randomizedQuickSort(array, left, pivotIndex - 1);
        randomizedQuickSort(array, pivotIndex + 1, right);
    }

    /**
     * Selects a random pivot and applies the standard partition
     * procedure inherited from QuickSort.
     * The randomly selected element is first moved to array[right]
     * because partition expects the pivot to be located there.
     *
     * @param array the array being partitioned
     * @param left  the first index of the current subarray
     * @param right the last index of the current subarray
     * @return the final index of the pivot
     */
    private int randomizedPartition(T[] array, int left, int right) {
        int randomIndex = ThreadLocalRandom.current().nextInt(left, right + 1);
        swap(array, randomIndex, right);
        return partition(array, left, right);
    }
}