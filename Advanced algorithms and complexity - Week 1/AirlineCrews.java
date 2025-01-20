import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

public class AirlineCrews {
    private static FastScanner in;
    private PrintWriter out;
    static int flow = 0;
    static List<Edge> listOfEdges;

    static int numLeft;
    static int numRight;

    public static void main(String[] args) throws IOException {
        new AirlineCrews().solve();

    }

    public void solve() throws IOException {
        in = new FastScanner();
        
        FlowGraph graph = readGraph();

        while ((graph.findResidualNetworkPathToSink())) {
            //graph.printEdges();
        }

        graph.printResult();

        /*graph.printEdges();*/
 
        /*System.out.println(flow);*/

    }


    
    static FlowGraph readGraph() throws IOException {
        numLeft = in.nextInt();
        numRight = in.nextInt();
        FlowGraph graph = new FlowGraph(numLeft + numRight + 2);

        for (int i = 1; i < numLeft + 1; ++i) {
            graph.addEdge(0, i, 1);
        }

        for (int i = 1; i < numLeft+1; ++i) {
            int from = i;
            for (int j = 0; j < numRight; ++j) {
                if (in.nextInt() == 0) {
                    continue;
                }
                
                graph.addEdge(from, j+numLeft+1, 1);
            }
        }

        for (int j = 0; j < numRight; ++j) {
            graph.addEdge(j + numLeft+1, numLeft + numRight+1, 1);
        }

        return graph;
    }

    private void writeResponse(int[] matching) {
        for (int i = 0; i < matching.length; ++i) {
            if (i > 0) {
                out.print(" ");
            }
            if (matching[i] == -1) {
                out.print("-1");
            } else {
                out.print(matching[i] + 1);
            }
        }
        out.println();
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

        public void printResult() {

            
            for (int i = 1; i < numLeft + 1; i++) {
                boolean found = false;
                for (Edge e : edges) {

                    if (e.from == i && e.flow > 0) {
                        found = true;
                        System.out.print(e.to - numLeft + " ");
                    }
                }
                if (!found) {
                    System.out.print(-1 + " ");
                }
            }
        }

        public void printEdges() {
            for (Edge e : edges) {
                System.out.println("(" + e.from + ", " + e.to + ", " + e.capacity + ", flow = " + e.flow + ")");
            }
        }

        public boolean findResidualNetworkPathToSink() {
            /*
             * int flow = 0;
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
                            // System.out.println("to " + edgeForId.to + " id " + edgeId);
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

                            // shortestPathToSink.add((int) (prevEdgeId[previousVertex]));
                            // if (getEdge(0).capacity < smallestCapacity)
                            // smallestCapacity = getEdge(0).capacity;

                            // System.out.println(smallestCapacity);

                            while (shortestPathToSink.isEmpty() == false) {

                                int idToAddFlow = shortestPathToSink.pop();
                                addFlow(idToAddFlow, smallestCapacity);
                                // System.out.println(idToAddFlow);
                            }
                            flow += smallestCapacity;
                            listOfEdges = edges;

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
