package graph.model;

import java.util.*;

public class Graph {
    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adj;

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public int n() { return n; }
    public boolean directed() { return directed; }
    public List<List<Edge>> adj() { return adj; }

    public void addEdge(int u, int v, int w) {
        adj.get(u).add(new Edge(u, v, w));
        if (!directed) adj.get(v).add(new Edge(v, u, w));
    }

    public Graph transpose() {
        if (!directed) throw new IllegalStateException("Transpose only for directed graphs");
        Graph t = new Graph(n, true);
        for (int u = 0; u < n; u++) {
            for (Edge e : adj.get(u)) t.addEdge(e.to, e.from, e.w);
        }
        return t;
    }

    public List<Edge> edges() {
        List<Edge> all = new ArrayList<>();
        for (int u = 0; u < n; u++) all.addAll(adj.get(u));
        return all;
    }
}
