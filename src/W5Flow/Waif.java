package W5Flow;

import java.util.HashSet;
import java.util.Scanner;

/*
Denne opgave er udarbejdet fælles både i hånden og skrevet af Jacob (jacg) og Thor (tcla)


Vi bruger DSA fra Robert Sedgewick and Kevin Wayne: https://algs4.cs.princeton.edu/home/

Ideen bag denne flow graph: (max flow problem)

Vi har altid en source og en sink.
Dernæst har vi to lag af vertices, ligesom paintball:
0. "lag" er source
1. lag er børnene
2. lag er unikke legetøjskategori
3. "lag" er sink
Fra source til børn: Capacity = 1,  hvert barn kun kan få et legetøj.
Fra børn til legetøjkategori: Capacity = 1, hvert barn kan kun få et legetøj.
Fra legetøj til sink: Capacity = givet af opgaven, antal af de legetøj som eksisterer.
(Så selvom hvert legetøj kun kan bruges en gang, kan der være flere af samme legetøj)



        4 3 1		4 children. 3 toys. 1 category
        2 1 2		Barn 1 kan lide toy 1 og 2
        2 1 2		Barn 2 kan lide toy 1 og 2
        1 3		    Barn 3 kan lide toy 3
        1 3		    Barn 4 kan lide toy 3
        2 1 2 1		toy 1 og 2 kan bruges 1 gang
* */

public class Waif {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        /*
        * Comments all concern the test input:
4 3 1
2 1 2
2 1 2
1 3
1 3
2 1 2 1
        * */

        int children = sc.nextInt();  // Number of children
        int toys = sc.nextInt();      // Number of toys
        int categories = sc.nextInt(); // Number of toy categories

        int source = 0;  // Source node index
        int toyStartIndex = children + 1;
        int categoryStartIndex = toyStartIndex + toys;
        int sink = categoryStartIndex + categories; // Sink node index after all categories and toys

        // Initialize the flow network with enough nodes for children, toy categories, uncategorized toys, source, and sink
        FlowNetwork G = new FlowNetwork(sink + 1);

        // Add edges from source to each child with capacity 1
        for (int i = 1; i <= children; i++) {
            G.addEdge(new FlowEdge(source, i, 1));
        }
        //[toy] -> [category]
        //example: toy 1,2 maps to 0  and toy 3 maps to 1
        var uncategorizedToys = new HashSet<Integer>();

        //add edges from children to toys
        for (int child = 1; child <= children; child++) {
            int numberOfToysAChildLikes = sc.nextInt();
            for (int j = 0; j < numberOfToysAChildLikes; j++) {
                int toy = sc.nextInt();
                int toyNode = toyStartIndex + toy - 1; // Place uncategorized toys (-1 since toy is 1-indexed)
                uncategorizedToys.add(toy);
                G.addEdge(new FlowEdge(child, toyNode, 1));
            }
        }


        //put toys in categories
        for (int cat = 0; cat < categories; cat++) {
            int categoryNode = categoryStartIndex + cat;
            int numberOfToysInCat = sc.nextInt();
            for (int j = 0; j < numberOfToysInCat; j++) {
                int toy = sc.nextInt();
                G.addEdge(new FlowEdge(toy, categoryNode, 1));
                uncategorizedToys.remove(toy);
            }
            int weight = sc.nextInt();
            G.addEdge(new FlowEdge(categoryNode, sink, weight));
        }

        var uncategorizedToysArray = uncategorizedToys.toArray();
        for (int i = 0; i < uncategorizedToysArray.length; i++) {
            int toy = (int)uncategorizedToysArray[i];
            G.addEdge(new FlowEdge(toyStartIndex + toy - 1, sink, 1));
        }

        // Use the Ford-Fulkerson algorithm to compute the maximum flow from source to sink
        FordFulkerson maxflow = new FordFulkerson(G, source, sink);

        // Output the maximum number of children that can be satisfied
        System.out.println((int)maxflow.value());


        //print all outflow of all vertices
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdge e : G.adj(v)) {
                if ((v == e.from()) && e.flow() > 0){
                    //System.out.println(e.from() + " -> " + e.to() + " flow: " + e.flow());
                }
            }
        }
    }
}
