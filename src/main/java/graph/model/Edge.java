package graph.model;

public class Edge {
    public final int from;
    public final int to;
    public final int w;

    public Edge(int from, int to, int w) {
        this.from = from;
        this.to = to;
        this.w = w;
    }

    @Override
    public String toString() {
        return from + "->" + to + " (w=" + w + ")";
    }
}
