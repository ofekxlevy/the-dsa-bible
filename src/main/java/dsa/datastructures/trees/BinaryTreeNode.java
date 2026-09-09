package dsa.datastructures.trees;

/**
 * Represents a node in a binary tree.
 * Each node stores:
 * 1. A value.
 * 2. A reference to its left child.
 * 3. A reference to its right child.
 */
public class BinaryTreeNode<T> {

    // -------- fields --------

    T data;
    BinaryTreeNode<T> left;
    BinaryTreeNode<T> right;


    // -------- constructor --------

    /**
     * Creates a new leaf node.
     * <p>
     * Time Complexity: O(1)
     */
    public BinaryTreeNode(T data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }


    // -------- methods --------

    /**
     * Returns the value stored in the node.
     * <p>
     * Time Complexity: O(1)
     */
    public T getData() {
        return data;
    }

    /**
     * Returns the left child.
     * <p>
     * Time Complexity: O(1)
     */
    public BinaryTreeNode<T> getLeft() {
        return left;
    }

    /**
     * Returns the right child.
     * <p>
     * Time Complexity: O(1)
     */
    public BinaryTreeNode<T> getRight() {
        return right;
    }
}