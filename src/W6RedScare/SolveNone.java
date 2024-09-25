package W6RedScare;

import Shared.Queue;

/**
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class SolveNone {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = is there an s->v path?
    private int[] edgeTo;      // edgeTo[v] = last edge on shortest s->v path
    private int[] distTo;      // distTo[v] = length of shortest s->v path
    Graph graph;
    int startVertex;


    public SolveNone(Graph graph, int startVertex) {
        marked = new boolean[graph.countOfVertices];
        distTo = new int[graph.countOfVertices];
        edgeTo = new int[graph.countOfVertices];
        this.graph = graph;
        this.startVertex = startVertex;
        for (int v = 0; v < graph.countOfVertices; v++)
            distTo[v] = INFINITY;
    }

    // BFS from single source
    private void bfs(Graph G, int s) {
        Queue<Integer> q = new Queue<>();
        marked[s] = true;
        distTo[s] = 0;
        q.enqueue(s);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    /**
     * Returns shortest distance from startVertex to v using bfs.
     * If no path exists, returns -1.
     */
    public int distTo(int v) {
        bfs(graph, startVertex);
        int dist = distTo[v];
        if (dist == INFINITY) {
            return -1;
        }
        return distTo[v];
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
                if (result != 3) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 3, got " + result);
                    System.out.println("------------");
                }
                break;
            case "P3.txt":
                if (result != -1) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected -1, got " + result);
                    System.out.println("------------");
                }
                break;
            case "rusty-1-17.txt":
                if (result != 10) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 10, got " + result);
                    System.out.println("------------");
                }
                break;

            case "grid-5-0.txt":
                if (result != 14) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 14, got " + result);
                    System.out.println("------------");
                }
                break;

            case "wall-z-3.txt":
                if (result != 1) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 1, got " + result);
                    System.out.println("------------");
                }
                break;

            case "ski-illustration.txt":
                if (result != 8) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected 8, got " + result);
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
