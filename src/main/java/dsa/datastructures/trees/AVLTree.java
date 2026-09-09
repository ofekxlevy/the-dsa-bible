package dsa.datastructures.trees;

/**
 * An AVL tree is a self-balancing Binary Search Tree.
 * For every node:
 * - All values in the left subtree are smaller.
 * - All values in the right subtree are greater.
 * - The difference between the heights of the left and right
 *   subtrees is at most 1.
 * Because the tree remains balanced, its height is O(log n),
 * which guarantees O(log n) search, insertion, and deletion.
 *
 * @param <T> the type of values stored in the tree
 */
public class AVLTree<T extends Comparable<? super T>>
        extends BinarySearchTree<T> {

    // -------- constructor --------

    /**
     * Constructs an empty AVL tree.
     * <p>
     * Time Complexity: O(1)
     */
    public AVLTree() {
        super();
    }


    // -------- insert --------

    /**
     * Inserts a value into the AVL tree.
     * After insertion, the tree is rebalanced if necessary.
     * <p>
     * Time Complexity: O(log n)
     */
    @Override
    public void insert(T value) {
        root = insert(root, value);
    }

    /**
     * Receives the root of a subtree and returns the root
     * of that subtree after inserting and rebalancing.
     */
    private BinaryTreeNode<T> insert(BinaryTreeNode<T> node, T value) {
        if (node == null) {
            size++;
            return new AVLNode<>(value);
        }

        int comparison = value.compareTo(node.data);

        if (comparison < 0)
            node.left = insert(node.left, value);
        else if (comparison > 0)
            node.right = insert(node.right, value);
        else
            return node;

        updateHeight(node);
        return rebalance(node);
    }


    // -------- remove --------

    /**
     * Removes a value from the AVL tree.
     * After removal, the tree is rebalanced if necessary.
     * <p>
     * Time Complexity: O(log n)
     */
    @Override
    public void remove(T value) {
        root = remove(root, value);
    }

    /**
     * Receives the root of a subtree and returns the root
     * of that subtree after removing the value and rebalancing.
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

            // Case 1: no left child
            if (node.left == null) {
                size--;
                return node.right;
            }

            // Case 2: no right child
            if (node.right == null) {
                size--;
                return node.left;
            }

            // Case 3: two children
            BinaryTreeNode<T> successor = min(node.right);
            node.data = successor.data;
            node.right = remove(node.right, successor.data);
        }

        updateHeight(node);
        return rebalance(node);
    }


    /**
     * Rebalancing behavior differs between insertion and deletion:
     * - After insertion, only the first unbalanced ancestor on the path
     *   back to the root may require a rotation. Once it is rebalanced,
     *   the subtree height is restored, so higher ancestors remain balanced.
     * - After deletion, rebalancing may be required at multiple ancestors
     *   on the path back to the root, because a rotation does not always
     *   restore the subtree to its previous height.
     */


    // -------- height --------

    /**
     * Returns the stored height of a node.
     * A null subtree has height -1.
     * <p>
     * Time Complexity: O(1)
     */
    private int height(BinaryTreeNode<T> node) {
        if (node == null)
            return -1;

        return ((AVLNode<T>) node).height;
    }

    /**
     * Updates the height of a node according to the heights
     * of its children.
     * <p>
     * Time Complexity: O(1)
     */
    private void updateHeight(BinaryTreeNode<T> node) {
        ((AVLNode<T>) node).height = 1 + Math.max(height(node.left), height(node.right));
    }


    // -------- balance --------

    /**
     * Returns the balance factor of a node.
     * balanceFactor = height(left subtree) - height(right subtree)
     * <p>
     * Time Complexity: O(1)
     */
    private int balanceFactor(BinaryTreeNode<T> node) {
        if (node == null)
            return 0;

        return height(node.left) - height(node.right);
    }

    /**
     * Rebalances a subtree if it violates the AVL property.
     * <p>
     * Time Complexity: O(1)
     */
    private BinaryTreeNode<T> rebalance(BinaryTreeNode<T> node) {
        int balance = balanceFactor(node);

        // Left-heavy subtree
        if (balance > 1) {

            // Left-Right case
            if (balanceFactor(node.left) < 0)
                node.left = rotateLeft(node.left);

            // Left-Left case
            return rotateRight(node);
        }

        // Right-heavy subtree
        if (balance < -1) {

            // Right-Left case
            if (balanceFactor(node.right) > 0)
                node.right = rotateRight(node.right);

            // Right-Right case
            return rotateLeft(node);
        }

        return node;
    }


    // -------- rotations --------

    /**
     * Performs a left rotation.
     *
     * Before:
     *
     *       x
     *        \
     *         y
     *        /
     *       T2
     *
     * After:
     *
     *         y
     *        /
     *       x
     *        \
     *         T2
     *
     * Time Complexity: O(1)
     */
    private BinaryTreeNode<T> rotateLeft(BinaryTreeNode<T> x) {

        BinaryTreeNode<T> y = x.right;
        BinaryTreeNode<T> t2 = y.left;

        y.left = x;
        x.right = t2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    /**
     * Performs a right rotation.
     * Before:
     *         y
     *        /
     *       x
     *        \
     *         T2
     * After:
     *       x
     *        \
     *         y
     *        /
     *       T2
     * Time Complexity: O(1)
     */
    private BinaryTreeNode<T> rotateRight(BinaryTreeNode<T> y) {
        BinaryTreeNode<T> x = y.left;
        BinaryTreeNode<T> t2 = x.right;

        x.right = y;
        y.left = t2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }
}