package metrics;

public interface Metrics {
    void reset();
    void addDfsVisit();
    void addDfsEdge();
    void addQueuePush();
    void addQueuePop();
    void addRelaxation();

    long getDfsVisits();
    long getDfsEdges();
    long getQueuePushes();
    long getQueuePops();
    long getRelaxations();

    void markStart();
    void markEnd();
    long getElapsedNanos();
}
