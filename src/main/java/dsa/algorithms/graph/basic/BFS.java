package dsa.algorithms.graph.basic;

import dsa.datastructures.graphs.Graph;
import dsa.datastructures.stacksqueues.Queue;
import dsa.datastructures.stacksqueues.LinkedQueue;
import dsa.datastructures.iterators.Iterator;

/**
 * Breadth-First Search (BFS).
 * BFS explores the graph level by level starting from a given
 * source vertex.
 * <p>
 * For a source vertex s, BFS computes:
 * 1. Which vertices are reachable from s.
 * 2. The shortest distance from s to every reachable vertex,
 *    measured by the number of edges.
 * 3. A predecessor tree (BFS tree), which can be used to
 *    reconstruct a shortest path from s to any reachable vertex.
 * <p>
 * During the algorithm, every vertex has one of three colors:
 * WHITE - The vertex has not been discovered yet.
 * GRAY  - The vertex has been discovered, but its adjacency list
 *         has not been completely processed.
 * BLACK - The vertex and all of its neighbors have been processed.
 * <p>
 * Time Complexity:
 * O(V + E), assuming the graph is represented using adjacency lists.
 * Space Complexity:
 * O(V), excluding the graph representation.
 */
public class BFS {

    /**
     * Represents the state of a vertex during BFS.
     */
    private enum Color {
        WHITE,
        GRAY,
        BLACK
    }

    private final Graph graph;

    /**
     * color[v] represents the current state of vertex v.
     */
    private Color[] color;

    /**
     * distance[v] is the shortest distance from the source
     * to vertex v, measured by the number of edges.
     * Integer.MAX_VALUE represents infinity, meaning that
     * the vertex is not reachable from the source.
     */
    private int[] distance;

    /**
     * predecessor[v] is the vertex that discovered v.
     * The predecessor values define the BFS tree.
     * A value of -1 means that v has no predecessor.
     */
    private int[] predecessor;

    /**
     * The source vertex of the most recent BFS execution.
     */
    private int source;

    /**
     * Creates a BFS object for the given graph.
     *
     * @param graph the graph on which BFS will be executed
     */
    public BFS(Graph graph) {
        this.graph = graph;
    }

    /**
     * Runs BFS starting from the given source vertex.
     * After the algorithm finishes:
     * distance[v] contains the shortest distance from source to v.
     * predecessor[v] contains the parent of v in the BFS tree.
     *
     * @param source the source vertex
     * <p>
     * Time Complexity: O(V + E)
     */
    public void run(int source) {
        validateVertex(source);
        this.source = source;
        int n = graph.vertexCount();

        color = new Color[n + 1];
        distance = new int[n + 1];
        predecessor = new int[n + 1];

        // Initialize all vertices.
        for (int vertex = 1; vertex <= n; vertex++) {
            color[vertex] = Color.WHITE;
            distance[vertex] = Integer.MAX_VALUE;
            predecessor[vertex] = -1;
        }

        //Initialize the source vertex.
        color[source] = Color.GRAY;
        distance[source] = 0;
        predecessor[source] = -1;

        // The queue contains discovered vertices whose
        // adjacency lists still need to be processed.
        Queue<Integer> queue = new LinkedQueue<>();
        queue.enqueue(source);

        // Continue until there are no more discovered vertices waiting to be processed.
        while (!queue.isEmpty()) {
            int u = queue.dequeue();
            Iterator<Integer> iterator = graph.getNeighbors(u).iterator();

            // Explore every neighbor of u.
            while (iterator.hasNext()) {
                int v = iterator.next();;

                // A WHITE vertex has not been discovered yet.
                // The first time BFS discovers v, it has found a shortest path from the source to v.
                if (color[v] == Color.WHITE) {

                    // v has now been discovered.
                    color[v] = Color.GRAY;

                    // Since v is a neighbor of u: d[v] = d[u] + 1
                    distance[v] = distance[u] + 1;

                    // u discovered v, so u becomes the predecessor of v in the BFS tree.
                    predecessor[v] = u;

                    // Add v to the queue so that its neighbors will later be explored.
                    queue.enqueue(v);
                }
            }

            // All neighbors of u have now been examined.
            color[u] = Color.BLACK;
        }
    }

    /**
     * Returns the shortest distance from the source to vertex v.
     * If v is unreachable from the source,
     * Integer.MAX_VALUE is returned.
     *
     * @param v the destination vertex
     * @return the shortest distance from the source to v
     * <p>
     * Time Complexity: O(1)
     */
    public int getDistance(int v) {
        validateRun();
        validateVertex(v);
        return distance[v];
    }

    /**
     * Returns the predecessor of vertex v in the BFS tree.
     * If v is the source or is unreachable from the source,
     * -1 is returned.
     *
     * @param v the vertex
     * @return the predecessor of v
     * <p>
     * Time Complexity: O(1)
     */
    public int getPredecessor(int v) {
        validateRun();
        validateVertex(v);
        return predecessor[v];
    }

    /**
     * Checks whether vertex v is reachable from the source.
     *
     * @param v the vertex
     * @return true if v is reachable from the source,
     *         false otherwise
     * <p>
     * Time Complexity: O(1)
     */
    public boolean isReachable(int v) {
        validateRun();
        validateVertex(v);
        return distance[v] != Integer.MAX_VALUE;
    }

    /**
     * Returns the source vertex used in the most recent BFS run.
     *
     * @return the BFS source vertex
     * <p>
     * Time Complexity: O(1)
     */
    public int getSource() {
        validateRun();
        return source;
    }

    /**
     * Ensures that BFS has already been executed.
     */
    private void validateRun() {
        if (distance == null)
            throw new IllegalStateException("BFS has not been executed yet.");
    }

    /**
     * Ensures that the given vertex belongs to the graph.
     * Since the graph vertices are numbered 1,...,n,
     * a valid vertex must satisfy:
     * 1 <= vertex <= n
     *
     * @param vertex the vertex to validate
     */
    private void validateVertex(int vertex) {
        if (vertex < 1 || vertex > graph.vertexCount())
            throw new IndexOutOfBoundsException("Vertex must be between 1 and " + graph.vertexCount() + ": " + vertex);
    }
}