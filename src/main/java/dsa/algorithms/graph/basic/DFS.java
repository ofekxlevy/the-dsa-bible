package dsa.algorithms.graph.basic;

import dsa.datastructures.graphs.Graph;
import dsa.datastructures.iterators.Iterator;

/**
 * Implements the Depth-First Search (DFS) algorithm.
 * DFS explores the graph by going as deep as possible along each path
 * before backtracking.
 * Unlike BFS, DFS does not necessarily start from a single source.
 * Instead, it scans all vertices of the graph. Whenever a WHITE vertex
 * is encountered, a new DFS tree is started from that vertex.
 * Therefore, the result of DFS is generally a DFS forest.
 * <p>
 * For every vertex v, DFS computes:
 * 1. predecessor[v]   - the parent of v in the DFS forest.
 * 2. discoveryTime[v] - the time at which v is first discovered.
 * 3. finishTime[v]    - the time at which the exploration of v finishes.
 * <p>
 * During DFS, every vertex has one of three colors:
 * WHITE - The vertex has not been discovered yet.
 * GRAY  - The vertex has been discovered, but its exploration
 *         has not finished yet.
 * BLACK - The exploration of the vertex has finished.
 * <p>
 * Time Complexity:
 * O(V + E), assuming the graph is represented using adjacency lists.
 * Space Complexity:
 * O(V), excluding the graph representation.
 * The recursive call stack may contain up to O(V) calls.
 */
public class DFS {

    /**
     * Represents the state of a vertex during DFS.
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
     * predecessor[v] is the parent of v in the DFS forest.
     * A value of -1 means that v has no predecessor,
     * so v is a root of one of the DFS trees.
     */
    private int[] predecessor;

    /**
     * discoveryTime[v] is the time at which vertex v is first discovered.
     * This corresponds to v.d in the presentation.
     */
    private int[] discoveryTime;

    /**
     * finishTime[v] is the time at which DFS finishes
     * exploring vertex v.
     * This corresponds to v.f in the presentation.
     */
    private int[] finishTime;

    /**
     * Global DFS clock.
     * The clock is incremented once when a vertex is discovered
     * and once when the exploration of that vertex finishes.
     */
    private int time;

    /**
     * Indicates whether DFS has already been executed.
     */
    private boolean hasRun;

    /**
     * Creates a DFS object for the given graph.
     *
     * @param graph the graph on which DFS will be executed
     */
    public DFS(Graph graph) {
        this.graph = graph;
    }

    /**
     * Runs DFS on the entire graph.
     * First, every vertex is initialized as WHITE with no predecessor.
     * Then, the vertices are scanned in the order 1,...,n.
     * Whenever a WHITE vertex is encountered, a new DFS tree
     * is started by calling dfsVisit().
     * This corresponds to DFS(G) in the presentation.
     * <p>
     * Time Complexity:
     * O(V + E)
     */
    public void run() {
        int n = graph.vertexCount();
        color = new Color[n + 1];
        predecessor = new int[n + 1];
        discoveryTime = new int[n + 1];
        finishTime = new int[n + 1];

        // Initialize every vertex.
        for (int u = 1; u <= n; u++) {
            color[u] = Color.WHITE;
            predecessor[u] = -1;
        }

        // No vertex has been discovered yet, so the global clock starts at 0.
        time = 0;

        // Scan all vertices.
        // If a vertex is still WHITE, it was not reached from any previously started DFS tree.
        // Therefore, start a new DFS tree from that vertex.
        for (int u = 1; u <= n; u++)
            if (color[u] == Color.WHITE)
                dfsVisit(u);

        hasRun = true;
    }

    /**
     * Recursively explores vertex u and all WHITE vertices
     * reachable from it.
     * This method corresponds directly to DFS-Visit(G, u)
     * in the presentation.
     *
     * @param u the vertex currently being explored
     */
    private void dfsVisit(int u) {

        // u has now been discovered.
        color[u] = Color.GRAY;

        // Record the discovery time of u.
        time++;
        discoveryTime[u] = time;

        // Examine every neighbor v of u.
        Iterator<Integer> iterator = graph.getNeighbors(u).iterator();

        while (iterator.hasNext()) {
            int v = iterator.next();

            // If v is WHITE, it has not been discovered yet.
            // Therefore, the edge (u, v) becomes a tree edge in the DFS forest.
            if (color[v] == Color.WHITE) {

                // u discovered v, so u becomes the predecessor of v in the DFS forest.
                predecessor[v] = u;

                // Immediately continue the search from v.
                // This recursive call is what makes the search go as deep as possible before backtracking.
                dfsVisit(v);
            }
        }

        /*
         * At this point, every neighbor of u has been examined
         * and every WHITE vertex discovered through u has been completely explored.
         * Therefore, u is finished.
         */
        color[u] = Color.BLACK;

        // Record the finishing time of u.
        time++;
        finishTime[u] = time;
    }

    /**
     * Returns the predecessor of vertex v in the DFS forest.
     * If v is a root of a DFS tree, -1 is returned.
     *
     * @param v the vertex
     * @return the predecessor of v
     * <p>
     * Time Complexity:
     * O(1)
     */
    public int getPredecessor(int v) {
        validateRun();
        validateVertex(v);
        return predecessor[v];
    }

    /**
     * Returns the discovery time of vertex v.
     * This corresponds to v.d in the presentation.
     *
     * @param v the vertex
     * @return the discovery time of v
     * <p>
     * Time Complexity:
     * O(1)
     */
    public int getDiscoveryTime(int v) {
        validateRun();
        validateVertex(v);
        return discoveryTime[v];
    }

    /**
     * Returns the finishing time of vertex v.
     * This corresponds to v.f in the presentation.
     *
     * @param v the vertex
     * @return the finishing time of v
     * <p>
     * Time Complexity:
     * O(1)
     */
    public int getFinishTime(int v) {
        validateRun();
        validateVertex(v);
        return finishTime[v];
    }

    /**
     * Ensures that DFS has already been executed.
     */
    private void validateRun() {
        if (!hasRun)
            throw new IllegalStateException("DFS has not been executed yet.");
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