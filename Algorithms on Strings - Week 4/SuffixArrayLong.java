import java.util.*;
import java.io.*;
import java.util.zip.CheckedInputStream;

public class SuffixArrayLong {
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

    public class Suffix implements Comparable {
        String suffix;
        int start;

        Suffix(String suffix, int start) {
            this.suffix = suffix;
            this.start = start;
        }

        @Override
        public int compareTo(Object o) {
            Suffix other = (Suffix) o;
            return suffix.compareTo(other.suffix);
        }
    }

    public static int letterToIndex(char c) {
        switch(c) {
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
                return -1;
        }
    }

    int[] buildSuffixArray(String s) {
        int[] order = sortCharacter(s);
        int[] classes = computeCharClasses(s, order);
        int l = 1;

        while (l < s.length()) {
            order = sortDoubled(s, l, order, classes);
            classes = updateClassses(order, classes, l);
            l *= 2;
        }

        return order;
    }

    int[] updateClassses(int[] newOrder, int[] classes, int l) {
        int n = newOrder.length;
        int[] newClasses = new int[n];
        newClasses[newOrder[0]] = 0;

        for (int i = 1; i < n; i++) {
            int cur = newOrder[i];
            int prev = newOrder[i - 1];
            int mid = cur + l;
            int midPrev = (prev + l) % n;
            if (classes[cur] != classes[prev] || classes[mid] != classes[midPrev]) {
                newClasses[cur] = newClasses[prev] + 1;
            }
            else {
                newClasses[cur] = newClasses[prev];
            }
        }
        return newClasses;
    }

    int[] sortDoubled(String s, int l, int[] order, int[] classes) {

        int[] newOrder = new int[s.length()];
        int[] count = new int[s.length()];

        for (int i = 0; i < classes.length; i++) {
            count[classes[i]] = count[classes[i]] + 1;
        }
        for (int i = 1; i < count.length; i++) {
            count[i] = count[i] + count[i - 1];
            
        }

        for (int i = s.length() - 1; i >= 0; i--) {
            int start = ((order[i] - l + s.length()) % s.length());
            int cl = classes[start];
            newOrder[--count[cl]] = start;
        }

        return newOrder;
    }

    int[] computeCharClasses(String s, int[] order){
        
        int[] classes = new int[s.length()];

        for (int i = 1; i < order.length; i++) {
            if ((letterToIndex(s.charAt(order[i]))) == (letterToIndex(s.charAt(order[i-1])))) {
                classes[order[i]] = classes[order[i-1]];
            }
            else{
                classes[order[i]] = classes[order[i-1]]+1;
            }
        }

        return classes;
    }



    int[] sortCharacter(String s) {

        int[] order = new int[s.length()];
        int[] l = new int[5];

        for (int i = 0; i < s.length(); i++) {
            l[letterToIndex(s.charAt(i))]++;
        }

        for (int i = 1; i < l.length; i++) {
            l[i] += l[i - 1];
        }

        for (int i = s.length() - 1; i > 0; i--) {
            order[--l[letterToIndex(s.charAt(i))]] = i;
        }

        return order;
    }

    // Build suffix array of the string text and
    // return an int[] result of the same length as the text
    // such that the value result[i] is the index (0-based)
    // in text where the i-th lexicographically smallest
    // suffix of text starts.
    public int[] computeSuffixArray(String text) {
        int[] result = buildSuffixArray(text);


        return result;
    }


    static public void main(String[] args) throws IOException {
        new SuffixArrayLong().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] suffix_array = computeSuffixArray(text);
        print(suffix_array);
    }
}
