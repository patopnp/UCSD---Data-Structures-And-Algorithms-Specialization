import java.util.*;

public class FibonacciSumLastDigit {

    private static long getFibonacciLastDigit(long n) {
        if (n <= 1)
            return n;

        long previous = 0;
        long current = 1;
        long c = 0;
        
        for (long i = 0; i < n - 1; ++i) {
            long tmp_previous = previous;
            previous = current;
            current = tmp_previous + current;
            if (current >= 10) {
                current -= 10;
            }

            if (previous == 0 && current == 1) {
                c = i + 1;
                i = (long) ((n + 1 - i - c) / c) * c + i;
            }
        }

        return current;
    }
    
    private static long getFibonacciSumEfficient(long n) {
        
        long result = getFibonacciLastDigit(n+2)-1;
        if (result < 0) {
            result += 10;
        }

        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        System.out.println(getFibonacciSumEfficient(n));
    }
}

