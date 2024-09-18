package W6RedScare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class RedScare {
    private Graph graph;
    private String startVertex;
    private Integer startVertexInt;
    private String endVertex;
    private Integer endVertexInt;
    private Boolean isDirected = false;

    public static void main(String[] args) {
        File dataDirectory = new File("src/W6RedScare/data");
        RedScare redScare = new RedScare();
        redScare.processAllFiles(dataDirectory);
    }


    private int solveNone(String filename) {
        //Make a copy unaffected by the other methods
        Graph graphCopy = graph.copy();
        //Our interpretation of the assignment text:
        // If start or end vertex is red, it should be treated as black
        //handled in remove all red
        graphCopy.removeAllRed(startVertexInt, endVertexInt);
        SolveNone solver = new SolveNone(graphCopy, startVertexInt);
        int result = solver.distTo(endVertexInt);
        SolveNone.VerifyEdgecaseResults(filename, result);
        return result;
    }

    /*

    See image SolveNone_example_Should_be_true.jpg for understanding.
    This image made us realise, that this problem is NP-hard.


    * The algorithm may need to explore all possible simple paths from s to t to
    * determine whether any of them includes at least one red node.
    *
    *  the number of simple paths can be up to O(n!)

    * */
    private ResultWithInfo solveSome(String filename) {
        // since some instances may take a long time to solve
        // we will limit the time to x seconds
        long startTime = System.currentTimeMillis();
        long timeLimit = 10 * 1000; // x secs
        Graph graphCopy = graph.copy();
        SolveSome solver = new SolveSome(graphCopy, startVertex, endVertex);
        var result = solver.findPathWithRedNodeDfs(startTime, timeLimit);
        SolveSome.VerifyEdgecaseResults(filename, result);
        return result;
    }

    /*
    https://en.wikipedia.org/wiki/Longest_path_problem
    * NP-hard.
    * Can be reduced to a Longest Path Problem, with instead of distTo
    * we have a redVerticesTo
    Longest Path Problem is NP-hard in general graphs with cycles
     */
    private int solveMany() {

        Graph graphCopy = graph.copy();
        //Check if DAG
        //if dag, we can use X algorithm
        //else we can use Y algorithm which runs in (something like O(2^n))

        return -1;

    }

    /*

    * we use a specialized BFS:
    * https://www.geeksforgeeks.org/0-1-bfs-shortest-path-binary-graph/
    Basically a dijkstra shortest path where edges going to black has weight 0
    and going to red has weight 1.
    Even if there are cycles, because there is no negative weight cycles
* (Weight is either 0 (black) or red (1)), we can use a modified BFS,
* and find shortest path as that path would have fewest red vertices
*
*
* TODO implement i verifyEdgecaseResults
* Another point:
* If SolveNone returns a path,
* then SolveFew will also return a path with 0 red vertices!
*
* If SolveSome returns false, then SolveFew will also return -1
*
    * */
    private int solveFew(String filename) {
        Graph graphCopy = graph.copy();
        SolveFew solver = new SolveFew(graphCopy, startVertexInt);
        var result = solver.minRedVerticesToV(endVertexInt);
        SolveFew.VerifyEdgecaseResults(filename, result);
        return result;
    }

    /*
 Uses a modified BFS, that checks color constraints on edges.
 checks color of v, then color of w.
 only if they alternate, we proceed to handle the edge and enqueue the vertex w.
    * */
    private boolean solveAlternate(String filename) {
        Graph graphCopy = graph.copy();
        var result = new SolveAlternate(graphCopy, startVertexInt).distTo(endVertexInt) != -1;
        SolveAlternate.VerifyEdgecaseResults(filename, result);
        return result;
    }

    private void processAllFiles(File directory) {
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        //HEADER
        System.out.printf("%-60s %-10s %-10s %-10s %-10s %-10s%n",
                "File Name", "None", "Some", "Many", "Few", "Alternate");
        System.out.println("-".repeat(110));

        for (File file : files) {
            try {
                readGraphFromFile(file);
                String fileName = file.getName();
                startVertexInt = graph.nameToIndex.get(startVertex);
                endVertexInt = graph.nameToIndex.get(endVertex);
                int noneResult = solveNone(fileName);
                ResultWithInfo someResult = solveSome(fileName);
                int manyResult = solveMany();
                int fewResult = solveFew(fileName);
                boolean alternateResult = solveAlternate(fileName);

                //Format filename + additional input info for the file
                String fileNameWithCounts = String.format("%s (V:%d - E:%d - D:%s)",
                        fileName, graph.getVertexCount(), graph.getEdgeCount(), isDirected ? 'T' : 'F');

                //Result of the file for all methods.
                System.out.printf("%-60s %-10d %-10s %-10d %-10d %-10b%n",
                        fileNameWithCounts, noneResult, someResult.toString(), manyResult, fewResult, alternateResult);

            } catch (FileNotFoundException e) {
                System.out.println("Error reading file: " + file.getName());
            }
        }
    }

    //Parsing of input.
    private void readGraphFromFile(File inputFile) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(inputFile));

        int countOfVertices = scanner.nextInt();
        int countOfEdges = scanner.nextInt();
        int countOfRedVertices = scanner.nextInt();
        startVertex = scanner.next();
        endVertex = scanner.next();
        scanner.nextLine();

        graph = new Graph(countOfVertices);

        for (int i = 0; i < countOfVertices; i++) {
            String vertexLine = scanner.nextLine();
            String[] vertexParts = vertexLine.split(" ");
            graph.addVertex(vertexParts[0], vertexParts.length > 1);
        }

        for (int i = 0; i < countOfEdges; i++) {
            String edgeLine = scanner.nextLine();
            String[] edgeParts = edgeLine.split(" ");
            String from = edgeParts[0];
            String to = edgeParts[2];
            String edgeType = edgeParts[1];
            if (edgeType.equals("->")) {
                graph.addDirectedEdge(from, to);
                isDirected = true;
            } else {
                graph.addUndirectedEdge(from, to);
                isDirected = false;
            }
        }

        scanner.close();
    }
}