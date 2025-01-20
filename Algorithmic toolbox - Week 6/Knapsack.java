import java.util.*;

public class Knapsack {
  static int optimalWeight(int W, int[] w2) {

    int[] w = new int[w2.length + 1];

    w[0] = 0;

    for (int i = 0; i < w2.length; i++) {
      w[i + 1] = w2[i];
    }

    int maxGoldWeight[][] = new int[W + 1][w.length];

    for (int j = 1; j <= w2.length; j++) {
      for (int i = 1; i <= W; i++) {
        maxGoldWeight[i][j] = maxGoldWeight[i][j - 1];
        if (w[j] <= i) {
          int goldWeight = maxGoldWeight[i - w[j]][j - 1] + w[j];
          if (maxGoldWeight[i][j] < goldWeight) {
            maxGoldWeight[i][j] = goldWeight;
          }
        }
      }

    }

    return maxGoldWeight[W][w.length - 1];
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int W, n;
    W = scanner.nextInt();
    n = scanner.nextInt();
    int[] w = new int[n];
    for (int i = 0; i < n; i++) {
      w[i] = scanner.nextInt();
    }
    System.out.println(optimalWeight(W, w));
  }
}
