package dsa.algorithms.sorting;

/**
 * Represents a comparison-based sorting algorithm.
 * Implementations of this interface sort the given array
 * in ascending order according to the natural ordering
 * of its elements.
 *
 * @param <T> the type of elements to be sorted
 */
public interface Sort<T extends Comparable<T>> {

    /**
     * Sorts the given array in ascending order.
     *
     * @param array the array to sort
     */
    void sort(T[] array);
}