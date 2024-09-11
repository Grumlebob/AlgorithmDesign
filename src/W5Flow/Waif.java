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


Comments referere til vores sample input:

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
         * Sample input:
         * 4 3 1
         * 2 1 2
         * 2 1 2
         * 1 3
         * 1 3
         * 2 1 2 1
         */

        int children = sc.nextInt();  // Number of children 4
        int toys = sc.nextInt();      // Number of toys 3
        int categories = sc.nextInt(); // Number of toy categories 1

        //Tldr layers are:
        // 0. source
        // 1. children
        // 2. toys
        // 3. categories
        // 4. sink
        int source = 0;
        int toyStartIndex = children + 1;
        int categoryStartIndex = toyStartIndex + toys;
        int sink = categoryStartIndex + categories;

        //init graph with space for all nodes +1 for the source.
        FlowNetwork G = new FlowNetwork(sink + 1);

        //cap: 1, each child can play with 1 toy
        for (int i = 1; i <= children; i++) {
            G.addEdge(new FlowEdge(source, i, 1));
        }

        //Toys in no category, (like toy 3 in our example)
        var uncategorizedToys = new HashSet<Integer>();

        // Add edges from children to their preferred toys with capacity 1
        for (int child = 1; child <= children; child++) {
            int numberOfToysAChildLikes = sc.nextInt();
            for (int j = 0; j < numberOfToysAChildLikes; j++) {
                int toy = sc.nextInt();
                int toyNode = toyStartIndex + toy - 1;
                uncategorizedToys.add(toy);  // Assume all toys are uncategorized initially
                G.addEdge(new FlowEdge(child, toyNode, 1));
            }
        }

        //Edges from toys to categories (and also from categories to sink)
        //Capacites are given by the input
        for (int cat = 0; cat < categories; cat++) {
            int categoryNode = categoryStartIndex + cat;
            int numberOfToysInCat = sc.nextInt();
            for (int j = 0; j < numberOfToysInCat; j++) {
                int toy = sc.nextInt();
                int toyNode = toyStartIndex + toy - 1;
                //Toy to category
                G.addEdge(new FlowEdge(toyNode, categoryNode, 1));
                //Remove toy from uncategorized set as it's categorized
                uncategorizedToys.remove(toy);
            }
            int maxUseForCategory = sc.nextInt();
            G.addEdge(new FlowEdge(categoryNode, sink, maxUseForCategory));
        }

        // All toys in no category, are connected directly to the sink with capacity 1
        for (int toy : uncategorizedToys) {
            int toyNode = toyStartIndex + toy - 1; // Adjust for node index
            G.addEdge(new FlowEdge(toyNode, sink, 1)); // Connect uncategorized toy to sink
        }

        // Use the Ford-Fulkerson algorithm to compute the maximum flow from source to sink
        FordFulkerson maxflow = new FordFulkerson(G, source, sink);

        // Output the maximum number of children that can be satisfied
        System.out.println((int) maxflow.value());
    }
}
