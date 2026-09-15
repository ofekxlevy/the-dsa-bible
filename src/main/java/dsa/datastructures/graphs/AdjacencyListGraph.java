package dsa.datastructures.graphs;

import dsa.datastructures.lists.List;
import dsa.datastructures.lists.SinglyLinkedList;

/**
 * Represents an unweighted graph using adjacency lists.
 * The graph has a fixed set of vertices: {1, 2, ..., n}.
 * Each vertex has a linked list containing its neighbors.
 * The graph may be directed or undirected.
 * Parallel edges and self-loops are not allowed.
 * <p>
 * Space complexity: O(V + E)
 * In an undirected graph, each edge is stored twice:
 * once in each endpoint's adjacency list.
 */
public class AdjacencyListGraph implements Graph {

    // -------- fields --------

    private final SinglyLinkedList<Integer>[] adjacencyLists;
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
     * Time complexity: O(V)
     * Space complexity: O(V)
     */
    @SuppressWarnings("unchecked")
    public AdjacencyListGraph(int vertexCount, boolean directed) {
        if (vertexCount < 0)
            throw new IllegalArgumentException("Number of vertices cannot be negative.");

        this.vertexCount = vertexCount;
        this.edgeCount = 0;
        this.directed = directed;

        adjacencyLists = (SinglyLinkedList<Integer>[]) new SinglyLinkedList[vertexCount];

        for (int i = 0; i < vertexCount; i++)
            adjacencyLists[i] = new SinglyLinkedList<>();
    }


    // -------- methods --------

    /**
     * Adds an edge between two vertices.
     * In a directed graph, only the edge from -> to is added.
     * In an undirected graph, both adjacency lists are updated:
     * from -> to and to -> from.
     * Parallel edges and self-loops are not allowed.
     *
     * @param from the source vertex
     * @param to   the destination vertex
     * @return true if the edge was added,
     *         false if the edge already exists
     * @throws IllegalArgumentException if either vertex is invalid
     *                                  or from == to
     * <p>
     * Time complexity: O(deg(from)).
     */
    @Override
    public boolean addEdge(int from, int to) {
        validateVertex(from);
        validateVertex(to);

        if (from == to)
            throw new IllegalArgumentException("Self-loops are not allowed.");

        if (containsEdge(from, to))
            return false;

        adjacencyLists[from - 1].addFirst(to);

        if (!directed)
            adjacencyLists[to - 1].addFirst(from);

        edgeCount++;
        return true;
    }

    /**
     * Removes an edge between two vertices.
     * In a directed graph, only from -> to is removed.
     * In an undirected graph, the edge is removed from both
     * adjacency lists.
     *
     * @param from the source vertex
     * @param to   the destination vertex
     * @return true if the edge existed and was removed,
     *         false otherwise
     * @throws IllegalArgumentException if either vertex is invalid
     * <p>
     * Time complexity:
     * - Directed:   O(deg(from))
     * - Undirected: O(deg(from) + deg(to))
     */
    @Override
    public boolean removeEdge(int from, int to) {
        validateVertex(from);
        validateVertex(to);

        int index = indexOf(adjacencyLists[from - 1], to);

        if (index == -1)
            return false;

        adjacencyLists[from - 1].remove(index);

        if (!directed) {
            int reverseIndex = indexOf(adjacencyLists[to - 1], from);
            adjacencyLists[to - 1].remove(reverseIndex);
        }

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
     *
     * Time complexity: O(deg(from))
     */
    @Override
    public boolean containsEdge(int from, int to) {
        validateVertex(from);
        validateVertex(to);

        return indexOf(adjacencyLists[from - 1], to) != -1;
    }

    /**
     * Returns a copy of the adjacency list of the given vertex.
     * Returning a copy prevents the caller from modifying the
     * internal representation of the graph.
     *
     * @param vertex the vertex whose neighbors are requested
     * @return a list containing all neighbors of the vertex
     * @throws IllegalArgumentException if the vertex is invalid
     * <p>
     * Time complexity: O(deg(vertex))
     * Space complexity: O(deg(vertex))
     */
    @Override
    public List<Integer> getNeighbors(int vertex) {
        validateVertex(vertex);

        SinglyLinkedList<Integer> neighbors = new SinglyLinkedList<>();
        SinglyLinkedList<Integer> adjacencyList = adjacencyLists[vertex - 1];

        for (int i = 0; i < adjacencyList.size(); i++)
            neighbors.add(adjacencyList.get(i));

        return neighbors;
    }

    /**
     * Returns all edges in the graph.
     * In a directed graph, every stored adjacency represents
     * one edge.
     * In an undirected graph, each edge is stored twice internally.
     * To return every edge exactly once, only the representation
     * (u, v) for which u < v is added to the result.
     *
     * @return a list containing all edges in the graph
     * <p>
     * Time complexity: O(V + E)
     * Space complexity: O(E)
     */
    @Override
    public List<Edge> getEdges() {
        SinglyLinkedList<Edge> edges = new SinglyLinkedList<>();

        for (int from = 1; from <= vertexCount; from++) {
            SinglyLinkedList<Integer> neighbors = adjacencyLists[from - 1];

            for (int i = 0; i < neighbors.size(); i++) {
                int to = neighbors.get(i);

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
     *
     * Time complexity: O(1)
     */
    @Override
    public int vertexCount() {
        return vertexCount;
    }

    /**
     * Returns the number of edges in the graph.
     * In an undirected graph, each edge is counted once,
     * even though it is stored in two adjacency lists.
     *
     * @return the number of edges
     *
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
     *
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
     *
     * Time complexity: O(1)
     */
    private void validateVertex(int vertex) {
        if (vertex < 1 || vertex > vertexCount)
            throw new IllegalArgumentException("Vertex must be between 1 and " + vertexCount + ".");
    }

    /**
     * Searches for a value in a linked list.
     *
     * @param list  the list to search
     * @param value the value to find
     * @return the index of the value if found,
     *         or -1 if the value does not exist
     *
     * Time complexity: O(n)
     */
    private int indexOf(SinglyLinkedList<Integer> list, int value) {
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).equals(value))
                return i;

        return -1;
    }
}