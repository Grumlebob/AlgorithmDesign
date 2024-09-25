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

    private class TraverseState {
        String currentNode;
        boolean hasEncounteredRedNodeInPath;
        Iterator<String> currentNodeNeighbours;

        TraverseState(String currentNode, boolean hasEncounteredRedNodeInPath) {
            this.currentNode = currentNode;
            this.hasEncounteredRedNodeInPath = hasEncounteredRedNodeInPath;
            this.currentNodeNeighbours = graph.adj(currentNode).iterator();
        }
    }

    public ResultWithBoolInfo findPathWithRedNodeDfs(long startTime, long timeLimit) {
        //FIFO stack for DFS traversal
        Stack<TraverseState> dfsTraversalStack = new Stack<>();
        //Nodes where all neighbor sub-graphs have been fully explored
        Set<String> fullyExploredGraphNodes = new HashSet<>();
        //"Marked" nodes in current exploration path
        Set<String> nodesInCurrentPath = new HashSet<>();

        // Initialize with the start vertex
        boolean isStartVertexRed = graph.vertexColors.get(startVertex);
        dfsTraversalStack.push(new TraverseState(startVertex, isStartVertexRed));
        nodesInCurrentPath.add(startVertex);

        while (!dfsTraversalStack.isEmpty()) {
            if (System.currentTimeMillis() - startTime > timeLimit) {
                // Time limit exceeded; return false
                return ResultWithBoolInfo.TIMEOUT;
            }

            TraverseState currentTraverseState = dfsTraversalStack.peek();
            String currentNode = currentTraverseState.currentNode;
            boolean redNodeEncountered = currentTraverseState.hasEncounteredRedNodeInPath;

            // Base case: if current node is the end vertex
            if (currentNode.equals(endVertex)) {
                if (redNodeEncountered) {
                    return ResultWithBoolInfo.TRUE;
                }
                // Backtracking logic
                dfsTraversalStack.pop();
                // We make current path, available for exploration again from other nodes
                nodesInCurrentPath.remove(currentNode);
                // Mark current node as fully explored, so it won't be explored again
                fullyExploredGraphNodes.add(currentNode);
                continue;
            }

            // Explore neighbors
            if (currentTraverseState.currentNodeNeighbours.hasNext()) {
                String neighborNode = currentTraverseState.currentNodeNeighbours.next();
                if (!nodesInCurrentPath.contains(neighborNode) && (!fullyExploredGraphNodes.contains(neighborNode) || redNodeEncountered)) {
                    boolean neighborNodeHasEncounteredRedNodeInPath = redNodeEncountered || graph.vertexColors.get(neighborNode);
                    // Push neighbor to stack
                    dfsTraversalStack.push(new TraverseState(neighborNode, neighborNodeHasEncounteredRedNodeInPath));
                    nodesInCurrentPath.add(neighborNode);
                }
            } else {
                // Backtrack: No more neighbors to explore from current node
                dfsTraversalStack.pop();
                nodesInCurrentPath.remove(currentNode);
                fullyExploredGraphNodes.add(currentNode);
            }
        }

        //No valid path with a red node found
        return ResultWithBoolInfo.FALSE;
    }

    public static void VerifyEdgecaseResults(String filename, ResultWithBoolInfo result) {
        switch (filename) {
            case "edgecase-solveNone-blackSrcToRedSink.txt",
                 "edgecase-solveNone-redSrcToBlackSink.txt",
                 "edgecase-solveSome-simple-example.txt",
                 "increase-n8-1.txt",
                 "G-ex.txt",
                 "P3.txt",
                 "rusty-1-17.txt",
                 "grid-5-0.txt",
                 "ski-illustration.txt":
                if (result != ResultWithBoolInfo.TRUE) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected true, got " + result);
                    System.out.println("------------");
                }
                break;

            case "wall-z-3.txt":
                if (result != ResultWithBoolInfo.FALSE) {
                    System.out.println("------------");
                    System.out.println("ERROR: Expected false, got " + result);
                    System.out.println("------------");
                }
                break;

        }
    }

}
