package W4DynamicProgramming;

import java.util.Scanner;

public class WeightliftingWalrus {
    //I opgaven, skal vi altid forsøge at ramme 1000 kg.
    private static final int TARGET_WEIGHT = 1000;
    //Vores største skive er 1000,
    //Lad os os lade som om vi har to plader af 999,99. Så kan vi max tage 2000 på i vægt.
    private static final int MAX_WEIGHT = 2000;
    private boolean[] dp = new boolean[MAX_WEIGHT + 1];

    public WeightliftingWalrus(int[] weights) {
        //Base case er 0 kg, kræver ingen plader og kan altid lade sig gøre.
        dp[0] = true;
        //Ligesom coin change, vi går igennem alle vægtene
        for (int weight : weights) {
            //Vi går fra 2000 til 0, fordi vi vil gerne have den største vægt først.
            for (int j = MAX_WEIGHT; j >= weight; j--) {
                //Hvis vi kan lave vægten j - weight, så kan vi også lave vægten j.
                //såsom på j = 900 og vores første weight er 900
                //så rammer vi vores base case, og kan lave vægten 900.
                //derfor sættes dp[900] = true;
                if (dp[j - weight]) {
                    dp[j] = true;
                }
            }
        }
    }

    public int findClosestWeight() {
        for (int i = 0; i <= MAX_WEIGHT; i++) {
            //Rækkefølgen er vigtigt. Vi tjekker først 1002 og så 998, fordi
            //opgaven kræver at vi tager væriden som er tættest på 1000, og hvis de er ens
            //tager vi den største af de to
            if (TARGET_WEIGHT + i <= MAX_WEIGHT && dp[TARGET_WEIGHT + i]) {
                return TARGET_WEIGHT + i;
            }
            if (TARGET_WEIGHT - i >= 0 && dp[TARGET_WEIGHT - i]) {
                return TARGET_WEIGHT - i;
            }
        }
        //Bør ikke kunne ske.
        return 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(); // Number of plates

        int[] weights = new int[n]; //vægten på tilgængelige plates
        for (int i = 0; i < n; i++) {
            weights[i] = scanner.nextInt();
        }
        scanner.close();

        WeightliftingWalrus walrus = new WeightliftingWalrus(weights);
        System.out.println(walrus.findClosestWeight());
    }
}
