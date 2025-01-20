import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class InverseBWT {

    int countChars[];
    int lastToFirst[];
    Queue<Integer> sortedBWTletterOcurrences[];

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

    public static int letterToIndex(char letter) {
        switch (letter) {
            case '$':
                return 0;
            case 'A':
                return 1;
            case 'C':
                return 2;
            case 'G':
                return 3;
            case 'T':
                return 4;
            default:
                System.out.println(letter + " not found");
                return 5;
        }
    }

    public static char indexToLetter(int ind) {
        switch (ind) {
            case 0:
                return '$';
            case 1:
                return 'A';
            case 2:
                return 'C';
            case 3:
                return 'G';
            case 4:
                return 'T';
            default:
                System.out.println(ind + " not found");
                return 5;
        }
    }   

    String inverseBWT(String bwt) {
        StringBuilder result = new StringBuilder();

        lastToFirst = new int[bwt.length()];
        sortedBWTletterOcurrences = new Queue[5];

        // write your code here
        for (int i = 0; i < 5; i++) {
            //lastToFirst[i] = new LinkedList<Integer>();
            sortedBWTletterOcurrences[i] = new LinkedList<Integer>();
        }

        countChars = new int[5];

        for (int l = 0; l < bwt.length(); l++) {
            countChars[letterToIndex(bwt.charAt(l))]++;
            //lastToFirst[letterToIndex(bwt.charAt(l))].add(l);
        }

        StringBuffer sortedBWTChars = new StringBuffer();

        for (int i = 0; i < countChars.length; i++) {
            for (int j = 0; j < countChars[i]; j++) {
                sortedBWTChars.append(indexToLetter(i));
            }
        }
        
        //System.out.println("sorted BWT: " + sortedBWTChars.toString());

        for (int i = 0; i < sortedBWTChars.length(); i++) {
            sortedBWTletterOcurrences[letterToIndex(sortedBWTChars.charAt(i))].add(i);
        }

        for (int i = 0; i < bwt.length(); i++) {
            lastToFirst[i] = sortedBWTletterOcurrences[letterToIndex(bwt.charAt(i))].poll();
            //System.out.println(sortedBWTChars.charAt(i) + "->" + sortedBWTChars.charAt(lastToFirst[i]));
        }

        int nextChar = 0;

        for (int i = 0; i < bwt.length(); i++) {
            result.append(sortedBWTChars.charAt(nextChar));
            nextChar = lastToFirst[nextChar];

        }
        
        result.reverse();
        return result.toString();
    }

    static public void main(String[] args) throws IOException {
        new InverseBWT().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String bwt = scanner.next();
        System.out.println(inverseBWT(bwt));
    }
}
