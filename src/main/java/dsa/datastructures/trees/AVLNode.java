package dsa.datastructures.trees;

/**
 * Represents a node in an AVL tree.
 * In addition to the regular binary tree node fields,
 * each node stores its height.
 * A leaf has height 0.
 *
 * @param <T> the type of value stored in the node
 */
public class AVLNode<T> extends BinaryTreeNode<T> {

    // -------- fields --------

    protected int height;


    // -------- constructor --------

    /**
     * Constructs a new AVL leaf node.
     * <p>
     * Time Complexity: O(1)
     */
    public AVLNode(T data) {
        super(data);
        this.height = 0;
    }
}