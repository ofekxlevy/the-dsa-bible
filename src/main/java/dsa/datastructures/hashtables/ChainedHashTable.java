package dsa.datastructures.hashtables;

/**
 * A hash table implementation using separate chaining for collision resolution.
 * Each bucket contains a singly linked list of entries. When multiple keys
 * are mapped to the same bucket, they are stored in the same linked list.
 * The table automatically grows when its load factor exceeds the maximum
 * allowed load factor.
 * <P>
 * Average time complexity:
 * put      - O(1)
 * get      - O(1)
 * remove   - O(1)
 * contains - O(1)
 * <P>
 * Worst-case time complexity for these operations is O(n), when many or all
 * keys are mapped to the same bucket.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class ChainedHashTable<K, V> implements HashTable<K, V> {

    // -------- nested class: Entry --------

    /**
     * Represents a key-value pair stored in the hash table.
     * The next reference is used to create the linked list inside each bucket.
     */
    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        private Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }


    // -------- fields --------

    private static final int DEFAULT_CAPACITY = 16;
    private static final double MAX_LOAD_FACTOR = 0.75;
    private Entry<K, V>[] buckets;
    private int size;


    // -------- constructors --------

    /**
     * Creates an empty hash table with the default capacity.
     * <P>
     * Time complexity: O(m), where m is the initial number of buckets.
     */
    @SuppressWarnings("unchecked")
    public ChainedHashTable() {
        buckets = (Entry<K, V>[]) new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    /**
     * Creates an empty hash table with the specified initial capacity.
     *
     * @param initialCapacity the initial number of buckets
     * @throws IllegalArgumentException if initialCapacity is not positive
     * <P>
     * Time complexity: O(m), where m is the requested number of buckets.
     */
    @SuppressWarnings("unchecked")
    public ChainedHashTable(int initialCapacity) {
        if (initialCapacity <= 0)
            throw new IllegalArgumentException("Capacity must be positive");

        buckets = (Entry<K, V>[]) new Entry[initialCapacity];
        size = 0;
    }


    // -------- methods --------

    /**
     * Associates the specified value with the given key.
     * If the key already exists, its current value is replaced.
     * Otherwise, a new entry is inserted at the beginning of the appropriate
     * bucket's linked list.
     * <P>
     * The table is resized when inserting a new entry would cause the
     * load factor to exceed the maximum allowed load factor.
     *
     * @return the previous value associated with the key,
     *         or null if the key was not previously present
     * <P>
     * Average time complexity: O(1).
     * Worst-case time complexity: O(n).
     * Resizing takes O(n), but because resizing happens only occasionally,
     * put has amortized average time complexity O(1).
     */
    @Override
    public V put(K key, V value) {
        int index = indexFor(key);
        Entry<K, V> current = buckets[index];

        // Check whether the key already exists.
        while (current != null) {
            if (keysEqual(current.key, key)) {
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            }

            current = current.next;
        }

        // Resize only when a new entry is actually going to be inserted.
        if ((size + 1.0) / buckets.length > MAX_LOAD_FACTOR) {
            resize(buckets.length * 2);
            index = indexFor(key);
        }

        buckets[index] = new Entry<>(key, value, buckets[index]);
        size++;
        return null;
    }

    /**
     * Returns the value associated with the specified key.
     *
     * @return the associated value, or null if the key is not present
     * <P>
     * Average time complexity: O(1).
     * Worst-case time complexity: O(n).
     */
    @Override
    public V get(K key) {
        Entry<K, V> current = buckets[indexFor(key)];

        while (current != null) {
            if (keysEqual(current.key, key))
                return current.value;

            current = current.next;
        }

        return null;
    }

    /**
     * Removes the entry associated with the specified key.
     *
     * @return the removed value, or null if the key is not present
     * <P>
     * Average time complexity: O(1).
     * Worst-case time complexity: O(n).
     */
    @Override
    public V remove(K key) {
        int index = indexFor(key);

        Entry<K, V> current = buckets[index];
        Entry<K, V> previous = null;

        while (current != null) {
            if (keysEqual(current.key, key)) {
                if (previous == null) // The entry is the first node in the bucket.
                    buckets[index] = current.next;
                else
                    previous.next = current.next;

                size--;
                return current.value;
            }

            previous = current;
            current = current.next;
        }

        return null;
    }

    /**
     * Returns whether the table contains the specified key.
     * <P>
     * Average time complexity: O(1).
     * Worst-case time complexity: O(n).
     */
    @Override
    public boolean containsKey(K key) {
        Entry<K, V> current = buckets[indexFor(key)];

        while (current != null) {
            if (keysEqual(current.key, key))
                return true;

            current = current.next;
        }

        return false;
    }

    /**
     * Returns the number of key-value pairs stored in the table.
     * <P>
     * Time complexity: O(1).
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns whether the hash table contains no entries.
     * <P>
     * Time complexity: O(1).
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all entries from the hash table and restores the default capacity.
     * <P>
     * Time complexity: O(m), where m is the default number of buckets.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        buckets = (Entry<K, V>[]) new Entry[DEFAULT_CAPACITY];
        size = 0;
    }


    // -------- helpers --------

    /**
     * Calculates the bucket index for the specified key.
     * Null keys are supported and are always mapped to bucket 0.
     * <P>
     * Time complexity: O(1), assuming hashCode() runs in O(1).
     */
    private int indexFor(K key) {
        if (key == null)
            return 0;

        return (key.hashCode() & 0x7fffffff) % buckets.length;
    }

    /**
     * Compares two keys while supporting null keys.
     * <P>
     * Time complexity: O(1), assuming equals() runs in O(1).
     */
    private boolean keysEqual(K first, K second) {
        if (first == null)
            return second == null;

        return first.equals(second);
    }

    /**
     * Resizes the bucket array and redistributes all existing entries
     * according to the new capacity.
     * Rehashing is necessary because a bucket index depends on the
     * number of buckets.
     * <P>
     * Time complexity: O(n + m), where n is the number of stored entries
     * and m is the new capacity.
     */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Entry<K, V>[] oldBuckets = buckets;
        buckets = (Entry<K, V>[]) new Entry[newCapacity];

        for (Entry<K, V> bucket : oldBuckets) {
            Entry<K, V> current = bucket;

            while (current != null) {
                Entry<K, V> next = current.next;
                int newIndex = indexFor(current.key);

                current.next = buckets[newIndex];
                buckets[newIndex] = current;
                current = next;
            }
        }
    }
}