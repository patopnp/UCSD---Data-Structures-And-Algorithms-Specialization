import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SuffixTreeFromArray {

	static int edgeAmount = 0;
	
    static class SuffixTreeNode {

        static SuffixTreeNode breakEdge(SuffixTreeNode node, String s, int start, int offset) {
            char startChar = s.charAt(start);
            char midChar = s.charAt(start + offset);
            SuffixTreeNode midNode = new SuffixTreeNode();
            midNode.parent = node;
            midNode.stringDepth = node.stringDepth + offset;
            midNode.edgeStart = start;
            midNode.edgeEnd = start + offset - 1;
            midNode.children[letterToIndex(midChar)] = node.children[letterToIndex(startChar)];
            node.children[letterToIndex(startChar)].parent = midNode;
            node.children[letterToIndex(startChar)].edgeStart += offset;
            node.children[letterToIndex(startChar)] = midNode;
            return midNode;
        }

        static SuffixTreeNode createNewLeaf(SuffixTreeNode node, String s, int suffix) {
            SuffixTreeNode leaf = new SuffixTreeNode();
            // leaf.children = new HashMap<Character, SuffixTreeNode>();
            leaf.parent = node;
            leaf.stringDepth = s.length() - suffix;
            leaf.edgeStart = suffix + node.stringDepth;
            leaf.edgeEnd = s.length() - 1;
            node.children[letterToIndex(s.charAt(leaf.edgeStart))] = leaf;
            return leaf;
        }

        static SuffixTreeNode createRootNode() {
            SuffixTreeNode root = new SuffixTreeNode();
            // root.children = new HashMap<Character, SuffixTreeNode>();
            root.edgeEnd = -1;
            root.edgeStart = -1;
            return root;
        }

        SuffixTreeNode parent;
        SuffixTreeNode[] children = new SuffixTreeNode[5];
        int stringDepth;
        int edgeStart;
        int edgeEnd;

    }

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

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    public static void traverseTreePrintingEdges(SuffixTreeNode stn) {
    	
    	Stack<SuffixTreeNode> childrenNodes  = new Stack<SuffixTreeNode>();
    	Stack<Integer> childrenNumberVisit = new Stack<Integer>();
    	
    	childrenNodes.add(stn);
    	childrenNumberVisit.add(0);
    	
    	while(!childrenNodes.isEmpty()) {
    		int childrenVisit = childrenNumberVisit.pop();
    		
    		SuffixTreeNode currNode = childrenNodes.peek();
    		
    		while(childrenVisit < 5 && currNode.children[childrenVisit] == null) {
    			childrenVisit++;
    		}
    		if (childrenVisit == 5) {
    			
    			childrenNodes.pop();
    			continue;
    		}
    		
    		System.out.println(currNode.children[childrenVisit].edgeStart + " " + (currNode.children[childrenVisit].edgeEnd+1));
    		childrenNodes.add(currNode.children[childrenVisit]);
    		childrenNumberVisit.add(++childrenVisit);
    		childrenNumberVisit.add(0);
    		
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

    public SuffixTreeNode suffixTreeFromSuffixArray(String s, int[] order, int[] lcpArray) {
        SuffixTreeNode root = SuffixTreeNode.createRootNode();
        int lcpPrev = 0;
        SuffixTreeNode curNode = root;

        for (int i = 0; i < s.length(); i++) {
            int suffix = order[i];
            while (curNode.stringDepth > lcpPrev) {
                curNode = curNode.parent;
            }
            if (curNode.stringDepth == lcpPrev) {
                curNode = SuffixTreeNode.createNewLeaf(curNode, s, suffix);
            } else {
                int edgeStart = order[i - 1] + curNode.stringDepth;
                int offset = lcpPrev - curNode.stringDepth;
                SuffixTreeNode midNode = SuffixTreeNode.breakEdge(curNode, s, edgeStart, offset);
                curNode = SuffixTreeNode.createNewLeaf(midNode, s, suffix);
            }
            if (i < s.length() - 1) {
                lcpPrev = lcpArray[i];
            }
        }
        return root;
    }

    int[] invertSuffixArray(int[] order) {
        int[] pos = new int[order.length];
        for (int i = 0; i < pos.length; i++) {
            pos[order[i]] = i;
        }
        return pos;
    }

    public int lcpOfSuffixes(String s, int i, int j, int equal) {
        int lcp = Math.max(0, equal);
        while (i + lcp < s.length() && j + lcp < s.length()) {
            if (s.charAt(i + lcp) == s.charAt(j + lcp)) {
                lcp++;
            } else {
                break;
            }
        }
        return lcp;
    }

    static public void main(String[] args) throws IOException {
        new SuffixTreeFromArray().run();
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
        String text = scanner.next();
        int[] suffixArray = new int[text.length()];
        for (int i = 0; i < suffixArray.length; ++i) {
            suffixArray[i] = scanner.nextInt();
        }
        int[] lcpArray = new int[text.length() - 1];
        for (int i = 0; i + 1 < text.length(); ++i) {
            lcpArray[i] = scanner.nextInt();
        }
        System.out.println(text);

        SuffixTreeNode root = suffixTreeFromSuffixArray(text, suffixArray, lcpArray);
        traverseTreePrintingEdges(root);
        //System.out.println(edgeAmount);
    }
}
