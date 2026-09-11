package dsa.datastructures.trees;

/**
 * An Order Statistic Tree implemented as an AVL Tree.
 * In addition to the AVL properties, every node stores the
 * number of nodes in its subtree.
 * This additional information allows efficient order-statistic
 * operations on the sorted order of the tree:
 * - select(k) returns the value at index k in the sorted order.
 *   The index is 0-based, so select(0) returns the smallest value.
 * - rank(x) returns the number of values strictly smaller than x.
 *   Therefore, if x exists in the tree, rank(x) is its 0-based
 *   index in the sorted order.
 * Because the tree is implemented as an AVL Tree, its height
 * remains O(log n).
 * <p>
 * Therefore:
 * - Search: O(log n)
 * - Insert: O(log n)
 * - Remove: O(log n)
 * - Select: O(log n)
 * - Rank:   O(log n)
 *
 * @param <T> the type of values stored in the tree
 */
public class OrderStatisticTree<T extends Comparable<? super T>> extends AVLTree<T> {

    // -------- constructor --------

    /**
     * Constructs an empty Order Statistic Tree.
     * <p>
     * Time Complexity: O(1)
     */
    public OrderStatisticTree() {
        super();
    }


    // -------- insert --------

    /**
     * Inserts a value into the tree.
     * After insertion, heights and subtree sizes are updated,
     * and the tree is rebalanced if necessary.
     * <p>
     * Time Complexity: O(log n)
     */
    @Override
    public void insert(T value) {
        root = insert(root, value);
    }

    /**
     * Receives the root of a subtree and returns the root of that subtree after inserting,
     * updating its metadata, and rebalancing.
     */
    private BinaryTreeNode<T> insert(BinaryTreeNode<T> node, T value) {
        if (node == null) {
            size++;
            return new OSTNode<>(value);
        }

        int comparison = value.compareTo(node.data);

        if (comparison < 0)
            node.left = insert(node.left, value);
        else if (comparison > 0)
            node.right = insert(node.right, value);
        else  // Duplicate values are not inserted.
            return node;

        update(node);
        return rebalance(node);
    }


    // -------- remove --------

    /**
     * Removes a value from the tree.
     * After removal, heights and subtree sizes are updated,
     * and every ancestor on the path to the root is rebalanced
     * if necessary.
     * <p>
     * Time Complexity: O(log n)
     */
    @Override
    public void remove(T value) {
        root = remove(root, value);
    }

    /**
     * Receives the root of a subtree and returns the root of that subtree after removing the value,
     * updating its metadata, and rebalancing.
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
            BinaryTreeNode<T> successor = minimum(node.right);
            node.data = successor.data;
            node.right = remove(node.right, successor.data);
        }

        update(node);
        return rebalance(node);
    }


    // -------- select --------

    /**
     * Returns the value at index k in the sorted order of the tree.
     * The index is 0-based:
     * - select(0) returns the smallest value.
     * - select(size - 1) returns the largest value.
     * <p>
     * Time Complexity: O(log n)
     *
     * @param k the 0-based index in the sorted order
     * @return the value at index k
     * @throws IndexOutOfBoundsException if k is outside the range [0, size - 1]
     */
    public T select(int k) {
        if (k < 0 || k >= size)
            throw new IndexOutOfBoundsException("k must be between 0 and " + (size - 1));

        return select(root, k).data;
    }

    /**
     * Recursively finds the node at index k in the sorted order of the given subtree.
     * The number of nodes in the left subtree determines the index of the current node:
     * current node index = subtreeSize(node.left)
     * If k is smaller than the current node's index, the desired node is in the left subtree.
     * If k is equal to the current node's index, the current node is the desired node.
     * If k is larger, the desired node is in the right subtree.
     * In that case, the entire left subtree and the current node are skipped.
     * <p>
     * Time Complexity: O(log n)
     *
     * @param node the root of the current subtree
     * @param k the 0-based index within the current subtree
     * @return the node at index k
     */
    private BinaryTreeNode<T> select(BinaryTreeNode<T> node, int k) {
        int leftSize = subtreeSize(node.left);

        if (k == leftSize)
            return node;

        if (k < leftSize)
            return select(node.left, k);

        return select(node.right, k - leftSize - 1);
    }


