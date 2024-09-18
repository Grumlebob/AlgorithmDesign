package W6RedScare;

import Shared.Queue;

/**
 * Inspired by algs4
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class SolveAlternate {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = is there an s->v path?
    private int[] edgeTo;      // edgeTo[v] = last edge on shortest s->v path
    private int[] distTo;      // distTo[v] = length of shortest s->v path
    private Graph graph;
    private int startVertex; // startVertex

    public SolveAlternate(Graph G, int s) {
        graph = G;
        startVertex = s;
        marked = new boolean[G.V];
        distTo = new int[G.V];
        edgeTo = new int[G.V];
        for (int v = 0; v < G.V; v++)
            distTo[v] = INFINITY;
    }

    // BFS from single source
    private void bfs(Graph G) {
        Queue<Integer> q = new Queue<Integer>();
        marked[startVertex] = true;
        distTo[startVertex] = 0;
        q.enqueue(startVertex);

        while (!q.isEmpty()) {
            int v = q.dequeue();

            String vName = G.indexToName.get(v);
            var vColor = G.vertexColors.get(vName);

            for (int w : G.adj(v)) {
                var wName = G.indexToName.get(w);
                var wColor = G.vertexColors.get(wName);
                if (!marked[w] && vColor != wColor) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    /**
     same as distTo in solveNone
     */
    public int distTo(int v) {
        bfs(graph);
        int dist = distTo[v];
        if (dist == INFINITY) {
            return -1;
        }
        return distTo[v];
    }

    public static void VerifyEdgecaseResults(String filename, boolean result) {
        switch (filename) {
            case "edgecase-solveNone-blackSrcToRedSink.txt",
                 "edgecase-solveNone-redSrcToBlackSink.txt":
                if (!result) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected true, got " + result);
                    System.out.println("------------");
                }
                break;
            case "G-ex.txt":
                if (!result) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected true, got " + result);
                    System.out.println("------------");
                }
                break;
            case "P3.txt":
                if (!result) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected true, got " + result);
                    System.out.println("------------");
                }
                break;
            case "rusty-1-17.txt":
                if (result) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected false, got " + result);
                    System.out.println("------------");
                }
                break;

            case "grid-5-0.txt":
                if (!result) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected true, got " + result);
                    System.out.println("------------");
                }
                break;

            case "wall-z-3.txt":
                if (result) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected false, got " + result);
                    System.out.println("------------");
                }
                break;

            case "ski-illustration.txt":
                if (result) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected false, got " + result);
                    System.out.println("------------");
                }
                break;

            case "increase-n8-1.txt":
                if (!result) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected true, got " + result);
                    System.out.println("------------");
                }
                break;
        }
    }

}
