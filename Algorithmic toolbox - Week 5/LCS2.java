import java.util.*;

public class LCS2 {

    private static int lcs2(int[] a, int[] b) {


        //Write your code here
        int matches[][] = new int[a.length + 1][b.length + 1];

        for (int i = 0; i < a.length + 1; i++) {
            matches[i][0] = 0;
        }

        for (int j = 0; j < b.length + 1; j++) {
            matches[0][j] = 0;
        }

        for (int i = 1; i < a.length + 1; i++) {
            for (int j = 1; j < b.length + 1; j++) {
                int insertion = matches[i][j - 1];
                int deletion = matches[i - 1][j];
                int match = matches[i - 1][j - 1] + 1;
                int mismatch = matches[i - 1][j - 1];

                if (a[i - 1] == b[j - 1]) {
                    matches[i][j] = max(insertion, deletion, match);
                } else {
                    matches[i][j] = max(insertion, deletion, mismatch);
                }
            }
        }

        return matches[a.length][b.length];
    }

    private static int max(int a, int b, int c) {

        if (a > b) {
            if (c > a)
                return c;
            else
                return a;
        } else {
            if (a > c)
                return b;
            // b < a a > c
            else {
                if (b > c) {
                    return b;

                } else {
                    return c;
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        int m = scanner.nextInt();
        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            b[i] = scanner.nextInt();
        }

        System.out.println(lcs2(a, b));
    }
}

