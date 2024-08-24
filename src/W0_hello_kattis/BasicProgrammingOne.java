package W0_hello_kattis;

import java.util.Scanner;

public class BasicProgrammingOne {

    public static void main(String[] args) {
        // Initialize the scanner
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();       // Size of the array, always 3 or more
        int action = scanner.nextInt();  // Action to perform
        int[] arr = new int[N];          // Array of non-negative integers
        for (int i = 0; i < N; i++) {
            arr[i] = scanner.nextInt();
        }

        switch (action) {
            case 1 -> {
                System.out.println(7);
            }
            case 2 -> {
                if (arr[0] > arr[1]) {
                    System.out.println("Bigger");
                } else if (arr[0] == arr[1]) {
                    System.out.println("Equal");
                } else {
                    System.out.println("Smaller");
                }
            }
            case 3 -> {
                // Find the median of the first three elements
                int[] sortedArr = {arr[0], arr[1], arr[2]};
                java.util.Arrays.sort(sortedArr);
                System.out.println(sortedArr[1]);
            }
            case 4 -> {
                // Sum of all elements
                int sum = 0;
                for (int i = 0; i < N; i++) {
                    sum += arr[i];
                }
                System.out.println(sum);
            }
            case 5 -> {
                // Sum of all even elements
                int sum = 0;
                for (int i = 0; i < N; i++) {
                    if (arr[i] % 2 == 0)
                        sum += arr[i];
                }
                System.out.println(sum);
            }
            case 6 -> {
                // Generate characters based on modulo 26
                for (int i = 0; i < N; i++) {
                    int modulo = arr[i] % 26;  // Modulo operation on non-negative integers
                    System.out.print((char) (modulo + 'a'));  // Map to 'a' to 'z'
                }
                System.out.println();  // Print newline after output
            }
            case 7 -> {
                // Index jumping simulation
                boolean[] visited = new boolean[N]; // Boolean array to check for cycles
                int i = 0;
                while (true) {
                    if (i < 0 || i >= N) {
                        System.out.println("Out");
                        break;
                    } else if (i == N - 1) {
                        System.out.println("Done");
                        break;
                    }
                    if (visited[i]) {
                        System.out.println("Cyclic");
                        break;
                    }
                    visited[i] = true;
                    i = arr[i];
                }
            }
            default -> {
                // Default case: action is always valid as per problem constraints, so no action needed
            }
        }

        scanner.close(); // Close scanner
    }
}
