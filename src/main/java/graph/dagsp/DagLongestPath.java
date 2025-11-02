package graph.dagsp;

import graph.model.Edge;
import graph.model.Graph;
import metrics.Metrics;

import java.util.Arrays;

public class DagLongestPath {
    private final Graph dag; private final int[] topo; private final Metrics metrics;
    public DagLongestPath(Graph dag, int[] topo, Metrics metrics) { this.dag=dag; this.topo=topo; this.metrics=metrics; }

    public static class Result {
        public final long[] best; public final int[] parent; public int argmax=-1;
        public Result(long[] best, int[] parent){ this.best=best; this.parent=parent; }
    }

    public Result run(int src){
        int n = dag.n(); long NEG = (long)-1e18;
        long[] best = new long[n]; int[] parent = new int[n];
        Arrays.fill(best, NEG); Arrays.fill(parent, -1); best[src]=0;

        metrics.markStart();
        for (int u : topo) {
            if (best[u]==NEG) continue;
            for (Edge e : dag.adj().get(u)) {
                long cand = best[u] + e.w; metrics.addRelaxation();
                if (cand > best[e.to]) { best[e.to]=cand; parent[e.to]=u; }
            }
        }
        metrics.markEnd();

        Result r = new Result(best, parent); long val = NEG;
        for (int i=0;i<n;i++) if (best[i]>val){ val=best[i]; r.argmax=i; }
        return r;
    }
}
