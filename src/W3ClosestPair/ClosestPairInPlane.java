package W3ClosestPair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


// Dette kan i øvrigt også løses med en line-sweep algorithme.
// Her er divide and conquer løsningen!
// Hjælp fra: https://www.youtube.com/watch?v=6u_hWxbOc7E
// Runtime er O(n logn).
public class ClosestPairInPlane {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //Input er n antal punkter i planet.
        int n = scanner.nextInt();

        //Liste af punkter
        List<Point> points = new ArrayList<>();

        //Parse input (x,y) til punkter.
        for (int i = 0; i < n; i++) {
            String xStr = scanner.next();
            String yStr = scanner.next();
            float x = Float.parseFloat(xStr);
            float y = Float.parseFloat(yStr);
            points.add(new Point(x, y));
        }

        //Sorter vores punkter baseret på x- og y-koordinat.
        var sortedPointsByX = points.stream().sorted(Comparator.comparingDouble(p -> p.x)).toList();
        var sortedPointsByY = points.stream().sorted(Comparator.comparingDouble(p -> p.y)).toList();

        //Vi finder det nærmeste punktpar
        ClosestPairResult result = closestPair(sortedPointsByX, sortedPointsByY);

        //Output i det ønskede format.
        System.out.printf("%.2f %.2f\n%.2f %.2f\n", result.point1.x, result.point1.y, result.point2.x, result.point2.y);
    }

    public static ClosestPairResult closestPair(List<Point> sortedPointsByX, List<Point> sortedPointsByY) {
        int n = sortedPointsByX.size();

        //Base cases. Når vi splitter kan vi inde med en liste med 1, 2 eller 3 punkter:
        //såsom 6 splittet: 3-3
        //såsom 5 splittet: 3-2
        //Såsom 3 splittet: 2-1
        //hvis vi har 1 punkt er det ikke et pair og derfor returnerer vi uendelig distance.

        if (n == 1) {
            return new ClosestPairResult(Double.POSITIVE_INFINITY, null, null);
        }
        //Hvis vi har 2 punkter, så er det bare afstanden mellem de to punkter.
        if (n == 2) {
            return new ClosestPairResult(sortedPointsByX.get(0).distance(sortedPointsByX.get(1)), sortedPointsByX.get(0), sortedPointsByX.get(1));
        }
        //Hvis vi har 3 punkter, så tjekker vi bare afstanden mellem de 3 punkter.
        if (n == 3) {
            Point p1 = sortedPointsByX.get(0);
            Point p2 = sortedPointsByX.get(1);
            Point p3 = sortedPointsByX.get(2);
            double d12 = p1.distance(p2);
            double d13 = p1.distance(p3);
            double d23 = p2.distance(p3);
            if (d12 <= d13 && d12 <= d23) {
                return new ClosestPairResult(d12, p1, p2);
            } else if (d13 <= d12 && d13 <= d23) {
                return new ClosestPairResult(d13, p1, p3);
            } else {
                return new ClosestPairResult(d23, p2, p3);
            }
        }

        //DIVIDE AND CONQUER

        //Vi deler listen 50/50
        //(Divide Left): Delta distance venstre
        ClosestPairResult deltaLeft = closestPair(sortedPointsByX.subList(0, n / 2), sortedPointsByY);
        //(Divide Right): Delta distance højre
        ClosestPairResult deltaRight = closestPair(sortedPointsByX.subList(n / 2, n), sortedPointsByY);
        //Vi kigger på bedste delta fra vores højre og venstre svar.
        ClosestPairResult delta = deltaLeft.distance < deltaRight.distance ? deltaLeft : deltaRight;

        //Dog er det ikke sikkert at vi har fundet bedste delta, fordi der kan være
        //punkter imellem opdelingen som er tættest, altså noget i Left er tættest på noget i Right
        //Derfor skal vi tjekke "the strip", som kigger på en delta afstand væk, og tjekker de punkter

        //Combine step
        //Vi finder den midterste x-koordinat.
        double midX = sortedPointsByX.get(n / 2).x;

        //Vi finder alle punkter fra Y (som er hele listen) som er indenfor delta distance fra midX.
        List<Point> strip = new ArrayList<>();
        for (Point point : sortedPointsByY) { //bemærk, vi bruger sorteret af y nu!
            if (Math.abs(point.x - midX) < delta.distance) {
                strip.add(point);
            }
        }

        //Vi sortere strip baseret på y-koordinat.
        strip.sort(Comparator.comparingDouble(p -> p.y));

        //Vi løber igennem strip listen og finder distance mellem punkter.
        for (int i = 0; i < strip.size(); i++) {
            //Vi løber igennem de næste 7 punkter.
            //Dette er fordi vi ved at den næste punkt ikke kan være længere væk end delta.
            //Derfor er det nok at kigge på de næste 7 punkter,
            //så dette tæller faktisk som runtime O(1).
            for (int j = i + 1; j < Math.min(i + 7, strip.size()); j++) {
                double dist = strip.get(i).distance(strip.get(j));
                if (dist < delta.distance) {
                    delta = new ClosestPairResult(dist, strip.get(i), strip.get(j));
                }
            }
        }
        return delta;
    }
}

class Point {
    float x;
    float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Point otherPoint) {
        return Math.sqrt(Math.pow(this.x - otherPoint.x, 2) + Math.pow(this.y - otherPoint.y, 2));
    }
}

//Nemmere at gemme som klasse, da vi skal ikke kun skal returnere distancen, men også de to punkter.
class ClosestPairResult {
    double distance;
    Point point1;
    Point point2;

    public ClosestPairResult(double distance, Point point1, Point point2) {
        this.distance = distance;
        this.point1 = point1;
        this.point2 = point2;
    }
}
