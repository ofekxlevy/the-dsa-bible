package dsa.datastructures.skiplists;

import java.util.Arrays;
import java.util.Random;

/**
 * A probabilistic ordered data structure implemented as a Skip List.
 * The Skip List consists conceptually of ordered lists:
 * S0, S1, ..., Sh
 * <p>
 * S0 contains every key in the data structure.
 * A node that belongs to level i is promoted to level i + 1
 * independently with probability 1/2.
 * <p>
 * Each key is represented by exactly one Node object.
 * A node of height h participates in levels 0, 1, ..., h
 * and therefore contains arrays:
 * next[0..h]
 * prev[0..h]
 * <p>
 * A sentinel node represents the beginning of every level.
 * Duplicate keys are not allowed.
 * <p>
 * Expected search time: O(log n)
 * Expected insertion time: O(log n)
 * Expected removal-by-key time: O(log n)
 * Space complexity: O(n) expected
 *
 * @param <T> the type of keys stored in the Skip List
 */
public class SkipList<T extends Comparable<T>> {

    // -------- nested class: Node --------

    /**
     * Represents one key in the Skip List.
     * A node of height h belongs to all levels 0 through h.
     * next[i] points to the next node in level i.
     * prev[i] points to the previous node in level i.
     * The sentinel node has a null key.
     */
    private static class Node<T> {

        private final T key;

        /**
         * Highest level in which this node participates.
         * A node with height h belongs to:
         * S0, S1, ..., Sh.
         */
        private int height;

        private Node<T>[] next;
        private Node<T>[] prev;

        /**
         * Creates a node of the given height.
         *
         * @param key    the key stored in the node
         * @param height the highest level of the node
         */
        @SuppressWarnings("unchecked")
        private Node(T key, int height) {
            this.key = key;
            this.height = height;

            this.next = (Node<T>[]) new Node[height + 1];
            this.prev = (Node<T>[]) new Node[height + 1];
        }
    }


    // -------- fields --------

    /**
     * Sentinel node representing the beginning of every level.
     * sentinel.next[i] points to the first node in level i.
     */
    private final Node<T> sentinel;

    private int size;
    private final Random random;
    private static final double PROMOTION_PROBABILITY = 0.5;


    // -------- constructor --------

    /**
     * Creates an empty Skip List.
     * Initially, the structure contains only a sentinel
     * of height 0.
     * <p>
     * Time complexity: O(1)
     * Space complexity: O(1)
     */
    public SkipList() {
        sentinel = new Node<>(null, 0);
        size = 0;
        random = new Random();
    }


    // -------- search --------

    /**
     * Returns whether the given key exists in the Skip List.
     * The search starts at the highest level of the sentinel.
     * At each level, it moves right while the next key is
     * less than or equal to the requested key.
     * When no further movement is possible, the search drops
     * one level.
     *
     * @param key the key to search for
     * @return true if the key exists, false otherwise
     * @throws IllegalArgumentException if key is null
     * <p>
     * Expected time complexity: O(log n)
     * Worst-case time complexity: O(n)
     * Space complexity: O(1)
     */
    public boolean contains(T key) {
        validateKey(key);
        Node<T> node = find(key);
        return node != sentinel && node.key.compareTo(key) == 0;
    }


    // -------- insertion --------

    /**
     * Inserts a key into the Skip List.
     * The height of the new node is determined by repeatedly
     * flipping a fair coin. The node starts at height 0 and
     * increases its height for every successful promotion.
     * If the generated height is greater than the current
     * sentinel height, the sentinel arrays are enlarged.
     * Duplicate keys are not inserted.
     *
     * @param key the key to insert
     * @return true if the key was inserted,
     *         false if it already existed
     * @throws IllegalArgumentException if key is null
     * <p>
     * Expected time complexity: O(log n)
     * Worst-case time complexity: O(n)
     * Expected additional space: O(1)
     */
    public boolean add(T key) {
        validateKey(key);

         // p is the node containing the largest key <= key,
         // or the sentinel if all existing keys are larger.
        Node<T> p = find(key);

        if (p != sentinel && p.key.compareTo(key) == 0)
            return false;

        // Determine the height of the new node.
        int newHeight = randomHeight();

        // If the new node is taller than the sentinel, enlarge the sentinel so
        // that it participates in all required levels.
        if (newHeight > sentinel.height)
            resizeSentinel(newHeight);

        Node<T> newNode = new Node<>(key, newHeight);

        // We currently know the predecessor at level 0: it is p.
        // After inserting into level i, we move left until
        // reaching a node that also participates in level i + 1.
        for (int i = 0; i <= newHeight; i++) {
            while (p != sentinel && p.height < i)
                p = p.prev[i - 1];

            insertAfter(p, newNode, i);
        }

        size++;
        return true;
    }


    // -------- removal --------

