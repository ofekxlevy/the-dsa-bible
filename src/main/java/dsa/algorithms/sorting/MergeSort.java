package dsa.algorithms.sorting;

/**
 * Implements the Merge Sort algorithm.
 * Merge Sort is a divide-and-conquer sorting algorithm.
 * The algorithm works in three steps:
 * 1. Divide the array into two halves.
 * 2. Recursively sort each half.
 * 3. Merge the two sorted halves into one sorted range.
 * <p>
 * Properties:
 * - Comparison-based
 * - Stable
 * - Not in-place (uses an auxiliary array)
 * <p>
 * Time Complexity:
 * - Best case:    O(n log n)
 * - Average case: O(n log n)
 * - Worst case:   O(n log n)
 * Space Complexity:
 * - O(n) for the auxiliary array
 * - O(log n) for the recursion stack
 *
 * @param <T> the type of elements to sort
 */
public class MergeSort<T extends Comparable<T>> implements Sort<T> {

    /**
     * Sorts the given array in ascending order using Merge Sort.
     * An auxiliary array is created once and reused throughout
     * the recursive calls.
     *
     * @param array the array to sort
     */
    @Override
    public void sort(T[] array) {
        if (array.length < 2)
            return;

        T[] temp = createTempArray(array);
        mergeSort(array, temp, 0, array.length - 1);
    }

    /**
     * Recursively sorts the range array[left...right].
     * The range is divided into two halves, each half is sorted
     * recursively, and then the two sorted halves are merged.
     *
     * @param array the array being sorted
     * @param temp  the auxiliary array used during merging
     * @param left  the first index of the range
     * @param right the last index of the range
     */
    private void mergeSort(T[] array, T[] temp, int left, int right) {
        // Base case: a range of size 0 or 1 is already sorted.
        if (left >= right)
            return;

        int mid = left + (right - left) / 2;

        mergeSort(array, temp, left, mid);
        mergeSort(array, temp, mid + 1, right);

        merge(array, temp, left, mid, right);
    }

    /**
     * Merges two consecutive sorted ranges:
     * array[left...mid]
     * array[mid + 1...right]
     * into one sorted range array[left...right].
     * The relevant elements are first copied into the auxiliary
     * array. The two sorted ranges are then scanned simultaneously,
     * and the smaller element is copied back into the original array.
     * If two elements are equal, the element from the left range
     * is chosen first. This preserves the relative order of equal
     * elements and makes the algorithm stable.
     * <p>
     * Time Complexity:
     * - O(right - left + 1)
     *
     * @param array the array being sorted
     * @param temp  the auxiliary array
     * @param left  the first index of the left range
     * @param mid   the last index of the left range
     * @param right the last index of the right range
     */
    private void merge(T[] array, T[] temp, int left, int mid, int right) {
        // Copy the relevant range into the auxiliary array.
        for (int i = left; i <= right; i++)
            temp[i] = array[i];

        int i = left;      // Current index in the left half.
        int j = mid + 1;   // Current index in the right half.
        int k = left;      // Current position in the original array.

        // Merge while both halves still contain elements.
        while (i <= mid && j <= right) {
            if (temp[i].compareTo(temp[j]) <= 0) {
                array[k] = temp[i];
                i++;
            }
            else {
                array[k] = temp[j];
                j++;
            }

            k++;
        }

        // Copy remaining elements from the left half.
        while (i <= mid) {
            array[k] = temp[i];
            i++;
            k++;
        }

        /*
         * There is no need to copy the remaining elements from
         * the right half.
         *
         * They are already located in their correct positions
         * in the original array.
         */
    }

    /**
     * Creates an auxiliary array with the same runtime component
     * type and length as the given array.
     *
     * @param array the original array
     * @return an auxiliary array
     */
    @SuppressWarnings("unchecked")
    private T[] createTempArray(T[] array) {
        return (T[]) java.lang.reflect.Array.newInstance(
                array.getClass().getComponentType(),
                array.length
        );
    }
}