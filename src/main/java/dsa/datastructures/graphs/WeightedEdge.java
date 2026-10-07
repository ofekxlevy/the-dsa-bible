package dsa.datastructures.graphs;

/**
 * Represents a weighted edge between two vertices.
 * A weighted edge is defined by its source vertex, destination vertex, and weight.
 * The edge is immutable: its vertices and weight cannot be changed after creation.
 * This class can be used for both directed and undirected graphs.
 * The interpretation of the edge depends on the graph:
 * - In a directed graph, (from, to, weight) represents
 *   an edge directed from {@code from} to {@code to}.
 * - In an undirected graph, the edge represents a connection
 *   between the two vertices with the given weight.
 */
public class WeightedEdge implements Comparable<WeightedEdge> {

    // -------- fields --------

    private final int from;
    private final int to;
    private final double weight;


    // -------- constructor --------

    /**
     * Creates a weighted edge between two vertices.
     *
     * @param from   the source vertex
     * @param to     the destination vertex
     * @param weight the weight of the edge
     */
    public WeightedEdge(int from, int to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }


    // -------- methods --------

    /**
     * Returns the source vertex of the edge.
     *
     * @return the source vertex
     */
    public int getFrom() {
        return from;
    }

    /**
     * Returns the destination vertex of the edge.
     *
     * @return the destination vertex
     */
    public int getTo() {
        return to;
    }

    /**
     * Returns the weight of the edge.
     *
     * @return the edge weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Checks whether this weighted edge is equal to another object.
     * Two weighted edges are considered equal if they have the same
     * source vertex, destination vertex, and weight.
     *
     * @param obj the object to compare with
     * @return true if the two weighted edges are equal,
     *         false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof WeightedEdge other))
            return false;

        return from == other.from
                && to == other.to
                && Double.compare(weight, other.weight) == 0;
    }

    /**
     * Returns a hash code based on the source vertex,
     * destination vertex, and weight.
     *
     * @return the hash code of the weighted edge
     */
    @Override
    public int hashCode() {
        int result = Integer.hashCode(from);
        result = 31 * result + Integer.hashCode(to);
        result = 31 * result + Double.hashCode(weight);
        return result;
    }

    /**
     * Returns a string representation of the weighted edge.
     *
     * @return the edge in the form "(from, to, weight)"
     */
    @Override
    public String toString() {
        return "(" + from + ", " + to + ", " + weight + ")";
    }

    /**
     * Compares this edge with another weighted edge according to their weights.
     * The natural ordering of weighted edges is by nondecreasing weight.
     * This ordering is useful for algorithms such as Kruskal's algorithm,
     * which processes edges from the lightest to the heaviest.
     *
     * @param other the weighted edge to compare with
     * @return a negative value if this edge is lighter,
     *         zero if both edges have the same weight,
     *         or a positive value if this edge is heavier
     */
    @Override
    public int compareTo(WeightedEdge other) {
        return Double.compare(this.weight, other.weight);
    }
}