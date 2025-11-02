package graph.scc;

import graph.model.Edge;
import graph.model.Graph;
import metrics.Metrics;

import java.util.*;

public class KosarajuSCC {
    public static class Result {
        public final List<List<Integer>> components;
        public final int[] compId;
        public Result(List<List<Integer>> components, int[] compId) { this.components = components; this.compId = compId; }
    }

    private final Graph g; private final Metrics metrics;
    public KosarajuSCC(Graph g, Metrics metrics) {
        if (!g.directed()) throw new IllegalArgumentException("SCC requires directed graph");
        this.g = g; this.metrics = metrics;
    }

    public Result run() {
        int n = g.n();
        boolean[] vis = new boolean[n];
        Deque<Integer> order = new ArrayDeque<>();

        metrics.reset(); metrics.markStart();
        for (int v = 0; v < n; v++) if (!vis[v]) dfs1(v, vis, order);

        Graph gt = g.transpose();
        Arrays.fill(vis, false);
        int[] compId = new int[n];
        Arrays.fill(compId, -1);
        List<List<Integer>> comps = new ArrayList<>();

        while (!order.isEmpty()) {
            int v = order.pop();
            if (!vis[v]) {
                List<Integer> comp = new ArrayList<>();
                dfs2(gt, v, vis, comp);
                for (int x : comp) compId[x] = comps.size();
                comps.add(comp);
            }
        }
        metrics.markEnd();
        return new Result(comps, compId);
    }

    private void dfs1(int v, boolean[] vis, Deque<Integer> order) {
        vis[v] = true; metrics.addDfsVisit();
        for (Edge e : g.adj().get(v)) { metrics.addDfsEdge(); if (!vis[e.to]) dfs1(e.to, vis, order); }
        order.push(v);
    }
    private void dfs2(Graph gt, int v, boolean[] vis, List<Integer> comp) {
        vis[v] = true; metrics.addDfsVisit(); comp.add(v);
        for (Edge e : gt.adj().get(v)) { metrics.addDfsEdge(); if (!vis[e.to]) dfs2(gt, e.to, vis, comp); }
    }

    public static class CondensedEdge {
        public final int fromC, toC, minW, maxW;
        public CondensedEdge(int fromC, int toC, int minW, int maxW) { this.fromC=fromC; this.toC=toC; this.minW=minW; this.maxW=maxW; }
        @Override public String toString() { return fromC + "->" + toC + " [min=" + minW + ", max=" + maxW + "]"; }
    }

    public static class Condensation {
        public final Graph dagMin, dagMax; public final List<CondensedEdge> edges; public final int compsCount;
        public Condensation(Graph dagMin, Graph dagMax, List<CondensedEdge> edges, int compsCount) {
            this.dagMin=dagMin; this.dagMax=dagMax; this.edges=edges; this.compsCount=compsCount;
        }
    }

    public static Condensation buildCondensation(Graph g, int[] compId, int compsCount) {
        Map<Long,int[]> agg = new HashMap<>(); // key=(from<<32)|to, val[0]=min, val[1]=max
        for (int u = 0; u < g.n(); u++) {
            int cu = compId[u];
            for (var e : g.adj().get(u)) {
                int cv = compId[e.to]; if (cu==cv) continue;
                long key = (((long)cu)<<32) | (cv & 0xffffffffL);
                int[] mm = agg.getOrDefault(key, new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE});
                mm[0] = Math.min(mm[0], e.w); mm[1] = Math.max(mm[1], e.w);
                agg.put(key, mm);
            }
        }
        Graph dagMin = new Graph(compsCount, true);
        Graph dagMax = new Graph(compsCount, true);
        List<CondensedEdge> list = new ArrayList<>();
        for (var en : agg.entrySet()) {
            int fromC = (int)(en.getKey() >> 32);
            int toC   = (int)(en.getKey().longValue() & 0xffffffffL);
            int minW = en.getValue()[0], maxW = en.getValue()[1];
            dagMin.addEdge(fromC, toC, minW); dagMax.addEdge(fromC, toC, maxW);
            list.add(new CondensedEdge(fromC, toC, minW, maxW));
        }
        return new Condensation(dagMin, dagMax, list, compsCount);
    }
}
