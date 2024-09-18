package W6RedScare;

import java.util.*;

public class SolveSome {
    private Graph graph;
    private String startVertex;
    private String endVertex;

    public SolveSome(Graph graph, String startVertex, String endVertex) {
        this.graph = graph;
        this.startVertex = startVertex;
        this.endVertex = endVertex;
    }

    public boolean solve() {
        return dfsSimplePath(startVertex, new HashSet<>(), false);
    }

    private boolean dfsSimplePath(String current, Set<String> visited, boolean seenRed) {
        if (current.equals(endVertex)) {
            return seenRed;
        }

        visited.add(current);

        if (graph.vertexColors.get(current)) {
            seenRed = true;
        }

        for (String neighbor : graph.adj(current)) {
            if (!visited.contains(neighbor)) {
                if (dfsSimplePath(neighbor, visited, seenRed)) {
                    return true;
                }
            }
        }

        visited.remove(current);
        return false;
    }
}