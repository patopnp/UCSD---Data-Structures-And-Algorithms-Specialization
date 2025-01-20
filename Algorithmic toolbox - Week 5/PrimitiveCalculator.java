import java.util.*;

public class PrimitiveCalculator {
    private static List<Integer> optimal_sequence(int n) {
        List<Integer> sequence = new ArrayList<Integer>();

        int m = 1;
        int minNumOperations[] = new int[n+1];


        while (m <= n) {

            int numOperations = Integer.MAX_VALUE;

            if (m % 3 == 0) {
                if (numOperations > minNumOperations[m / 3] + 1) {
                    numOperations = minNumOperations[m / 3] + 1;
                }
            }
            if (m % 2 == 0) {
                if (numOperations > minNumOperations[m / 2] + 1) {
                    numOperations = minNumOperations[m / 2] + 1;
                }
            }

            if (minNumOperations[m - 1] + 1 < numOperations) {
                numOperations = minNumOperations[m - 1] + 1;
            }

            minNumOperations[m] = numOperations;
            m++;
        }

        m--;

        while (m > 0) {

            sequence.add(m);

            int numOperations = minNumOperations[m];

            if (m % 3 == 0 && numOperations == minNumOperations[m / 3] + 1) {
                m /= 3;
            }
            else if (m % 2 == 0 && numOperations == minNumOperations[m / 2] + 1) {
                m /= 2;
            }
            else if (minNumOperations[m - 1] + 1 == numOperations) {
                m--;
            }


           
        }

        Collections.reverse(sequence);

        return sequence;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Integer> sequence = optimal_sequence(n);
        System.out.println(sequence.size() - 1);
        for (Integer x : sequence) {
            System.out.print(x + " ");
        }
        System.out.println("");
    }
}

