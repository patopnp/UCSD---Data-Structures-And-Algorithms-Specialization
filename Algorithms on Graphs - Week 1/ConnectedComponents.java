import java.util.ArrayList;
import java.util.Scanner;

public class ConnectedComponents {
    private static int numberOfComponents(ArrayList<Integer>[] adj, int n, boolean[] visited) {
        int result = 0;
        //write your code here
        int connectedComponentNumber = 0;
        for (int i = 0; i < n; i++) {
            if (visited[i] == false) {
                connectedComponentNumber++;
                explore(adj, visited, i);
            }
            
        }
        result = connectedComponentNumber;
        return result;
    }

    public static void explore(ArrayList<Integer>[] adj, boolean[] visited, int vertex) {
        visited[vertex] = true;
        for (int neighbor : adj[vertex]) {
            if (!visited[neighbor]) {
                vertex = neighbor;
                explore(adj, visited, vertex);
            }
        }
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
            adj[y - 1].add(x - 1);
        }
        boolean[] visited = new boolean[n];
        System.out.println(numberOfComponents(adj, n, visited));
    }
}

