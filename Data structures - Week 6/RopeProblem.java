import java.io.*;
import java.util.*;

public class RopeProblem {

	BufferedReader br;
	PrintWriter out;
	StringTokenizer st;
	boolean eof;
	// Splay tree implementation

	// Vertex of a splay tree
	class Vertex {
		char key;
		int size;
		Vertex left;
		Vertex right;
		Vertex parent;

		Vertex(char key, int size, Vertex left, Vertex right, Vertex parent) {
			this.key = key;
			this.size = size;
			this.left = left;
			this.right = right;
			this.parent = parent;
		}
	}

	void update(Vertex v) {
		if (v == null)
			return;
		v.size = 1 + (v.left != null ? v.left.size : 0) + (v.right != null ? v.right.size : 0);
		if (v.left != null) {
			v.left.parent = v;
		}
		if (v.right != null) {
			v.right.parent = v;
		}
	}

	void smallRotation(Vertex v) {
		Vertex parent = v.parent;
		if (parent == null) {
			return;
		}
		Vertex grandparent = v.parent.parent;
		if (parent.left == v) {
			Vertex m = v.right;
			v.right = parent;
			parent.left = m;
		} else {
			Vertex m = v.left;
			v.left = parent;
			parent.right = m;
		}
		update(parent);
		update(v);
		v.parent = grandparent;
		if (grandparent != null) {
			if (grandparent.left == parent) {
				grandparent.left = v;
			} else {
				grandparent.right = v;
			}
		}
	}

	void bigRotation(Vertex v) {
		if (v.parent.left == v && v.parent.parent.left == v.parent) {
			// Zig-zig
			smallRotation(v.parent);
			smallRotation(v);
		} else if (v.parent.right == v && v.parent.parent.right == v.parent) {
			// Zig-zig
			smallRotation(v.parent);
			smallRotation(v);
		} else {
			// Zig-zag
			smallRotation(v);
			smallRotation(v);
		}
	}

	// Makes splay of the given vertex and returns the new root.
	Vertex splay(Vertex v) {
		if (v == null)
			return null;
		while (v.parent != null) {
			if (v.parent.parent == null) {
				smallRotation(v);
				break;
			}
			bigRotation(v);
		}
		return v;
	}

	class VertexPair {
		Vertex left;
		Vertex right;

		VertexPair() {
		}

		VertexPair(Vertex left, Vertex right) {
			this.left = left;
			this.right = right;
		}
	}

	// get the p-th element
	Vertex orderStatistics(Vertex root, int p) {

		int s = root.left != null ? root.left.size : 0;
		do {
			s = root.left != null ? root.left.size : 0;
			if (p == s + 1) {
				return root;
			} else if (p < s + 1) {
				root = root.left;
			} else {
				root = root.right;
				p = p - s - 1;
			}
		} while (true);
	}

	VertexPair split(Vertex root, int p) {
		VertexPair result = new VertexPair();

		if (p == 0) {
			result.left = null;
			result.right = root;
			return result;
		}
		Vertex pth_element = orderStatistics(root, p);
		root = splay(pth_element);

		if (root.right != null) {
			root.size -= root.right.size;
			root.right.parent = null;
			result.right = root.right;
			root.right = null;
		}
		result.left = root;

		return result;
	}

	Vertex merge(Vertex left, Vertex right) {
		if (left == null)
			return right;
		if (right == null)
			return left;
		while (right.left != null) {
			right = right.left;
		}
		right = splay(right);
		right.left = left;
		update(right);
		return right;
	}

	// Code that uses splay tree to solve the problem

	Vertex root = null;

	void insert(char x) {
		Vertex yes = root;

		if (yes.right == null) {
			yes.right = new Vertex(x, 1, null, null, yes);
		} else {
			while (yes.right != null) {
				yes = yes.right;
			}
		}

		yes.right = new Vertex(x, 1, null, null, yes);
		root = splay(yes.right);

	}

	class Rope {
		String s;

		void process(int i, int j, int k) {
			// Replace this code with a faster implementation
			String t = s.substring(0, i) + s.substring(j + 1);
			s = t.substring(0, k) + s.substring(i, j + 1) + t.substring(k);
		}

		String result() {
			return s;
		}

		Rope(String s) {
			this.s = s;
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

	public static void main(String[] args) throws IOException {
		new RopeProblem().run();
	}

	void inOrder(Vertex root) {
		/* ArrayList<Integer> result = new ArrayList<Integer>(); */
		// Finish the implementation
		// inOrderTraversal(0, result);

		Stack<Vertex> inOrderRemainingElements = new Stack<Vertex>();
		inOrderRemainingElements.add(root);

		while (inOrderRemainingElements.peek().left != null) {
			inOrderRemainingElements.add(inOrderRemainingElements.peek().left);
		}

		while (!inOrderRemainingElements.empty()) {

			Vertex nextElement = inOrderRemainingElements.pop();
			System.out.print(nextElement.key);

			if (nextElement.right != null) {
				inOrderRemainingElements.add(nextElement.right);
				while (inOrderRemainingElements.peek().left != null) {
					inOrderRemainingElements.add(inOrderRemainingElements.peek().left);
				}
			}
		}
	}

	public void print(Vertex node) {
		if (node == null) {
			return;
		}
		print(node.left);
		System.out.print(node.key);
		print(node.right);
	}

	public String result(Vertex node) {
		if (node == null) {
			return "";
		}
		String result = result(node.left);
		result += node.key;
		result += result(node.right);
		return result;
	}

	public Vertex executeQuery(int i, int j, int ok) {

		VertexPair leftAndRightTree = split(root, i);
		VertexPair middleAndRightTree = split(leftAndRightTree.right, j - i + 1);
		Vertex remainingStringAsTree = merge(leftAndRightTree.left, middleAndRightTree.right);
		VertexPair destinationPlaceAndRightMostTree = split(remainingStringAsTree, ok);
		Vertex result = merge(destinationPlaceAndRightMostTree.left,
				merge(middleAndRightTree.left, destinationPlaceAndRightMostTree.right));

		return result;
	}

	protected String getSaltString(int length) {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < length) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	public void run() throws IOException {
		FastScanner in = new FastScanner();
		String willBeSolved = in.next();

		root = new Vertex(willBeSolved.charAt(0), 1, null, null, null);

		for (int j = 1; j < willBeSolved.length(); j++) {
			insert(willBeSolved.charAt(j));
		}

		for (int q = in.nextInt(); q > 0; q--) {
			int i = in.nextInt();
			int j = in.nextInt();
			int ok = in.nextInt();

			root = executeQuery(i, j, ok);
		}

		// inOrderTraversal(root);

		inOrder(root);
	}
}
