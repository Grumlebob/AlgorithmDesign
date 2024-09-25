package W6RedScare;

import Shared.Bag;
import Shared.Queue;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * Modified version of the Graph class to handle vertices with string names, both directed and undirected edges, and vertex coloring.
 */
public class Graph {
    private static final String NEWLINE = System.getProperty("line.separator");
    public boolean isDirected;
    public int countOfVertices; // number of vertices in this graph
    public int countOfEdgesCalculated;       // number of edges in this graph
    public int countOfEdgesAssignment;
    public int countOfRedVertices;
    public Bag<Integer>[] adj;    // adj[v] = adjacency list for vertex v
    public int[] indegree;        // indegree[v] = indegree of vertex v
    public HashMap<String, Integer> nameToIndex; // map from vertex name to index
    public ArrayList<String> indexToName;        // map from index to vertex name

    //(true = Red, false = Black)
    public HashMap<String, Boolean> vertexColors; // map from vertex name to its color


    /**
     * Initializes an empty graph with a given number of vertices.
     *
     * @param countOfVertices the number of vertices
     */
    public Graph(int countOfVertices, int countOfEdgesAssignment, int countOfRedVertices) {
        this.countOfVertices = countOfVertices;
        this.countOfEdgesCalculated = 0;
        this.countOfEdgesAssignment = countOfEdgesAssignment;
        this.countOfRedVertices = countOfRedVertices;
        indegree = new int[countOfVertices];
        adj = (Bag<Integer>[]) new Bag[countOfVertices];
        for (int v = 0; v < countOfVertices; v++) {
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
        countOfEdgesCalculated++;
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
        return countOfVertices;
    }

    public int getEdgeCountCalculated() {
        return countOfEdgesCalculated;
    }

    public int getEdgeCountAssignment() {
        return countOfEdgesAssignment;
    }

    public int GetCountOfRedVertices() {
        return countOfRedVertices;
    }


    public Graph copy() {
        Graph copyGraph = new Graph(this.countOfVertices, this.countOfEdgesAssignment, this.countOfRedVertices);
        copyGraph.isDirected = this.isDirected;
        // Copy vertex mappings and colors
        copyGraph.nameToIndex = new HashMap<>(this.nameToIndex);
        copyGraph.indexToName = new ArrayList<>(this.indexToName);
        copyGraph.vertexColors = new HashMap<>(this.vertexColors);

        // Copy adjacency lists
        copyGraph.adj = (Bag<Integer>[]) new Bag[this.countOfVertices];
        for (int v = 0; v < this.countOfVertices; v++) {
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
        copyGraph.indegree = new int[this.countOfVertices];
        System.arraycopy(this.indegree, 0, copyGraph.indegree, 0, this.countOfVertices);

        // Copy the edge count
        copyGraph.countOfEdgesCalculated = this.countOfEdgesCalculated;

        return copyGraph;
    }



    /**
     * USED FOR SOLVENONE, where we remove red vertices from the graph
     * Removes all red vertices from the graph and updates all relevant data structures.
     */
    public void removeAllRed(int s, int t) {
        // Create a boolean array to mark red vertices
        boolean[] isRedVertex = new boolean[countOfVertices];
        for (int v = 0; v < countOfVertices; v++) {
            String vertexName = indexToName.get(v);
            if (vertexName != null && vertexColors.get(vertexName)) {
                isRedVertex[v] = true;
            }
        }

        // Do not remove s and t even if they are red
        isRedVertex[s] = false;
        isRedVertex[t] = false;

        // Update adjacency lists to exclude edges to red vertices
        for (int v = 0; v < countOfVertices; v++) {
            if (adj[v] != null && !isRedVertex[v]) {
                Bag<Integer> newAdjList = new Bag<>();
                for (int w : adj[v]) {
                    if (!isRedVertex[w]) {
                        newAdjList.add(w);
                    } else {
                        // Update indegree of the red vertex since we're removing an incoming edge
                        indegree[w]--;
                        countOfEdgesCalculated--; // Decrement edge count
                    }
                }
                adj[v] = newAdjList;
            } else if (isRedVertex[v]) {
                adj[v] = null; // Remove adjacency list for red vertices
            }
        }

        // Remove red vertices from data structures, except s and t
        for (int v = 0; v < countOfVertices; v++) {
            if (isRedVertex[v] && v != s && v != t) {
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
        if (v >= countOfVertices) {
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
     * Returns a string representation of the graph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     * followed by the <em>V</em> adjacency lists and the color of each vertex
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(countOfVertices + " vertices, " + countOfEdgesCalculated + " edges " + NEWLINE);
        for (int v = 0; v < countOfVertices; v++) {
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

    //Topological sorting, Inspired by Kahns Algortihm
    //https://www.geeksforgeeks.org/topological-sorting-indegree-based-solution/

    /**
     * Performs a topological sort on the graph and checks if it is a DAG (Directed Acyclic Graph).
     * If the graph contains a cycle, it is not a DAG and the method returns null.
     * Otherwise, it returns an ordered list of vertices in topological order.
     *
     * @return A list of vertex names in topological order if the graph is a DAG, null otherwise.
     */
    public ArrayList<String> topologicalSortDirected() {
        // Create an array to keep track of the in-degree of each vertex
        int[] inDegree = new int[countOfVertices];
        System.arraycopy(indegree, 0, inDegree, 0, countOfVertices); // Copy the in-degree values

        // Queue for vertices with no incoming edges (in-degree 0)
        ArrayList<String> topologicalOrder = new ArrayList<>();
        Queue<Integer> zeroInDegreeQueue = new Queue<>();

        // Add all vertices with in-degree 0 to the queue
        for (int v = 0; v < countOfVertices; v++) {
            if (inDegree[v] == 0 && indexToName.get(v) != null) {
                zeroInDegreeQueue.enqueue(v);
            }
        }

        // Process vertices with in-degree 0
        while (!zeroInDegreeQueue.isEmpty()) {
            int v = zeroInDegreeQueue.dequeue();
            topologicalOrder.add(indexToName.get(v));

            // Reduce in-degree of all adjacent vertices
            for (int w : adj[v]) {
                inDegree[w]--;
                // If in-degree becomes 0, add the vertex to the queue
                if (inDegree[w] == 0) {
                    zeroInDegreeQueue.enqueue(w);
                }
            }
        }

        // If topological order includes all vertices, the graph is a DAG
        if (topologicalOrder.size() == nameToIndex.size()) {
            return topologicalOrder;
        } else {
            // The graph has a cycle and is not a DAG
            return null;
        }
    }
}
