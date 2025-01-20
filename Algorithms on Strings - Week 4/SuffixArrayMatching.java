import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SuffixArrayMatching {
    class fastscanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        fastscanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextint() throws IOException {
            return Integer.parseInt(next());
        }
    }

    public static int letterToIndex(char c) {
        switch (c) {
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
    
    int[] computeCharClasses(String s, int[] order) {

        int[] classes = new int[s.length()];

        for (int i = 1; i < order.length; i++) {
            if ((letterToIndex(s.charAt(order[i]))) == (letterToIndex(s.charAt(order[i - 1])))) {
                classes[order[i]] = classes[order[i - 1]];
            } else {
                classes[order[i]] = classes[order[i - 1]] + 1;
            }
        }

        return classes;
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
            } else {
                newClasses[cur] = newClasses[prev];
            }
        }
        return newClasses;
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
    
    public int[] computeSuffixArray(String text) {

        // write your code here
        int[] suffixArray = sortCharacter(text);
        int[] classes = computeCharClasses(text, suffixArray);
        int l = 1;

        while (l < text.length()) {
        	suffixArray = sortDoubled(text, l, suffixArray, classes);
            classes = updateClassses(suffixArray, classes, l);
            l *= 2;
        }
        
        return suffixArray;
    }

    public List<Integer> findOccurrences(String pattern, String text, int[] suffixArray) {
        List<Integer> result = new ArrayList<>();

        // write your code here
        int minIndex = 0;
        int maxIndex = text.length();
        
        while (minIndex < maxIndex) {
        	int midIndex = (minIndex + maxIndex)/2;
        	
        	int suffixLength = text.length() - suffixArray[midIndex];
        	
        	suffixLength = suffixLength<pattern.length()?suffixLength:pattern.length();
        	
        	if (pattern.compareTo(text.substring(suffixArray[midIndex], suffixArray[midIndex] + suffixLength)) > 0) {
        		minIndex = midIndex + 1;
        	}
        	else {
        		maxIndex = midIndex;
        	}
        }
        int start = minIndex;
        maxIndex = text.length();
        
        while (minIndex < maxIndex) {
        	int midIndex = (minIndex + maxIndex)/2;
        	
        	int suffixLength = text.length() - suffixArray[midIndex];
        	
        	suffixLength = suffixLength<pattern.length()?suffixLength:pattern.length();
        	
        	if (pattern.compareTo(text.substring(suffixArray[midIndex], suffixArray[midIndex] + suffixLength)) < 0) {
        		maxIndex = midIndex;
        	}
        	else {
        		minIndex = midIndex + 1;
        	}
        }
        int end = maxIndex;
        
    	for (int i = start; i < end; i++) {
    		result.add(suffixArray[i]);
    	}
        
        return result;
    }

    static public void main(String[] args) throws IOException {
        new SuffixArrayMatching().run();
    }

    public void print(boolean[] x) {
        for (int i = 0; i < x.length; ++i) {
            if (x[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }

    public void run() throws IOException {
        fastscanner scanner = new fastscanner();
        String text = scanner.next() + "$";
        int[] suffixArray = computeSuffixArray(text);
        int patternCount = scanner.nextint();
        boolean[] occurs = new boolean[text.length()];
        for (int patternIndex = 0; patternIndex < patternCount; ++patternIndex) {
            String pattern = scanner.next();
            List<Integer> occurrences = findOccurrences(pattern, text, suffixArray);
            for (int x : occurrences) {
                occurs[x] = true;
            }
        }
        print(occurs);
    }
}
