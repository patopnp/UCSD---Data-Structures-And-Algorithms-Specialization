import java.util.*;

public class ShortestPaths {
    
    public static long[] shortestPaths(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s){
        // write your code here
        // it's going to work correctly now
        long[] dist = new long[adj.length];
        int[] prev = new int[adj.length];


        for (int i = 0; i < adj.length; i++) {
            dist[i] = Long.MAX_VALUE;
            prev[i] = -1;
        }

        dist[s] = 0;

        //run size(V)-1 iterations of Bellman-Ford algorithm
        for (int loopExec = 0; loopExec < adj.length - 1; loopExec++) {
            for (int i = 0; i < adj.length; i++) {
                if (dist[i] != Long.MAX_VALUE) {
                    for (int j = 0; j < adj[i].size(); j++) {
                        relax(i, adj[i].get(j), cost[i].get(j), dist, prev);
                    }
                }
            }
        }

        List<Integer> a = new LinkedList<Integer>();

        //check if it has any dist update on Vth iteration
        for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < adj[i].size(); j++) {
                if (dist[i] != Long.MAX_VALUE) {
                    if (dist[adj[i].get(j)] > dist[i] + cost[i].get(j)) {
                        a.add(adj[i].get(j));
                    }
                }
            }
        }

        for (int vertexIndex : a) {
            explore(adj, dist, vertexIndex);
        }

        //BFS for all relaxed nodes on v iteration
        /*while (!a.isEmpty()) {
            int nextN = a.poll();
            breadthFirstSearch(adj, dist, nextN);
        }*/

        return dist;
    }

    public static void explore(ArrayList<Integer>[] adj, long[] dist, int vertex) {
        dist[vertex] = Long.MIN_VALUE;
        for (int neighbor : adj[vertex]) {
            if (dist[neighbor] != Long.MIN_VALUE) {
                vertex = neighbor;
                explore(adj, dist, vertex);
            }
        }
    }

    /*
    public static void breadthFirstSearch(ArrayList<Integer>[] adj, long[] dist, int s) {
        Queue<Integer> q = new LinkedList<Integer>();
        dist[s] = Long.MIN_VALUE;
        q.add(s);
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int neighbor : adj[u]) {
                if (dist[neighbor] != Long.MIN_VALUE) {
                    q.add(neighbor);
                    dist[neighbor] = Long.MIN_VALUE;
                }
            }
        }
    }
    */

    public static void relax(int u, int v, int cost, long[] dist, int[] prev) {
        if (dist[v] > dist[u] + cost) {
            dist[v] = dist[u] + cost;
            prev[v] = u;
        }
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
        int s = scanner.nextInt() - 1;
        long distance[] = new long[n];
        int reachable[] = new int[n];
        int shortest[] = new int[n];
        for (int i = 0; i < n; i++) {
            distance[i] = Long.MAX_VALUE;
            reachable[i] = 0;
            shortest[i] = 1;
        }
        long[] dist = shortestPaths(adj, cost, s);
        for (int i = 0; i < dist.length; i++) {
            if (dist[i] == Long.MIN_VALUE) {
                System.out.println("-");
            }
            else if (dist[i] == Long.MAX_VALUE){
                System.out.println("*");
            }
            else {
                System.out.println(dist[i]);
            }
        }
        /*
        shortestPaths(adj, cost, s, distance, reachable, shortest);
        for (int i = 0; i < n; i++) {
            if (reachable[i] == 0) {
                System.out.println('*');
            } else if (shortest[i] == 0) {
                System.out.println('-');
            } else {
                System.out.println(distance[i]);
            }
        }
        */
    }

}

