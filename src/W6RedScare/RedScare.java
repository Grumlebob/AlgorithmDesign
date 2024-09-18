package W6RedScare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class RedScare {
    private Graph graph;
    private String startVertex;
    private String endVertex;
    private Boolean isDirected = false;

    public static void main(String[] args) {
        // Path to the data directory within the W6RedScare directory
        File dataDirectory = new File("src/W6RedScare/data");

        RedScare redScare = new RedScare();
        redScare.processAllFiles(dataDirectory);
    }


    private int solveNone() {
        // Create a copy of the graph to avoid modifying the original
        Graph graphCopy = graph.copy();
        //TODO. Nedenunder er ikke helt korrekt. Hvis s og t er rød, så
        //SKAL MAN IKKE FJERNE DEM.
        //Give removeallred s and t, og
        graphCopy.removeAllRed();


        //Check if start and end is still in the graph, ie not red.
        Integer startIdx = graphCopy.nameToIndex.get(startVertex);
        Integer endIdx = graphCopy.nameToIndex.get(endVertex);

        if (startIdx == null || endIdx == null) {
            // One or both vertices have been removed; no path exists
            return -1;
        }

        SolveNone solver = new SolveNone(graphCopy, startIdx);
        return solver.distTo(endIdx);
    }

    private boolean solveSome() {
        Graph graphCopy = graph.copy();

        SolveSome solver = new SolveSome(graphCopy, startVertex, endVertex);
        return solver.solve();
    }


    private int solveMany() {
        // TODO: Implement solution for the "Many" problem.
        return -1; // Placeholder return value
    }

    private int solveFew() {
        // TODO: Implement solution for the "Few" problem.
        return -1; // Placeholder return value
    }

    private boolean solveAlternate() {
        // TODO: Implement solution for the "Alternate" problem.
        return false; // Placeholder return value
    }

    private void processAllFiles(File directory) {
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.out.println("No .txt files found in the directory.");
            return;
        }

        // Print table header
        System.out.printf("%-60s %-10s %-10s %-10s %-10s %-10s%n",
                "File Name", "None", "Some", "Many", "Few", "Alternate");
        System.out.println("-".repeat(110));

        for (File file : files) {
            try {
                readGraphFromFile(file);
                String fileName = file.getName();
                int noneResult = solveNone();
                boolean someResult = solveSome();
                int manyResult = solveMany();
                int fewResult = solveFew();
                boolean alternateResult = solveAlternate();

                // Create file name with vertex and edge counts
                String fileNameWithCounts = String.format("%s (V:%d - E:%d - D:%s)",
                        fileName, graph.getVertexCount(), graph.getEdgeCount(), isDirected ? 'T' : 'F');

                // Print results in table format
                System.out.printf("%-60s %-10d %-10b %-10d %-10d %-10b%n",
                        fileNameWithCounts, noneResult, someResult, manyResult, fewResult, alternateResult);

            } catch (FileNotFoundException e) {
                System.out.println("Error reading file: " + file.getName());
            }
        }
    }

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