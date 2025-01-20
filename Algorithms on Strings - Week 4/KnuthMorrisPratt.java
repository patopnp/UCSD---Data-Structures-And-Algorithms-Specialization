import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class KnuthMorrisPratt {
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

    // Find all the occurrences of the pattern in the text and return
    // a list of all positions in the text (starting from 0) where
    // the pattern starts in the text.
    public List<Integer> findPattern(String pattern, String text) {
        List<Integer> result = knuthMorrisPratt(pattern, text);
        // Implement this function yourself
        return result;
    }

    public int[] computePrefixFunction(String p) {
        int[] s = new int[p.length()];
        int border = 0;

        for (int i = 1; i < p.length(); i++) {
            while (border > 0 && p.charAt(i) != p.charAt(border)) {
                border = s[border - 1];
            }
            if (p.charAt(i) == p.charAt(border)) {
                border++;
            } else {
                border = 0;
            }
            s[i] = border;
        }
        return s;
    }

    public List<Integer> knuthMorrisPratt(String pattern, String text) {
        String patternAndText = pattern + '$' + text;
        List<Integer> result = new ArrayList<Integer>();
        int prefixFunction[] = computePrefixFunction(patternAndText);

        for (int i = 0; i < prefixFunction.length; i++) {
            if (prefixFunction[i] == pattern.length()) {
                result.add(i - 2 * pattern.length());
            }
        }
        return result;
    }

    static public void main(String[] args) throws IOException {
        new KnuthMorrisPratt().run();
    }

    public void print(List<Integer> x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String pattern = scanner.next();
        String text = scanner.next();
        List<Integer> positions = findPattern(pattern, text);
        print(positions);
    }
}
