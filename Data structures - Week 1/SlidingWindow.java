import java.util.*;
import java.io.*;

public class SlidingWindow {

    int[] input;
    int n;
    int m;

    public static void main(String... args) {
        SlidingWindow slidingW = new SlidingWindow();

        try {
            slidingW.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        slidingW.maxOfWindow();
    }

    class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    void read() throws IOException {
        FastScanner in = new FastScanner();
        n = in.nextInt();
        input = new int[n];
        for (int i = 0; i < n; i++) {
            input[i] = in.nextInt();
        }

        m = in.nextInt();
    }

    void maxOfWindow() {

        int midIndex = m - 1;

        while (midIndex + m < n) {
            Stack<Integer> leftMaxStack = new Stack<>();

            leftMaxStack.push(input[midIndex]);

            for (int i = midIndex - 1; i >= midIndex - (m - 1); i--) {
                int previousMax = leftMaxStack.peek();
                int thisMax = previousMax < input[i] ? input[i] : previousMax;
                leftMaxStack.push(thisMax);
            }

            System.out.println(leftMaxStack.pop());

            int maxRightLast = 0;

            for (int i = midIndex + 1; i < midIndex + m; i++) {
                int maxRight = maxRightLast < input[i] ? input[i] : maxRightLast;
                int maxLeftStack = leftMaxStack.pop();
                maxRightLast = maxRight;
                int maxOfWindow = maxLeftStack < maxRight ? maxRight : maxLeftStack;
                System.out.println(maxOfWindow);
            }

            midIndex += m;

        }

        if (midIndex + m >= n) {
            int midIndex2 = midIndex;

            Stack<Integer> leftMaxStack = new Stack<>();

            leftMaxStack.push(input[midIndex2]);

            for (int i = midIndex2 - 1; i > midIndex - m; i--) {
                int previousMax = leftMaxStack.peek();
                int thisMax = previousMax < input[i] ? input[i] : previousMax;
                leftMaxStack.push(thisMax);
            }

            System.out.println(leftMaxStack.pop());

            int maxRightLast = 0;

            for (int i = midIndex2 + 1; i < n; i++) {
                int maxRight = maxRightLast < input[i] ? input[i] : maxRightLast;
                int maxLeftStack = leftMaxStack.pop();
                maxRightLast = maxRight;
                int maxOfWindow = maxLeftStack < maxRight ? maxRight : maxLeftStack;
                System.out.println(maxOfWindow);

            }
        }

    }
}