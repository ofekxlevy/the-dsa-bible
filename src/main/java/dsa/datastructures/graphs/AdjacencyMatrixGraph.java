package dsa.datastructures.graphs;

import dsa.datastructures.lists.List;
import dsa.datastructures.lists.SinglyLinkedList;

/**
 * Represents an unweighted graph using an adjacency matrix.
 * The graph has a fixed set of vertices: {1, 2, ..., n}.
 * matrix[u - 1][v - 1] is true if and only if the edge
 * from u to v exists in the graph.
 * The graph may be directed or undirected.
 * Parallel edges and self-loops are not allowed.
 * <p>
 * Space complexity: O(V^2)
 */
public class AdjacencyMatrixGraph implements Graph {

    // -------- fields --------

    private final boolean[][] adjacencyMatrix;
    private final int vertexCount;
    private int edgeCount;
    private final boolean directed;


    // -------- constructor --------

    /**
     * Creates an empty graph with vertices {1, 2, ..., vertexCount}.
     *
     * @param vertexCount the number of vertices in the graph
     * @param directed    true for a directed graph,
     *                    false for an undirected graph
     * @throws IllegalArgumentException if vertexCount is negative
     * <p>
     * Time complexity: O(V^2)
     * Space complexity: O(V^2)
     */
    public AdjacencyMatrixGraph(int vertexCount, boolean directed) {
        if (vertexCount < 0)
            throw new IllegalArgumentException(
                    "Number of vertices cannot be negative."
            );

        this.vertexCount = vertexCount;
        this.edgeCount = 0;
        this.directed = directed;

        adjacencyMatrix = new boolean[vertexCount][vertexCount];
    }


    // -------- methods --------

    /**
     * Adds an edge between two vertices.
     * In a directed graph, only the edge from -> to is added.
     * In an undirected graph, both matrix entries are updated:
     * matrix[from - 1][to - 1] and matrix[to - 1][from - 1].
     * Parallel edges and self-loops are not allowed.
     *
     * @param from the source vertex
     * @param to   the destination vertex
     * @return true if the edge was added,
     *         false if the edge already exists
     * @throws IllegalArgumentException if either vertex is invalid or from == to
     * <p>
     * Time complexity: O(1)
     */
    @Override
    public boolean addEdge(int from, int to) {
        validateVertex(from);
        validateVertex(to);

        if (from == to)
            throw new IllegalArgumentException("Self-loops are not allowed.");

        if (adjacencyMatrix[from - 1][to - 1])
            return false;

        adjacencyMatrix[from - 1][to - 1] = true;

        if (!directed)
            adjacencyMatrix[to - 1][from - 1] = true;

        edgeCount++;
        return true;
    }

    /**
     * Removes an edge between two vertices.
     * In a directed graph, only the edge from -> to is removed.
     * In an undirected graph, both corresponding matrix entries
     * are cleared.
     *
     * @param from the source vertex
     * @param to   the destination vertex
     * @return true if the edge existed and was removed,
     *         false otherwise
     * @throws IllegalArgumentException if either vertex is invalid
     * <p>
     * Time complexity: O(1)
     */
    @Override
    public boolean removeEdge(int from, int to) {
        validateVertex(from);
        validateVertex(to);

        if (!adjacencyMatrix[from - 1][to - 1])
            return false;

        adjacencyMatrix[from - 1][to - 1] = false;

        if (!directed)
            adjacencyMatrix[to - 1][from - 1] = false;

        edgeCount--;
        return true;
    }

    /**
     * Checks whether an edge exists between two vertices.
     * In a directed graph, this checks specifically for from -> to.
     *
     * @param from the source vertex
     * @param to   the destination vertex
     * @return true if the edge exists, false otherwise
     * @throws IllegalArgumentException if either vertex is invalid
     * <p>
     * Time complexity: O(1)
     */
    @Override
    public boolean containsEdge(int from, int to) {
        validateVertex(from);
        validateVertex(to);
        return adjacencyMatrix[from - 1][to - 1];
    }

    /**
     * Returns the neighbors of the given vertex.
     * In a directed graph, these are the vertices reachable
     * by outgoing edges from the given vertex.
     * The entire row corresponding to the vertex must be scanned,
     * regardless of the number of neighbors.
     *
     * @param vertex the vertex whose neighbors are requested
     * @return a list containing all neighbors of the vertex
     * @throws IllegalArgumentException if the vertex is invalid
     * <p>
     * Time complexity: O(V)
     * Space complexity: O(deg(vertex))
     */
    @Override
    public List<Integer> getNeighbors(int vertex) {
        validateVertex(vertex);

        SinglyLinkedList<Integer> neighbors = new SinglyLinkedList<>();

        for (int to = 1; to <= vertexCount; to++)
            if (adjacencyMatrix[vertex - 1][to - 1])
                neighbors.add(to);

        return neighbors;
    }

    /**
     * Returns all edges in the graph.
     * In a directed graph, every true entry in the matrix
     * represents one edge.
     * In an undirected graph, every edge appears twice in the
     * matrix. Therefore, only entries above the main diagonal
     * are added to the result, so each edge appears exactly once.
     *
     * @return a list containing all edges in the graph
     * <p>
     * Time complexity: O(V^2)
     * Space complexity: O(E)
     */
    @Override
    public List<Edge> getEdges() {
        SinglyLinkedList<Edge> edges = new SinglyLinkedList<>();

        for (int from = 1; from <= vertexCount; from++) {
            for (int to = 1; to <= vertexCount; to++) {
                if (!adjacencyMatrix[from - 1][to - 1])
                    continue;

                if (directed || from < to)
                    edges.add(new Edge(from, to));
            }
        }

        return edges;
    }

    /**
     * Returns the number of vertices in the graph.
     *
     * @return the number of vertices
     * <p>
     * Time complexity: O(1)
     */
    @Override
    public int vertexCount() {
        return vertexCount;
    }

    /**
     * Returns the number of edges in the graph.
     * In an undirected graph, each edge is counted once,
     * even though it is represented twice in the matrix.
     *
     * @return the number of edges
     * <p>
     * Time complexity: O(1)
     */
    @Override
    public int edgeCount() {
        return edgeCount;
    }

    /**
     * Returns whether the graph is directed.
     *
     * @return true if the graph is directed,
     *         false otherwise
     * <p>
     * Time complexity: O(1)
     */
    @Override
    public boolean isDirected() {
        return directed;
    }


    // -------- helpers --------

    /**
     * Validates that a vertex belongs to the graph.
     * Valid vertices are in the range [1, vertexCount].
     *
     * @param vertex the vertex to validate
     * @throws IllegalArgumentException if the vertex is invalid
     * <p>
     * Time complexity: O(1)
     */
    private void validateVertex(int vertex) {
        if (vertex < 1 || vertex > vertexCount)
            throw new IllegalArgumentException("Vertex must be between 1 and " + vertexCount + ".");
    }
}