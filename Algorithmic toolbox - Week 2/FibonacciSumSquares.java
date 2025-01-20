import java.util.*;

public class FibonacciSumSquares {

    private static long getFibonacciHugeImproved(long n, long m) {
        if (n <= 1)
            return n;

        long previous = 0;
        long current = 1;
        long c = 0;

        for (long i = 0; i < n - 1; ++i) {

            long tmp_previous = previous;
            previous = current;
            current = tmp_previous + current;
            if (current >= m) {
                current -= m;
            }

            if (previous == 0 && current == 1) {
                c = i + 1;
                i = (long) ((n + 1 - i - c) / c) * c + i;
            }
        }

        return current;
    }

    private static long getFibonacciSumSquaresEfficient(long n){
        long area = getFibonacciHugeImproved((n + 1), 10) * getFibonacciHugeImproved(n, 10);
        return area % 10;
    }


    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        System.out.println(getFibonacciSumSquaresEfficient(n));
    }
}

