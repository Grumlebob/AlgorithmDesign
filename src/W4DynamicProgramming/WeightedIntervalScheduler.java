package W4DynamicProgramming;

import java.util.Arrays;
import java.util.Scanner;

class Interval implements Comparable<Interval> {
    int start;
    int end;
    int weight;

    public Interval(int start, int end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }


    //Ligesom almindelig Scheduling problem, så sortere vi efter sluttider
    //Dog har de samme sluttid, sortere vi efter weight descending,
    //så vi får den højeste weight.
    @Override
    public int compareTo(Interval other) {
        if (this.end == other.end) {
            return other.weight - this.weight; // Descending order for weight
        }
        return this.end - other.end;
    }
}

public class WeightedIntervalScheduler {
    private Interval[] intervals;

    public WeightedIntervalScheduler(Interval[] intervals) {
        this.intervals = intervals;
    }

    //Da listen af intervaller er sorteret
    //Kan vi bruge binary search, for at finde en forrig sluttid
    //som slutter før vores nye starttid
    //O(n log n)
    private int findLastNonOverlapping(int current) {
        int low = 0, high = current - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (intervals[mid].end <= intervals[current].start) {
                if (mid == high || intervals[mid + 1].end > intervals[current].start) {
                    return mid;
                }
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }


    public int computeMaxWeight() {
        Arrays.sort(intervals);
        int[] dp = new int[intervals.length];

        //dp[4] = max vægt, ved at kigge på første 4 intervaller
        //Så hvis dp[4] = 7
        //så kan vi max få en vægt på 7, ved brug af interval 0,1,2,3,4
        //Da alle intervals er sorteret efter sluttid, så ved vi at det også er vores max weight
        //i alt, hvis vi går hele vejen op til dp[max(intervals)]

        //DP 0 = første interval, fordi det er sorteret, så på index 0 lægger der et interval med tidligst sluttid og højeste vægt
        dp[0] = intervals[0].weight;

        for (int i = 1; i < intervals.length; i++) {

            //Lad os sige vi har current interval som: (2,3,2)
            int currentWeight = intervals[i].weight;
            //Så finder denne metode (1,2,2), som slutter præcist før vores nuværende starter
            //Bemærk, at den kigger på FORRIGE INTERVALLER, vi allerede HAR TJEKKET

            int lastNonOverlapIndex = findLastNonOverlapping(i);
            //findes ingen non-overlapping, returnere den -1
            if (lastNonOverlapIndex != -1) {
                //Da de forrige intervaller er tjekket, har de i DP array
                //den største vægt vi kunne opnå med interval 0..Nuværende
                currentWeight += dp[lastNonOverlapIndex];
            }

            //Vi tager kun vores nyfundet vægt på det tilføjet interval, hvis
            //det er bedre end forrige løsning.
            dp[i] = Math.max(dp[i - 1], currentWeight);
        }

        return dp[intervals.length - 1];
    }

    public static void main(String[] args) {
        //Vores "Runner", som blot tager input
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Interval[] intervals = new Interval[n];

        for (int i = 0; i < n; i++) {
            intervals[i] = new Interval(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
        }
        scanner.close();

        WeightedIntervalScheduler scheduler = new WeightedIntervalScheduler(intervals);
        System.out.println(scheduler.computeMaxWeight());
    }
}
