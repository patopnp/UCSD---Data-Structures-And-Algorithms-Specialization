import java.util.*;
import java.util.Collections;
import java.util.Scanner;

public class Toposort {
    private static Deque<Integer> toposort(ArrayList<Integer>[] adj) {
        Deque<Integer> order = new LinkedList<Integer>();
        //write your code here
        boolean[] visited = new boolean[adj.length];
        for (int i = 0; i < adj.length; i++) {
            if (visited[i] == false) {
                explore(adj, visited, i, order);
            }

        }
        
        return order;
    }

    public static void explore(ArrayList<Integer>[] adj, boolean[] visited, int vertex, Deque<Integer> order) {
        visited[vertex] = true;
        for (int neighbor : adj[vertex]) {
            if (!visited[neighbor]) {
                explore(adj, visited, neighbor, order);
            }
        }
        
        order.addFirst(vertex);
        
    }

    private static void dfs(ArrayList<Integer>[] adj, int[] used, ArrayList<Integer> order, int s) {
      //write your code here
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
        }
        Deque<Integer> order = toposort(adj);

        for (int x : order) {
            System.out.print((x + 1) + " ");
        }
    }
}

