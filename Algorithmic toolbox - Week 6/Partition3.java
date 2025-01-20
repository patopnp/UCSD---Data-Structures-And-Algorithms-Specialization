import java.util.*;
import java.io.*;

public class Partition3 {
    private static boolean partition3(int[] A) {

        /*
        int[] A = new int[Aminus1.length+1];
        
        for (int i = 0; i < Aminus1.length; i++) {
            A[i+1] = Aminus1[i];
        }
        */

        
        if (sum(A) % 3 != 0) {
            return false;
        }


        int p = sum(A) / 3;
        //System.out.println(p);
        int n = A.length;

        //write your code here
        boolean q[][][] = new boolean[p+1][p+1][n+1];

        /*
        for (int j = 1; j <= n; j++) {
            for (int h = 0; h <= p; h++) {
                for (int i = 0; i <= p; i++){
                    q[h][i][j] = q[h][i][j - 1] || q[h][i - A[j]][j - 1] || q[h - A[j]][i][j - 1];
                }
            }
        }
        
        return 0;
        }
        */
        for (int j = 0; j < n; j++) {
            q[0][0][j] = true;
        }

        for (int i = 1; i <= p; i++) {
            for (int j = 1; j <= n; j++) {
                q[i][0][j] = q[i][0][j - 1];
                if (!q[i][0][j] && i >= A[j - 1])
                    q[i][0][j] = q[i - A[j - 1]][0][j - 1];
            }
        }

        

        
        for (int i = 1; i <= p; i++) {
            for (int j = 1; j <= n; j++) {
                q[0][i][j] = q[0][i][j - 1];
                if (!q[0][i][j] && i >= A[j - 1])
                    q[0][i][j] = q[0][i - A[j - 1]][j - 1];
            }
        }        

        

        for (int j = 1; j <= n; j++) {
            for (int h = 1; h <= p; h++) {
                for (int i = 1; i <= p; i++) {
                    if (i - A[j-1] >= 0 && h - A[j-1] >= 0) {
                        q[h][i][j] = q[h][i][j - 1] || q[h][i - A[j-1]][j - 1] || q[h - A[j-1]][i][j - 1];
//                        System.out.println("q[" + h + "][" + i + "][" + j + "] =" + q[h][i][j]);
                    }
                }
            }
        }

        return q[p][p][n];
    }

    private static int sum(int[] V){
        int sum = 0;
        for (int i = 0; i < V.length; i++){
            sum += V[i];
        }
        return sum;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = scanner.nextInt();
        }
        System.out.println(partition3(A)?1:0);
    }
}

