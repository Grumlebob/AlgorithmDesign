package W2Greedy;

import java.util.*;

public class MinimumScalarProduct {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Scanner object to read input

        int T = scanner.nextInt(); // Number of test cases
        List<TestCase> testCases = new ArrayList<>();

        //Parser vores input, og laver testcases som objecter, som vi kan arbejde med.
        for (int t = 0; t < T; t++) {
            //hver test case har v1 og v2 som er de to vectorer vi finder dot-product af.
            int vectorSize = scanner.nextInt();
            List<Integer> v1 = new ArrayList<>();
            List<Integer> v2 = new ArrayList<>();

            for (int i = 0; i < vectorSize; i++) {
                v1.add(scanner.nextInt());
            }
            for (int i = 0; i < vectorSize; i++) {
                v2.add(scanner.nextInt());
            }
            testCases.add(new TestCase(v1, v2));
        }

        //Hver test case skal printes "Case #X: Y" hvor X er test case nummer og Y er resultatet.
        for (int i = 0; i < testCases.size(); i++) {
            TestCase testCase = testCases.get(i);
            long result = testCase.calculateMinimumScalarProduct();
            System.out.println("Case #" + (i + 1) + ": " + result);
        }
    }
}

class TestCase {
    private final List<Integer> v1;
    private final List<Integer> v2;

    public TestCase(List<Integer> v1, List<Integer> v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    /**
     *
     * Ideen er at vi vil gange det største tal fra ene vektor med midnste tal fra anden.
     * Derfor sortere vi den ene ascending og anden descending.
     */
    public long calculateMinimumScalarProduct() {
        //Ascending sort
        Collections.sort(v1);
        //Descending sort
        v2.sort(Collections.reverseOrder());

        //Nu regner vi bare dot-product som almindelig dot-product.
        long scalarProduct = 0;
        for (int i = 0; i < v1.size(); i++) {
            scalarProduct += (long) v1.get(i) * v2.get(i);
        }
        return scalarProduct;
    }
}
