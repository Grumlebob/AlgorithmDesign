package W6RedScare;

import java.util.ArrayDeque;
import java.util.Deque;

/**
    Inspired by algs4
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 */
public class SolveFew {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;   // marked[v] = is there an s->v path?
    private int[] edgeTo;       // edgeTo[v] = last edge on path with min red vertices
    private int[] minRedVerticesToV;

    /**
     * Computes the minimum number of red vertices on any path from {@code s} to every other vertex in graph {@code G}.
     * @param G the graph
     * @param s the source vertex index
     */
    public SolveFew(Graph G, int s) {
        marked = new boolean[G.V];
        minRedVerticesToV = new int[G.V];
        edgeTo = new int[G.V];

        for (int v = 0; v < G.V; v++)
            minRedVerticesToV[v] = INFINITY;

        // Initialize minRedVerticesToV[V]
        boolean isRedS = G.vertexColors.get(G.indexToName.get(s));
        minRedVerticesToV[s] = isRedS ? 1 : 0;

        bfs(G, s);
    }

    // 0-1 BFS from single source
    // Inspired from https://www.geeksforgeeks.org/0-1-bfs-shortest-path-binary-graph/
    private void bfs(Graph G, int s) {
        Deque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(s);
        marked[s] = true;

        while (!deque.isEmpty()) {
            int v = deque.pollFirst();
            for (int w : G.adj(v)) {
                boolean isRedW = G.vertexColors.get(G.indexToName.get(w));
                int edgeWeight = isRedW ? 1 : 0;
                int newDist = minRedVerticesToV[v] + edgeWeight;

                //If shorter distance found
                if (minRedVerticesToV[w] > newDist) {
                    minRedVerticesToV[w] = newDist;
                    edgeTo[w] = v;
                    if (edgeWeight == 0) {
                        deque.addFirst(w); //explore cheapest first
                    } else {
                        deque.addLast(w);
                    }
                }
            }
        }
    }

    /**
     * Same as distTo from solveNone
     */
    public int minRedVerticesToV(int v) {
        int dist = minRedVerticesToV[v];
        if (dist == INFINITY) {
            return -1;
        }
        return dist;
    }

    public static void VerifyEdgecaseResults(String filename, int result) {
        switch (filename) {
            case "edgecase-solveNone-blackSrcToRedSink.txt",
                 "edgecase-solveNone-redSrcToBlackSink.txt":
                if (result != 1) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 1, got " + result);
                    System.out.println("------------");
                }
                break;
            case "G-ex.txt":
                if (result != 0) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 0, got " + result);
                    System.out.println("------------");
                }
                break;
            case "P3.txt":
                if (result != 1) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 1, got " + result);
                    System.out.println("------------");
                }
                break;
            case "rusty-1-17.txt":
                if (result != 0) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 0, got " + result);
                    System.out.println("------------");
                }
                break;

            case "grid-5-0.txt":
                if (result != 0) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 0, got " + result);
                    System.out.println("------------");
                }
                break;

            case "wall-z-3.txt":
                if (result != 0) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 0, got " + result);
                    System.out.println("------------");
                }
                break;

            case "ski-illustration.txt":
                if (result != 0) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 0, got " + result);
                    System.out.println("------------");
                }
                break;

            case "increase-n8-1.txt":
                if (result != 1) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 1, got " + result);
                    System.out.println("------------");
                }
                break;
        }
    }
}
