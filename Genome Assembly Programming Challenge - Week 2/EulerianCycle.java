import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

public class EulerianCycle {
	
	public static void main(String... args) throws IOException{
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		String nextLine = reader.readLine();
		int n = Integer.parseInt(nextLine.split(" ")[0]);
		int m = Integer.parseInt(nextLine.split(" ")[1]);
		
		Stack<Integer> vStack = new Stack<>();
		Stack<Integer> vEdges[] = new Stack[n];

		int[] inDegree = new int[n];
		int[] outDegree = new int[n];
		
		for (int i = 0; i < n; i++) {
			vEdges[i] = new Stack<>();
		}
		
		for (int e = 0; e < m; e++) {
			nextLine = reader.readLine();
			int source = Integer.parseInt(nextLine.split(" ")[0])-1;
			int end = Integer.parseInt(nextLine.split(" ")[1])-1;
			
			vEdges[source].add(end);
			inDegree[end]++;
			outDegree[source]++;
		}
		
		for (int i = 0; i < n; i++) {
			if (inDegree[i] != outDegree[i]) {
				
				System.out.println(0);
				return;
			}
		}
		System.out.println(1);
		Deque<Integer> path = new LinkedList<>();
		vStack.add(0);
		while (!vStack.isEmpty()) {
			
			int currVertex = vStack.peek();
			
			if (vEdges[currVertex].isEmpty() == false) {
				vStack.add(vEdges[currVertex].pop());
				
			}
			else {
				path.addFirst(vStack.pop());
			}
		}
		
		path.removeLast();
		
		for (int next : path) {
			System.out.print( (next+1) + " ");
		}
		
	}
	
	
}
