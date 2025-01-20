import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Bipartite {
    private static int bipartite(ArrayList<Integer>[] adj) {
        // write your code here
        int[] colors = BFSBipartiteSearch(adj);
        return checkBipartite(colors, adj);
    }

    public static int[] BFSBipartiteSearch(ArrayList<Integer>[] adj) {
        Queue<Integer> q = new LinkedList<Integer>();
        int[] colors = new int[adj.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = -1;
        }

        for (int p = 0; p < adj.length; p++) {
            if (colors[p] == -1) {
                colors[p] = 0;
                q.add(p);
                while (!q.isEmpty()) {
                    int u = q.poll();
                    for (int neighbor : adj[u]) {
                        if (colors[neighbor] == -1) {
                            q.add(neighbor);
                            if (colors[u] == 0) {
                                colors[neighbor] = 1;
                            } else if (colors[u] == 1) {
                                colors[neighbor] = 0;
                            }
                        }
                    }
                }
            }
        }
        return colors;
    }

    public static int checkBipartite(int[] colors, ArrayList<Integer>[] adj) {

        for (int i = 0; i < adj.length; i++) {
            int vertexColor = colors[i];
            for (int x : adj[i]) {
                if (colors[x] == vertexColor) {
                    return 0;
                }
            }
        }
        return 1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
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
        System.out.println(bipartite(adj));
    }
}
