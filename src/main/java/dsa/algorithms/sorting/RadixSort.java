package dsa.algorithms.sorting;

/**
 * Implements the LSD (Least Significant Digit) Radix Sort algorithm.
 * Radix Sort sorts non-negative integers digit by digit,
 * starting from the least significant digit.
 * For example:
 * 329
 * 457
 * 657
 * 839
 * 436
 * 720
 * 355
 * First, the numbers are sorted by the units' digit.
 * Then by the tens' digit.
 * Then by the hundreds' digit.
 * A stable sorting algorithm must be used for every digit.
 * This implementation uses Counting Sort because decimal
 * digits belong to the range {0, ..., 9}.
 * After processing digit i, the array is correctly sorted
 * according to the i least significant digits.
 * <p>
 * Properties:
 * - Not comparison-based
 * - Stable
 * - Not in-place
 * <p>
 * Assumption:
 * - All values are non-negative integers.
 * <p>
 * Let:
 * - n = number of elements
 * - d = number of digits
 * - b = base
 * Time Complexity: Theta(d * (n + b))
 * - This implementation uses base 10, so b = 10: Theta(d * n)
 * Space Complexity: Theta(n + b)
 * - Theta(n) for base 10
 */
public class RadixSort {

    private static final int BASE = 10;

    /**
     * Sorts the given array in ascending order using LSD Radix Sort.
     *
     * @param array the array to sort
     * @throws IllegalArgumentException if the array contains a negative value
     */
    public void sort(int[] array) {
        if (array.length < 2)
            return;

        int max = findMax(array);

        /*
         * exponent identifies the current digit:
         * 1   -> units
         * 10  -> tens
         * 100 -> hundreds
         * ...
         */
        for (long exponent = 1; max / exponent > 0; exponent *= BASE)
            countingSortByDigit(array, exponent);
    }

    /**
     * Stably sorts the array according to one decimal digit.
     * The digit of a value is calculated as: (value / exponent) % 10
     * For example, for value = 457:
     * exponent = 1   -> digit = 7
     * exponent = 10  -> digit = 5
     * exponent = 100 -> digit = 4
     * Counting Sort is used because there are exactly 10
     * possible decimal digits.
     * <p>
     * Time Complexity: Theta(n + BASE)
     * Space Complexity: Theta(n + BASE)
     *
     * @param array    the array being sorted
     * @param exponent identifies the digit being processed
     */
    private void countingSortByDigit(int[] array, long exponent) {
        int[] count = new int[BASE];
        int[] output = new int[array.length];

        /*
         * Count the occurrences of every digit.
         * Time Complexity: Theta(n)
         */
        for (int value : array) {
            int digit = (int) ((value / exponent) % BASE);
            count[digit]++;
        }

        /*
         * Convert count into prefix sums.
         * After this loop:
         * count[d] = number of elements whose current
         * digit is <= d.
         * Time Complexity: Theta(BASE)
         */
        for (int i = 1; i < BASE; i++)
            count[i] += count[i - 1];

        /*
         * Build the output array from right to left.
         * This is essential because the sorting of each digit
         * must be stable.
         * Time Complexity: Theta(n)
         */
        for (int i = array.length - 1; i >= 0; i--) {
            int digit = (int) ((array[i] / exponent) % BASE);
            output[count[digit] - 1] = array[i];
            count[digit]--;
        }

        /*
         * Copy the result back into the original array.
         * Time Complexity: Theta(n)
         */
        System.arraycopy(output, 0, array, 0, array.length);
    }

    /**
     * Finds the largest value in the array.
     * The maximum determines how many digit positions
     * Radix Sort needs to process.
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
                throw new IllegalArgumentException("Radix Sort supports only non-negative integers.");

            if (value > max)
                max = value;
        }

        return max;
    }
}