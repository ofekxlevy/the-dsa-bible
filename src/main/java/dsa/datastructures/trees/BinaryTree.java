package dsa.datastructures.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * General binary tree implementation.
 * No ordering rule is imposed on the values.
 * Insertion is random:
 * at each node, the algorithm randomly chooses
 * whether to continue to the left or right subtree.
 */
public class BinaryTree<T> {

    // -------- fields --------

    protected BinaryTreeNode<T> root;
    protected int size;
    private final Random random;


    // -------- constructors --------

    /**
     * Creates an empty binary tree.
     *
     * Time Complexity: O(1)
     */
    public BinaryTree() {
        root = null;
        size = 0;
        random = new Random();
    }

    /**
     * Creates a binary tree with one root node.
     * <p>
     * Time Complexity: O(1)
     */
    public BinaryTree(T rootValue) {
        root = new BinaryTreeNode<>(rootValue);
        size = 1;
        random = new Random();
    }


    // -------- methods --------

    /**
     * Returns the root.
     * <p>
     * Time Complexity: O(1)
     */
    public BinaryTreeNode<T> getRoot() {
        return root;
    }

    /**
     * Returns the number of nodes.
     * <p>
     * Time Complexity: O(1)
     */
    public int size() {
        return size;
    }

    /**
     * Returns true if the tree is empty.
     * <p>
     * Time Complexity: O(1)
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Inserts a value into a random position in the tree.
     * <p>
     * Time Complexity: O(h)
     * Worst Case: O(n)
     */
    public void insert(T value) {
        root = insert(root, value);
    }

    /**
     * Recursively inserts the new value.
     * Receives the root of a subtree and returns the root
     * of that subtree after inserting the new value.
     */
    private BinaryTreeNode<T> insert(BinaryTreeNode<T> node, T value) {
        if (node == null) {
            size++;
            return new BinaryTreeNode<>(value);
        }

        if (random.nextBoolean())
            node.left = insert(node.left, value);
        else
            node.right = insert(node.right, value);

        return node;
    }

    /**
     * Returns true if the tree contains the given value.
     * Since the tree is not ordered,
     * both subtrees may need to be searched.
     * <p>
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public boolean contains(T value) {
        return contains(root, value);
    }

    private boolean contains(BinaryTreeNode<T> node, T value) {
        if (node == null)
            return false;

        if (valuesEqual(node.data, value))
            return true;

        return contains(node.left, value) || contains(node.right, value);
    }

    /**
     * Finds and returns the first node containing the value.
     * <p>
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public BinaryTreeNode<T> search(T value) {
        return search(root, value);
    }

    private BinaryTreeNode<T> search(BinaryTreeNode<T> node, T value) {
        if (node == null)
            return null;

        if (valuesEqual(node.data, value))
            return node;

        BinaryTreeNode<T> found = search(node.left, value);

        if (found != null)
            return found;

        return search(node.right, value);
    }

    /**
     * Returns the height of the tree.
     * Definition:
     * empty tree = -1
     * leaf = 0
     * <p>
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public int height() {
        return height(root);
    }

    private int height(BinaryTreeNode<T> node) {

        if (node == null)
            return -1;

        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Returns preorder traversal:
     * Root -> Left -> Right
     * <p>
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public List<T> preorder() {
        List<T> result = new ArrayList<>();
        preorder(root, result);
        return result;
    }

    private void preorder(BinaryTreeNode<T> node, List<T> result) {
        if (node == null)
            return;

        result.add(node.data);
        preorder(node.left, result);
        preorder(node.right, result);
    }

    /**
     * Returns inorder traversal:
     * Left -> Root -> Right
     * <p>
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public List<T> inorder() {
        List<T> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(BinaryTreeNode<T> node, List<T> result) {
        if (node == null)
            return;

        inorder(node.left, result);
        result.add(node.data);
        inorder(node.right, result);
    }

    /**
     * Returns postorder traversal:
     * Left -> Right -> Root
     * <p>
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public List<T> postorder() {
        List<T> result = new ArrayList<>();
        postorder(root, result);
        return result;
    }

    private void postorder(BinaryTreeNode<T> node, List<T> result) {
        if (node == null)
            return;

        postorder(node.left, result);
        postorder(node.right, result);
        result.add(node.data);
    }

    /**
     * Counts the number of nodes recursively.
     * <p>
     * Time Complexity: O(n)
     */
    public int countNodes() {
        return countNodes(root);
    }

    private int countNodes(BinaryTreeNode<T> node) {
        if (node == null)
            return 0;

        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    /**
     * Counts the number of leaves.
     * <p>
     * Time Complexity: O(n)
     */
    public int countLeaves() {
        return countLeaves(root);
    }

    private int countLeaves(BinaryTreeNode<T> node) {
        if (node == null)
            return 0;

        if (node.left == null && node.right == null)
            return 1;

        return countLeaves(node.left) + countLeaves(node.right);
    }

    /**
     * Clears the tree.
     * <p>
     * Time Complexity: O(1)
     * The disconnected nodes will be handled
     * by Java's garbage collector.
     */
    public void clear() {
        root = null;
        size = 0;
    }


    // -------- helper --------

    private boolean valuesEqual(T first, T second) {
        if (first == null)
            return second == null;

        return first.equals(second);
    }
}