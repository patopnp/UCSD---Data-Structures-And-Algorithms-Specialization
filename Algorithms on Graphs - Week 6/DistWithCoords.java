import java.awt.geom.Point2D;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.lang.Math;

public class DistWithCoords {
    private static class Impl {
        Double[] potVal;
        int s;
        int t;
        // Number of nodes
        int n;
        // Coordinates of nodes
        int[] x;
        int[] y;
        // See description of these fields in the starters for friend_suggestion
        ArrayList<Integer>[][] adj;
        ArrayList<Integer>[][] cost;
        Long[][] distance;
        ArrayList<PriorityQueue<Entry>> queue;
        boolean[] visited;
        ArrayList<Integer> workset;
        final Long INFINITY = Long.MAX_VALUE / 4;

        Impl(int n) {
            this.n = n;
            visited = new boolean[n];
            x = new int[n];
            y = new int[n];
            workset = new ArrayList<Integer>();
            potVal = new Double[n];
            distance = new Long[][] { new Long[n], new Long[n] };
            for (int i = 0; i < n; ++i) {
                distance[0][i] = distance[1][i] = INFINITY;
            }
            queue = new ArrayList<PriorityQueue<Entry>>();
            queue.add(new PriorityQueue<Entry>(n));
            queue.add(new PriorityQueue<Entry>(n));
        }

        // See the description of this method in the starters for friend_suggestion
        void clear() {
            for (int v : workset) {
                distance[0][v] = distance[1][v] = INFINITY;
                visited[v] = false;
            }
            workset.clear();
            queue.get(0).clear();
            queue.get(1).clear();
        }

        Long shortestPath(int s, int t) {
            long distance = INFINITY;
            for (int u : workset) {
                if (this.distance[0][u] + this.distance[1][u] < distance) {
                    distance = this.distance[0][u] + this.distance[1][u];
                }
            }
            return distance;
        }

        // See the description of this method in the starters for friend_suggestion
        void visit(int side, int v, Long dist) {
            // Implement this method yourself
            if (distance[side][v] > dist) {
                distance[side][v] = dist;
                Double deltaDistance = (side == 0) ? potVal[v] : -potVal[v];
                //System.out.println("added " + ((double) dist) + deltaDistance + "   potVal="+ potVal[v]);
                queue.get(side).add(new Entry(((double)dist) + deltaDistance, v));
                workset.add(v);
            }
        }

        void process(int side, int node) {
            for (int i = 0; i < adj[side][node].size(); i++) {
                long dist = distance[side][node] + cost[side][node].get(i);
                visit(side, adj[side][node].get(i), dist);
            }
        }

        // Returns the distance from s to t in the graph.
        Long query(int s, int t) {

            for (int i = 0; i < n; i++) {

                double xDist = Math.abs(x[i] - x[s]);
                double yDist = Math.abs(y[i] - y[s]);

                Double distanceToSource;

                if (xDist == 0) {
                    distanceToSource = yDist;
                }
                else if (yDist == 0) {
                    distanceToSource = xDist;
                }
                else {
                    distanceToSource = Math.sqrt(xDist) * Math.sqrt(yDist) * Math.sqrt(xDist / yDist + yDist / xDist);
                }

                Double distanceToTarget;

                double xDistToTarget = Math.abs(x[t] - x[i]);
                double yDistToTarget = Math.abs(y[t] - y[i]);

                if (xDistToTarget == 0) {
                    distanceToTarget = yDistToTarget;
                } else if (yDistToTarget == 0) {
                    distanceToTarget = xDistToTarget;
                } else {
                    distanceToTarget = Math.sqrt(xDistToTarget) * Math.sqrt(yDistToTarget) * Math.sqrt(xDistToTarget / yDistToTarget + yDistToTarget / xDistToTarget);
                }

                //System.out.println(distanceToSource);
                potVal[i] = (distanceToTarget-distanceToSource)/2.0;
            }

            clear();
            this.s = s;
            this.t = t;
            visit(0, s, 0L);
            visit(1, t, 0L);
            // Implement the rest of the algorithm yourself
            do {
                Entry v = queue.get(0).poll();
                
                while (v.cost != distance[0][v.node] + potVal[v.node]) {
                    v = queue.get(0).poll();
                }

                process(0, v.node);
                if (visited[v.node]) {
                    return shortestPath(s, t);
                }
                visited[v.node] = true;


                Entry vr = queue.get(1).poll();
                while (vr.cost != distance[1][vr.node] - potVal[vr.node]) {
                    vr = queue.get(1).poll();
                }
                process(1, vr.node);
                if (visited[vr.node]) {
                    return shortestPath(s, t);
                }
                visited[vr.node] = true;

            } while (!queue.get(0).isEmpty() && !queue.get(1).isEmpty());

            return -1L;
        }

        class Entry implements Comparable<Entry> {
            Double cost;
            int node;

            public Entry(double cost, int node) {
                this.cost = cost;
                this.node = node;
            }

            public int compareTo(Entry other) {
                return cost < other.cost ? -1 : cost > other.cost ? 1 : 0;
            }
        }
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        Impl DistWithCoords = new Impl(n);
        DistWithCoords.adj = (ArrayList<Integer>[][])new ArrayList[2][];
        DistWithCoords.cost = (ArrayList<Integer>[][])new ArrayList[2][];
        for (int side = 0; side < 2; ++side) {
            DistWithCoords.adj[side] = (ArrayList<Integer>[])new ArrayList[n];
            DistWithCoords.cost[side] = (ArrayList<Integer>[])new ArrayList[n];
            for (int i = 0; i < n; i++) {
                DistWithCoords.adj[side][i] = new ArrayList<Integer>();
                DistWithCoords.cost[side][i] = new ArrayList<Integer>();
            }
        }

        for (int i = 0; i < n; i++) { 
            int x, y;
            x = in.nextInt();
            y = in.nextInt();
            DistWithCoords.x[i] = x;
            DistWithCoords.y[i] = y;
            
        }

        for (int i = 0; i < m; i++) {
            int x, y, c;
            x = in.nextInt();
            y = in.nextInt();
            c = in.nextInt();
            DistWithCoords.adj[0][x - 1].add(y - 1);
            DistWithCoords.cost[0][x - 1].add(c);
            DistWithCoords.adj[1][y - 1].add(x - 1);
            DistWithCoords.cost[1][y - 1].add(c);
        }

        int t = in.nextInt();

        for (int i = 0; i < t; i++) {
            int u, v;
            u = in.nextInt();
            v = in.nextInt();
            System.out.println(DistWithCoords.query(u-1, v-1));
        }
    }
}