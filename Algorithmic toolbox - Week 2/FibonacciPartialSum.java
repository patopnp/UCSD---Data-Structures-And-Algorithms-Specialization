import java.util.*;

public class FibonacciPartialSum {
    
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

    private static long getFibonacciPartialSumEfficient(long from, long to) {
        long firstPartialSum = getFibonacciHugeImproved(from+1, 10);
        long secondPartialSum = getFibonacciHugeImproved(to + 2, 10);
        long difference = secondPartialSum - firstPartialSum;

        if (difference < 0) {
            difference += 10;
        }

        return difference;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long from = scanner.nextLong();
        long to = scanner.nextLong();
        System.out.println(getFibonacciPartialSumEfficient(from, to));
    }
}

