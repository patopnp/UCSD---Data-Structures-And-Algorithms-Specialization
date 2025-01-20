import java.util.*;
import java.io.*;

public class tree_height {
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

	public class TreeHeight {
		int n;
		int parent[];
		Node nodes[];
		int rootIndex;
		Node root;

		void read() throws IOException {
			FastScanner in = new FastScanner();
			n = in.nextInt();
			parent = new int[n];
			for (int i = 0; i < n; i++) {
				parent[i] = in.nextInt();
			}
		}

		void buildTree() {
			int n = parent.length;
			nodes = new Node[n];

			for (int i = 0; i < n; i++) {
				nodes[i] = new Node();
			}

			for (int i = 0; i < n; i++) {
				if (parent[i] == -1) {
					//revisar si se asigna el root correcto
					rootIndex = i;
				}
				else {
					nodes[parent[i]].addChild(nodes[i]);
				}
			}

			root = nodes[rootIndex];
		}

		int computeHeight() {
			
			/*
			int maxHeight = 0;
			for (int vertex = 0; vertex < n; vertex++) {
				int height = 0;
				for (int i = vertex; i != -1; i = parent[i])
					height++;
				maxHeight = Math.max(maxHeight, height);
			}
			return maxHeight;
			*/

			return root.heightWithStack();
		}
	}

	class Node {
		
		LinkedList<Node> children;

		Node() {
			children = new LinkedList<>();
		}

		void addChild(Node nChild) {
			children.add(nChild);
		}

		int height() {
			int maxHeight = 0;
			for (Node nChild : children) {
				int nextHeight = nChild.height();
				if (nextHeight > maxHeight) {
					maxHeight = nextHeight;
				}
			}

			return maxHeight + 1;
		}

		int heightWithStack() {

			int maxDepth = 0;

			Stack<Node> nodesToVisit = new Stack<Node>();
			Stack<Integer> depthOfNodesToVisit = new Stack<Integer>();
			nodesToVisit.push(this);
			depthOfNodesToVisit.push(1);
			while (nodesToVisit.empty() == false) {
				Node nextVisitedNode = nodesToVisit.pop();
				int currentDepth = depthOfNodesToVisit.pop();

				if (maxDepth < currentDepth) {
					maxDepth = currentDepth;
				}

				for (Node childNode : nextVisitedNode.children) {
					nodesToVisit.push(childNode);
					depthOfNodesToVisit.push(currentDepth + 1);
				}
			}

			return maxDepth;
		}
	}

	static public void main(String[] args) throws IOException {
            new Thread(null, new Runnable() {
                    public void run() {
                        try {
                            new tree_height().run();
                        } catch (IOException e) {
                        }
                    }
                }, "1", 1 << 26).start();
	}
	public void run() throws IOException {
		TreeHeight tree = new TreeHeight();
		tree.read();
		tree.buildTree();
		System.out.println(tree.computeHeight());
	}
}
