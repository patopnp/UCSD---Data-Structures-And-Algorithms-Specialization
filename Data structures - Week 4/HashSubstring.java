import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class HashSubstring {

    private static FastScanner in;
    private static PrintWriter out;

    public static void main(String[] args) throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        printOccurrences(positionsByRabinKarp(readInput()));
        out.close();
    }

    private static Data readInput() throws IOException {
        String pattern = in.next();
        String text = in.next();
        return new Data(pattern, text);
    }

    private static void printOccurrences(List<Integer> ans) throws IOException {
        for (Integer cur : ans) {
            out.print(cur);
            out.print(" ");
        }
    }

    private static List<Integer> positionsByRabinKarp(Data input) {

        String t = input.text;
        String pattern = input.pattern;

        long p = 1000000003L;
        long x = (long) (new Random().nextInt(1000000003-1))+1 /*% (100003L-1)*/;
        List<Integer> positions = new LinkedList<Integer>();
        long pHash = polyHash(pattern, p, x);
        long[] h = precomputeHashes(t, pattern.length(), p, x);

        for (int i = 0; i <= t.length() - pattern.length(); i++) {
            if (pHash != h[i]) {
                continue;
            }
            if (t.substring(i, i + pattern.length()).equals(pattern)) {
                positions.add(i);
            }
        }
        return positions;
    }

    private static long polyHash(String s, long prime, long x) {
        long hash = 0;
        for (int i = s.length() - 1; i >= 0; --i) {

            hash = (((hash * x + s.charAt(i)) % prime)) ;
            if (hash < 0) {
                hash += prime;
            }

        }
        return hash;
    }

    static long[] precomputeHashes(String t, int size, long prime, long x) {
        long h[] = new long[t.length() - size + 1];
        String tSubstring = t.substring(t.length() - size, t.length());
        h[t.length() - size] = polyHash(tSubstring, prime, x);
        long y = 1;

        for (int i = 1; i <= size; i++) {
            y = (y * x) % prime;
        }
        if (y < 0) {
            y += prime;
        }

        for (int i = t.length() - size - 1; i >= 0; i--) {
            h[i] = ((x * h[i + 1]) + ((((((long)t.charAt(i)) - ((long)t.charAt(i + size)) * y ))))) % prime;
            if (h[i] < 0) {
                h[i] += prime;
            }
        }

        return h;
    }

    static class Data {
        String pattern;
        String text;
        public Data(String pattern, String text) {
            this.pattern = pattern;
            this.text = text;
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}

