import java.util.ArrayList;
import java.util.Scanner;

public class NegativeCycle {
    private static int negativeCycle(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost) {
        // write your code here
        

        return new NegativeCycle().negativeCycleBellmanFord(adj,cost,0)?1:0;
    }

    public void relax(int u, int v, int cost, int[] dist, int[] prev) {
        if (dist[v] > dist[u] + cost) {
            dist[v] = dist[u] + cost;
            prev[v] = u;
        }
    }

    public boolean negativeCycleBellmanFord(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s){

        int[] dist = new int[adj.length];
        int[] prev = new int[adj.length];


        for (int i = 0; i < adj.length; i++) {
            //dist[i] = Integer.MAX_VALUE;
            //to find any negative cycle we dont care about the actual value of dist only if it is being updated at vth iteration
            prev[i] = -1;
        }

        dist[s] = 0;

        //run size(V)-1 iterations of Bellman-Ford algorithm
        for (int loopExec = 0; loopExec < adj.length - 1; loopExec++) {
            for (int i = 0; i < adj.length; i++) {
                //if (dist[i] != Integer.MAX_VALUE) {
                    for (int j = 0; j < adj[i].size(); j++) {
                        relax(i, adj[i].get(j), cost[i].get(j), dist, prev);
                    }
                //}
            }
        }

        //check if it has any dist update on Vth iteration
        for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < adj[i].size(); j++) {
                if (dist[adj[i].get(j)] > dist[i] + cost[i].get(j)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        ArrayList<Integer>[] cost = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y, w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            adj[x - 1].add(y - 1);
            cost[x - 1].add(w);
        }
        System.out.println(negativeCycle(adj, cost));
    }
}

