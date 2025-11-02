package io;

import com.google.gson.*;
import graph.model.Graph;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonGraphReader {
    public static class InputData {
        public boolean directed; public int n;
        public EdgeObj[] edges;
        public Integer source;
        public String weight_model;
    }
    public static class EdgeObj { public int u, v, w; }

    public static class Loaded {
        public final Graph g; public final int source; public final String weightModel;
        public Loaded(Graph g, int source, String wm) { this.g = g; this.source = source; this.weightModel = wm; }
    }

    public static Loaded load(String path) throws IOException {
        try (Reader r = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8)) {
            JsonElement root = JsonParser.parseReader(r);
            InputData id = new Gson().fromJson(root, InputData.class);
            Graph g = new Graph(id.n, id.directed);
            for (EdgeObj e : id.edges) g.addEdge(e.u, e.v, e.w);
            int src = (id.source != null) ? id.source : 0;
            String wm = (id.weight_model != null) ? id.weight_model : "edge";
            return new Loaded(g, src, wm);
        }
    }
}
