package dsa.algorithms.sorting;

/**
 * Implements the Selection Sort algorithm.
 * Selection Sort divides the array into two parts:
 * - A sorted prefix.
 * - An unsorted suffix.
 * In each iteration, the smallest element in the unsorted part
 * is found and swapped with the first element of that part.
 * <p>
 * Properties:
 * - Comparison-based
 * - In-place
 * - Not stable
 * <p>
 * Time Complexity:
 * - Best case:    O(n^2)
 * - Average case: O(n^2)
 * - Worst case:   O(n^2)
 * Space Complexity: O(1)
 *
 * @param <T> the type of elements to sort
 */
public class SelectionSort<T extends Comparable<T>> implements Sort<T> {

    /**
     * Sorts the given array in ascending order using Selection Sort.
     * Loop invariant:
     * At the beginning of each iteration, array[0...i-1]
     * contains the smallest i elements of the array in sorted order.
     *
     * @param array the array to sort
     */
    @Override
    public void sort(T[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;

            // Find the smallest element in the unsorted part.
            for (int j = i + 1; j < array.length; j++)
                if (array[j].compareTo(array[minIndex]) < 0)
                    minIndex = j;

            // Place the smallest element at the beginning of the unsorted part.
            if (minIndex != i)
                swap(array, i, minIndex);
        }
    }

    /**
     * Swaps two elements in the array.
     * Time Complexity: O(1)
     *
     * @param array the array containing the elements
     * @param i     the index of the first element
     * @param j     the index of the second element
     */
    private void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}