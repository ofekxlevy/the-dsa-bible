package dsa.datastructures.graphs;

import dsa.datastructures.lists.List;
import dsa.datastructures.lists.SinglyLinkedList;

/**
 * Represents a weighted graph using adjacency lists.
 * The graph has a fixed set of vertices: {1, 2, ..., n}.
 * Each vertex has a linked list containing the weighted edges adjacent to it.
 * The graph may be directed or undirected.
 * Parallel edges and self-loops are not allowed.
 * In an undirected graph, each edge is stored twice:
 * once in each endpoint's adjacency list.
 * <p>
 * Space complexity: O(V + E)
 */
public class WeightedAdjacencyListGraph implements WeightedGraph {

    // -------- fields --------

    private final SinglyLinkedList<WeightedEdge>[] adjacencyLists;
    private final int vertexCount;
    private int edgeCount;
    private final boolean directed;


    // -------- constructor --------

    /**
     * Creates an empty weighted graph with vertices
     * {1, 2, ..., vertexCount}.
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
    public WeightedAdjacencyListGraph(int vertexCount, boolean directed) {
        if (vertexCount < 0)
            throw new IllegalArgumentException("Number of vertices cannot be negative.");

        this.vertexCount = vertexCount;
        this.edgeCount = 0;
        this.directed = directed;

        adjacencyLists = (SinglyLinkedList<WeightedEdge>[]) new SinglyLinkedList[vertexCount];

        for (int i = 0; i < vertexCount; i++)
            adjacencyLists[i] = new SinglyLinkedList<>();
    }


    // -------- methods --------

    /**
     * Adds a weighted edge between two vertices.
     * In a directed graph, only the edge from -> to is added.
     * In an undirected graph, the edge is stored in both
     * adjacency lists: (from, to, weight) and (to, from, weight).
     * Parallel edges and self-loops are not allowed.
     *
     * @param from   the source vertex
     * @param to     the destination vertex
     * @param weight the weight of the edge
     * @return true if the edge was added,
     *         false if an edge between the vertices already exists
     * @throws IllegalArgumentException if either vertex is invalid
     *                                  or from == to
     *
     * Time complexity: O(deg(from))
     */
    @Override
    public boolean addEdge(int from, int to, double weight) {
        validateVertex(from);
        validateVertex(to);

        if (from == to)
            throw new IllegalArgumentException("Self-loops are not allowed.");

        if (containsEdge(from, to))
            return false;

        adjacencyLists[from - 1].addFirst(new WeightedEdge(from, to, weight));

        if (!directed)
            adjacencyLists[to - 1].addFirst(new WeightedEdge(to, from, weight));

        edgeCount++;
        return true;
    }

    /**
     * Removes an edge between two vertices.
     * In a directed graph, only the edge from -> to is removed.
     * In an undirected graph, both stored representations of the edge are removed.
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

        int index = indexOfEdge(adjacencyLists[from - 1], to);

        if (index == -1)
            return false;

        adjacencyLists[from - 1].remove(index);

        if (!directed) {
            int reverseIndex = indexOfEdge(adjacencyLists[to - 1], from);
            adjacencyLists[to - 1].remove(reverseIndex);
        }

        edgeCount--;
        return true;
    }

    /**
     * Checks whether an edge exists between two vertices.
     * In a directed graph, this checks specifically for the edge from -> to.
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

        return indexOfEdge(adjacencyLists[from - 1], to) != -1;
    }

    /**
     * Returns the weight of the edge between two vertices.
     * In a directed graph, this returns the weight of the edge from -> to.
     *
     * @param from the source vertex
     * @param to   the destination vertex
     * @return the weight of the edge
     * @throws IllegalArgumentException if either vertex is invalid or the edge does not exist
     *
     * Time complexity: O(deg(from))
     */
    @Override
    public double getWeight(int from, int to) {
        validateVertex(from);
        validateVertex(to);

        SinglyLinkedList<WeightedEdge> edges = adjacencyLists[from - 1];
        int index = indexOfEdge(edges, to);

        if (index == -1)
            throw new IllegalArgumentException("Edge does not exist.");

        return edges.get(index).getWeight();
    }

    /**
     * Returns a copy of all weighted edges adjacent to the given vertex.
     * Returning a copy prevents the caller from modifying the internal adjacency list of the graph.
     * In a directed graph, these are the outgoing edges.
     * In an undirected graph, every returned edge has the requested vertex as its source.
     *
     * @param vertex the vertex whose adjacent edges are requested
     * @return a list containing the weighted edges adjacent
     *         to the vertex
     * @throws IllegalArgumentException if the vertex is invalid
     * <p>
     * Time complexity: O(deg(vertex))
     * Space complexity: O(deg(vertex))
     */
    @Override
    public List<WeightedEdge> getAdjacentEdges(int vertex) {
        validateVertex(vertex);
        SinglyLinkedList<WeightedEdge> result = new SinglyLinkedList<>();
        SinglyLinkedList<WeightedEdge> adjacencyList = adjacencyLists[vertex - 1];

        for (int i = 0; i < adjacencyList.size(); i++)
            result.add(adjacencyList.get(i));

        return result;
    }

    /**
     * Returns all weighted edges in the graph.
     * In a directed graph, every stored edge is returned.
     * In an undirected graph, every edge is stored twice internally.
     * To return each edge exactly once, only the representation (u, v, w) for which u < v is added.
     *
     * @return a list containing all weighted edges in the graph
     * <p>
     * Time complexity: O(V + E)
     * Space complexity: O(E)
     */
    @Override
    public List<WeightedEdge> getEdges() {
        SinglyLinkedList<WeightedEdge> edges = new SinglyLinkedList<>();

        for (int from = 1; from <= vertexCount; from++) {
            SinglyLinkedList<WeightedEdge> adjacentEdges = adjacencyLists[from - 1];

            for (int i = 0; i < adjacentEdges.size(); i++) {
                WeightedEdge edge = adjacentEdges.get(i);

                if (directed || edge.getFrom() < edge.getTo())
                    edges.add(edge);
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
     * Searches an adjacency list for an edge whose destination vertex is equal to {@code to}.
     * Since parallel edges are not allowed, the destination vertex uniquely identifies
     * an edge inside the adjacency list of a given source vertex.
     *
     * @param list the adjacency list to search
     * @param to   the destination vertex to find
     * @return the index of the edge if found,
     *         or -1 if the edge does not exist
     *
     * Time complexity: O(n)
     */
    private int indexOfEdge(SinglyLinkedList<WeightedEdge> list, int to) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTo() == to)
                return i;
        }

        return -1;
    }
}