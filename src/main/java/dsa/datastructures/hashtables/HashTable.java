package dsa.datastructures.hashtables;

/**
 * A generic key-value hash table.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public interface HashTable<K, V> {

    /**
     * Associates the given value with the specified key.
     * If the key already exists, its value is replaced.
     *
     * @return the previous value associated with the key,
     *         or null if the key was not present
     */
    V put(K key, V value);

    /**
     * Returns the value associated with the given key.
     *
     * @return the associated value, or null if the key does not exist
     */
    V get(K key);

    /**
     * Removes the entry associated with the given key.
     *
     * @return the removed value, or null if the key does not exist
     */
    V remove(K key);

    /**
     * Returns whether the table contains the given key.
     */
    boolean containsKey(K key);

    /**
     * Returns the number of key-value pairs stored in the table.
     */
    int size();

    /**
     * Returns whether the table contains no entries.
     */
    boolean isEmpty();

    /**
     * Removes all entries from the table.
     */
    void clear();
}