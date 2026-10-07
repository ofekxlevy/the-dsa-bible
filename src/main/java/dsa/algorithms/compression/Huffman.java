package dsa.algorithms.compression;

import dsa.datastructures.hashtables.ChainedHashTable;
import dsa.datastructures.heaps.BinaryHeap;

import java.util.Comparator;

/**
 * Implements Huffman coding for lossless data compression.
 * <p>
 * Huffman coding constructs an optimal prefix code according to the
 * frequencies of the characters in the input text.
 * Characters that occur more frequently tend to receive shorter codewords,
 * while less frequent characters tend to receive longer codewords.
 * <p>
 * The Huffman tree is constructed using a min-priority queue:
 * 1. Count the frequency of every character.
 * 2. Create one leaf node for every distinct character.
 * 3. Insert all leaf nodes into a min-priority queue.
 * 4. Repeatedly extract the two nodes with minimum frequency.
 * 5. Create a new internal node whose frequency is their sum.
 * 6. Insert the new node back into the priority queue.
 * 7. When one node remains, it is the root of the Huffman tree.
 * The left edge represents 0 and the right edge represents 1.
 * <p>
 * If σ is the number of distinct characters, constructing the
 * Huffman tree takes O(σ log σ) time after the frequency table
 * has been constructed.
 */
public class Huffman {

    /**
     * Builds a Huffman tree for the given text.
     *
     * @param text the text whose character frequencies determine the tree
     * @return the root of the Huffman tree
     *
     * @throws IllegalArgumentException if the text is null or empty
     */
    public static Node buildTree(String text) {

        if (text == null || text.isEmpty())
            throw new IllegalArgumentException("Text cannot be null or empty.");

        /*
         * Count the frequency of every character using our
         * chained hash table implementation.
         */
        ChainedHashTable<Character, Integer> frequencies = buildFrequencyTable(text);

        /*
         * Q is the min-priority queue used by Huffman's algorithm.
         * Nodes with smaller frequencies have higher priority.
         */
        BinaryHeap<Node> queue = new BinaryHeap<>(Comparator.comparingInt(Node::getFrequency));

        /*
         * Our ChainedHashTable currently does not provide iteration
         * over its keys.
         *
         * Therefore, we scan the text again and use another hash table
         * to remember which characters have already been inserted
         * into the priority queue.
         */
        ChainedHashTable<Character, Boolean> inserted = new ChainedHashTable<>();

        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);

