import java.util.ArrayList;
import java.util.Scanner;

public class Acyclicity {

    static boolean isCyclic;

    private static int acyclic(ArrayList<Integer>[] adj, int n, boolean[] visited, boolean[] currentlyVisiting) {
        //write your code here

        for (int i = 0; i < n; i++) {
            if (visited[i] == false) {
                explore(adj, visited, currentlyVisiting, i);
            }

        }

        return isCyclic?1:0;
    }

    public static void explore(ArrayList<Integer>[] adj, boolean[] visited, boolean[] currentlyVisiting, int vertex) {
        previsit(vertex, currentlyVisiting, visited);

        for (int neighbor : adj[vertex]) {

            if (currentlyVisiting[neighbor]){
                isCyclic = true;
            }

            if (!visited[neighbor]) {
                explore(adj, visited, currentlyVisiting, neighbor);
            }
        }
        postvisit(vertex, currentlyVisiting);
    }

    public static void previsit(int vertex, boolean[] visited, boolean[] currentlyVisiting) {
        visited[vertex] = true;
        currentlyVisiting[vertex] = true;
    }

    public static void postvisit(int vertex, boolean[] currentlyVisiting) {
        currentlyVisiting[vertex] = false;
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

        boolean[] visited = new boolean[n];
        boolean[] currentlyVisiting = new boolean[n];
        System.out.println(acyclic(adj, n, visited, currentlyVisiting));
    }
}

