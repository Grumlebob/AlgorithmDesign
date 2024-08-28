package W5Flow;

import java.util.HashMap;
import java.util.Scanner;

//Løste denne opgave i Applied algoritmer faget (Algorithmic Problem Solving, BSc (Spring 2023))

//Logikken er at alle shooters får en capacity på 1 fra source.
//Dette giver deres ene ammo som de har.
//All capacity imellem en shooter og target er sat til 9999 så algoritmen kan sætte værdier op og ned
//Men bare rolig, et target har så en capacity på 1 hen til sink. Hvilket gør, at hvert target
//kun kan blive skudt 1 gang.

//Jeg bruger DSA fra Robert Sedgewick and Kevin Wayne: https://algs4.cs.princeton.edu/home/

public class Paintball {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int players = sc.nextInt();
        int amountWhoCanSeeEachOther = sc.nextInt();


        int source = 0; //Source vertex
        int sink = (players*2)+1; //Target / sink vertex

        if (players > amountWhoCanSeeEachOther) {
            System.out.println("Impossible");
            return;
        }

        //Example:
        //source = 0.
        // Player(shooter) 1,2,3,
        // Player(Target) 4,5,6
        // sink = 7.
        // Thus we need space for two times the amount of players, one for role shooter one for role target
        //+ 2 for source and sink.
        FlowNetwork G = new FlowNetwork((players*2)+2);

        for (int i = 1; i < amountWhoCanSeeEachOther+1; i++) {
            //The first players are the shooters, the second players are the targets.
            if (i <= players) {
                //Add edges from source to all players, with capacity 1 (one shot)
                G.addEdge(new FlowEdge(source, i, 1));
                //Add edges from all players to sink with capacity 1 (one shot)
                G.addEdge(new FlowEdge(i+players, sink, 1));
            }
            //Add edges between players who can see each other
            int a = sc.nextInt();
            int b = sc.nextInt();
            var edgeOneWay = new FlowEdge(a,(b+players),999999);
            var edgeOtherWay = new FlowEdge(b,(a+players),99999);
            G.addEdge(edgeOneWay);
            G.addEdge(edgeOtherWay);
        }

        //Map of [shooter] -> [target]
        var resultMap = new HashMap<Integer, Integer>();
        FordFulkerson maxflow = new FordFulkerson(G, source, sink);
        //We iterate through all edges and check if the flow is greater than 0.
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdge e : G.adj(v)) {
                if ((v == e.from()) && e.flow() > 0)
                    //If it is greater than 0, then we know that the player has killed another player.
                    if (e.to() > players && e.to() != sink && e.from() != source) {
                        //System.out.println(e.from()+"<-Shooter - Killed->:" + (e.to() - players));
                        resultMap.put(e.from(), e.to() - players);
                    }
            }
        }


        //If everyone has killed someone, then we print the result.
        if (maxflow.value() == players) {
            //Assignment want us to print shooter i, killed target i.
            for (int i = 1; i < players+1; i++) {
                System.out.println(resultMap.get(i));
            }
        } else {
            //otherwise assignment requires us to print impossible.
            System.out.println("Impossible");
        }

        //Print maxflow
        System.out.println("Max flow from source to sink: " + maxflow.value());

    }
}