            // Each distinct character must produce exactly one initial Huffman tree.
            if (!inserted.containsKey(character)) {
                Node leaf = new Node(character, frequencies.get(character), null, null);
                queue.insert(leaf);
                inserted.put(character, true);
            }
        }

        /*
         * Repeatedly combine the two trees with the smallest frequencies.
         * This corresponds directly to:
         * z.left  = ExtractMin(Q)
         * z.right = ExtractMin(Q)
         * z.freq  = z.left.freq + z.right.freq
         * Insert(Q, z)
         */
        while (queue.size() > 1) {
            Node left = queue.extract();
            Node right = queue.extract();
            Node parent = new Node(null, left.frequency + right.frequency, left, right);
            queue.insert(parent);
        }

        // The only remaining tree is the complete Huffman tree.
        return queue.extract();
    }


    /**
     * Encodes the given text using Huffman coding.
     * A Huffman tree is constructed from the character frequencies,
     * and then a codeword is generated for every character.
     * Left edge  -> 0
     * Right edge -> 1
     *
     * @param text the text to encode
     * @return the Huffman-encoded binary string
     *
     * @throws IllegalArgumentException if the text is null or empty
     */
    public static String encode(String text) {

        Node root = buildTree(text);
        ChainedHashTable<Character, String> codes = buildCodeTable(root);
        StringBuilder encoded = new StringBuilder();

        // Replace every character with its Huffman codeword.
        for (int i = 0; i < text.length(); i++)
            encoded.append(codes.get(text.charAt(i)));

        return encoded.toString();
    }


    /**
     * Decodes a Huffman-encoded binary string using the given Huffman tree.
     * Starting at the root:
     * 0 -> move to the left child
     * 1 -> move to the right child
     * When a leaf is reached, its character is appended to the
     * decoded text and traversal starts again from the root.
     *
     * @param encoded the encoded binary string
     * @param root the Huffman tree used for encoding
     * @return the decoded text
     *
     * @throws IllegalArgumentException if the encoded string is invalid
     * @throws IllegalArgumentException if the root is null
     */
    public static String decode(String encoded, Node root) {

        if (encoded == null)
            throw new IllegalArgumentException("Encoded text cannot be null.");

        if (root == null)
            throw new IllegalArgumentException("Huffman tree cannot be null.");

        /*
         * Special case:
         * If the alphabet contains only one character, the Huffman
         * tree consists of a single leaf. We use "0" as the codeword
         * for that character.
         */
        if (root.isLeaf()) {
            StringBuilder decoded = new StringBuilder();

            for (int i = 0; i < encoded.length(); i++) {
                if (encoded.charAt(i) != '0')
                    throw new IllegalArgumentException("Invalid Huffman encoding.");

                decoded.append(root.character);
            }

            return decoded.toString();
        }

        StringBuilder decoded = new StringBuilder();
        Node current = root;

        for (int i = 0; i < encoded.length(); i++) {
            char bit = encoded.charAt(i);

            if (bit == '0')
                current = current.left;

            else if (bit == '1')
                current = current.right;

            else
                throw new IllegalArgumentException("Encoded text must contain only 0 and 1.");

            // A null child means that the bit sequence does not represent a valid path in this Huffman tree.
            if (current == null)
                throw new IllegalArgumentException("Invalid Huffman encoding.");

            // Reaching a leaf completes one codeword.
            if (current.isLeaf()) {
                decoded.append(current.character);

                // Start decoding the next codeword from the root.
                current = root;
            }
        }

        // If we did not return to the root, the encoded string ended before the current codeword was completed.
        if (current != root)
            throw new IllegalArgumentException("Encoded text ends in the middle of a codeword.");

        return decoded.toString();
    }


    /**
     * Counts the number of occurrences of every character in the text.
     * The frequencies are stored in our ChainedHashTable implementation.
     * <p>
     * Expected Time Complexity: O(n),
     * where n is the length of the text.
     *
     * @param text the input text
     * @return a hash table mapping every character to its frequency
     */
    private static ChainedHashTable<Character, Integer>
    buildFrequencyTable(String text) {

        ChainedHashTable<Character, Integer> frequencies = new ChainedHashTable<>();

        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);

            if (frequencies.containsKey(character)) {
                int frequency = frequencies.get(character);
                frequencies.put(character, frequency + 1);
            }
            else
                frequencies.put(character, 1);
        }

        return frequencies;
    }


    /**
     * Builds a table mapping every character in the Huffman tree
     * to its binary codeword.
     *
     * @param root the root of the Huffman tree
     * @return a hash table mapping characters to their codewords
     */
    private static ChainedHashTable<Character, String> buildCodeTable(Node root) {
        ChainedHashTable<Character, String> codes = new ChainedHashTable<>();
        buildCodeTable(root, "", codes);
        return codes;
    }


    /**
     * Recursively traverses the Huffman tree and constructs the
     * codeword of every character.
     * Moving left appends 0.
     * Moving right appends 1.
     *
     * @param node the current Huffman tree node
     * @param code the binary path from the root to this node
     * @param codes the table in which the codewords are stored
     */
    private static void buildCodeTable(Node node, String code, ChainedHashTable<Character, String> codes) {
        // A leaf represents an actual character.
        if (node.isLeaf()) {
            // If there is only one distinct character, the tree contains no edges. We assign "0" as its codeword.
            if (code.isEmpty())
                code = "0";

            codes.put(node.character, code);
            return;
        }

        // Left edge represents 0.
        buildCodeTable(node.left, code + "0", codes);

        // Right edge represents 1.
        buildCodeTable(node.right, code + "1", codes);
    }


    /**
     * Represents a node in a Huffman tree.
     * A leaf represents a character and stores its frequency.
     * An internal node does not represent a specific character.
     * Its frequency is the sum of the frequencies of all leaves
     * in its subtree.
     */
    public static class Node {

        private final Character character;
        private final int frequency;
        private final Node left;
        private final Node right;


        /**
         * Creates a Huffman tree node.
         *
         * @param character the represented character,
         *                  or null for an internal node
         * @param frequency the frequency represented by this node
         * @param left the left child
         * @param right the right child
         */
        private Node(Character character, int frequency, Node left, Node right) {
            this.character = character;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }


        /**
         * Checks whether this node is a leaf.
         *
         * @return true if the node has no children
         */
        public boolean isLeaf() {
            return left == null && right == null;
        }


        /**
         * Returns the character represented by this node.
         * Internal nodes return null.
         *
         * @return the represented character, or null for an internal node
         */
        public Character getCharacter() {
            return character;
        }


        /**
         * Returns the frequency represented by this node.
         *
         * @return the node's frequency
         */
        public int getFrequency() {
            return frequency;
        }


        /**
         * Returns the left child.
         *
         * @return the left child, or null for a leaf
         */
        public Node getLeft() {
            return left;
        }


        /**
         * Returns the right child.
         *
         * @return the right child, or null for a leaf
         */
        public Node getRight() {
            return right;
        }
    }
}