package dsa.algorithms.sorting;

/**
 * Implements the Counting Sort algorithm.
 * Counting Sort is a non-comparison-based sorting algorithm
 * for integer values from the range {0, 1, ..., k}.
 * The algorithm works in four phases:
 * 1. Count:
 *    C[i] stores the number of occurrences of value i.
 * 2. Accumulate:
 *    Convert C into a prefix-sum array.
 *    After this phase, C[i] stores the number of elements <= i.
 * 3. Build output:
 *    Traverse the input array from right to left and place each
 *    element in its correct position in the output array.
 * 4. Copy:
 *    Copy the sorted output back into the original array.
 * Traversing the input from right to left during the third phase
 * makes the algorithm stable.
 * <p>
 * Properties:
 * - Not comparison-based
 * - Stable
 * - Not in-place
 * <p>
 * Assumption:
 * - Every element must belong to {0, 1, ..., k}.
 * <p>
 * Time Complexity: Theta(n + k)
 * If k = O(n), the running time is Theta(n).
 * Space Complexity: Theta(n + k)
 */
public class CountingSort {

    /**
     * Sorts the array in ascending order.
     * The largest value in the array is used as k.
     *
     * @param array the array to sort
     * @throws IllegalArgumentException if the array contains a negative value
     */
    public void sort(int[] array) {
        if (array.length < 2)
            return;

        int k = findMax(array);
        countingSort(array, k);
    }

    /**
     * Performs Counting Sort assuming all values belong
     * to the range {0, 1, ..., k}.
     *
     * @param array the array to sort
     * @param k     the largest possible value
     */
    private void countingSort(int[] array, int k) {
        int[] count = new int[k + 1];
        int[] output = new int[array.length];

        /*
         * Phase 1:
         * Count the number of occurrences of each value.
         * After this loop:
         * count[i] = number of elements equal to i.
         * Time Complexity: Theta(n)
         */
        for (int value : array)
            count[value]++;

        /*
         * Phase 2:
         * Compute prefix sums.
         * After this loop:
         * count[i] = number of elements <= i.
         * Time Complexity: Theta(k)
         */
        for (int i = 1; i <= k; i++)
            count[i] += count[i - 1];

        /*
         * Phase 3:
         * Build the sorted output array.
         * We traverse from right to left in order to preserve
         * the relative order of equal elements.
         * count[value] tells us how many elements are <= value,
         * so count[value] - 1 is the last available index
         * for that value.
         * Time Complexity: Theta(n)
         */
        for (int j = array.length - 1; j >= 0; j--) {
            int value = array[j];
            output[count[value] - 1] = value;
            count[value]--;
        }

        /*
         * Phase 4:
         * Copy the sorted result back into the original array.
         * Time Complexity: Theta(n)
         */
        System.arraycopy(output, 0, array, 0, array.length);
    }

    /**
     * Finds the largest value in the array.
     * Counting Sort requires all values to be non-negative.
     * <p>
     * Time Complexity: Theta(n)
     *
     * @param array the input array
     * @return the largest value
     */
    private int findMax(int[] array) {
        int max = array[0];

        for (int value : array) {
            if (value < 0)
                throw new IllegalArgumentException("Counting Sort supports only non-negative integers.");

            if (value > max)
                max = value;
        }

        return max;
    }
}