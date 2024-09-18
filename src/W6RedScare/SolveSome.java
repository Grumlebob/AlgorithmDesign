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

    public boolean solve() {
        return dfsSimplePathIterative();
    }

    private class NodeState {
        String currentNodeId;
        boolean hasEncounteredRedNodeInPath;
        Iterator<String> nodeNeighbours;

        NodeState(String currentNodeId, boolean hasEncounteredRedNodeInPath) {
            this.currentNodeId = currentNodeId;
            this.hasEncounteredRedNodeInPath = hasEncounteredRedNodeInPath;
            this.nodeNeighbours = graph.adj(currentNodeId).iterator();
        }
    }

    private boolean dfsSimplePathIterative() {
        //FIFO stack for DFS traversal
        Stack<NodeState> dfsTraversalStack = new Stack<>();
        Set<String> fullyExploredGraphNodes = new HashSet<>();
        Set<String> graphNodesInCurrentExplorationPath = new HashSet<>();

        // Initialize with the start vertex
        boolean initialNodeHasEncounteredRedNodeInPath = graph.vertexColors.getOrDefault(startVertex, false);
        dfsTraversalStack.push(new NodeState(startVertex, initialNodeHasEncounteredRedNodeInPath));
        graphNodesInCurrentExplorationPath.add(startVertex);

        while (!dfsTraversalStack.isEmpty()) {
            NodeState currentTraversalNodeState = dfsTraversalStack.peek();
            String currentGraphNodeIdentifier = currentTraversalNodeState.currentNodeId;
            boolean hasEncounteredRedNodeInPath = currentTraversalNodeState.hasEncounteredRedNodeInPath;

            // Base case: if current node is the end vertex
            if (currentGraphNodeIdentifier.equals(endVertex)) {
                if (hasEncounteredRedNodeInPath) {
                    return true;
                }
                // Backtracking logic
                dfsTraversalStack.pop();
                // We make current path, available for exploration again from other nodes
                graphNodesInCurrentExplorationPath.remove(currentGraphNodeIdentifier);
                // Mark current node as fully explored, so it won't be explored again
                fullyExploredGraphNodes.add(currentGraphNodeIdentifier);
                continue;
            }

            // Explore neighbors
            if (currentTraversalNodeState.nodeNeighbours.hasNext()) {
                String neighborGraphNodeIdentifier = currentTraversalNodeState.nodeNeighbours.next();
                if (!graphNodesInCurrentExplorationPath.contains(neighborGraphNodeIdentifier) && !fullyExploredGraphNodes.contains(neighborGraphNodeIdentifier)) {
                    boolean neighborNodeHasEncounteredRedNodeInPath = hasEncounteredRedNodeInPath || graph.vertexColors.getOrDefault(neighborGraphNodeIdentifier, false);
                    // Push neighbor to stack
                    dfsTraversalStack.push(new NodeState(neighborGraphNodeIdentifier, neighborNodeHasEncounteredRedNodeInPath));
                    graphNodesInCurrentExplorationPath.add(neighborGraphNodeIdentifier);
                }
            } else {
                // Backtrack: No more neighbors to explore from current node
                dfsTraversalStack.pop();
                graphNodesInCurrentExplorationPath.remove(currentGraphNodeIdentifier);
                fullyExploredGraphNodes.add(currentGraphNodeIdentifier);
            }
        }

        //No valid path with a red node found
        return false;
    }
}
