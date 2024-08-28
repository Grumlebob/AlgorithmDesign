package W4DynamicProgramming;

import java.util.*;

/*
 * Dette var min eksamen i applied algorithms "Cheatey Petey"
 * Den store forskel er at man kan GÅ OVER target!!
 * Til det skal vi så expande vores array dynamisk.
 * og til sidste tjekke target, target+1, target+2, target+3... for at finde den mindste værdi.
 * */
public class ExactChange {

    private static class Solver {

        public static int[] bottomUp(Set<Integer> coins, int target) {

            // DP array ide:
            // [4] = 3  . For at ramme target 4, skal vi bruge 3 mønter.
            // [5] = 4  . For at ramme target 5, skal vi bruge 4 mønter.
            List<Integer> memoization = new ArrayList<>();

            // Sætter alle targets fra 0 -> target til MAX_VALUE
            // med andre ord har de alle "den dårligste løsning" som vi kan
            // bottom up, finde bedre løsning til hver target.
            for (int i = 0; i <= target; i++) {
                memoization.add(Integer.MAX_VALUE);
            }

            //Base case, target 0 kræver 0 mønter.
            memoization.set(0, 0);

            //Loop over alle mønterne
            for (int coin : coins) {
                //Vi ønsker at opdatere vores array inde i vores loop,
                //derfor skal vi lave et kopi.
                List<Integer> currentMemo = new ArrayList<>(memoization);

                //loop bottom up over alle targets
                //Så alle mønter looper over alle targets: O(n^2)
                for (int currentTarget = 0; currentTarget < memoization.size(); currentTarget++) {

                    //Hvis vi har en løsning til currentTarget, så prøver vi at opdatere vores løsning
                    //Så tænk på at første gang har vi kun løsning til target 0,
                    //Så får vi løsning til target 0+coin, såsom 500
                    //På den måde behøver vi ikke at tjekke 1,2,3,..499. Fordi de mønter er der alligevel ikke.

                    if (memoization.get(currentTarget) != Integer.MAX_VALUE) {
                        int newTarget = currentTarget + coin;

                        //i vores edge case at vores target er 1500
                        // men vi kan kun ramme ex 1600 med vores mønter
                        if (newTarget >= memoization.size()) {
                            //Expand array, med nye targets sat til værste løsning, så de kan forbedres.
                            for (int i = memoization.size(); i <= newTarget; i++) {
                                currentMemo.add(Integer.MAX_VALUE);
                            }
                        }

                        //Opdater med løsning[x] med bedste (mindste værdi) af følgende 2 cases
                        //2 cases:
                        //memoization.get(currentTarget) + 1):
                        //Hvis vi TAGER mønten, tilføj +1 til antal mønter, plus antal mønter det tog at komme til currentTarget

                        //currentMemo.get(newTarget):
                        //Hvis vi IKKE TAGER mønten, så er det bare antal mønter det tog at komme til currentTarget
                        currentMemo.set(newTarget, Math.min(currentMemo.get(newTarget), memoization.get(currentTarget) + 1));
                    }
                }
                //Sæt vores DP array til vores DP array kopi som er opdateret
                memoization = currentMemo;
            }


            //Her håndtere vi edgecase at target ex er 1500
            //men vi kan ikke ramme 1500
            //så vi tjekker 1501,1502...
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
