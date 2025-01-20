import java.util.*;
import java.io.*;

public class is_bst {
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

    public class IsBST {
        int n;
        int[] key, left, right;

        void read() throws IOException {
            FastScanner in = new FastScanner();
            n = in.nextInt();

            
            key = new int[n];
            left = new int[n];
            right = new int[n];
            for (int i = 0; i < n; i++) {
                key[i] = in.nextInt();
                left[i] = in.nextInt();
                right[i] = in.nextInt();
            }
        }

        boolean isBinarySearchTree() {
            if (n == 0) {
                return true;
            }
            int lastElement = Integer.MIN_VALUE;
            // Finish the implementation
            // You may need to add a new recursive method to do that
            // inOrderTraversal(0, result);

            Stack<Integer> inOrderRemainingElements = new Stack<Integer>();
            inOrderRemainingElements.add(0);

            while (left[inOrderRemainingElements.peek()] != -1) {
                inOrderRemainingElements.add(left[inOrderRemainingElements.peek()]);
            }

            while (!inOrderRemainingElements.empty()) {

                int nextElement = inOrderRemainingElements.pop();

                if (key[nextElement] < lastElement) {
                    return false;
                }

                lastElement = key[nextElement];

                if (right[nextElement] != -1) {
                    inOrderRemainingElements.add(right[nextElement]);
                    while (left[inOrderRemainingElements.peek()] != -1) {
                        inOrderRemainingElements.add(left[inOrderRemainingElements.peek()]);
                    }
                }
            }

            return true;
        }
    }

    static public void main(String[] args) throws IOException {
        new Thread(null, new Runnable() {
            public void run() {
                try {
                    new is_bst().run();
                } catch (IOException e) {
                }
            }
        }, "1", 1 << 26).start();
    }
    public void run() throws IOException {
        IsBST tree = new IsBST();
        tree.read();
        if (tree.isBinarySearchTree()) {
            System.out.println("CORRECT");
        }
        else {
            System.out.println("INCORRECT");
        }
    }
}
