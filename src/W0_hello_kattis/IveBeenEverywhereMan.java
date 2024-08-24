package W0_hello_kattis;

import java.util.HashSet;
import java.util.Scanner;

public class IveBeenEverywhereMan {

    //main
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int testcases = scanner.nextInt();

        for (int i = 0; i < testcases; i++) {
            int worktrips = scanner.nextInt();
            scanner.nextLine();
            HashSet<String> cities = new HashSet<>();
            for (int j = 0; j < worktrips; j++) {
                cities.add(scanner.nextLine());
            }
            System.out.println(cities.size());
        }
    }
}
