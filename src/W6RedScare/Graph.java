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
    private HashMap<String, Integer> nameToIndex; // map from vertex name to index
    private ArrayList<String> indexToName;        // map from index to vertex name

    private HashMap<String, Boolean> vertexColors; // map from vertex name to its color (true = Red, false = Black)

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

    /**
     * Removes all red vertices from the graph and updates all relevant data structures.
     */
    public void removeAllRed() {
        // Create a list of red vertices to be removed
        ArrayList<Integer> redVertexIndices = new ArrayList<>();
        for (String vertex : vertexColors.keySet()) {
            if (vertexColors.get(vertex)) { // true means Red
                redVertexIndices.add(nameToIndex.get(vertex));
            }
        }

        // For each red vertex index, remove its adjacency list and remove it from other adjacency lists
        for (int redIndex : redVertexIndices) {
            // Remove all edges from other vertices to this red vertex
            for (int v = 0; v < V; v++) {
                if (adj[v] != null && v != redIndex) {
                    Bag<Integer> newAdjList = new Bag<>();
                    for (int w : adj[v]) {
                        if (w != redIndex) {
                            newAdjList.add(w); // Only keep non-red vertices
                        }
                    }
                    adj[v] = newAdjList; // Replace with the updated adjacency list
                }
            }

            // Remove the red vertex from all data structures
            String redVertexName = indexToName.get(redIndex);
            nameToIndex.remove(redVertexName);
            indexToName.set(redIndex, null); // Mark the index as null in the indexToName list
            vertexColors.remove(redVertexName);
            adj[redIndex] = null; // Clear adjacency list for the red vertex
            indegree[redIndex] = 0;
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

    /**
     * Unit tests the {@code Graph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Graph G = new Graph(5);
        G.addVertex("A", true); // Add vertex A and paint it Red
        G.addVertex("B", false); // Add vertex B and paint it Black
        G.addVertex("C", true); // Add vertex C and paint it Red
        G.addVertex("D", false); // Add vertex D and paint it Black
        G.addVertex("E", true); // Add vertex E and paint it Red

        G.addUndirectedEdge("A", "B");
        G.addDirectedEdge("A", "C");
        G.addUndirectedEdge("B", "D");
        G.addDirectedEdge("C", "D");
        G.addDirectedEdge("D", "E");

        StdOut.println("Before removing all red vertices:");
        StdOut.println(G);

        G.removeAllRed();

        StdOut.println("After removing all red vertices:");
        StdOut.println(G);
    }
}
