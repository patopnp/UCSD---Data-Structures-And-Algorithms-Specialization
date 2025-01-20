import java.io.*;
import java.util.*;

public class Sorting {
    private static Random random = new Random();

    // split in three intervals
    private static int[] partition3(int[] a, int left, int right) {
        int x = a[left];
        int j = left;
        int m = left;

        for (int i = left + 1; i <= right; i++) {

            if (a[i] == x) {
                m++;
                j++;
                int aux = a[j];
                a[j] = a[i];
                a[i] = aux;

                aux = a[j];
                a[j] = a[m];
                a[m] = aux;
            } else if (a[i] < x) {
                j++;
                int aux = a[j];
                a[j] = a[i];
                a[i] = aux;
            }
        }

        /*
         * int aux = a[j]; a[j] = a[left]; a[left] = aux;
         */

        int rindex = j;
        for (int i = left; i <= m; i++) {
            int aux = a[rindex];
            a[rindex] = a[i];
            a[i] = aux;
            rindex--;
        }
        return new int[] { j - (m - left), j };
    }

    private static int partition2(int[] a, int l, int r) {
        int x = a[l];
        int j = l;
        for (int i = l + 1; i <= r; i++) {
            if (a[i] <= x) {
                j++;
                int t = a[i];
                a[i] = a[j];
                a[j] = t;
            }
        }
        int t = a[l];
        a[l] = a[j];
        a[j] = t;
        return j;
    }

    private static void randomizedQuickSort(int[] a, int l, int r) {
        if (l >= r) {
            return;
        }
        int k = random.nextInt(r - l + 1) + l;
        int t = a[l];
        a[l] = a[k];
        a[k] = t;

        int bounds[] = partition3(a, l, r);
        randomizedQuickSort(a, l, bounds[0] - 1);
        randomizedQuickSort(a, bounds[1] + 1, r);

    }

    public static void main(String[] args) {
        FastScanner scanner = new FastScanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        randomizedQuickSort(a, 0, n - 1);
        for (int i = 0; i < n; i++) {
            System.out.print(a[i] + " ");
        }
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(InputStream stream) {
            try {
                br = new BufferedReader(new InputStreamReader(stream));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}

