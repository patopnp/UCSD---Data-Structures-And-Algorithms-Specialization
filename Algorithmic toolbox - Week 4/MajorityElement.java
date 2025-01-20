import java.util.*;
import java.io.*;

public class MajorityElement {
    private static int getMajorityElement(int[] a, int left, int right) {
        if (left == right+1) {
            return -1;
        }
        if (left + 1 == right+1) {
            return a[left];
        }

        randomizedQuickSort(a, left, right);

        int middle = Math.floorDiv(right + left + 1, 2);

        for (int i = 0; i + middle < a.length; i++) {
            if (a[i] == a[i + middle]) {
                return 1;
            }
        }
        
        return -1;
    }

    private static void randomizedQuickSort(int[] a, int left, int right) {
        if (left >= right) {
            return;
        }

        // Pick random pivot
        /*
         Random rand = new Random(); int randNumber = rand.nextInt((right + 1 - left)) + left;
         */

        int pivot = left;
        // Pick median pivot
        int middle = Math.floorDiv(right + left, 2);

        if (a[left] < a[middle]) {
            if (a[middle] < a[right]) {
                pivot = middle;
            } else {
                pivot = right;
            }
        } else {
            if (a[left] < a[middle]) {
                pivot = left;
            } else {
                pivot = middle;
            }
        }

        int aux = a[left];
        a[left] = a[pivot];
        a[pivot] = aux;

        int bounds[] = partition3(a, left, right);
        randomizedQuickSort(a, left, bounds[0] - 1);
        randomizedQuickSort(a, bounds[1] + 1, right);

    }

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

    public static void main(String[] args) {
        FastScanner scanner = new FastScanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        if (getMajorityElement(a, 0, a.length-1) != -1) {
            System.out.println(1);
        } else {
            System.out.println(0);
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

