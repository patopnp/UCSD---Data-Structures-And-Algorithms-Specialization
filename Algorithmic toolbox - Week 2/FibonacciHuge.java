import java.util.*;

public class FibonacciHuge {

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
                
                /*
                while (i + c < n - 1) {
                    i = i + c;
                }
                */

                i = (long)((n+1-i-c)/c)*c+i;
                //i = -((n + 1 + i - c) % c) + n + 1 + 2 * i - c;
            }
        }

        return current;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        long m = scanner.nextLong();
       
        System.out.println(getFibonacciHugeImproved(n, m));
    }
}