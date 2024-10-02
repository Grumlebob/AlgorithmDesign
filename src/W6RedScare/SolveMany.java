package W6RedScare;


import Shared.Queue;

import java.util.ArrayList;
import java.util.Arrays;

/**
 Inspired by algs4
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 */
public class SolveMany {
    private static final int NEG_INFINITY = Integer.MIN_VALUE;
    private Graph graph;
    private int startVertex;
    private int endVertex;
    private int[] maxRedVerticesToV;

    //for bfs, in case we have an undirected graph
    private boolean[] marked;  // marked[v] = is there an s->v path?
    private int[] edgeTo;      // edgeTo[v] = last edge on shortest s->v path


    public SolveMany(Graph G, int s, int t) {
        graph = G;
        startVertex = s;
        endVertex = t;
        marked = new boolean[G.countOfVertices];
        edgeTo = new int[G.countOfVertices];
    }

    public ResultWithIntInfo solve() {
        if (graph.isDirected) {
            var toplogicalSort = graph.topologicalSortDirected();
            if (toplogicalSort == null) {
                //not a DAG
                return ResultWithIntInfo.HARD;
            } else {
                //Is a DAG
                longestPathInDAG(toplogicalSort);
            }
            if (maxRedVerticesToV == null) {
                return ResultWithIntInfo.HARD;  // Not a DAG
            }
            //Ex, common-1-20, has 0 edges. So technically a DAG, but no path from start to end
            else if (maxRedVerticesToV[endVertex] == NEG_INFINITY) {
                return ResultWithIntInfo.VALUE.setValue(-1);  // No path from startVertex to endVertex
            }

            return ResultWithIntInfo.VALUE.setValue(maxRedVerticesToV[endVertex]);
        }

        else { //undirected
            return bfsUndirectedAcyclic();
        }
    }


    /**
     * Solves the longest path problem in a DAG from source vertex s considering vertex colors.
     * Black vertices and edges has weight 0, red vertices has weight 1.
     */
    public void longestPathInDAG(ArrayList<String> topologicalOrder) {
        maxRedVerticesToV = new int[graph.countOfVertices];
        Arrays.fill(maxRedVerticesToV, NEG_INFINITY);
        maxRedVerticesToV[startVertex] = 0; // Distance to the source is 0

        //O(V)
        for (String vertexName : topologicalOrder) {
            int v = graph.nameToIndex.get(vertexName);

            // Relax all edges from vertex v
            //O(E)
            for (int w : graph.adj(v)) {
                relax(v, w);
            }
        }
        //Total: O(V + E)
    }

    /**
     * Relax edge v->w by considering the color of vertex w.
     * Red vertices contribute weight 1, black vertices contribute weight 0.
     */
    private void relax(int v, int w) {
        int weight = graph.vertexColors.get(graph.indexToName.get(w)) ? 1 : 0; // 1 for red, 0 for black

        if (maxRedVerticesToV[w] < maxRedVerticesToV[v] + weight) {
            //inf + w = inf
            if (maxRedVerticesToV[v] == NEG_INFINITY) {
                maxRedVerticesToV[w] = NEG_INFINITY;
            }
            else {
                maxRedVerticesToV[w] = maxRedVerticesToV[v] + weight;
            }
        }
    }


    public ResultWithIntInfo bfsUndirectedAcyclic() {
        Queue<Integer> q = new Queue<>();
        marked[startVertex] = true;
        var reachedDestination = false;
        q.enqueue(startVertex);

        while (!q.isEmpty()) {
            int v = q.dequeue();
            if (v == endVertex) {
                reachedDestination = true;
            }
            for (int w : graph.adj(v)) {
                // in undirected graph egdes point both ways. But we don't go back
                if (w == edgeTo[v]) {
                    continue;
                }
                if (marked[w]) { // Cycle
                    return ResultWithIntInfo.HARD;
                }
                edgeTo[w] = v;
                marked[w] = true;
                q.enqueue(w);
            }
        }
        if (!reachedDestination) {
            return ResultWithIntInfo.VALUE.setValue(-1);
        }
        var v = endVertex;
        var redsOnPath = 0;
        //traverse from end to start, counting all red vertices
        while (v != startVertex) {
            String vName = graph.indexToName.get(v);
            var vColor = graph.vertexColors.get(vName);
            if (vColor) {
                redsOnPath++;
            }
            v = edgeTo[v];
        }
        // remember to include the start vertex
        if (graph.vertexColors.get(graph.indexToName.get(startVertex))) {
            redsOnPath++;
        }
        return ResultWithIntInfo.VALUE.setValue(redsOnPath);
    }

    public static void VerifyEdgecaseResults(String filename, ResultWithIntInfo result) {
        switch (filename) {
            case "G-ex.txt":
                if (result != ResultWithIntInfo.HARD) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected HARD, got " + result);
                    System.out.println("------------");
                }
                break;
            case "P3.txt":
                if (result != ResultWithIntInfo.VALUE.setValue(1)) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 1, got " + result);
                    System.out.println("------------");
                }
                break;
            case "rusty-1-17.txt":
                if (result != ResultWithIntInfo.HARD) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected HARD, got " + result);
                    System.out.println("------------");
                }
                break;

            case "grid-5-0.txt":
                if (result != ResultWithIntInfo.HARD) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected HARD, got " + result);
                    System.out.println("------------");
                }
                break;

            case "wall-z-3.txt":
                if (result != ResultWithIntInfo.HARD) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected HARD, got " + result);
                    System.out.println("------------");
                }
                break;

            case "ski-illustration.txt":
                if (result != ResultWithIntInfo.VALUE.setValue(1)) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 1, got " + result);
                    System.out.println("------------");
                }
                break;

            case "increase-n8-1.txt":
                if (result != ResultWithIntInfo.VALUE.setValue(3)) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 3, got " + result);
                    System.out.println("------------");
                }
                break;
        }
    }
}
