import java.util.Scanner;

public class ChangeDP {
    private static int getChange(int m) {
        //write your code here

        if (m == 0) {
            return 0;
        }

        int minNumCoins[] = new int[m+1];

        minNumCoins[0] = 0;

        int coins[] = {1, 3, 4};


        for (int o = 1; o <= m; o++) {
            minNumCoins[o] = Integer.MAX_VALUE;
            for (int i = 0; i < coins.length; i++) {
                if (o >= coins[i]) {
                    int numCoins = minNumCoins[o - coins[i]] + 1;
                    if (numCoins < minNumCoins[o])
                        minNumCoins[o] = numCoins;
                }
            }
        }

        return minNumCoins[m];
    }
/*
    private static int minNumCoins(int m) {
        
    }
*/
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        System.out.println(getChange(m));

    }
}

