import java.util.Scanner;

public class Change {
    private static int getChange(int m) {
        int numCoins = Math.floorDiv(m, 10) + Math.floorDiv(m % 10, 5) + (m % 5);
        return numCoins;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        System.out.println(getChange(m));

    }
}

