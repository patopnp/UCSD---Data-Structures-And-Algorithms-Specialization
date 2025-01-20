import java.util.ArrayList;
import java.util.Scanner;

public class Reachability {


    static boolean found;

    private static int reach(ArrayList<Integer>[] adj, int x, int y, boolean[] visited) {
        //write your code here
        explore(adj, visited, x, y);
        return found?1:0;
    }

    public static void explore(ArrayList<Integer>[] adj, boolean[] visited, int vertex, int destination) {
        visited[vertex] = true;
        for (int neighbor : adj[vertex]) {
            if (!visited[neighbor]) {
                vertex = neighbor;
                if (vertex == destination) {
                    found = true;
                }
                explore(adj, visited, vertex, destination);
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
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        boolean[] visited = new boolean[n];
        System.out.println(reach(adj, x, y, visited));
    }
}

