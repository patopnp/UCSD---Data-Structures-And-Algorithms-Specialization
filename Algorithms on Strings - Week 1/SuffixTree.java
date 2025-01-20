import java.util.*;
import java.io.*;

public class SuffixTree {



    static class Node {
        public static final int Letters = 5;
        public Node next[];
        public String[] edgeLabel;

        Node() {
            edgeLabel = new String[Letters];
            next = new Node[Letters];
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
    }

    static int letterToIndex(char letter) {
        switch (letter) {
            case 'A':
                return 0;
            case 'C':
                return 1;
            case 'T':
                return 2;
            case 'G':
                return 3;
            case '$':
                return 4;
            default:
                assert (false);
                return -1;
        }
    }

    static public void main(String... args) throws IOException {

        new SuffixTree().run();

    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();

        Node root = new Node();
        for (int i = 0; i < text.length(); i++) {
            // System.out.println("Added suffix " + input.substring(i) + " to the tree");
            addSuffixToTree(text.substring(i), root);
        }

        printSuffixTree(root);
    }

    public static void printSuffixTree(Node root) {

        for (int i = 0; i < 5; i++) {
            if (root.next[i] != null) {
                System.out.println(root.edgeLabel[i]);
                printSuffixTree(root.next[i]);
            }
        }

    }

    public static void addSuffixToTree(String suffix, Node root) {

        if (root.next[letterToIndex(suffix.charAt(0))] != null) {

            int i = 0;
            int j = 0;

            String edgeLabelFromRoot = root.edgeLabel[letterToIndex(suffix.charAt(0))];

            while (i < suffix.length() && j < edgeLabelFromRoot.length()
                    && suffix.charAt(i) == edgeLabelFromRoot.charAt(j)) {
                i++;
                j++;
            }

            // case when the branching happens in the middle of the edge label
            if (i < suffix.length() && j < edgeLabelFromRoot.length()) {

                Node nextNodeFromRootBeforeBranching = root.next[letterToIndex(suffix.charAt(0))];
                root.edgeLabel[letterToIndex(suffix.charAt(0))] = edgeLabelFromRoot.substring(0, j);

                Node newBranchingNode = new Node();
                root.next[letterToIndex(suffix.charAt(0))] = newBranchingNode;

                newBranchingNode.next[letterToIndex(edgeLabelFromRoot.charAt(j))] = nextNodeFromRootBeforeBranching;
                newBranchingNode.edgeLabel[letterToIndex(edgeLabelFromRoot.charAt(j))] = edgeLabelFromRoot.substring(j);

                newBranchingNode.next[letterToIndex(suffix.charAt(i))] = new Node();
                newBranchingNode.edgeLabel[letterToIndex(suffix.charAt(i))] = suffix.substring(i);
                return;
            }

            // case when it reached the end of both strings
            if (i == suffix.length() && j == root.edgeLabel[letterToIndex(suffix.charAt(0))].length()) {
                // no new node is added, suffix is already included in the tree
                return;
            }

            // case when it reached the end of the edge label
            if (i < suffix.length()) {
                root = root.next[letterToIndex(suffix.charAt(0))];
                suffix = suffix.substring(i);
                addSuffixToTree(suffix, root);
                return;
            }

            // case when it reached the end of the added suffix string
            if (j < edgeLabelFromRoot.length()) {

                // add node in the middle between root and its next
                Node nextNodeFromRootBeforeBranching = root.next[letterToIndex(suffix.charAt(0))];
                root.edgeLabel[letterToIndex(suffix.charAt(0))] = edgeLabelFromRoot.substring(0, j);
                Node newMiddleNode = new Node();
                newMiddleNode.next[letterToIndex(edgeLabelFromRoot.charAt(j))] = nextNodeFromRootBeforeBranching;
                newMiddleNode.edgeLabel[letterToIndex(edgeLabelFromRoot.charAt(j))] = edgeLabelFromRoot.substring(j);
                root.next[letterToIndex(suffix.charAt(0))] = newMiddleNode;

            }

        } else {
            root.next[letterToIndex(suffix.charAt(0))] = new Node();
            root.edgeLabel[letterToIndex(suffix.charAt(0))] = suffix;
        }

    }
}
