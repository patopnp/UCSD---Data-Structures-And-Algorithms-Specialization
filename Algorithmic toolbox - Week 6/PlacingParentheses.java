import java.util.Scanner;
import java.util.*;

public class PlacingParentheses {


    private static long getMaximValue(String exp) {

        ArrayList<Long> numbers = new ArrayList<>();
        ArrayList<Character> operations = new ArrayList<>();

        String number = "";

        for (int i = 0; i < exp.length(); i++) {
            char nextCharacter = exp.charAt(i);
            if (Character.isDigit(nextCharacter)) {
                number += "" + nextCharacter;
            } else {
                if (number.isBlank() == false) {
                    long numberAsLong = Long.parseLong(number);
                    numbers.add(numberAsLong);
                    number = "";
                }
                if (nextCharacter == '+' || nextCharacter == '-' || nextCharacter == '*') {
                    operations.add(nextCharacter);
                }
            }
        }

        if (number.isBlank() == false) {
            long numberAsLong = Long.parseLong(number);
            numbers.add(numberAsLong);
            number = "";
        }

        long[] numbersAsArray = new long[numbers.size()];
        char[] operationsAsArray = new char[operations.size()];

        for (int i = 0; i < numbers.size(); i++) {
            numbersAsArray[i] = numbers.get(i);
        }

        for (int i = 0; i < operations.size(); i++) {
            operationsAsArray[i] = operations.get(i);
        }

        return parentheses(numbersAsArray, operationsAsArray);
    }

    private static long parentheses(long[] numbers, char[] operations) {
        
        long min[][] = new long[numbers.length][numbers.length];
        long max[][] = new long[numbers.length][numbers.length];

        for (int i = 0; i < numbers.length; i++) {
            min[i][i] = max[i][i] = numbers[i];
        }
        for (int s = 1; s <= numbers.length - 1; s++) {
            for (int i = 0; i < numbers.length - s; i++) {
                int j = i + s;
                
                long[] minAndMax = minMax(i,j, min, max, operations);
                min[i][j] = minAndMax[0];
                max[i][j] = minAndMax[1];
            }
        } 

        return max[0][numbers.length-1];
    }

    private static long[] minMax(int i, int j, long[][] min, long[][] max, char[] operations) {

        long minValue = Long.MAX_VALUE;
        long maxValue = Long.MIN_VALUE;

        for (int k = i; k <= j - 1; k++) {

            long a = eval(max[i][k], max[k + 1][j], operations[k]);
            long b = eval(max[i][k], min[k + 1][j], operations[k]);
            long c = eval(min[i][k], max[k + 1][j], operations[k]);
            long d = eval(min[i][k], min[k + 1][j], operations[k]);

            minValue = minValue(minValue, a, b, c, d);
            maxValue = maxValue(maxValue, a, b, c, d);

 //           minResult = min(minResult, a, b, c, d);
 //           maxResult = max(minResult, a, b, c, d);
        }

        return new long[]{ minValue, maxValue};
    }

    private static long eval(long a, long b, char op) {
        if (op == '+') {
            return a + b;
        } else if (op == '-') {
            return a - b;
        } else if (op == '*') {
            return a * b;
        } else {
            assert false;
            return 0;
        }
    }

    private static long minValue(long first, long second, long third, long fourth, long fifth) {
        long minValue = first;
        if (second < minValue) {
            minValue = second;
        }
        if (third < minValue) {
            minValue = third;
        }
        if (fourth < minValue) {
            minValue = fourth;
        }
        if (fifth < minValue) {
            minValue = fourth;
        }
        return minValue;
    }

    private static long maxValue(long first, long second, long third, long fourth, long fifth) {
        long maxValue = first;
        if (second > maxValue) {
            maxValue = second;
        }
        if (third > maxValue) {
            maxValue = third;
        }
        if (fourth > maxValue) {
            maxValue = fourth;
        }
        if (fifth > maxValue) {
            maxValue = fourth;
        }
        return maxValue;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String exp = scanner.next();
        System.out.println(getMaximValue(exp));
    }
}

