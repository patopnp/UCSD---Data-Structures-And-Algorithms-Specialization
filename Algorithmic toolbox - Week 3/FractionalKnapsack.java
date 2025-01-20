import java.util.Scanner;

public class FractionalKnapsack {


    private static double getOptimalValue(int capacity, int[] values, int[] weights) {
        double value = 0;
        double capacityAsDouble = capacity;
        for(int i = 0; i < values.length; i++){
            if (capacityAsDouble <= 0) {
                return value;
            }
            int bestItemIndex = bestItem(values, weights);
            if (weights[bestItemIndex] < capacityAsDouble) {
                value += values[bestItemIndex];
            }
            else{
                value += ((double)(capacityAsDouble * ((double)values[bestItemIndex]))) / ((double)weights[bestItemIndex]);
            }

            capacityAsDouble -= weights[bestItemIndex];
            weights[bestItemIndex] = 0;
            
        }
        return value;
    }

    private static int bestItem(int[] values, int[] weights) {
        double maxValuePerWeight = 0;
        int bestItem = 0;
        for (int i = 0; i < values.length; i++) {
            if (weights[i] > 0) {
                double valuePerWeight = ((double)values[i]) / ((double)weights[i]);
                if (valuePerWeight > maxValuePerWeight) {
                    maxValuePerWeight = valuePerWeight;
                    bestItem = i;
                }
            }
        }
        return bestItem;
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int capacity = scanner.nextInt();
        int[] values = new int[n];
        int[] weights = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = scanner.nextInt();
            weights[i] = scanner.nextInt();
        }
        System.out.println(getOptimalValue(capacity, values, weights));
    }
} 
