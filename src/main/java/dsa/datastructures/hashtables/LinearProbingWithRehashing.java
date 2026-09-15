package dsa.datastructures.hashtables;

/**
 * A hash table using open addressing with linear probing.
 * Deletions are handled by removing and reinserting the entries
 * that follow the deleted position in the same cluster.
 * Therefore, no tombstones are required.
 * <p>
 * Average time complexity:
 * put      - O(1) amortized
 * get      - O(1)
 * contains - O(1)
 * remove may require reinserting entries from the cluster.
 * Worst-case time complexity: O(n).
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class LinearProbingWithRehashing<K, V> implements HashTable<K, V> {

    // -------- nested class: Entry --------

    /**
     * Represents a key-value pair.
     */
    private static class Entry<K, V> {
        private final K key;
        private V value;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }


    // -------- fields --------

    private static final int DEFAULT_CAPACITY = 16;
    private static final double MAX_LOAD_FACTOR = 0.75;
    private Entry<K, V>[] table;
    private int size;


    // -------- constructors --------

    /**
     * Creates an empty table with the default capacity.
     * <p>
     * Time complexity: O(m).
     */
    @SuppressWarnings("unchecked")
    public LinearProbingWithRehashing() {
        table = (Entry<K, V>[]) new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    /**
     * Creates an empty table with the specified capacity.
     * <p>
     * Time complexity: O(m).
     */
    @SuppressWarnings("unchecked")
    public LinearProbingWithRehashing(int initialCapacity) {
        if (initialCapacity <= 0)
            throw new IllegalArgumentException("Capacity must be positive");

        table = (Entry<K, V>[]) new Entry[initialCapacity];
        size = 0;
    }


    // -------- methods --------

    /**
     * Inserts or updates a key-value pair.
     * Linear probing is used until either the key or an empty position is found.
     * <p>
     * Average amortized time complexity: O(1).
     * Worst-case time complexity: O(n).
     */
    @Override
    public V put(K key, V value) {
        int existingIndex = findKeyIndex(key);

        if (existingIndex != -1) {
            V oldValue = table[existingIndex].value;
            table[existingIndex].value = value;
            return oldValue;
        }

        if ((size + 1.0) / table.length > MAX_LOAD_FACTOR)
            resize(table.length * 2);

        insertNewEntry(new Entry<>(key, value));
        size++;

        return null;
    }

    /**
     * Returns the value associated with the key.
     * <p>
     * Average time complexity: O(1).
     * Worst-case time complexity: O(n).
     */
    @Override
    public V get(K key) {
        int index = findKeyIndex(key);

        if (index == -1)
            return null;

        return table[index].value;
    }

    /**
     * Removes the entry associated with the key.
     * After removing the entry, every following entry in the same
     * cluster is temporarily removed and inserted again. This repairs
     * all probe sequences that may have depended on the deleted position.
     * <p>
     * Average time depends on the cluster length.
     * Worst-case time complexity: O(n).
     */
    @Override
    public V remove(K key) {
        int index = findKeyIndex(key);

        if (index == -1)
            return null;

        V oldValue = table[index].value;

        table[index] = null;
        size--;

        // Reinsert all following entries in the cluster.
        index = (index + 1) % table.length;

        while (table[index] != null) {
            Entry<K, V> entry = table[index];

            table[index] = null;
            size--;

            insertNewEntry(entry);
            size++;

            index = (index + 1) % table.length;
        }

        return oldValue;
    }

    /**
     * Checks whether the key exists.
     * <p>
     * Average time complexity: O(1).
     * Worst-case time complexity: O(n).
     */
    @Override
    public boolean containsKey(K key) {
        return findKeyIndex(key) != -1;
    }

    /**
     * Returns the number of stored entries.
     * <p>
     * Time complexity: O(1).
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns whether the table is empty.
     * <p>
     * Time complexity: O(1).
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all entries and restores the default capacity.
     * <p>
     * Time complexity: O(m).
     */
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        table = (Entry<K, V>[]) new Entry[DEFAULT_CAPACITY];
        size = 0;
    }


    // -------- helpers --------

    /**
     * Searches for a key using linear probing.
     * Because this implementation has no tombstones, reaching
     * null means that the key is not present.
     * <p>
     * Average time complexity: O(1).
     * Worst-case time complexity: O(n).
     */
    private int findKeyIndex(K key) {
        int index = indexFor(key);

        for (int probes = 0; probes < table.length; probes++) {

            if (table[index] == null)
                return -1;

            if (keysEqual(table[index].key, key))
                return index;

            index = (index + 1) % table.length;
        }

        return -1;
    }

    /**
     * Inserts an entry into the first available position in its probe sequence.
     * <p>
     * Average time complexity: O(1).
     * Worst-case time complexity: O(n).
     */
    private void insertNewEntry(Entry<K, V> entry) {
        int index = indexFor(entry.key);

        while (table[index] != null)
            index = (index + 1) % table.length;

        table[index] = entry;
    }

    /**
     * Calculates the initial position of a key.
     * <p>
     * Time complexity: O(1), assuming hashCode() is O(1).
     */
    private int indexFor(K key) {
        if (key == null)
            return 0;

        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    /**
     * Compares two keys.
     * <p>
     * Time complexity: O(1), assuming equals() is O(1).
     */
    private boolean keysEqual(K first, K second) {
        if (first == null)
            return second == null;

        return first.equals(second);
    }

    /**
     * Resizes the table and reinserts all existing entries.
     * <p>
     * Time complexity: O(n + m).
     */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;

        table = (Entry<K, V>[]) new Entry[newCapacity];

        for (Entry<K, V> entry : oldTable) {
            if (entry != null)
                insertNewEntry(entry);
        }
    }
}