    /**
     * Removes the given key from the Skip List.
     * First, the node is located using the Skip List search.
     * If it exists, it is removed from every level in which
     * it participates.
     * Empty highest levels are then removed from the sentinel.
     * <p>
     * Note:
     * If the node itself were already given, unlinking it would
     * take O(h) time, which is O(1) expected because the expected
     * height of a node is constant.
     * This public method receives a key, so it must first search
     * for the node.
     *
     * @param key the key to remove
     * @return true if the key existed and was removed,
     *         false otherwise
     * @throws IllegalArgumentException if key is null
     * <p>
     * Expected time complexity: O(log n)
     * Worst-case time complexity: O(n)
     * Space complexity: O(1)
     */
    public boolean remove(T key) {
        validateKey(key);
        Node<T> node = find(key);

        if (node == sentinel || node.key.compareTo(key) != 0)
            return false;

        // Remove x from every level in which it participates.
        for (int i = 0; i <= node.height; i++)
            removeFromLevel(node, i);

        // Remove empty levels from the top of the Skip List.
        shrinkSentinel();

        size--;
        return true;
    }


    // -------- basic information --------

    /**
     * Returns the number of keys stored in the Skip List.
     *
     * @return the number of keys
     *
     * Time complexity: O(1)
     */
    public int size() {
        return size;
    }

    /**
     * Returns whether the Skip List contains no keys.
     *
     * @return true if the Skip List is empty,
     *         false otherwise
     *
     * Time complexity: O(1)
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the current height of the Skip List.
     * Level 0 always exists, even when the Skip List is empty.
     *
     * @return the highest existing level
     *
     * Time complexity: O(1)
     */
    public int height() {
        return sentinel.height;
    }


    // -------- helpers --------

    /**
     * Implements FIND(S, k).
     * Starting from the sentinel at the highest level,
     * move right while the next key is <= key.
     * When movement is no longer possible, drop one level.
     * The returned node contains the largest key <= key.
     * If every key is greater than key, the sentinel is returned.
     *
     * @param key the search key
     * @return the node containing the largest key <= key,
     *         or the sentinel if no such key exists
     * <p>
     * Expected time complexity: O(log n)
     * Worst-case time complexity: O(n)
     * Space complexity: O(1)
     */
    private Node<T> find(T key) {
        Node<T> p = sentinel;

        for (int i = sentinel.height; i >= 0; i--)
            while (p.next[i] != null && p.next[i].key.compareTo(key) <= 0)
                p = p.next[i];

        return p;
    }

    /**
     * Generates a random node height.
     * Every node belongs to level 0.
     * For every successful coin flip, the node is promoted
     * one additional level.
     * Therefore:
     * P(height >= 1) = 1/2
     * P(height >= 2) = 1/4
     * P(height >= 3) = 1/8
     * ...
     *
     * @return the generated height
     *
     * Expected time complexity: O(1)
     */
    private int randomHeight() {
        int height = 0;

        while (random.nextDouble() < PROMOTION_PROBABILITY)
            height++;

        return height;
    }

    /**
     * Inserts a node into one level immediately after
     * the given predecessor.
     * Before: predecessor <-> successor
     * After: predecessor <-> node <-> successor
     *
     * @param predecessor the node after which to insert
     * @param node        the node being inserted
     * @param level       the level in which to insert it
     *
     * Time complexity: O(1)
     */
    private void insertAfter(Node<T> predecessor, Node<T> node, int level) {
        Node<T> successor = predecessor.next[level];

        node.prev[level] = predecessor;
        node.next[level] = successor;

        predecessor.next[level] = node;

        if (successor != null)
            successor.prev[level] = node;
    }

    /**
     * Removes a node from one level.
     *
     * @param node  the node to remove
     * @param level the level from which to remove it
     *
     * Time complexity: O(1)
     */
    private void removeFromLevel(Node<T> node, int level) {
        Node<T> predecessor = node.prev[level];
        Node<T> successor = node.next[level];

        predecessor.next[level] = successor;

        if (successor != null)
            successor.prev[level] = predecessor;

        node.prev[level] = null;
        node.next[level] = null;
    }

    /**
     * Enlarges the sentinel so that its height becomes newHeight.
     * The newly created next entries are initialized to null,
     * representing empty levels.
     *
     * @param newHeight the new sentinel height
     *
     * Time complexity: O(newHeight)
     */
    private void resizeSentinel(int newHeight) {
        sentinel.next = Arrays.copyOf(sentinel.next, newHeight + 1);
        sentinel.prev = Arrays.copyOf(sentinel.prev, newHeight + 1);
        sentinel.height = newHeight;
    }

    /**
     * Removes empty levels from the top of the Skip List.
     * Level 0 is never removed.
     * <p>
     * Time complexity:
     * O(number of removed levels)
     */
    private void shrinkSentinel() {
        int newHeight = sentinel.height;

        while (newHeight > 0 && sentinel.next[newHeight] == null)
            newHeight--;

        if (newHeight == sentinel.height)
            return;

        sentinel.next = Arrays.copyOf(sentinel.next, newHeight + 1);
        sentinel.prev = Arrays.copyOf(sentinel.prev, newHeight + 1);
        sentinel.height = newHeight;
    }

    /**
     * Validates that a key can be used by the Skip List.
     *
     * @param key the key to validate
     * @throws IllegalArgumentException if key is null
     *
     * Time complexity: O(1)
     */
    private void validateKey(T key) {
        if (key == null)
            throw new IllegalArgumentException("Key cannot be null.");
    }
}