package W4DynamicProgramming;

import java.util.*;

/*
 * Dette var min eksamen i applied algorithms "Cheatey Petey"
 * Den store forskel er at man kan GÅ OVER target!!
 * Til det skal vi så expande vores array dynamisk.
 * og til sidste tjekke target, target+1, target+2, target+3... for at finde den mindste værdi.
 * */
public class ExactChangeOptimizedArray {

    private static class Solver {

        public static int[] bottomUp(Set<Integer> coins, int target) {
            int maxCoin = Collections.max(coins);
            int totalCoinSum = coins.stream().mapToInt(Integer::intValue).sum();
            int maxPossibleTarget = Math.max(target + maxCoin, totalCoinSum);
            // DP array ide:
            // [4] = 3  . For at ramme target 4, skal vi bruge 3 mønter.
            // [5] = 4  . For at ramme target 5, skal vi bruge 4 mønter.
            int[] dp = new int[maxPossibleTarget + 1];
            // Sætter alle targets fra 0 -> target til MAX_VALUE
            // med andre ord har de alle "den dårligste løsning" som vi kan
            // bottom up, finde bedre løsning til hver target.
            Arrays.fill(dp, Integer.MAX_VALUE);
            dp[0] = 0;

            for (int coin : coins) {
                for (int i = coin; i <= maxPossibleTarget; i++) {
                    if (dp[i - coin] != Integer.MAX_VALUE) {
                        dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                    }
                }
            }

            int smallestOverTarget = target;
            while (dp[smallestOverTarget] == Integer.MAX_VALUE && smallestOverTarget <= maxPossibleTarget) {
                smallestOverTarget++;
            }

            return new int[]{smallestOverTarget, dp[smallestOverTarget]};
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int testCases = scanner.nextInt();

        while (testCases-- > 0) {
            int price = scanner.nextInt();
            int n = scanner.nextInt();
            Set<Integer> coins = new HashSet<>();

            for (int i = 0; i < n; i++) {
                coins.add(scanner.nextInt());
            }

            int[] result = Solver.bottomUp(coins, price);
            System.out.println(result[0] + " " + result[1]);
        }

        scanner.close();
    }
}
