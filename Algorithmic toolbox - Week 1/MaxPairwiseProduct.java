import java.util.*;
import java.io.*;

public class MaxPairwiseProduct {

    static long getMaxPairwiseProductFast(int[] numbers) {
        int first_max;
        int second_max;
        int n = numbers.length;

        if (numbers[0] < numbers[1]) {
            first_max = numbers[1];
            second_max = numbers[0];
        }
        else {
            first_max = numbers[0];
            second_max = numbers[1];
        }

        for (int i = 2; i < n; i++) {
            if (second_max < numbers[i]){
                if(first_max < numbers[i]){
                    second_max = first_max;
                    first_max = numbers[i];
                }
                else{
                    second_max = numbers[i];
                }
            }            
        }
        return (long)first_max * second_max;
    }

    static long getMaxPairwiseProduct(int[] numbers) {
        long max_product = 0;
        int n = numbers.length;

        for (int first = 0; first < n; ++first) {
            for (int second = first + 1; second < n; ++second) {
                max_product = Math.max(max_product, (long)numbers[first] * numbers[second]);
            }
        }

        return max_product;
    }

    

    public static void main(String[] args) {

        /*
        while(true)
        {
            Random rnd = new Random();
            int stN = rnd.nextInt(10)+2;
            int[] stNumbers = new int[stN];
        
            for(int i = 0; i < stN; i++)
            {
                stNumbers[i] = rnd.nextInt(10);
            }
        
            if(getMaxPairwiseProductFast(stNumbers) != getMaxPairwiseProduct(stNumbers))
            {
                System.out.println("Wrong output: " + getMaxPairwiseProductFast(stNumbers) + " "
                        + getMaxPairwiseProduct(stNumbers));
                break;
            }
            else
            {
                System.out.println("OK");
            }
        }
        */
        
        FastScanner scanner = new FastScanner(System.in);
        int n = scanner.nextInt();
        int[] numbers = new int[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = scanner.nextInt();
        }
        System.out.println(getMaxPairwiseProductFast(numbers));
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(InputStream stream) {
            try {
                br = new BufferedReader(new
                    InputStreamReader(stream));
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
