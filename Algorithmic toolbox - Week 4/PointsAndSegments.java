import java.util.Scanner;

public class PointsAndSegments {

    private static int[] fastCountSegments(int[] starts, int[] ends, int[] points) {

        int[] cnt = new int[points.length];

        int[] ind = new int[points.length];

        for (int i = 0; i < points.length; i++) {
            ind[i] = i;
        }

        randomizedQuickSort(starts, 0, starts.length - 1);
        randomizedQuickSort(ends, 0, ends.length - 1);
        randomizedQuickSortWithIndexes(points, ind, 0, points.length - 1);

        int lastStartingPointChecked = 0;
        int lastEndingPointChecked = 0;

        for (int i = 0; i < points.length; i++) {
            while (lastEndingPointChecked < ends.length && ends[lastEndingPointChecked] < points[i]) {
                lastEndingPointChecked++;
            }

            while (lastStartingPointChecked < starts.length && starts[lastStartingPointChecked] <= points[i]) {
                lastStartingPointChecked++;
            }

            cnt[ind[i]] = lastStartingPointChecked - lastEndingPointChecked;
        }

        return cnt;
    }

    private static void randomizedQuickSort(int[] a, int left, int right) {
        if (left >= right) {
            return;
        }

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

    private static void randomizedQuickSortWithIndexes(int[] a, int[] ind, int left, int right) {
        if (left >= right) {
            return;
        }

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

        int auxInd = ind[left];
        ind[left] = ind[pivot];
        ind[pivot] = auxInd;

        int bounds[] = partition3WithIndexes(a, ind, left, right);
        randomizedQuickSortWithIndexes(a, ind, left, bounds[0] - 1);
        randomizedQuickSortWithIndexes(a, ind, bounds[1] + 1, right);

    }

    // split in three intervals
    private static int[] partition3WithIndexes(int[] a, int[] ind, int left, int right) {
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

                int auxInd = ind[j];
                ind[j] = ind[i];
                ind[i] = auxInd;

                aux = a[j];
                a[j] = a[m];
                a[m] = aux;

                auxInd = ind[j];
                ind[j] = ind[m];
                ind[m] = auxInd;

            } else if (a[i] < x) {
                j++;
                int aux = a[j];
                a[j] = a[i];
                a[i] = aux;

                int auxInd = ind[j];
                ind[j] = ind[i];
                ind[i] = auxInd;
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

            int auxInd = ind[rindex];
            ind[rindex] = ind[i];
            ind[i] = auxInd;

            rindex--;
        }
        return new int[] { j - (m - left), j };
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n, m;
        n = scanner.nextInt();
        m = scanner.nextInt();
        int[] starts = new int[n];
        int[] ends = new int[n];
        int[] points = new int[m];
        for (int i = 0; i < n; i++) {
            starts[i] = scanner.nextInt();
            ends[i] = scanner.nextInt();
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //use fastCountSegments
        int[] cnt = fastCountSegments(starts, ends, points);
        for (int x : cnt) {
            System.out.print(x + " ");
        }
    }
}

