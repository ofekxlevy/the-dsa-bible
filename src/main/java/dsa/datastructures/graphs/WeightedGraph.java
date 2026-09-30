package dsa.datastructures.graphs;

import dsa.datastructures.lists.List;

/**
 * Represents a weighted graph with a fixed set of vertices: {1, 2, ..., n}.
 * The number of vertices is determined when the graph is created
 * and cannot be changed afterward.
 * Every edge has an associated weight.
 * The graph may be directed or undirected, depending on the implementation.
 */
public interface WeightedGraph {

    /**
     * Adds a weighted edge between two vertices.
     * In a directed graph, the edge is from {@code from} to {@code to}.
     * In an undirected graph, the edge connects both vertices.
     *
     * @param from   the source vertex
     * @param to     the destination vertex
     * @param weight the weight of the edge
     * @return true if the edge was added,
     *         false if an edge between the vertices already exists
     */
    boolean addEdge(int from, int to, double weight);

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
     * In a directed graph, checks specifically for an edge from {@code from} to {@code to}.
     *
     * @param from the source vertex
     * @param to   the destination vertex
     * @return true if the edge exists, false otherwise
     */
    boolean containsEdge(int from, int to);

    /**
     * Returns the weighted edges leaving the given vertex.
     * In a directed graph, these are the outgoing edges.
     * In an undirected graph, these are all edges incident
     * to the given vertex, represented as edges whose
     * {@code from} vertex is the requested vertex.
     *
     * @param vertex the vertex whose adjacent edges are requested
     * @return a list containing the weighted edges adjacent
     *         to the vertex
     */
    List<WeightedEdge> getNeighbors(int vertex);

    /**
     * Returns all weighted edges in the graph.
     * In an undirected graph, each edge appears exactly once,
     * even if the internal representation stores it twice.
     *
     * @return a list containing all weighted edges in the graph
     */
    List<WeightedEdge> getEdges();

    /**
     * Returns the number of vertices in the graph.
     * The vertices are {1, 2, ..., n}.
     *
     * @return the number of vertices
     */
    int vertexCount();

    /**
     * Returns the number of edges in the graph.
     * In an undirected graph, each edge is counted once, even if it is stored twice internally.
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
}