    // -------- rank --------

    /**
     * Returns the number of values strictly smaller than the given value.
     * If the value exists in the tree, the returned rank is its 0-based index in the sorted order.
     * <p>
     * Time Complexity: O(log n)
     *
     * @param value the value whose rank is requested
     * @return the number of values strictly smaller than value
     */
    public int rank(T value) {
        return rank(root, value);
    }

    /**
     * Recursively calculates the rank of a value.
     */
    private int rank(BinaryTreeNode<T> node, T value) {
        if (node == null)
            return 0;

        int comparison = value.compareTo(node.data);

         // If value <= node.data, every value that may be smaller than value must be in the left subtree.
        if (comparison <= 0)
            return rank(node.left, value);

        // node.data < value. Therefore, all nodes in the left subtree and the current node are smaller than value.
        return subtreeSize(node.left) + 1 + rank(node.right, value);
    }


    // -------- metadata --------

    /**
     * Returns the height stored in a node.
     * A null subtree has height -1.
     * <p>
     * Time Complexity: O(1)
     */
    private int height(BinaryTreeNode<T> node) {
        if (node == null)
            return -1;

        return ((OSTNode<T>) node).height;
    }

    /**
     * Returns the number of nodes in a subtree.
     * A null subtree contains 0 nodes.
     * <p>
     * Time Complexity: O(1)
     */
    private int subtreeSize(BinaryTreeNode<T> node) {
        if (node == null)
            return 0;

        return ((OSTNode<T>) node).subtreeSize;
    }

    /**
     * Updates both the height and subtree size of a node.
     * <p>
     * Time Complexity: O(1)
     */
    private void update(BinaryTreeNode<T> node) {
        OSTNode<T> ostNode = (OSTNode<T>) node;
        ostNode.height = 1 + Math.max(height(node.left), height(node.right));
        ostNode.subtreeSize = 1 + subtreeSize(node.left) + subtreeSize(node.right);
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
     * Rebalances a subtree if necessary.
     * <p>
     * Time Complexity: O(1)
     */
    private BinaryTreeNode<T> rebalance(BinaryTreeNode<T> node) {
        int balance = balanceFactor(node);

        // Left-heavy
        if (balance > 1) {

            // Left-Right
            if (balanceFactor(node.left) < 0)
                node.left = rotateLeft(node.left);

            // Left-Left
            return rotateRight(node);
        }

        // Right-heavy
        if (balance < -1) {

            // Right-Left
            if (balanceFactor(node.right) > 0)
                node.right = rotateRight(node.right);

            // Right-Right
            return rotateLeft(node);
        }

        return node;
    }


    // -------- rotations --------

    /**
     * Performs a left rotation and updates both height and subtree size.
     * <p>
     * Time Complexity: O(1)
     */
    private BinaryTreeNode<T> rotateLeft(BinaryTreeNode<T> x) {
        BinaryTreeNode<T> y = x.right;
        BinaryTreeNode<T> t2 = y.left;
        y.left = x;
        x.right = t2;

        // x must be updated first because it becomes a child of y.
        update(x);
        update(y);
        return y;
    }

    /**
     * Performs a right rotation and updates both height and subtree size.
     * <p>
     * Time Complexity: O(1)
     */
    private BinaryTreeNode<T> rotateRight(BinaryTreeNode<T> y) {
        BinaryTreeNode<T> x = y.left;
        BinaryTreeNode<T> t2 = x.right;

        x.right = y;
        y.left = t2;

        // y must be updated first because it becomes a child of x.
        update(y);
        update(x);
        return x;
    }


    // -------- helpers --------

    /**
     * Returns the node containing the minimum value
     * in a subtree.
     * <p>
     * Time Complexity: O(log n)
     */
    private BinaryTreeNode<T> minimum(BinaryTreeNode<T> node) {
        if (node.left == null)
            return node;

        return minimum(node.left);
    }
}