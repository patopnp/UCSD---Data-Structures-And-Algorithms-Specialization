import java.util.*;
import java.io.*;

public class tree_orders {
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

	public class TreeOrders {
		int n;
		int[] key, left, right;
		
		void read() throws IOException {
			FastScanner in = new FastScanner();
			n = in.nextInt();
			key = new int[n];
			left = new int[n];
			right = new int[n];
			for (int i = 0; i < n; i++) { 
				key[i] = in.nextInt();
				left[i] = in.nextInt();
				right[i] = in.nextInt();
			}
		}

		List<Integer> inOrder() {
			ArrayList<Integer> result = new ArrayList<Integer>();
			// Finish the implementation
			// You may need to add a new recursive method to do that
			//inOrderTraversal(0, result);

			Stack<Integer> inOrderRemainingElements = new Stack<Integer>();
			inOrderRemainingElements.add(0);

			while (left[inOrderRemainingElements.peek()] != -1) {
				inOrderRemainingElements.add(left[inOrderRemainingElements.peek()]);
			}

			while (!inOrderRemainingElements.empty()) {

				int nextElement = inOrderRemainingElements.pop();
				result.add(key[nextElement]);

				if (right[nextElement] != -1) {
					inOrderRemainingElements.add(right[nextElement]);
					while (left[inOrderRemainingElements.peek()] != -1) {
						inOrderRemainingElements.add(left[inOrderRemainingElements.peek()]);
					}
				}
			}

			return result;
		}

		private void inOrderTraversal(int index, List<Integer> result) {
			if (index == -1) {
				return;
			}
			inOrderTraversal(left[index], result);
			result.add(key[index]);
			inOrderTraversal(right[index], result);
		}

		List<Integer> preOrder() {
			ArrayList<Integer> result = new ArrayList<Integer>();
			// Finish the implementation
			// You may need to add a new recursive method to do that
			//preOrderTraversal(0, result);

			Stack<Integer> preOrderRemainingElements = new Stack<Integer>();
			preOrderRemainingElements.add(0);

			while (!preOrderRemainingElements.empty()) {

				int nextElement = preOrderRemainingElements.pop();
				result.add(key[nextElement]);

				if (right[nextElement] != -1) {
					preOrderRemainingElements.add(right[nextElement]);
				}
				if (left[nextElement] != -1) {
					preOrderRemainingElements.add(left[nextElement]);
				}
			}

			return result;
		}

		private void preOrderTraversal(Queue<Integer> result) {

			Queue<Integer> preOrderRemainingElements = new LinkedList<Integer>();
			preOrderRemainingElements.add(0);

			while (preOrderRemainingElements.isEmpty() == false) {
				
				int index = preOrderRemainingElements.remove();

				result.add(key[index]);
				
				if (left[index] != -1) {
					preOrderRemainingElements.add(left[index]);
				}
				if((right[index] == -1) == false) {
					preOrderRemainingElements.add(right[index]);
				}

			};
		}

		List<Integer> postOrder() {
			ArrayList<Integer> result = new ArrayList<Integer>();
			// Finish the implementation
			// You may need to add a new recursive method to do that
			//postOrderTraversal(0, result);
			
			Stack<Integer> postOrderRemainingElements = new Stack<Integer>();
			Stack<Integer> postOrderElementRemainingVisits = new Stack<Integer>();

			postOrderElementRemainingVisits.add(1);

			postOrderRemainingElements.add(0);
			postOrderElementRemainingVisits.add(2);


			while (!postOrderRemainingElements.empty()) {

				if (postOrderElementRemainingVisits.peek() == 2) {
					if (left[postOrderRemainingElements.peek()] != -1) {
						postOrderRemainingElements.add(left[postOrderRemainingElements.peek()]);
						postOrderElementRemainingVisits.add(2);
					}
					else
					{
						int remainingVisits = postOrderElementRemainingVisits.pop();
						postOrderElementRemainingVisits.add(--remainingVisits);
					}
				}
				else if (postOrderElementRemainingVisits.peek() == 1) {
					if (right[postOrderRemainingElements.peek()] != -1) {
						postOrderRemainingElements.add(right[postOrderRemainingElements.peek()]);
						postOrderElementRemainingVisits.add(2);
					}
					else
					{
						int remainingVisits = postOrderElementRemainingVisits.pop();
						postOrderElementRemainingVisits.add(--remainingVisits);
					}
				}
				else if (postOrderElementRemainingVisits.peek() == 0) {
					result.add(key[postOrderRemainingElements.pop()]);
					postOrderElementRemainingVisits.pop();
					int remainingVisits = postOrderElementRemainingVisits.pop();
					postOrderElementRemainingVisits.add(--remainingVisits);
				}
			}

			return result;
		}

		private void postOrderTraversal(int index, List<Integer> result) {
			if (index == -1) {
				return;
			}
			postOrderTraversal(left[index], result);
			postOrderTraversal(right[index], result);
			result.add(key[index]);
		}

	}

	static public void main(String[] args) throws IOException {
            new Thread(null, new Runnable() {
                    public void run() {
                        try {
                            new tree_orders().run();
                        } catch (IOException e) {
                        }
                    }
                }, "1", 1 << 26).start();
	}

	public void print(List<Integer> x) {
		for (Integer a : x) {
			System.out.print(a + " ");
		}
		System.out.println();
	}

	public void run() throws IOException {
		TreeOrders tree = new TreeOrders();
		tree.read();
		print(tree.inOrder());
		print(tree.preOrder());
		print(tree.postOrder());
	}
}
