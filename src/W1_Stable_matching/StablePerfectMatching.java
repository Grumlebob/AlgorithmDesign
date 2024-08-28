package W1_Stable_matching;

/*Notes:
r: rejecter / women
p: proposer / men
S: matching
P: proposers
R: rejecters

Bipartite check is not necessary for this problem. It is given that the input is bipartite for subtask 2 and 3.
Did it to learn how to check bipartite graph.
 */

import java.util.*;

public class StablePerfectMatching {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt(); // Number of vertices
        scanner.nextInt(); // Discard: Number of edges
        scanner.nextLine(); // Consume the newline character

        MatchingGraph graph = new MatchingGraph(n);

        // Read vertex preferences from input
        for (int i = 0; i < n; i++) {
            String[] input = scanner.nextLine().split(" ");
            String vertexName = input[0];
            List<String> prefList = Arrays.asList(Arrays.copyOfRange(input, 1, input.length));
            graph.addVertex(vertexName, prefList, i, n); // Pass index and total number of vertices
        }

        // Check if the graph is bipartite
        if (!graph.isBipartite()) {
            System.out.println("-");
        } else {
            // Find stable perfect matching
            List<String[]> result = graph.findStablePerfectMatching();

            if (result == null) {
                System.out.println("-");
            } else {
                for (String[] pair : result) {
                    System.out.println(pair[0] + " " + pair[1]);
                }
            }
        }

        scanner.close();
    }
}

class Vertex {
    public final String name;
    public List<String> preferences;
    private final Map<String, Integer> preferenceRank;
    private int currentProposalIndex; // Index of the next proposal to be made

    public Vertex(String name, List<String> preferences) {
        this.name = name;
        this.preferences = preferences;
        this.preferenceRank = new HashMap<>();
        this.currentProposalIndex = 0;

        // Fill the hash map with preference ranking for O(1) access
        for (int i = 0; i < preferences.size(); i++) {
            preferenceRank.put(preferences.get(i), i);
        }
    }

    // p proposes to the next r on its list
    public String getNextProposal() {
        if (currentProposalIndex < preferences.size()) {
            return preferences.get(currentProposalIndex++);
        }
        return null;
    }

    // Check if r prefers p over its current partner p'
    public boolean prefers(String newPartner, String currentPartner) {
        return preferenceRank.get(newPartner) < preferenceRank.get(currentPartner);
    }
}

class MatchingGraph {
    private final Map<String, Vertex> proposers; // Set P: proposers
    private final Map<String, Vertex> rejecters; // Set R: rejecters
    private final Map<String, String> matches; // S: matching

    public MatchingGraph(int n) {
        this.proposers = new HashMap<>(n / 2);
        this.rejecters = new HashMap<>(n / 2);
        this.matches = new HashMap<>(n); // Start with an empty matching S
    }

    // Adding vertices to proposers or rejecters based on index
    public void addVertex(String name, List<String> preferences, int index, int n) {
        Vertex vertex = new Vertex(name, preferences);
        if (index < n / 2) {
            proposers.put(name, vertex); // Add to proposers
        } else {
            rejecters.put(name, vertex); // Add to rejecters
        }
    }

    // Optional: Check if the graph is bipartite (not required for subtask 2 and 3)
    public boolean isBipartite() {
        Map<String, Integer> color = new HashMap<>();
        Queue<String> queue = new LinkedList<>();

        for (String vertex : proposers.keySet()) {
            if (!color.containsKey(vertex)) {
                color.put(vertex, 0);
                queue.offer(vertex);

                while (!queue.isEmpty()) {
                    String current = queue.poll();
                    Vertex currentVertex = proposers.get(current);

                    if (currentVertex == null) continue;

                    for (String neighbor : currentVertex.preferences) {
                        if (!color.containsKey(neighbor)) {
                            color.put(neighbor, 1 - color.get(current));
                            queue.offer(neighbor);
                        } else if (color.get(neighbor).equals(color.get(current))) {
                            return false; // Found the same color in adjacent vertices, therefore not bipartite
                        }
                    }
                }
            }
        }

        return true; // No same color found in adjacent vertices, therefore bipartite
    }

    public List<String[]> findStablePerfectMatching() {
        Queue<Vertex> unmatchedProposers = new LinkedList<>(proposers.values()); // While some proposer p is unmatched

        while (!unmatchedProposers.isEmpty()) { // while some proposer p is unmatched
            Vertex proposer = unmatchedProposers.poll(); // p
            String preferred = proposer.getNextProposal(); // r ← first rejecter on p’s list

            if (preferred == null) {
                continue; // No more proposals possible for this proposer
            }

            if (!matches.containsKey(preferred)) { // if r is unmatched
                addMatchesBothWays(proposer.name, preferred); // add p–r to S (“p and r are engaged”)
            } else {
                String currentPartner = matches.get(preferred); // Current partner p' of r
                Vertex rejecter = rejecters.get(preferred); // r

                if (rejecter == null) continue; // Safeguard against non-bipartite graph scenarios

                if (rejecter.prefers(proposer.name, currentPartner)) { // else if r prefers p to current partner p'
                    addMatchesBothWays(proposer.name, preferred); // replace p'–r with p–r in matching M (“r dumps p' for p”)
                    unmatchedProposers.add(proposers.get(currentPartner)); // p' becomes unmatched
                } else {
                    unmatchedProposers.add(proposer); // else skip (“r rejects p”)
                }
            }
        }

        // Return stable matching S
        if (matches.size() == proposers.size() + rejecters.size()) {
            List<String[]> result = new ArrayList<>();
            Set<String> added = new HashSet<>();
            for (Map.Entry<String, String> pair : matches.entrySet()) {
                String vertex1 = pair.getKey();
                String vertex2 = pair.getValue();
                if (!added.contains(vertex1) && !added.contains(vertex2)) {
                    result.add(new String[]{vertex1, vertex2});
                    added.add(vertex1);
                    added.add(vertex2);
                }
            }
            return result;
        } else {
            return null;
        }
    }

    // Helper function to add matches both ways
    private void addMatchesBothWays(String proposer, String rejecter) {
        matches.put(proposer, rejecter);
        matches.put(rejecter, proposer);
    }
}
