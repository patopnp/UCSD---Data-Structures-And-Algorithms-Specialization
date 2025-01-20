import java.util.*;

public class DotProduct {
    private static long maxDotProduct(int[] a, int[] b) {
        long result = 0;
        for (int i = 0; i < a.length; i++) {
            result += (long)maxNumberInArray(a) * (long)maxNumberInArray(b);
        }
        return result;
    }


    private static int maxNumberInArray(int[] a) {
	int maxNumber = 0;
	int indexMaxNumber = 0;
	for (int i = 0; i < a.length; i++) {
	    if (a[i] > maxNumber) {
		maxNumber = a[i];
		indexMaxNumber = i;
	    }
	}
	a[indexMaxNumber] = 0;
	return maxNumber;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            b[i] = scanner.nextInt();
        }
        System.out.println(maxDotProduct(a, b));
    }
}

