import java.util.*;

public class LCS3 {

    private static int lcs3(int[] a, int[] b, int[] c) {
        //Write your code here
        int matches[][][] = new int[a.length + 1][b.length + 1][c.length + 1];

        for (int i = 0; i < a.length + 1; i++) {
            matches[i][0][0] = 0;
        }

        for (int j = 0; j < b.length + 1; j++) {
            matches[0][j][0] = 0;
        }

        for (int m = 0; m < c.length + 1; m++) {
            matches[0][0][m] = 0;
        }        

        for (int i = 1; i < a.length + 1; i++) {
            for (int j = 1; j < b.length + 1; j++) {
                for (int m = 1; m < c.length + 1; m++) {

                    int insertion = matches[i][j-1][m];
                    int insertion2 = matches[i][j][m-1];
                    int insertion3 = matches[i][j-1][m-1];
                    int insertion4 = matches[i-1][j][m];
                    int insertion5 = matches[i-1][j-1][m];
                    int insertion6 = matches[i - 1][j][m - 1];
                    
                    int match = matches[i-1][j-1][m-1] + 1;
                    int mismatch = matches[i -1][j - 1][m - 1];
                    

                    if (a[i - 1] == b[j - 1] && a[i - 1] == c[m - 1]) {
                        matches[i][j][m] = max(insertion, insertion2, insertion3, insertion4, insertion5, insertion6,
                                match);
                        //System.out.println("se computa 1 punto mas");
                    } else {
                        matches[i][j][m] = max(insertion, insertion2, insertion3, insertion4, insertion5, insertion6,
                                mismatch);
                       /* System.out.println("(" + i + ", " + j + ", " + m + ") = " + matches[i][j][m] + " " + i + " " + j + "  " + m);*/
                    }

    
                }
            }
        }

        return matches[a.length][b.length][c.length];
    }

    private static int max(int... nums) {

        int max = Integer.MIN_VALUE;

        for (int x : nums) {
            if (x > max) {
                max = x;
            }
        }

        return max;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int an = scanner.nextInt();
        int[] a = new int[an];
        for (int i = 0; i < an; i++) {
            a[i] = scanner.nextInt();
        }
        int bn = scanner.nextInt();
        int[] b = new int[bn];
        for (int i = 0; i < bn; i++) {
            b[i] = scanner.nextInt();
        }
        int cn = scanner.nextInt();
        int[] c = new int[cn];
        for (int i = 0; i < cn; i++) {
            c[i] = scanner.nextInt();
        }
        System.out.println(lcs3(a, b, c));
    }
}

