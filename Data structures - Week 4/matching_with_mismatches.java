import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class matching_with_mismatches {
    public List<Integer> solve(int k, String text, String pattern) {
        ArrayList<Integer> pos = new ArrayList<>();
        return pos;
    }

    long[] hvt;
    long[] hvs;

    long[] hvt2;
    long[] hvs2;

    private static long substringHash(int from, int length, long x, long m, long[] h) {

        long xl = 1;

        for (int i = 1; i <= length; i++) {
            xl = (xl * x) % m;
        }
        if (xl < 0) {
            xl += m;
        }

        long value = (h[from + length] - xl * h[from]) % m;

        if (value < 0) {
            value += m;
        }

        return value;
    }

    static long[] precomputeHashes(String t, long m, long x) {
        long h[] = new long[t.length() + 1];
        h[0] = 0;

        for (int i = 1; i <= t.length(); i++) {
            h[i] = (x * h[i - 1] + (long) t.charAt(i - 1)) % m;
            if (h[i] < 0) {
                h[i] += m;
            }
        }

        return h;
    }

    static int findNextMismatchSlow(String s, String t, int index_s, int index_t) {
        for (int j = index_t, q = index_s; j < t.length() && q < s.length(); j++, q++) {
            if (t.charAt(j) != s.charAt(q)) {
                return q;
            }
        }
        return -1;
    }

    static int findNextMismatch(String s, String t, int index_s, int index_t, long[] hvs, long[] hvt, long[] hvs2,
            long[] hvt2) {

        if (index_s >= s.length() || index_t >= t.length()) {
            return -1;
        }

        long p2 = 10000007;
        long p = 10000003;
        int length = 1;
        int minLength = 0;
        int maxLength = Math.min(s.length()-index_s, t.length()-index_t);


        boolean found = false;
        int mismatchIndex = -1;

        while (minLength <= maxLength) {

            length = (int)((maxLength + minLength) / 2);
            found = false;

            if (substringHash(index_s, length, 31, p, hvs) == substringHash(index_t, length, 31, p, hvt) 
                    && substringHash(index_s, length, 31, p2, hvs2) == substringHash(index_t, length, 31, p2, hvt2)) {
                found = true;
                minLength = length + 1;
            }

            if (found == false) {
                maxLength = length - 1;
                mismatchIndex = index_s + length;
            }

            /*
             * System.out.println("length is now " + length + " maxLength is now " +
             * maxLength);
             */

        }
        /*System.out.println(mismatchIndex);*/
        return mismatchIndex-1;
    }

    public void run(String s, String t, int allowedNumMismatches) {

        //findNextMismatchSlow(s, t, 0, 0);
        /*
        int indexNextMismatch = findNextMismatchSlow(s, t, 0, 0);
        System.out.println(indexNextMismatch);

        indexNextMismatch = findNextMismatchSlow(s, t, indexNextMismatch+1, indexNextMismatch + 1);
        System.out.println(indexNextMismatch);

        indexNextMismatch = findNextMismatchSlow(s, t, indexNextMismatch + 1, indexNextMismatch + 1);
        System.out.println(indexNextMismatch);
*/
        

        long p = 10000003;

        hvs = precomputeHashes(s, p, 31);
        hvt = precomputeHashes(t, p, 31);

        long p2 = 10000007;

        hvs2 = precomputeHashes(s, p2, 31);
        hvt2 = precomputeHashes(t, p2, 31);

        int numOfMismatches = 0;
        
        int nextStartingIndex = 0;

        ArrayList<Integer> answer = new ArrayList<>();

        
        /*
        int indexNextMismatch = findNextMismatch(s, t, 0, 0, hvs, hvt, hvs2, hvt2);
        System.out.println(indexNextMismatch);
          
        indexNextMismatch = findNextMismatch(s, t, indexNextMismatch+1,
                indexNextMismatch + 1, hvs, hvt, hvs2, hvt2);
        System.out.println(indexNextMismatch);
          
        indexNextMismatch = findNextMismatch(s, t, indexNextMismatch + 1,
        indexNextMismatch + 1, hvs, hvt, hvs2, hvt2); 
        System.out.println(indexNextMismatch);
         */


         
        for (int n = 0; n <= t.length() - s.length(); n++) {
            nextStartingIndex = -1;

            numOfMismatches = 0;
            int offset = 0;
            while (numOfMismatches <= allowedNumMismatches) {

                //nextStartingIndex = findNextMismatch(s, t, nextStartingIndex, n+offset, hvs, hvt, hvs2, hvt2);
                nextStartingIndex = findNextMismatchSlow(s, t, nextStartingIndex+1, n+1+ nextStartingIndex);
                /*System.out.println(nextStartingIndex);*/
                if (nextStartingIndex == -1) {
                    answer.add(n);

                    break;
                } else {
                    numOfMismatches++;
                }
            };
        }
        
        

        /*
            while (numOfMismatches <= allowedNumMismatches){
        
                nextSartingIndex = findNextMismatch(s, t, nextSartingIndex, n);
                System.out.println("next starting index is " + nextSartingIndex);
        
                if (nextSartingIndex == -1) {
                    answer.add(n);
                    System.out.println("answer was added");
                    break;
                }
                else{
                    numOfMismatches++;
                }
            } ;
        */


        System.out.print(answer.size());

        for (int ans : answer) {
            System.out.print(" " + ans);
        }
        System.out.println();

        

        /*
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        in.lines().forEach(line -> {
            StringTokenizer tok = new StringTokenizer(line);
            int allowedNumMismatches = Integer.valueOf(tok.nextToken());
            String s = tok.nextToken();
            String t = tok.nextToken();
            findNextMismatch(s, t, 0, 0);
            List<Integer> ans = solve(k, s, t);
            out.format("%d ", ans.size());
            out.println(ans.stream()
                    .map(n -> String.valueOf(n))
                    .collect(Collectors.joining(" "))
            );
        });
        out.close();
        */
    }

    static public void main(String[] args) {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        in.lines().forEach(line -> {
            StringTokenizer tok = new StringTokenizer(line);
            int allowedNumMismatches = Integer.valueOf(tok.nextToken());
            String t = tok.nextToken();
            String s = tok.nextToken();
            new matching_with_mismatches().run(s, t, allowedNumMismatches);
            /*out.format("%d ", ans.size());
            out.println(ans.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(" ")));*/
        });
        out.close();


        //new matching_with_mismatches().run();
    }
}
