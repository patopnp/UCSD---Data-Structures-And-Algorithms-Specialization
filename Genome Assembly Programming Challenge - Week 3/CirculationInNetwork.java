import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.HashMap;

public class CirculationInNetwork {
    private static FastScanner in;

    static int flow = 0;

    
    public static void main(String[] args) throws IOException {
        in = new FastScanner();

        FlowGraph graph = readGraph();
        
        while((graph.findResidualNetworkPathToSink())) {
        }

        if(graph.checkCircFlow()) {
        	System.out.println("YES");
        	graph.printFlowsAdjusted();
        	
        }
        else if (graph.checkCircFlow()==false){
        	System.out.println("NO");
        }
        
        

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
        FlowGraph graph = new FlowGraph(vertex_count, edge_count);

        for (int i = 0; i < edge_count; i++) {
            int from = in.nextInt() - 1, to = in.nextInt() - 1, lowerBound = in.nextInt(), capacity = in.nextInt()-lowerBound;
            graph.addEnforcedMinimumFlow(from, to, lowerBound);
            graph.addEdge(from, to, capacity, lowerBound);
        }
        
        graph.addSourcesAndSinks();
        
        
        return graph;
    }

    static class Edge {
        int from, to, capacity, flow, lowerBound;

        public Edge(int from, int to, int capacity, int lb) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
            this.lowerBound = lb;
        }
    }

    
    /*
     * This class implements a bit unusual scheme to store the graph edges, in order
     * to retrieve the backward edge for a given edge quickly.
     */
    static class FlowGraph {
        /* List of all - forward and backward - edges */
    	int vertexCount, edgeCount;
        private List<Edge> edges;
        
        /* These adjacency lists store only indices of edges from the edges list */
        private List<Integer>[] graph;
        private int[] netFlowIntoVertices;
        private int sourceIndex;
        private int sinkIndex;
        
        
        public FlowGraph(int vertexCount, int edgeCount) {
            this.graph = (ArrayList<Integer>[]) new ArrayList[vertexCount+2];
            this.netFlowIntoVertices = new int[vertexCount];
            this.edgeCount = edgeCount;
            this.vertexCount = vertexCount;
            for (int i = 0; i < vertexCount+2; i++)
                this.graph[i] = new ArrayList<>();
            this.edges = new ArrayList<>();

        }

        boolean checkCircFlow() {

        	for (int edgeIndex = edgeCount*2; edgeIndex < edges.size(); edgeIndex = edgeIndex + 2) {
        		if (edges.get(edgeIndex).flow != edges.get(edgeIndex).capacity) {
        			return false;
        		}
        	}
        	
        	
        	return true;
        }
        
        void printFlowsAdjusted() {
        	for (int edgeIndex = 0; edgeIndex < edgeCount*2; edgeIndex += 2) {
        		System.out.println(edges.get(edgeIndex).flow + edges.get(edgeIndex).lowerBound);
        	}
        }
        

        void addSourcesAndSinks() {
        	
        	sourceIndex = vertexCount;
        	sinkIndex = vertexCount+1;
        	
        	for (int i = 0; i < netFlowIntoVertices.length; i++) {
        		if (netFlowIntoVertices[i] > 0) {
        			addEdge(sourceIndex, i, netFlowIntoVertices[i], 0);
        		}
        		else if (netFlowIntoVertices[i] < 0) {
        			addEdge(i, sinkIndex, netFlowIntoVertices[i]*-1, 0);
        		}
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

            q.add(sourceIndex);
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

                        if (edgeForId.to == sinkIndex) {
                            // Print trace
                            int previousVertex = edgeForId.to;
                            int smallestCapacity = Integer.MAX_VALUE;
                            while (previousVertex != sourceIndex) {
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
        
        /*
        public boolean findResidualNetworkPathToSink() {
            Queue<Integer> q = new LinkedList<Integer>();

            Stack<Integer> shortestPathToSink = new Stack<Integer>();

            boolean[] visited = new boolean[graph.length];
            int[] prev = new int[graph.length];
            int[] prevEdgeId = new int[graph.length];
            int[] smallestCapacityLeft = new int[graph.length];
            //check that n is the source
            q.add(sourceIndex);
            while (!q.isEmpty()) {
                int u = q.poll();
                List<Integer> edgesFromCity = getIds(u);
                for (int edgeId : edgesFromCity) {
                    Edge edgeForId = getEdge(edgeId);
                    if (edgeForId.capacity - edgeForId.flow > 0) {

                        if (visited[edgeForId.to] == false) {
                            q.add(edgeForId.to);
                            prev[edgeForId.to] = edgeForId.from;
                            prevEdgeId[edgeForId.to] = edgeId;
                            smallestCapacityLeft[edgeForId.to] = edgeForId.capacity - edgeForId.flow;
                            visited[edgeForId.to] = true;
                        }
                        //check that it always matches the sink
                        if (edgeForId.to == sinkIndex) {
                            // Print trace
                            int previousVertex = edgeForId.to;
                            int smallestCapacity = Integer.MAX_VALUE;
                            while (previousVertex != sourceIndex) {
                                shortestPathToSink.add(prevEdgeId[previousVertex]);
                                if (smallestCapacityLeft[previousVertex] < smallestCapacity) {
                                    smallestCapacity = smallestCapacityLeft[previousVertex];
                                }
                                previousVertex = prev[previousVertex];

                            }


                            while (!shortestPathToSink.isEmpty()) {
                                
                                int idToAddFlow = shortestPathToSink.pop();
                                addFlow(idToAddFlow, smallestCapacity);
                            }
                            flow += smallestCapacity;

                            return true;
                        }

                    }
                }
            }

            return false;
        }
*/
        void addEnforcedMinimumFlow(int from, int to, int lowerBound) {
        	
        	netFlowIntoVertices[from] -= lowerBound;
        	netFlowIntoVertices[to] += lowerBound;
        	
        	
        	
        }
        
        public void addEdge(int from, int to, int capacity, int lowerBound) {
            /*
             * Note that we first append a forward edge and then a backward edge, so all
             * forward edges are stored at even indices (starting from 0), whereas backward
             * edges are stored at odd indices.
             */
            Edge forwardEdge = new Edge(from, to, capacity, lowerBound);
            Edge backwardEdge = new Edge(to, from, 0, lowerBound);
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
