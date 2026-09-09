package dsa.datastructures.trees;

/**
 * A Binary Search Tree (BST).
 * For every node in the tree:
 * - All values in the left subtree are smaller than the node's value.
 * - All values in the right subtree are greater than the node's value.
 * Duplicate values are not allowed.
 *
 * @param <T> the type of values stored in the tree
 */
public class BinarySearchTree<T extends Comparable<? super T>> extends BinaryTree<T> {

    // -------- constructor --------
    /**
     * Constructs an empty Binary Search Tree.
     * <p>
     * Time Complexity: O(1)
     */
    public BinarySearchTree() {
        super();
    }


    // -------- methods --------

    /**
     * Inserts a value into the BST.
     * <p>
     * Time Complexity: O(h)
     * Average Case: O(log n)
     * Worst Case: O(n)
     */
    @Override
    public void insert(T value) {
        root = insert(root, value);
    }

    /**
     * Receives the root of a subtree and returns the root
     * of that subtree after inserting the new value.
     */
    private BinaryTreeNode<T> insert(BinaryTreeNode<T> node, T value) {
        if (node == null) {
            size++;
            return new BinaryTreeNode<>(value);
        }

        int comparison = value.compareTo(node.data);

        if (comparison < 0)
            node.left = insert(node.left, value);
        else if (comparison > 0)
            node.right = insert(node.right, value);

        return node;
    }

    /**
     * Searches for a node containing the given value.
     *
     * @return the node containing the value,
     *         or null if the value does not exist
     * <p>
     * Time Complexity: O(h)
     * Average Case: O(log n)
     * Worst Case: O(n)
     */
    @Override
    public BinaryTreeNode<T> search(T value) {
        return search(root, value);
    }

    /**
     * Recursively searches for a value using the BST ordering property.
     */
    private BinaryTreeNode<T> search(BinaryTreeNode<T> node, T value) {
        if (node == null)
            return null;

        int comparison = value.compareTo(node.data);

        if (comparison == 0)
            return node;

        if (comparison < 0)
            return search(node.left, value);

        return search(node.right, value);
    }

    /**
     * Returns true if the tree contains the given value.
     * <p>
     * Time Complexity: O(h)
     * Average Case: O(log n)
     * Worst Case: O(n)
     */
    @Override
    public boolean contains(T value) {
        return search(value) != null;
    }

    /**
     * Returns the minimum value in the tree.
     *
     * @throws IllegalStateException if the tree is empty
     * <p>
     * Time Complexity: O(h)
     */
    public T min() {
        if (isEmpty())
            throw new IllegalStateException("Tree is empty");

        return min(root).data;
    }

    /**
     * Recursively finds the leftmost node in a subtree.
     */
    private BinaryTreeNode<T> min(BinaryTreeNode<T> node) {
        if (node.left == null)
            return node;

        return min(node.left);
    }

    /**
     * Returns the maximum value in the tree.
     *
     * @throws IllegalStateException if the tree is empty
     * <p>
     * Time Complexity: O(h)
     */
    public T max() {
        if (isEmpty())
            throw new IllegalStateException("Tree is empty");

        return max(root).data;
    }

    /**
     * Recursively finds the rightmost node in a subtree.
     */
    private BinaryTreeNode<T> max(BinaryTreeNode<T> node) {
        if (node.right == null)
            return node;

        return max(node.right);
    }

    /**
     * Removes the given value from the BST.
     * If the value does not exist, the tree remains unchanged.
     * <p>
     * Time Complexity: O(h)
     * Average Case: O(log n)
     * Worst Case: O(n)
     */
    public void remove(T value) {
        root = remove(root, value);
    }

    /**
     * Receives the root of a subtree and returns the root
     * of that subtree after removing the given value.
     */
    private BinaryTreeNode<T> remove(BinaryTreeNode<T> node, T value) {
        if (node == null)
            return null;

        int comparison = value.compareTo(node.data);

        if (comparison < 0)
            node.left = remove(node.left, value);
        else if (comparison > 0)
            node.right = remove(node.right, value);
        else {

            // Case 1: The node has no left child.
             // This also handles the case where the node is a leaf.
            if (node.left == null) {
                size--;
                return node.right;
            }

            // Case 2: The node has no right child.
            if (node.right == null) {
                size--;
                return node.left;
            }

            // Case 3: The node has two children.
            // Replace its value with the smallest value in its right subtree (inorder successor).
            BinaryTreeNode<T> successor = min(node.right);
            node.data = successor.data;
            // Remove the successor from the right subtree.
            // size is decreased inside the recursive call, so it must not be decreased here.
            node.right = remove(node.right, successor.data);
        }

        return node;
    }

    /**
     * Returns the smallest value strictly greater than the given value.
     * Returns null if no such value exists.
     * <p>
     * Time Complexity: O(h)
     */
    public T successor(T value) {
        BinaryTreeNode<T> node = successor(root, value, null);
        return node == null ? null : node.data;
    }

    /**
     * Recursively searches for the smallest value greater than the given value.
     * candidate stores the best successor found so far.
     */
    private BinaryTreeNode<T> successor(BinaryTreeNode<T> node, T value, BinaryTreeNode<T> candidate) {
        if (node == null)
            return candidate;

        int comparison = value.compareTo(node.data);

        if (comparison < 0)
            return successor(node.left, value, node);

        return successor(node.right, value, candidate);
    }

    /**
     * Returns the largest value strictly smaller than the given value.
     * Returns null if no such value exists.
     * <p>
     * Time Complexity: O(h)
     */
    public T predecessor(T value) {
        BinaryTreeNode<T> node = predecessor(root, value, null);
        return node == null ? null : node.data;
    }

    /**
     * Recursively searches for the largest value smaller than the given value.
     * candidate stores the best predecessor found so far.
     */
    private BinaryTreeNode<T> predecessor(BinaryTreeNode<T> node, T value, BinaryTreeNode<T> candidate) {
        if (node == null)
            return candidate;

        int comparison = value.compareTo(node.data);

        if (comparison > 0) 
            return predecessor(node.right, value, node);

        return predecessor(node.left, value, candidate);
    }
}