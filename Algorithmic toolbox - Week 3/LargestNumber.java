import java.util.*;

public class LargestNumber {
    private static String largestNumber(String[] a) {

        String result = "";
        for (int i = 0; i < a.length; i++) {
            result += getNextDigit(a);
        }
        return result;
    }

    private static String getNextDigit(String[] a) {

        int indexOfBiggestNumber = 0;
        String biggestNumber = "0";

        for (int i = 0; i < a.length; i++) {
            if (a[i] != null) {
                if (Integer.parseInt(biggestNumber + "" + a[i]) <= Integer.parseInt(a[i] + "" + biggestNumber)) {
                    biggestNumber = a[i];
                    indexOfBiggestNumber = i;
                }
            }

        }
        
        a[indexOfBiggestNumber] = null;
        return biggestNumber;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String[] a = new String[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.next();
        }
        System.out.println(largestNumber(a));
    }
}

