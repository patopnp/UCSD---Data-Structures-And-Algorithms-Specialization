import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.BiFunction;

public class BWMatching {

    public int letterToIndex(char letter) {
        switch(letter) {
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
                return 5;
        }
    }

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

    // Preprocess the Burrows-Wheeler Transform bwt of some text
    // and compute as a result:
    //   * starts - for each character C in bwt, starts[C] is the first position
    //       of this character in the sorted array of
    //       all characters of the text.
    //   * occ_count_before - for each character C in bwt and each position P in bwt,
    //       occ_count_before[C][P] is the number of occurrences of character C in bwt
    //       from position 0 to position P inclusive.
    private void PreprocessBWT(String bwt, int[] starts, int[][] occ_counts_before) {
        // Implement this function yourself
        
        char sortedCharsArray[] = bwt.toCharArray();
        Arrays.sort(sortedCharsArray);
        String sortedChars = new String(sortedCharsArray);

        for (int j = 0; j < 5; j++) {
            occ_counts_before[j] = new int[bwt.length()+1];
        }

        

        // start at 0 
        for (int i = 1; i <= bwt.length(); i++) {
            for (int j = 0; j < 5; j++) {
                occ_counts_before[j][i] = occ_counts_before[j][i - 1];
            }
            occ_counts_before[letterToIndex(bwt.charAt(i - 1))][i]++;
        }

        for (int i = 0; i < sortedChars.length(); i += occ_counts_before[letterToIndex(sortedChars.charAt(i))][sortedChars.length()]) {
            starts[letterToIndex(sortedChars.charAt(i))] = i;
        }
    }

    // Compute the number of occurrences of string pattern in the text
    // given only Burrows-Wheeler Transform bwt of the text and additional
    // information we get from the preprocessing stage - starts and occ_counts_before.
    int CountOccurrences(String pattern, String bwt, int[] starts, int[][] occ_counts_before) {
        // Implement this function yourself
        int top = 0;
        int bottom = bwt.length() - 1;
        int currentCharPosition = pattern.length()-1;

        while (top <= bottom) {
            if (currentCharPosition >= 0) {
                char symbol = pattern.charAt(currentCharPosition);
                currentCharPosition--;
                int[] occForSymbol = occ_counts_before[letterToIndex(symbol)];
                if (occForSymbol[bottom + 1] - occForSymbol[top] > 0) {
                    top = starts[letterToIndex(symbol)] + occForSymbol[top];
                    bottom = starts[letterToIndex(symbol)] + occForSymbol[bottom + 1] - 1;
                } else {
                    return 0;
                }
            } else {
                return bottom - top + 1;
            }
        }
        return bottom - top + 1;
    }

    static public void main(String[] args) throws IOException {
        new BWMatching().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String bwt = scanner.next();
        // Start of each character in the sorted list of characters of bwt,
        // see the description in the comment about function PreprocessBWT
        int[] starts = new int[5];
        // Occurrence counts for each character and each position in bwt,
        // see the description in the comment about function PreprocessBWT
        int[][] occ_counts_before = new int[5][bwt.length()+1];
        // Preprocess the BWT once to get starts and occ_count_before.
        // For each pattern, we will then use these precomputed values and
        // spend only O(|pattern|) to find all occurrences of the pattern
        // in the text instead of O(|pattern| + |text|).
        PreprocessBWT(bwt, starts, occ_counts_before);
        int patternCount = scanner.nextInt();
        String[] patterns = new String[patternCount];
        int[] result = new int[patternCount];
        for (int i = 0; i < patternCount; ++i) {
            patterns[i] = scanner.next();
            result[i] = CountOccurrences(patterns[i], bwt, starts, occ_counts_before);
        }
        print(result);
    }
}
