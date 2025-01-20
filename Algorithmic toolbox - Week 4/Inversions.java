import java.util.*;

public class Inversions {

    static int totalAmountOfInversions = 0;

    private static long getNumberOfInversions(int[] a, int[] b, int left, int right) {
        long numberOfInversions = 0;
        if (right <= left + 1) {
            return numberOfInversions;
        }

        mergeSort(a, 0, a.length-1);

        return totalAmountOfInversions;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int[] b = new int[n];
        System.out.println(getNumberOfInversions(a, b, 0, a.length));
    }

        private static void mergeSort(int a[], int p, int r) {
            if (p >= r)
                return;
            int q = (int) ((p + r) / 2);

            

            mergeSort(a, p, q);
            mergeSort(a, q + 1, r);
            merge(a, p, q, r);

        }

        private static int merge(int[] a, int p, int q, int r) {
            
            int nl = q - p + 1;
            int nr = r - q;
            int la[] = new int[nl];
            int ra[] = new int[nr];

            for (int i = 0; i <= nl - 1; i++) {
                la[i] = a[p + i];
            }

            for (int i = 0; i <= nr - 1; i++) {
                ra[i] = a[q + i + 1];
            }

            int t = p;

            int i = 0;
            int j = 0;

            int amountOfInversionsPerOrdering = 0;

            while (i < nl && j < nr) {
                if (la[i] <= ra[j]) {
                    a[t] = la[i];
                    i++;
                    totalAmountOfInversions += amountOfInversionsPerOrdering;
                } else {
                    a[t] = ra[j];
                    j++;
                    amountOfInversionsPerOrdering++;
                }

                t++;
            }

            while (i < nl) {
                a[t] = la[i];
                i++;
                t++;
                totalAmountOfInversions += amountOfInversionsPerOrdering;
            }
            while (j < nr) {
                a[t] = ra[j];
                j++;
                t++;
            }

            return totalAmountOfInversions;
        }

        private static int calculateAmountOfInversionsSlowly(int[] a) {
            int taoi = 0;
            for (int i = 0; i < a.length; i++) {
                for (int j = i + 1; j < a.length; j++) {
                    if (a[i] > a[j]) {
                        taoi++;
                    }
                }
            }
            return taoi;
        }

}

