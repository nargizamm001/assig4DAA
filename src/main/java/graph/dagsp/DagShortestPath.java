package graph.dagsp;

import graph.model.Edge;
import graph.model.Graph;
import metrics.Metrics;

import java.util.Arrays;

public class DagShortestPath {
    private final Graph dag; private final int[] topo; private final Metrics metrics;
    public DagShortestPath(Graph dag, int[] topo, Metrics metrics) { this.dag=dag; this.topo=topo; this.metrics=metrics; }

    public static class Result { public final long[] dist; public final int[] parent;
        public Result(long[] dist, int[] parent){ this.dist=dist; this.parent=parent; } }

    public Result run(int src) {
        int n = dag.n(); long INF = (long)1e18;
        long[] dist = new long[n]; int[] parent = new int[n];
        Arrays.fill(dist, INF); Arrays.fill(parent, -1); dist[src]=0;

        metrics.markStart();
        for (int u : topo) {
            if (dist[u]==INF) continue;
            for (Edge e : dag.adj().get(u)) {
                long cand = dist[u] + e.w; metrics.addRelaxation();
                if (cand < dist[e.to]) { dist[e.to]=cand; parent[e.to]=u; }
            }
        }
        metrics.markEnd();
        return new Result(dist, parent);
    }
}
