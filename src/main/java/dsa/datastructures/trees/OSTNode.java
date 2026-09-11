package dsa.datastructures.trees;

/**
 * Represents a node in an Order Statistic Tree.
 * In addition to the information stored in an AVL node,
 * each node stores the number of nodes in its subtree,
 * including itself.
 *
 * @param <T> the type of value stored in the node
 */
public class OSTNode<T> extends AVLNode<T> {

    // -------- fields --------

    protected int subtreeSize;


    // -------- constructor --------

    /**
     * Constructs a new OST leaf node.
     * A new leaf has:
     * height = 0
     * subtreeSize = 1
     * <p>
     * Time Complexity: O(1)
     */
    public OSTNode(T data) {
        super(data);
        this.subtreeSize = 1;
    }
}