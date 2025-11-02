import graph.model.Graph;
import graph.scc.KosarajuSCC;
import graph.topo.KahnTopo;
import metrics.SimpleMetrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasicTests {
    @Test
    void scc_groupsAreConsistent() {
        Graph g = new Graph(5, true);
        g.addEdge(0,1,1); g.addEdge(1,2,1); g.addEdge(2,0,1);
        g.addEdge(3,4,1); g.addEdge(4,3,1);
        g.addEdge(2,3,5);
        var res = new KosarajuSCC(g, new SimpleMetrics()).run();
        int c012 = res.compId[0], c34 = res.compId[3];
        assertEquals(c012, res.compId[1]);
        assertEquals(c012, res.compId[2]);
        assertEquals(c34, res.compId[4]);
        assertNotEquals(c012, c34);
    }

    @Test
    void topo_isValidOrder() {
        Graph dag = new Graph(4, true);
        dag.addEdge(0,1,1); dag.addEdge(1,2,1); dag.addEdge(0,3,1);
        int[] order = new KahnTopo(dag, new SimpleMetrics()).order();
        int p0=pos(order,0), p1=pos(order,1), p2=pos(order,2), p3=pos(order,3);
        assertTrue(p0 < p1); assertTrue(p1 < p2); assertTrue(p0 < p3);
    }
    private int pos(int[] a, int x){ for(int i=0;i<a.length;i++) if(a[i]==x) return i; return -1; }
}
