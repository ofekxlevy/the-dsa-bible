package dsa.datastructures.hashtables;

/**
 * A hash table using open addressing with linear probing.
 * Deletions are handled using tombstones.
 * <p>
 * Each position can be:
 * EMPTY    - never occupied
 * OCCUPIED - currently contains an entry
 * DELETED  - previously occupied, but the entry was removed
 * <p>
 * Average time complexity:
 * put      - O(1) amortized
 * get      - O(1)
 * remove   - O(1)
 * contains - O(1)
 * Worst-case time complexity: O(n).
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class LinearProbingWithTombstone<K, V> implements HashTable<K, V> {

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

    private enum State { EMPTY, OCCUPIED, DELETED }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double MAX_LOAD_FACTOR = 0.75;

    private Entry<K, V>[] table;
    private State[] states;
    private int size;


    // -------- constructors --------

    /**
     * Creates an empty table with the default capacity.
     * <p>
     * Time complexity: O(m).
     */
    @SuppressWarnings("unchecked")
    public LinearProbingWithTombstone() {
        table = (Entry<K, V>[]) new Entry[DEFAULT_CAPACITY];
        states = new State[DEFAULT_CAPACITY];

        for (int i = 0; i < states.length; i++)
            states[i] = State.EMPTY;

        size = 0;
    }

    /**
     * Creates an empty table with the specified capacity.
     * <p>
     * Time complexity: O(m).
     */
    @SuppressWarnings("unchecked")
    public LinearProbingWithTombstone(int initialCapacity) {
        if (initialCapacity <= 0)
            throw new IllegalArgumentException("Capacity must be positive");

        table = (Entry<K, V>[]) new Entry[initialCapacity];
        states = new State[initialCapacity];

        for (int i = 0; i < states.length; i++)
            states[i] = State.EMPTY;

        size = 0;
    }


    // -------- methods --------

    /**
     * Inserts a new key-value pair into the hash table or updates the value
     * associated with an existing key.
     * <p>
     * First, the table is searched for the key. If the key already exists,
     * its value is updated and the previous value is returned.
     * If the key does not exist, linear probing is used to find an insertion
     * position. The new entry is inserted immediately into the first DELETED
     * or EMPTY position encountered.
     * The table is resized before inserting a new entry if the insertion would
     * cause the load factor to exceed the maximum allowed load factor.
     *
     * @param key   the key to insert
     * @param value the value associated with the key
     * @return the previous value if the key already existed,
     *         or null if a new entry was inserted
     * <p>
     * Average amortized time complexity: O(1).
     * Worst-case time complexity: O(n).
     */
    @Override
    public V put(K key, V value) {
        // First check whether the key already exists.
        int existingIndex = findKeyIndex(key);

        if (existingIndex != -1) {
            V oldValue = table[existingIndex].value;
            table[existingIndex].value = value;
            return oldValue;
        }

        // The key is new, so check whether the table must grow.
        if ((size + 1.0) / table.length > MAX_LOAD_FACTOR)
            resize(table.length * 2);

        int index = indexFor(key);

        // Search for the first available position.
        for (int probes = 0; probes < table.length; probes++) {
            if (states[index] == State.EMPTY ||
                    states[index] == State.DELETED) {

                table[index] = new Entry<>(key, value);
                states[index] = State.OCCUPIED;
                size++;

                return null;
            }

            index = (index + 1) % table.length;
        }

        throw new IllegalStateException("Hash table is full");
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
     * The position is marked DELETED instead of EMPTY so that
     * existing probe sequences are not broken.
     * <p>
     * Average time complexity: O(1).
     * Worst-case time complexity: O(n).
     */
    @Override
    public V remove(K key) {
        int index = findKeyIndex(key);

        if (index == -1)
            return null;

        V oldValue = table[index].value;

        table[index] = null;
        states[index] = State.DELETED;
        size--;

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
        states = new State[DEFAULT_CAPACITY];

        for (int i = 0; i < states.length; i++)
            states[i] = State.EMPTY;

        size = 0;
    }


    // -------- helpers --------

    /**
     * Searches for a key using linear probing.
     * EMPTY stops the search.
     * DELETED does not stop the search.
     * <p>
     * Average time complexity: O(1).
     * Worst-case time complexity: O(n).
     */
    private int findKeyIndex(K key) {
        int index = indexFor(key);

        for (int probes = 0; probes < table.length; probes++) {

            if (states[index] == State.EMPTY)
                return -1;

            if (states[index] == State.OCCUPIED && keysEqual(table[index].key, key))
                return index;

            index = (index + 1) % table.length;
        }

        return -1;
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
     * Tombstones disappear during resizing.
     * <p>
     * Time complexity: O(n + m).
     */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        State[] oldStates = states;

        table = (Entry<K, V>[]) new Entry[newCapacity];
        states = new State[newCapacity];

        for (int i = 0; i < states.length; i++)
            states[i] = State.EMPTY;

        for (int i = 0; i < oldTable.length; i++)
            if (oldStates[i] == State.OCCUPIED)
                insertDuringResize(oldTable[i]);
    }

    /**
     * Inserts an existing entry during resizing.
     * <p>
     * Average time complexity: O(1).
     * Worst-case time complexity: O(n).
     */
    private void insertDuringResize(Entry<K, V> entry) {
        int index = indexFor(entry.key);

        while (states[index] == State.OCCUPIED)
            index = (index + 1) % table.length;

        table[index] = entry;
        states[index] = State.OCCUPIED;
    }
}