package dsa.algorithms.graph.mst;

import dsa.datastructures.graphs.WeightedEdge;
import dsa.datastructures.graphs.WeightedGraph;
import dsa.datastructures.heaps.BinaryHeap;
import dsa.datastructures.lists.List;
import dsa.datastructures.lists.SinglyLinkedList;

import java.util.Comparator;

/**
 * Implements Prim's algorithm for finding a Minimum Spanning Tree (MST)
 * of a connected, undirected, weighted graph.
 * <p>
 * The algorithm grows a single tree starting from a given root vertex.
 * For every vertex v that has not yet been added to the tree, key[v]
 * stores the minimum weight of an edge that can connect v to the
 * current tree.
 * parent[v] stores the vertex through which v should be connected
 * to the tree using the edge represented by key[v].
 * <p>
 * The algorithm uses a min-priority queue. At every iteration,
 * the vertex with the smallest key is extracted and added to the tree.
 * Then, all of its adjacent edges are examined. If an edge provides
 * a cheaper way to connect a neighboring vertex to the tree, the
 * neighbor's key and parent are updated.
 * <p>
 * This implementation follows the classical Prim algorithm:
 *
 * <pre>
 * for each vertex u:
 *     key[u] = infinity
 *     parent[u] = null
 *
 * key[root] = 0
 * Q = all vertices
 *
 * while Q is not empty:
 *     u = ExtractMin(Q)
 *
 *     for each v adjacent to u:
 *         if v is in Q and w(u, v) &lt; key[v]:
 *             parent[v] = u
 *             DecreaseKey(Q, v, w(u, v))
 * </pre>
 *
 * Note: the BinaryHeap implementation currently locates an element
 * linearly before performing decreaseKey. Therefore, decreaseKey takes
 * O(V) time in the current implementation rather than O(log V).
 */
public class Prim {

    /**
     * Finds a Minimum Spanning Tree of the given graph using Prim's algorithm.
     *
     * @param graph the connected, undirected, weighted graph
     * @param root  the vertex from which Prim's algorithm starts
     * @return a list containing the edges of the Minimum Spanning Tree
     *
     * @throws IllegalArgumentException if the graph is directed
     * @throws IllegalArgumentException if the root is not a valid vertex
     * @throws IllegalArgumentException if the graph is disconnected
     */
    public static List<WeightedEdge> mst(WeightedGraph graph, int root) {

        if (graph.isDirected())
            throw new IllegalArgumentException("Prim's algorithm requires an undirected graph.");

        int n = graph.vertexCount();

        if (root < 1 || root > n)
            throw new IllegalArgumentException("Root must be between 1 and " + n + ".");

        /*
         * key[v] stores the minimum weight of an edge connecting vertex v to the current MST.
         * The arrays have size n + 1 because the graph vertices are numbered from 1 to n.
         */
        double[] key = new double[n + 1];

        /*
         * parent[v] stores the vertex from which v will be connected to the MST.
         * parent[root] remains -1 because the root has no parent.
         */
        int[] parent = new int[n + 1];

        /*
         * inQueue[v] represents whether vertex v is still in Q.
         * In the pseudocode from the lecture this corresponds to
         * the condition: v ∈ Q
         */
        boolean[] inQueue = new boolean[n + 1];

        // Initialize all vertices.
        for (int v = 1; v <= n; v++) {
            key[v] = Double.POSITIVE_INFINITY;
            parent[v] = -1;
            inQueue[v] = true;
        }

        // The root is the first vertex that should be extracted.
        key[root] = 0.0;

        // Q is a min-priority queue ordered by the key of each vertex.
        BinaryHeap<VertexKey> queue = new BinaryHeap<>(Comparator.comparingDouble(VertexKey::key));

        // Initially Q contains every vertex in the graph.
        for (int v = 1; v <= n; v++)
            queue.insert(new VertexKey(v, key[v]));

        // Main loop of Prim's algorithm.
        while (!queue.isEmpty()) {

            /*
             * Extract the vertex with the smallest key.
             * This vertex is now permanently added to the MST.
             */
            VertexKey minimum = queue.extract();

            int u = minimum.vertex();
            inQueue[u] = false;

            /*
             * If the smallest remaining key is infinity, then there
             * is no edge connecting the remaining vertices to the current tree.
             * Therefore, the graph is disconnected and no spanning tree exists.
             */
            if (Double.isInfinite(minimum.key()))
                throw new IllegalArgumentException("The graph is disconnected.");


            // Examine every edge adjacent to u.
            List<WeightedEdge> adjacentEdges = graph.getAdjacentEdges(u);

            for (int i = 0; i < adjacentEdges.size(); i++) {
                WeightedEdge edge = adjacentEdges.get(i);
                int v = edge.getTo();
                double weight = edge.getWeight();

                /*
                 * This condition corresponds directly to:
                 * v ∈ Q && w(u, v) < v.key
                 * from the lecture pseudocode.
                 */
                if (inQueue[v] && weight < key[v]) {

                    /*
                     * Remember the old queue entry because decreaseKey
                     * must locate the current value inside the heap.
                     */
                    VertexKey oldValue = new VertexKey(v, key[v]);

                    /*
                     * u becomes the new parent of v because (u, v)
                     * is currently the cheapest edge connecting v to the growing MST.
                     */
                    parent[v] = u;

                    // Update the priority of v in the queue.
                    VertexKey newValue = new VertexKey(v, weight);
                    queue.decreaseKey(oldValue, newValue);
                    key[v] = weight;
                }
            }
        }

        /*
         * The parent array now describes the MST.
         * For every vertex except the root, the edge
         *     (parent[v], v)
         * belongs to the MST.
         */
        SinglyLinkedList<WeightedEdge> mst = new SinglyLinkedList<>();

        for (int v = 1; v <= n; v++) {
            if (v == root)
                continue;

            mst.add(new WeightedEdge(parent[v], v, key[v]));
        }

        return mst;
    }


    /**
     * Represents a vertex together with its current key in Prim's
     * priority queue.
     * <p>
     * The vertex identifies the graph vertex, while key represents
     * the minimum known weight of an edge connecting that vertex to
     * the current Minimum Spanning Tree.
     *
     * @param vertex the graph vertex
     * @param key    the current priority of the vertex
     */
    private record VertexKey(int vertex, double key) {
    }
}