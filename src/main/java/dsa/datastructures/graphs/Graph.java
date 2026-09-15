package dsa.datastructures.graphs;

import dsa.datastructures.lists.List;

/**
 * Represents an unweighted graph with a fixed set of vertices:
 * {1, 2, ..., n}.
 * The number of vertices is determined when the graph is created
 * and cannot be changed afterward.
 * The graph may be directed or undirected, depending on the implementation.
 */
public interface Graph {

    /**
     * Adds an edge between two vertices.
     * In a directed graph, the edge is from {@code from} to {@code to}.
     * In an undirected graph, the edge connects both vertices.
     *
     * @param from the source vertex
     * @param to   the destination vertex
     * @return true if the edge was added,
     *         false if the edge already exists
     */
    boolean addEdge(int from, int to);

    /**
     * Removes an edge between two vertices.
     * In a directed graph, removes the edge from {@code from} to {@code to}.
     * In an undirected graph, removes the edge connecting the two vertices.
     *
     * @param from the source vertex
     * @param to   the destination vertex
     * @return true if the edge existed and was removed,
     *         false otherwise
     */
    boolean removeEdge(int from, int to);

    /**
     * Checks whether an edge exists between two vertices.
     *
     * @param from the source vertex
     * @param to   the destination vertex
     * @return true if the edge exists, false otherwise
     */
    boolean containsEdge(int from, int to);

    /**
     * Returns the neighbors of the given vertex.
     * In a directed graph, returns the vertices reachable by
     * outgoing edges from the given vertex.
     *
     * @param vertex the vertex whose neighbors are requested
     * @return a list containing the neighbors of the vertex
     */
    List<Integer> getNeighbors(int vertex);

    /**
     * Returns the number of vertices in the graph.
     * The vertices are {1, 2, ..., n}.
     *
     * @return the number of vertices
     */
    int vertexCount();

    /**
     * Returns the number of edges in the graph.
     * In an undirected graph, each edge is counted once.
     *
     * @return the number of edges
     */
    int edgeCount();

    /**
     * Checks whether the graph is directed.
     *
     * @return true if the graph is directed,
     *         false if it is undirected
     */
    boolean isDirected();

    /**
     * Returns all edges in the graph.
     * In an undirected graph, each edge appears exactly once.
     *
     * @return a list containing all edges in the graph
     */
    List<Edge> getEdges();
}