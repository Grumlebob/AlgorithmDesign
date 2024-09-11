package W6RedScare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;



/*
Short version of the problem:

Unweighted
Both directed and undirected
No parallel edges

simple path: no repeated vertices (så ex, kan man ikke lave et spanning tree, og blot vandrer frem og tilbage og ramme alle vertices)


Always has start and end vertices (Can also be red)


Data format:
n m r       //n vertices, m edges, r red vertices
s t         //start and end vertices
<vertices>
black: BOB
red: BOB *
<edges>
undirected: u -- v
directed: u -> v

*
* */
public class RedScare {
    public static void main(String[] args) throws FileNotFoundException {

        // Path to the data directory within the W6RedScare directory
        File dataDirectory = new File("src/W6RedScare/data");

        // Constructing the path to the specific file within the data directory
        File inputFile = new File(dataDirectory, "increase-n8-1.txt");

        System.out.println("Reading file: " + inputFile.getAbsolutePath());

        Scanner scanner = new Scanner(new FileReader(inputFile));

        int countOfVertices = scanner.nextInt();
        int countOfEdges = scanner.nextInt();
        int countOfRedVertices = scanner.nextInt();
        int startVertex = scanner.nextInt();
        int endVertex = scanner.nextInt();
        scanner.nextLine();

        Graph graph = new Graph(countOfVertices);
        Graph redFreeGraph = new Graph(countOfVertices);

        for (int i = 0; i < countOfVertices; i++) {
            String vertexLine = scanner.nextLine();
            String[] vertexParts = vertexLine.split(" ");
            // Black vertex
            if (vertexParts.length == 1) {
                graph.addVertex(vertexParts[0], false);
                redFreeGraph.addVertex(vertexParts[0], false);
            }
            // Red vertex
            else {
                graph.addVertex(vertexParts[0], true);
                redFreeGraph.addVertex(vertexParts[0], true);
            }
        }

        for (int i = 0; i < countOfEdges; i++) {
            String edgeLine = scanner.nextLine();
            String[] edgeParts = edgeLine.split(" ");
            var from = edgeParts[0];
            var to = edgeParts[2];
            var edgeType = edgeParts[1];
            if (edgeType.equals("->")) {
                graph.addDirectedEdge(from, to);
            } else {
                graph.addUndirectedEdge(from, to);
            }
        }

        redFreeGraph.removeAllRed();

    }

    public void solveProblem(String problem) {
        switch (problem) {
            case "none":
                solveNone();
                break;
            case "some":
                solveSome();
                break;
            case "many":
                solveMany();
                break;
            case "few":
                solveFew();
                break;
            case "alternate":
                solveAlternate();
                break;
            default:
                System.out.println("Invalid problem type. Choose from: none, some, many, few, alternate.");
        }
    }


    /*
None Return the length of a shortest s, t-path internally avoiding
R. To be precise, let P be the set of s, t-paths v1, . . . , vl such that
vi /2 R if 1 < i < l. Let l(p) denote the length of a path p. Return
minf l(p) : p 2 P g. If no such path exists, return ‘-1’. Note that the
edge st, if it exists, is an s, t-path with l = 2. Thus, if st 2 E(G)
then the answer is 1, no matter the colour of s or t. In Gex, the
answer is 3 (because of the path 0, 1, 2, 3.)

Tl:dr shortest path avoiding red
*/
    private void solveNone() {
        // TODO: Implement solution for the "None" problem.
        System.out.println("Solving 'None' problem...");
    }

    /*
Some Return ‘true’ if there is a path from s to t that includes at least
one vertex from R. Otherwise, return ‘false.’ In Gex, the answer is
‘yes’ (in fact, two such paths exist: the path 0, 4, 3 and the path 0,
5, 6, 7, 3.)

tl:dr Is there a path, atleast 1 red
    * */
    private void solveSome() {
        // TODO: Implement solution for the "Some" problem.
        System.out.println("Solving 'Some' problem...");
    }

    /*
Many Return the maximum number of red vertices on any path from
s to t. To be precise, let P be the set of s, t-paths and let r(p) denote
the number of red vertices on a path p. Return maxf r(p) : p 2 P g.
If no path from s to t exists, return ‘-1’. In Gex, the answer is ‘2’
(because of the path 0, 5, 6, 7, 3.)

tl:dr max red vertices on path
    * */
    private void solveMany() {
        // TODO: Implement solution for the "Many" problem.
        System.out.println("Solving 'Many' problem...");
    }

    /*
Few Return the minimum number of red vertices on any path from s
to t. To be precise, let P be the set of s, t-paths and let r(p) denote
red scare! 2
the number of red vertices on a path p. Return minf r(p) : p 2 P g.
If no path from s to t exists, return ‘-1’. In Gex, the answer is 0
(because of the path 0, 1, 2, 3.)

tl:dr min red vertices on path

*/

    private void solveFew() {
        // TODO: Implement solution for the "Few" problem.
        System.out.println("Solving 'Few' problem...");
    }

/*
* Alternate Return ‘true’ if there is a path from s to t that alternates
between red and non-red vertices. To be precise, a path v1, . . . , vl is
alternating if for each i 2 f1, . . . , l 􀀀 1g, exactly one endpoint of the
edge vivi+1 is red. Otherwise, return ‘false.’ In Gex, the answer is
‘yes’ (because of the path 0, 5, 6, 7, 3.)

tl:dr: simple path with alternating red and non-red vertices
* */

    private void solveAlternate() {
        // TODO: Implement solution for the "Alternate" problem.
        System.out.println("Solving 'Alternate' problem...");
    }
}
