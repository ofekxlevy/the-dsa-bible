package dsa.datastructures.disjointsets;

/**
 * An interface for the Disjoint Set (Union-Find) data structure.
 * A disjoint set structure maintains a collection of pairwise
 * disjoint sets. Each set has a representative element.
 * <p>
 * The structure supports three fundamental operations:
 * <ul>
 *     <li>{@code makeSet} - creates a new set containing a single element.</li>
 *     <li>{@code findSet} - returns the representative of the set
 *         containing a given element.</li>
 *     <li>{@code union} - merges two disjoint sets into one set.</li>
 * </ul>
 *
 * @param <T> the type of elements stored in the sets
 */
public interface DisjointSet<T> {

    /**
     * Creates a new set containing only the given element.
     *
     * @param value the element for which a new set is created
     */
    void makeSet(T value);

    /**
     * Returns the representative of the set containing the given element.
     *
     * @param value the element whose set representative is requested
     * @return the representative of the set containing {@code value}
     */
    T findSet(T value);

    /**
     * Merges the sets containing {@code x} and {@code y}.
     * It is assumed that {@code x} and {@code y} belong to different sets before this operation.
     *
     * @param x an element from the first set
     * @param y an element from the second set
     */
    void union(T x, T y);
}