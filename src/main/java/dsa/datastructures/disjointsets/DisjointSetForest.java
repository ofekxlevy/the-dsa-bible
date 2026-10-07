package dsa.datastructures.disjointsets;

import dsa.datastructures.hashtables.ChainedHashTable;

/**
 * A Disjoint Set (Union-Find) implementation using a forest of rooted trees.
 * Each set is represented by a rooted tree. Every element is stored in a
 * node that contains a pointer to its parent. The root of the tree is the
 * representative of the set and points to itself.
 * <p>
 * This implementation uses two optimizations:
 * <ul>
 *     <li>
 *         <b>Path compression:</b> during FindSet, every node visited on
 *         the path to the root is changed to point directly to the root.
 *     </li>
 *     <li>
 *         <b>Union by rank:</b> the root with the smaller rank is attached
 *         below the root with the larger rank.
 *     </li>
 * </ul>
 *
 * The values are mapped to their corresponding nodes using a chained
 * hash table.
 *
 * @param <T> the type of elements stored in the sets
 */
public class DisjointSetForest<T> implements DisjointSet<T> {

    /**
     * Maps each value to its corresponding node in the forest.
     */
    private final ChainedHashTable<T, Node<T>> nodes;

    /**
     * Creates an empty disjoint-set forest.
     */
    public DisjointSetForest() {
        nodes = new ChainedHashTable<>();
    }

    /**
     * Creates a new set containing only {@code value}.
     * The new node is initially the root of its own tree.
     * Therefore, its parent points to itself and its rank is 0.
     *
     * @param value the value for which a new set is created
     * @throws IllegalArgumentException if the value already belongs to a set
     */
    @Override
    public void makeSet(T value) {
        if (nodes.containsKey(value))
            throw new IllegalArgumentException("The value already belongs to a set: " + value);

        nodes.put(value, new Node<>(value));
    }

    /**
     * Returns the representative of the set containing {@code value}.
     * This operation uses path compression. After the representative
     * is found, all nodes visited on the path point directly to it.
     *
     * @param value the value whose set representative is requested
     * @return the representative of the set containing {@code value}
     * @throws IllegalArgumentException if the value does not belong to any set
     */
    @Override
    public T findSet(T value) {
        Node<T> node = getNode(value);
        return findSet(node).value;
    }

    /**
     * Finds the root of the tree containing {@code node}.
     * If {@code node} is not a root, the method recursively finds the
     * root of its parent. On the way back from the recursion, the parent
     * pointer is changed to point directly to the root.
     * This is the path-compression optimization.
     *
     * @param node the node whose root is requested
     * @return the root of the tree containing {@code node}
     */
    private Node<T> findSet(Node<T> node) {
        if (node.parent != node)
            node.parent = findSet(node.parent);

        return node.parent;
    }

    /**
     * Merges the sets containing {@code x} and {@code y}.
     * The representatives of both sets are found first. The root with
     * the smaller rank is attached below the root with the larger rank.
     * If both roots have the same rank, the root of {@code x} is attached
     * below the root of {@code y}, and the rank of the root of {@code y}
     * is increased by one.
     * If both values already belong to the same set, no change is made.
     *
     * @param x an element in the first set
     * @param y an element in the second set
     * @throws IllegalArgumentException if either value does not belong to a set
     */
    @Override
    public void union(T x, T y) {
        Node<T> rootX = findSet(getNode(x));
        Node<T> rootY = findSet(getNode(y));

        if (rootX == rootY)
            return;

        if (rootX.rank > rootY.rank)
            rootY.parent = rootX;
        else {
            rootX.parent = rootY;

            if (rootX.rank == rootY.rank)
                rootY.rank++;
        }
    }

    /**
     * Returns the node associated with {@code value}.
     *
     * @param value the value whose node is requested
     * @return the node associated with {@code value}
     * @throws IllegalArgumentException if the value does not belong to any set
     */
    private Node<T> getNode(T value) {
        Node<T> node = nodes.get(value);

        if (node == null)
            throw new IllegalArgumentException("The value does not belong to any set: " + value);

        return node;
    }

    /**
     * Represents an element in the disjoint-set forest.
     * Each node stores its value, a pointer to its parent, and its rank.
     * A root node points to itself. The rank is an upper bound on the
     * height of the tree and is used by the union-by-rank optimization.
     *
     * @param <T> the type of value stored in the node
     */
    private static class Node<T> {

        private final T value;
        private Node<T> parent;
        private int rank;

        /**
         * Creates a node representing a singleton set.
         *
         * @param value the value stored in the node
         */
        private Node(T value) {
            this.value = value;
            this.parent = this;
            this.rank = 0;
        }
    }
}