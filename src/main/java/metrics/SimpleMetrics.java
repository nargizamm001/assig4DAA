package metrics;

public class SimpleMetrics implements Metrics {
    private long dfsVisits, dfsEdges, queuePushes, queuePops, relaxations;
    private long start, end;

    @Override public void reset() { dfsVisits=dfsEdges=queuePushes=queuePops=relaxations=0; start=end=0; }
    @Override public void addDfsVisit() { dfsVisits++; }
    @Override public void addDfsEdge() { dfsEdges++; }
    @Override public void addQueuePush() { queuePushes++; }
    @Override public void addQueuePop() { queuePops++; }
    @Override public void addRelaxation() { relaxations++; }

    @Override public long getDfsVisits() { return dfsVisits; }
    @Override public long getDfsEdges() { return dfsEdges; }
    @Override public long getQueuePushes() { return queuePushes; }
    @Override public long getQueuePops() { return queuePops; }
    @Override public long getRelaxations() { return relaxations; }

    @Override public void markStart() { start = System.nanoTime(); }
    @Override public void markEnd() { end = System.nanoTime(); }
    @Override public long getElapsedNanos() { return end - start; }
}
