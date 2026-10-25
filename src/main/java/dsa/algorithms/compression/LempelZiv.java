package dsa.algorithms.compression;

import dsa.datastructures.hashtables.ChainedHashTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the LZ78 (Lempel-Ziv 1978) lossless compression algorithm.
 * <p>
 * LZ78 compresses text by building a dictionary of substrings that
 * have already appeared in the input.
 * <p>
 * Initially, the dictionary contains only the empty string:
 *
 * <pre>
 * index 0 -> ""
 * </pre>
 *
 * During encoding, the algorithm repeatedly finds the longest prefix
 * of the remaining input that already exists in the dictionary.
 * Suppose this prefix is w and the next character is c.
 * The encoder outputs the pair:
 *
 * <pre>
 * (index(w), c)
 * </pre>
 *
 * and inserts the new string:
 *
 * <pre>
 * w + c
 * </pre>
 *
 * into the dictionary.
 *
 * <p>
 * Example:
 *
 * <pre>
 * Input: ABAABABAABAB
 *
 * Dictionary:
 *
 * 0 -> ""
 * 1 -> "A"
 * 2 -> "B"
 * 3 -> "AA"
 * 4 -> "BA"
 * 5 -> "BAA"
 * 6 -> "BAB"
 *
 * Output pairs:
 *
 * (0, A)
 * (0, B)
 * (1, A)
 * (2, A)
 * (4, A)
 * (4, B)
 * </pre>
 *
 * <p>
 * Unlike Huffman coding, LZ78 does not require knowing character
 * frequencies in advance. The dictionary is constructed dynamically
 * while the input is processed.
 */
public class LempelZiv {

    /**
     * Represents one encoded LZ78 pair.
     * <p>
     * Each pair contains:
     *
     * <pre>
     * index     - index of a previously discovered dictionary entry
     * character - the next character appended to that entry
     * </pre>
     *
     * If the input ends with a substring that already exists in the
     * dictionary, character may be null.
     */
    public static class Pair {

        private final int index;
        private final Character character;


        /**
         * Creates an encoded LZ78 pair.
         *
         * @param index the dictionary index
         * @param character the next character, or null if no character follows
         */
        public Pair(int index, Character character) {
            this.index = index;
            this.character = character;
        }


        /**
         * Returns the dictionary index.
         *
         * @return the dictionary index
         */
        public int getIndex() {
            return index;
        }


        /**
         * Returns the character stored in this pair.
         *
         * @return the character, or null if no character follows
         */
        public Character getCharacter() {
            return character;
        }


        @Override
        public String toString() {
            return "(" + index + ", " + character + ")";
        }
    }


    /**
     * Encodes the given text using the LZ78 compression algorithm.
     * <p>
     * The dictionary maps every previously discovered substring
     * to its dictionary index.
     * <p>
     * The empty string is considered entry 0.
     *
     * @param text the text to encode
     * @return the sequence of LZ78 pairs
     *
     * @throws IllegalArgumentException if the text is null
     */
    public static List<Pair> encode(String text) {

        if (text == null)
            throw new IllegalArgumentException("Text cannot be null.");

        List<Pair> encoded = new ArrayList<>();

        if (text.isEmpty())
            return encoded;

        /*
         * Maps discovered strings to their dictionary indices.
         * The empty string has index 0.
         */
        ChainedHashTable<String, Integer> dictionary = new ChainedHashTable<>();
        dictionary.put("", 0);
        int nextIndex = 1;

        /*
         * w is the longest substring seen so far that already
         * exists in the dictionary.
         */
        String w = "";

        for (int i = 0; i < text.length(); i++) {

            char c = text.charAt(i);

            /*
             * Check whether extending w by the current character
             * still produces a known dictionary entry.
             */
            String candidate = w + c;

            if (dictionary.containsKey(candidate)) {

                /*
                 * candidate already exists, so continue searching
                 * for a longer dictionary entry.
                 */
                w = candidate;
            }

            else {

                /*
                 * w exists in the dictionary, but w + c does not.
                 * Therefore output:
                 * (index(w), c)
                 */
                encoded.add(new Pair(dictionary.get(w), c));


                // Add the newly discovered string to the dictionary.
                dictionary.put(candidate, nextIndex);
                nextIndex++;

                // Start searching for the next phrase.
                w = "";
            }
        }

        /*
         * The input may end while w already represents a complete
         * dictionary entry.
         *
         * There is no additional character to append, so we output
         * the dictionary index with a null character.
         */
        if (!w.isEmpty())
            encoded.add(new Pair(dictionary.get(w), null));

        return encoded;
    }


    /**
     * Decodes a sequence of LZ78 pairs back into the original text.
     *
     * <p>
     * During decoding, the same dictionary constructed by the encoder
     * is reconstructed.
     *
     * <p>
     * For every pair:
     *
     * <pre>
     * (index, character)
     * </pre>
     *
     * we retrieve the string stored at index and append character.
     * The resulting string is both:
     * 1. appended to the decoded text,
     * 2. inserted into the dictionary.
     *
     * @param encoded the encoded LZ78 pairs
     * @return the original text
     *
     * @throws IllegalArgumentException if encoded is null
     * @throws IllegalArgumentException if an invalid dictionary index appears
     */
    public static String decode(List<Pair> encoded) {

        if (encoded == null)
            throw new IllegalArgumentException("Encoded data cannot be null.");

        if (encoded.isEmpty())
            return "";

        /*
         * During decoding we need to access dictionary entries
         * directly by their integer index.
         *
         * Entry 0 is always the empty string.
         */
        List<String> dictionary = new ArrayList<>();
        dictionary.add("");
        StringBuilder decoded = new StringBuilder();

        for (Pair pair : encoded) {

            int index = pair.getIndex();

            /*
             * Every index must refer to an entry that has already
             * been reconstructed.
             */
            if (index < 0 || index >= dictionary.size())
                throw new IllegalArgumentException("Invalid LZ78 dictionary index: " + index);

            String prefix = dictionary.get(index);

            /*
             * A null character is allowed only for the final phrase.
             * It means that the input ended with an already existing
             * dictionary entry.
             */
            if (pair.getCharacter() == null) {
                decoded.append(prefix);
                continue;
            }


            // Reconstruct the new phrase.
            String phrase = prefix + pair.getCharacter();
            decoded.append(phrase);

            /*
             * The decoder inserts phrases in exactly the same order
             * as the encoder, so both dictionaries receive identical
             * indices.
             */
            dictionary.add(phrase);
        }

        return decoded.toString();
    }
}