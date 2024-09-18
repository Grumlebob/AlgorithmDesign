package W6RedScare;

import Shared.Bag;
import Shared.StdOut;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Modified version of the Graph class to handle vertices with string names, both directed and undirected edges, and vertex coloring.
 */
public class Graph {
    private static final String NEWLINE = System.getProperty("line.separator");

    public int V; // number of vertices in this graph
    private int E;       // number of edges in this graph
    private Bag<Integer>[] adj;    // adj[v] = adjacency list for vertex v
    private int[] indegree;        // indegree[v] = indegree of vertex v
    public HashMap<String, Integer> nameToIndex; // map from vertex name to index
    public ArrayList<String> indexToName;        // map from index to vertex name

    public HashMap<String, Boolean> vertexColors; // map from vertex name to its color (true = Red, false = Black)

    /**
     * Initializes an empty graph with a given number of vertices.
     *
     * @param V the number of vertices
     */
    public Graph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Graph must be non-negative");
        this.V = V;
        this.E = 0;
        indegree = new int[V];
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }

        nameToIndex = new HashMap<>();
        indexToName = new ArrayList<>();
        vertexColors = new HashMap<>();
    }

    /**
     * Adds a vertex with the given name and color.
     *
     * @param name  the name of the vertex
     * @param isRed the color of the vertex (true = Red, false = Black)
     * @throws IllegalArgumentException if the vertex name already exists
     */
    public void addVertex(String name, boolean isRed) {
        if (nameToIndex.containsKey(name)) {
            throw new IllegalArgumentException("Vertex with name " + name + " already exists.");
        }
        int index = nameToIndex.size();
        nameToIndex.put(name, index);
        indexToName.add(name);
        vertexColors.put(name, isRed); // Assign color at the time of vertex addition
    }

    /**
     * Adds a directed edge v→w to this graph.
     *
     * @param vName the tail vertex name
     * @param wName the head vertex name
     */
    public void addDirectedEdge(String vName, String wName) {
        if (!nameToIndex.containsKey(vName)) {
            throw new IllegalArgumentException("Vertex " + vName + " does not exist.");
        }
        if (!nameToIndex.containsKey(wName)) {
            throw new IllegalArgumentException("Vertex " + wName + " does not exist.");
        }
        int v = nameToIndex.get(vName);
        int w = nameToIndex.get(wName);
        adj[v].add(w);
        indegree[w]++;
        E++;
    }

    /**
     * Adds an undirected edge between v and w. This adds both v→w and w→v.
     *
     * @param vName the first vertex name
     * @param wName the second vertex name
     */
    public void addUndirectedEdge(String vName, String wName) {
        addDirectedEdge(vName, wName);
        addDirectedEdge(wName, vName);
    }


    public int getVertexCount() {
        return V;
    }

    public int getEdgeCount() {
        return E;
    }


    public Graph copy() {
        Graph copyGraph = new Graph(this.V);

        // Copy vertex mappings and colors
        copyGraph.nameToIndex = new HashMap<>(this.nameToIndex);
        copyGraph.indexToName = new ArrayList<>(this.indexToName);
        copyGraph.vertexColors = new HashMap<>(this.vertexColors);

        // Copy adjacency lists
        copyGraph.adj = (Bag<Integer>[]) new Bag[this.V];
        for (int v = 0; v < this.V; v++) {
            if (this.adj[v] != null) {
                copyGraph.adj[v] = new Bag<>();
                for (int w : this.adj[v]) {
                    copyGraph.adj[v].add(w);
                }
            } else {
                copyGraph.adj[v] = null;
            }
        }

        // Copy indegrees
        copyGraph.indegree = new int[this.V];
        System.arraycopy(this.indegree, 0, copyGraph.indegree, 0, this.V);

        // Copy the edge count
        copyGraph.E = this.E;

        return copyGraph;
    }



    /**
     * USED FOR SOLVENONE, where we remove red vertices from the graph
     * Removes all red vertices from the graph and updates all relevant data structures.
     */
    public void removeAllRed() {
        // Create a boolean array to mark red vertices
        boolean[] isRedVertex = new boolean[V];
        for (int v = 0; v < V; v++) {
            String vertexName = indexToName.get(v);
            if (vertexName != null && vertexColors.get(vertexName)) {
                isRedVertex[v] = true;
            }
        }

        // Update adjacency lists to exclude edges to red vertices
        for (int v = 0; v < V; v++) {
            if (adj[v] != null && !isRedVertex[v]) {
                Bag<Integer> newAdjList = new Bag<>();
                for (int w : adj[v]) {
                    if (!isRedVertex[w]) {
                        newAdjList.add(w);
                    } else {
                        // Update indegree of the red vertex since we're removing an incoming edge
                        indegree[w]--;
                        E--; // Decrement edge count
                    }
                }
                adj[v] = newAdjList;
            } else {
                adj[v] = null; // Remove adjacency list for red vertices
            }
        }

        // Remove red vertices from data structures
        for (int v = 0; v < V; v++) {
            if (isRedVertex[v]) {
                String vertexName = indexToName.get(v);
                nameToIndex.remove(vertexName);
                indexToName.set(v, null);
                vertexColors.remove(vertexName);
                indegree[v] = 0;
            }
        }
    }

    /**
     * Returns the vertices adjacent from vertex with the given name in this graph.
     *
     * @param vName the name of the vertex
     * @return the vertices adjacent from vertex {@code vName} in this graph, as an iterable
     */
    public Iterable<String> adj(String vName) {
        if (!nameToIndex.containsKey(vName)) {
            throw new IllegalArgumentException("Vertex " + vName + " does not exist.");
        }
        int v = nameToIndex.get(vName);
        Bag<String> adjacentVertices = new Bag<>();
        if (adj[v] != null) {
            for (int w : adj[v]) {
                adjacentVertices.add(indexToName.get(w));
            }
        }
        return adjacentVertices;
    }

    public Iterable<Integer> adj(int v) {
        if (v >= V) {
            throw new IllegalArgumentException("Vertex " + v + " does not exist.");
        }

        Bag<Integer> adjacentVertices = new Bag<>();
        if (adj[v] != null) {
            for (int w : adj[v]) {
                adjacentVertices.add(w);
            }
        }
        return adjacentVertices;
    }

    /**
     * Returns the reverse of the graph.
     *
     * @return the reverse of the graph
     */
    public Graph reverse() {
        Graph reverse = new Graph(V);
        for (int v = 0; v < V; v++) {
            if (adj[v] != null) {
                for (int w : adj[v]) {
                    reverse.addDirectedEdge(indexToName.get(w), indexToName.get(v));
                }
            }
        }
        return reverse;
    }

    /**
     * Returns a string representation of the graph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     * followed by the <em>V</em> adjacency lists and the color of each vertex
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int v = 0; v < V; v++) {
            if (indexToName.get(v) != null) {
                String color = vertexColors.get(indexToName.get(v)) ? "Red" : "Black";
                s.append(String.format("%s (%s): ", indexToName.get(v), color));
                if (adj[v] != null) {
                    for (int w : adj[v]) {
                        s.append(String.format("%s ", indexToName.get(w)));
                    }
                }
                s.append(NEWLINE);
            }
        }
        return s.toString();
    }
}
