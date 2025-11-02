package util;

import java.util.*;

public class Paths {
    public static List<Integer> reconstructPath(int src, int dst, int[] parent) {
        if (dst < 0) return List.of();
        List<Integer> path = new ArrayList<>();
        for (int cur = dst; cur != -1; cur = parent[cur]) {
            path.add(cur);
            if (cur == src) break;
        }
        if (path.get(path.size()-1) != src) return List.of();
        Collections.reverse(path);
        return path;
    }

    public static String toStringPath(List<Integer> path) {
        if (path.isEmpty()) return "(unreachable)";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) sb.append(" -> ");
            sb.append(path.get(i));
        }
        return sb.toString();
    }
}
