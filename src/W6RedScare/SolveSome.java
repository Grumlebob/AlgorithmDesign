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
        return dfsSimplePath(startVertex, new HashSet<>(), new HashSet<>(), false);
    }

    //Visited: Fully explored all paths from this node
    //InPath: Current path being explored, ensures it is a simple path (no repeated vertices)
    private boolean dfsSimplePath(String current, Set<String> visited, Set<String> inPath, boolean seenRed) {
        // Base case: if current node is the end vertex
        if (current.equals(endVertex)) {
            // Return true only if a red node was encountered in this path
            return seenRed;
        }

        // Mark the current node as part of the current path
        inPath.add(current);

        // Check if the current node is red
        if (graph.vertexColors.get(current)) {
            seenRed = true;
        }

        // Explore all neighbors
        for (String neighbor : graph.adj(current)) {
            if (!inPath.contains(neighbor) && !visited.contains(neighbor)) {
                // Recursively visit neighbors
                if (dfsSimplePath(neighbor, visited, inPath, seenRed)) {
                    return true; // Found a valid path
                }
            }
        }

        // Backtrack: Remove the current node from the path and mark it as visited
        inPath.remove(current);
        visited.add(current);

        return false; // No valid path found in this branch
    }
}