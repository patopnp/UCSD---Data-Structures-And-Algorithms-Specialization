import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Arrays;

public class Evacuation {
    private static FastScanner in;

    static int flow = 0;

    public static void main(String[] args) throws IOException {
        in = new FastScanner();

        FlowGraph graph = readGraph();
        
        while((graph.findResidualNetworkPathToSink())) {
            /*graph.printEdges();*/
        }

        System.out.println(flow);

        // System.out.println(maxFlow(graph, 0, graph.size() - 1));
    }

    private static int maxFlow(FlowGraph graph, int from, int to) {
        int flow = 0;
        /* your code goes here */
        return flow;
    }

    static FlowGraph readGraph() throws IOException {
        int vertex_count = in.nextInt();
        int edge_count = in.nextInt();
        FlowGraph graph = new FlowGraph(vertex_count);

        for (int i = 0; i < edge_count; ++i) {
            int from = in.nextInt() - 1, to = in.nextInt() - 1, capacity = in.nextInt();
            graph.addEdge(from, to, capacity);
        }
        return graph;
    }

    static class Edge {
        int from, to, capacity, flow;

        public Edge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }
    }

    /*
     * This class implements a bit unusual scheme to store the graph edges, in order
     * to retrieve the backward edge for a given edge quickly.
     */
    static class FlowGraph {
        /* List of all - forward and backward - edges */
        private List<Edge> edges;

        /* These adjacency lists store only indices of edges from the edges list */
        private List<Integer>[] graph;

        public FlowGraph(int n) {
            this.graph = (ArrayList<Integer>[]) new ArrayList[n];
            for (int i = 0; i < n; ++i)
                this.graph[i] = new ArrayList<>();
            this.edges = new ArrayList<>();
        }

        public void printEdges() {
            for (Edge e : edges) {
                System.out.println("(" + e.from + ", " + e.to + ", " + e.capacity + ", flow = " + e.flow + ")");
            }
        }

        public boolean findResidualNetworkPathToSink() {
            /*
            int flow = 0;
            */
            Queue<Integer> q = new LinkedList<Integer>();

            Stack<Integer> shortestPathToSink = new Stack<Integer>();

            boolean[] visited = new boolean[graph.length];
            int[] prev = new int[graph.length];
            int[] prevEdgeId = new int[graph.length];
            int[] smallestCapacityLeft = new int[graph.length];

            q.add(0);
            while (!q.isEmpty()) {
                int u = q.poll();
                List<Integer> edgesFromCity = getIds(u);
                for (int edgeId : edgesFromCity) {
                    Edge edgeForId = getEdge(edgeId);
                    if (edgeForId.capacity - edgeForId.flow > 0) {

                        if (visited[edgeForId.to] == false) {
                            q.add(edgeForId.to);
                            // System.out.println(edgeForId.from+1 + " " + (int)(edgeForId.to+1));
                            prev[edgeForId.to] = edgeForId.from;
                            prevEdgeId[edgeForId.to] = edgeId;
                            //System.out.println("to " + edgeForId.to + " id " + edgeId);
                            smallestCapacityLeft[edgeForId.to] = edgeForId.capacity - edgeForId.flow;
                            // System.out.println(edgeForId.from + " capacity = " + edgeForId.capacity);
                            visited[edgeForId.to] = true;
                        }

                        if (edgeForId.to == graph.length - 1) {
                            // Print trace
                            int previousVertex = edgeForId.to;
                            int smallestCapacity = Integer.MAX_VALUE;
                            while (previousVertex != 0) {
                                shortestPathToSink.add((int) (prevEdgeId[previousVertex]));
                                // System.out.println(smallestCapacityLeft[previousVertex]);
                                if (smallestCapacityLeft[previousVertex] < smallestCapacity) {
                                    smallestCapacity = smallestCapacityLeft[previousVertex];
                                }
                                previousVertex = prev[previousVertex];

                            }

                            //shortestPathToSink.add((int) (prevEdgeId[previousVertex]));
                            //if (getEdge(0).capacity < smallestCapacity)
                            //    smallestCapacity = getEdge(0).capacity;

                            //System.out.println(smallestCapacity);

                            while (shortestPathToSink.isEmpty() == false) {
                                
                                int idToAddFlow = shortestPathToSink.pop();
                                addFlow(idToAddFlow, smallestCapacity);
                                //System.out.println(idToAddFlow);
                            }
                            flow += smallestCapacity;

                            return true;
                        }

                    }
                }
            }

            return false;
        }

        public void addEdge(int from, int to, int capacity) {
            /*
             * Note that we first append a forward edge and then a backward edge, so all
             * forward edges are stored at even indices (starting from 0), whereas backward
             * edges are stored at odd indices.
             */
            Edge forwardEdge = new Edge(from, to, capacity);
            Edge backwardEdge = new Edge(to, from, 0);
            graph[from].add(edges.size());
            edges.add(forwardEdge);
            graph[to].add(edges.size());
            edges.add(backwardEdge);
        }

        public int size() {
            return graph.length;
        }

        public List<Integer> getIds(int from) {
            return graph[from];
        }

        public Edge getEdge(int id) {
            return edges.get(id);
        }

        public void addFlow(int id, int flow) {
            /*
             * To get a backward edge for a true forward edge (i.e id is even), we should
             * get id + 1 due to the described above scheme. On the other hand, when we have
             * to get a "backward" edge for a backward edge (i.e. get a forward edge for
             * backward - id is odd), id - 1 should be taken.
             *
             * It turns out that id ^ 1 works for both cases. Think this through!
             */
            edges.get(id).flow += flow;
            edges.get(id ^ 1).flow -= flow;
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
