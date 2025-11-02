package graph.topo;

import graph.model.Edge;
import graph.model.Graph;
import metrics.Metrics;

import java.util.*;

public class KahnTopo {
    private final Graph dag; private final Metrics metrics;
    public KahnTopo(Graph dag, Metrics metrics) { this.dag = dag; this.metrics = metrics; }

    public int[] order() {
        int n = dag.n();
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) for (Edge e : dag.adj().get(u)) indeg[e.to]++;

        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i]==0) { q.add(i); metrics.addQueuePush(); }

        int[] out = new int[n]; int idx=0;
        metrics.markStart();
        while (!q.isEmpty()) {
            int u = q.remove(); metrics.addQueuePop(); out[idx++] = u;
            for (Edge e : dag.adj().get(u)) if (--indeg[e.to]==0) { q.add(e.to); metrics.addQueuePush(); }
        }
        metrics.markEnd();
        if (idx != n) throw new IllegalStateException("Condensation has a cycle (shouldn't happen)");
        return out;
    }
}
