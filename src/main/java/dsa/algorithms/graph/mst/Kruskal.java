package dsa.algorithms.graph.mst;

import dsa.algorithms.sorting.MergeSort;
import dsa.datastructures.disjointsets.DisjointSet;
import dsa.datastructures.disjointsets.DisjointSetForest;
import dsa.datastructures.graphs.WeightedEdge;
import dsa.datastructures.graphs.WeightedGraph;
import dsa.datastructures.lists.List;
import dsa.datastructures.lists.SinglyLinkedList;

/**
 * Implements Kruskal's algorithm for finding a Minimum Spanning Tree (MST)
 * of a connected, undirected, weighted graph.
 * <p>
 * Kruskal's algorithm processes all graph edges in nondecreasing order
 * of weight. An edge is added to the MST if its endpoints currently
 * belong to different connected components.
 * <p>
 * A Disjoint Set (Union-Find) data structure is used to efficiently
 * maintain the connected components and determine whether adding an
 * edge would create a cycle.
 */
public class Kruskal {

    /**
     * Finds a Minimum Spanning Tree of the given graph using Kruskal's algorithm.
     *
     * @param graph the connected, undirected, weighted graph
     * @return a list containing the edges of a Minimum Spanning Tree
     *
     * @throws IllegalArgumentException if the graph is directed
     * @throws IllegalArgumentException if the graph is disconnected
     * <p>
     * Time complexity: O(E log E), dominated by sorting the edges.
     * Space complexity: O(V + E).
     */
    public static List<WeightedEdge> mst(WeightedGraph graph) {

        if (graph.isDirected())
            throw new IllegalArgumentException("Kruskal's algorithm requires an undirected graph.");

        int n = graph.vertexCount();

        /*
         * Initially, every vertex belongs to a separate set.
         * These sets represent the connected components formed
         * by the edges selected so far.
         */
        DisjointSet<Integer> sets = new DisjointSetForest<>();

        for (int v = 1; v <= n; v++)
            sets.makeSet(v);

        /*
         * Get all graph edges.
         * In an undirected graph, getEdges() returns each edge exactly once.
         */
        List<WeightedEdge> graphEdges = graph.getEdges();

        /*
         * Copy the edges into an array because our MergeSort
         * implementation operates on arrays.
         */
        WeightedEdge[] edges = new WeightedEdge[graphEdges.size()];

        for (int i = 0; i < graphEdges.size(); i++)
            edges[i] = graphEdges.get(i);

        /*
         * Sort all edges in nondecreasing order of weight.
         *
         * WeightedEdge implements Comparable<WeightedEdge>,
         * where edges are compared according to their weights.
         */
        MergeSort<WeightedEdge> mergeSort = new MergeSort<>();
        mergeSort.sort(edges);

        /*
         * The edges selected for the Minimum Spanning Tree.
         */
        SinglyLinkedList<WeightedEdge> mst = new SinglyLinkedList<>();

        /*
         * Examine the edges from the lightest to the heaviest.
         */
        for (WeightedEdge edge : edges) {

            int u = edge.getFrom();
            int v = edge.getTo();

            /*
             * If u and v belong to different connected components,
             * adding this edge cannot create a cycle.
             */
            if (!sets.findSet(u).equals(sets.findSet(v))) {

                // Add the edge to the MST.
                mst.add(edge);

                /*
                 * The edge connects the two components,
                 * so merge them into one component.
                 */
                sets.union(u, v);

                /*
                 * Every spanning tree on n vertices contains
                 * exactly n - 1 edges.
                 */
                if (mst.size() == n - 1)
                    break;
            }
        }

        /*
         * If fewer than n - 1 edges were selected, the graph is
         * disconnected and therefore has no spanning tree.
         */
        if (mst.size() != n - 1)
            throw new IllegalArgumentException("The graph is disconnected.");

        return mst;
    }
}