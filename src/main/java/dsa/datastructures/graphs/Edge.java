package dsa.datastructures.graphs;

/**
 * Represents an unweighted edge between two vertices.
 * An edge is defined by its source vertex and destination vertex.
 * The vertices are immutable and cannot be changed after the edge
 * is created.
 */
public class Edge {

    // -------- fields --------

    private final int from;
    private final int to;


    // -------- constructor --------

    /**
     * Creates an edge between two vertices.
     *
     * @param from the source vertex
     * @param to   the destination vertex
     */
    public Edge(int from, int to) {
        this.from = from;
        this.to = to;
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
     * Checks whether this edge is equal to another object.
     * Two edges are considered equal if they have the same
     * source and destination vertices.
     *
     * @param obj the object to compare with
     * @return true if the two edges are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Edge other))
            return false;

        return from == other.from && to == other.to;
    }

    /**
     * Returns a hash code based on the two vertices of the edge.
     *
     * @return the hash code of the edge
     */
    @Override
    public int hashCode() {
        int result = Integer.hashCode(from);
        result = 31 * result + Integer.hashCode(to);
        return result;
    }

    /**
     * Returns a string representation of the edge.
     *
     * @return the edge in the form "(from, to)"
     */
    @Override
    public String toString() {
        return "(" + from + ", " + to + ")";
    }
}