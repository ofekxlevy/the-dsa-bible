package dsa.algorithms.sorting;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Bucket Sort algorithm for values in [0, 1).
 * The interval [0, 1) is divided into n equal-sized intervals,
 * where n is the number of elements in the input array.
 * Each interval corresponds to one bucket.
 * A value x is placed into bucket: floor(n * x)
 * After distributing all elements:
 * 1. Each bucket is sorted using Insertion Sort.
 * 2. The buckets are concatenated in increasing order.
 * <p>
 * Assumption:
 * - Every element satisfies 0 <= value < 1.
 * <p>
 * Properties:
 * - Not comparison-based as a whole
 * - Uses comparison-based Insertion Sort inside each bucket
 * - Deterministic
 * - Not in-place
 * <p>
 * Average-case assumption:
 * - Elements are independently and uniformly distributed
 *   over [0, 1).
 * <p>
 * Time Complexity:
 * - Average case: Theta(n)
 * - Worst case:   Theta(n^2)
 * The worst case occurs when all elements are placed
 * into the same bucket.
 * Space Complexity:
 * - Theta(n)
 */
public class BucketSort {

    /**
     * Sorts the given array in ascending order.
     *
     * @param array the array to sort
     * @throws IllegalArgumentException if an element is
     *                                  outside [0, 1)
     */
    public void sort(double[] array) {
        int n = array.length;

        if (n < 2) {
            validateValues(array);
            return;
        }

        /*
         * Create n buckets.
         * Each bucket represents an interval of length 1/n.
         */
        List<List<Double>> buckets = new ArrayList<>(n);

        for (int i = 0; i < n; i++)
            buckets.add(new ArrayList<>());

        /*
         * Phase 1:
         * Distribute the elements among the buckets.
         * An element x is placed in: floor(n * x)
         * Time Complexity: Theta(n)
         */
        for (double value : array) {
            validateValue(value);
            int bucketIndex = (int) (n * value);
            buckets.get(bucketIndex).add(value);
        }

        /*
         * Phase 2:
         * Sort every bucket using Insertion Sort.
         */
        for (List<Double> bucket : buckets)
            insertionSort(bucket);

        /*
         * Phase 3:
         * Concatenate the buckets back into the array.
         * Since every value in bucket i is smaller than
         * every value in bucket j for i < j, concatenating
         * the sorted buckets produces the sorted array.
         * Time Complexity: Theta(n)
         */
        int arrayIndex = 0;

        for (List<Double> bucket : buckets) {
            for (double value : bucket) {
                array[arrayIndex] = value;
                arrayIndex++;
            }
        }
    }

    /**
     * Sorts a single bucket using Insertion Sort.
     * If the bucket contains m elements:
     * <p>
     * Time Complexity:
     * - Best case:    Theta(m)
     * - Worst case:   Theta(m^2)
     *
     * @param bucket the bucket to sort
     */
    private void insertionSort(List<Double> bucket) {
        for (int i = 1; i < bucket.size(); i++) {
            double key = bucket.get(i);
            int j = i - 1;

            while (j >= 0 && bucket.get(j) > key) {
                bucket.set(j + 1, bucket.get(j));
                j--;
            }

            bucket.set(j + 1, key);
        }
    }

    /**
     * Validates all elements of an array.
     * <p>
     * Time Complexity:
     * - Theta(n)
     *
     * @param array the array to validate
     */
    private void validateValues(double[] array) {
        for (double value : array)
            validateValue(value);
    }

    /**
     * Verifies that a value belongs to [0, 1).
     *
     * @param value the value to validate
     */
    private void validateValue(double value) {
        if (value < 0.0 || value >= 1.0 || Double.isNaN(value)) 
            throw new IllegalArgumentException("Bucket Sort requires values in [0, 1).");
    }
}