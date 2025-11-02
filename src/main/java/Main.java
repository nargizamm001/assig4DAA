import graph.dagsp.DagLongestPath;
import graph.dagsp.DagShortestPath;
import graph.model.Graph;
import graph.scc.KosarajuSCC;
import graph.topo.KahnTopo;
import io.JsonGraphReader;
import metrics.Metrics;
import metrics.SimpleMetrics;
import util.Paths;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String inputPath = (args.length > 0) ? args[0] : "data/tasks.json";
        if (!new File(inputPath).exists()) {
            System.err.println("Input not found: " + inputPath);
            return;
        }
        var loaded = JsonGraphReader.load(inputPath);
        Graph g = loaded.g; int srcOriginal = loaded.source;

        System.out.println("Loaded: n=" + g.n() + ", directed=" + g.directed());
        System.out.println("Edges: " + g.edges().size() + ", source(original)=" + srcOriginal);
        System.out.println();

        // 1) SCC
        Metrics sccM = new SimpleMetrics();
        var scc = new KosarajuSCC(g, sccM);
        var sccRes = scc.run();
        System.out.println("SCC count = " + sccRes.components.size());
        for (int i=0;i<sccRes.components.size();i++)
            System.out.println("  C"+i+" size="+sccRes.components.get(i).size()+" -> "+sccRes.components.get(i));
        System.out.printf("SCC metrics: dfsVisits=%d dfsEdges=%d time=%.3f ms%n",
                sccM.getDfsVisits(), sccM.getDfsEdges(), sccM.getElapsedNanos()/1e6);
        System.out.println();

        // Condensation DAGs
        var cond = KosarajuSCC.buildCondensation(g, sccRes.compId, sccRes.components.size());
        System.out.println("Condensation edges:");
        for (var ce : cond.edges) System.out.println("  " + ce);
        System.out.println();

        // 2) Topo order (components)
        Metrics topoM = new SimpleMetrics();
        var topoOrder = new KahnTopo(cond.dagMin, topoM).order();
        System.out.println("Topo order (components): " + Arrays.toString(topoOrder));
        System.out.printf("Topo metrics: pushes=%d pops=%d time=%.3f ms%n",
                topoM.getQueuePushes(), topoM.getQueuePops(), topoM.getElapsedNanos()/1e6);
        System.out.println("Derived original tasks by components:");
        for (int cid : topoOrder) System.out.println("  C"+cid+" -> "+sccRes.components.get(cid));
        System.out.println();

        int srcComp = sccRes.compId[srcOriginal];
        System.out.println("Source component: C" + srcComp);
        System.out.println();

        // 3) Shortest paths (min weights)
        Metrics spM = new SimpleMetrics();
        var sp = new DagShortestPath(cond.dagMin, topoOrder, spM);
        var spRes = sp.run(srcComp);
        System.out.println("Shortest distances (from C"+srcComp+"):");
        for (int c=0;c<cond.dagMin.n();c++) {
            var d = spRes.dist[c]; System.out.printf("  C%d : %s%n", c, d >= (long)1e18 ? "INF" : String.valueOf(d));
        }
        int far = -1; long best = Long.MIN_VALUE;
        for (int c=0;c<cond.dagMin.n();c++) if (spRes.dist[c] < (long)1e18 && spRes.dist[c] > best) { best = spRes.dist[c]; far = c; }
        var spPath = util.Paths.reconstructPath(srcComp, far, spRes.parent);
        System.out.println("One shortest-path example (to farthest reachable by distance): " + Paths.toStringPath(spPath));
        System.out.printf("SP metrics: relaxations=%d time=%.3f ms%n", spM.getRelaxations(), spM.getElapsedNanos()/1e6);
        System.out.println();

        // 4) Longest (critical) path (max weights)
        Metrics lpM = new SimpleMetrics();
        var lp = new DagLongestPath(cond.dagMax, topoOrder, lpM);
        var lpRes = lp.run(srcComp);
        var lpPath = Paths.reconstructPath(srcComp, lpRes.argmax, lpRes.parent);
        System.out.println("Critical path (components): " + Paths.toStringPath(lpPath));
        System.out.println("Critical path length: " + lpRes.best[lpRes.argmax]);
        System.out.printf("LP metrics: relaxations=%d time=%.3f ms%n", lpM.getRelaxations(), lpM.getElapsedNanos()/1e6);

        System.out.println("\n=== Done ===");
    }
}
