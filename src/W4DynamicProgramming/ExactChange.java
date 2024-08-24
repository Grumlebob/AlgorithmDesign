package W4DynamicProgramming;

import java.util.*;

public class ExactChange {

    private static class Solver {

        public static int[] bottomUp(Set<Integer> coins, int target) {
            // Use an ArrayList to store the minimum number of coins required for each amount
            List<Integer> memoization = new ArrayList<>();
            // Initialize the list with a large enough size
            for (int i = 0; i <= target; i++) {
                memoization.add(Integer.MAX_VALUE);
            }
            memoization.set(0, 0); // Zero coins are needed to make 0 cents

            // Loop over each coin
            for (int coin : coins) {
                // Use a new ArrayList to avoid modifying the current state while iterating
                List<Integer> currentMemo = new ArrayList<>(memoization);
                for (int currentTarget = 0; currentTarget < memoization.size(); currentTarget++) {

                    if (memoization.get(currentTarget) != Integer.MAX_VALUE) {
                        int newTarget = currentTarget + coin;

                        //In case we need to pay like 1600 in bills to reach target 1500, then we make sure to update the memoization to check up to 1600
                        if (newTarget >= memoization.size()) {
                            // Expand memoization dynamically if needed
                            for (int i = memoization.size(); i <= newTarget; i++) {
                                currentMemo.add(Integer.MAX_VALUE);
                            }
                        }
                        //Update the newTarget with the minimum coins needed
                        //memoization.get(currentTarget) + 1): If we take the current coin, we need to add 1 to the number of coins needed, plus the number it took to come to currentTarget
                        //currentMemo.get(newTarget): If the newTarget is already a calculated solution, we take it if it is better than the current solution
                        currentMemo.set(newTarget, Math.min(currentMemo.get(newTarget), memoization.get(currentTarget) + 1));
                    }
                }
                memoization = currentMemo; // Update memoization with the current state
            }

            //in case our target is larger than the memoization size, we need to find the first non-Integer.MAX_VALUE value
            //due to assignment description.
            int amountToPay = target;
            while (amountToPay < memoization.size() && memoization.get(amountToPay) == Integer.MAX_VALUE) {
                amountToPay++;
            }

            return new int[]{amountToPay, memoization.get(amountToPay)};
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
