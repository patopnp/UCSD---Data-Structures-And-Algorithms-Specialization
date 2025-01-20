import java.util.*;
import java.util.Collections;
import java.util.Scanner;

public class StronglyConnected {
    private static int numberOfStronglyConnectedComponents(ArrayList<Integer>[] adj, ArrayList<Integer>[] adjR) {
        //write your code here
        int numSCC = 0;
        Deque<Integer> order = dfs(adjR);

        boolean[] visited = new boolean[adj.length];

        for (int v : order) {
            if (visited[v] == false) {
                explore(adj, visited, v);
                numSCC++;
            }
        }

        return numSCC;
    }

    private static Deque<Integer> dfs(ArrayList<Integer>[] adj) {
        Deque<Integer> order = new LinkedList<Integer>();
        // write your code here
        boolean[] visited = new boolean[adj.length];
        for (int i = 0; i < adj.length; i++) {
            if (visited[i] == false) {
                explore(adj, visited, i, order);
            }

        }

        return order;
    }

    public static void explore(ArrayList<Integer>[] adj, boolean[] visited, int vertex) {
        visited[vertex] = true;
        for (int neighbor : adj[vertex]) {
            if (!visited[neighbor]) {
                explore(adj, visited, neighbor);
            }
        }
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
        ArrayList<Integer>[] adjR = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
            adjR[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
            adjR[y - 1].add(x - 1);
        }
        System.out.println(numberOfStronglyConnectedComponents(adj, adjR));
    }
}

