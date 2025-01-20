import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.StringTokenizer;

public class CircuitDesignWithDeque {
    private final InputReader reader;
    private final OutputWriter writer;

    int varScc[];
    
    public CircuitDesignWithDeque(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    private List<List<Integer>> findStronglyConnectedComponents(List<Integer>[] adj, List<Integer>[] adjR) {
        
        int numSCC = 0;
        Deque<Integer> order = dfs(adjR);
        List<List<Integer>> sccs = new ArrayList<List<Integer>>();
        
        boolean[] visited = new boolean[adj.length];

        for (int v : order) {
            if (visited[v] == false) {
            	sccs.add(new ArrayList<Integer>());
                explore(adj, visited, v, sccs);
                numSCC++;
            }
        }
        
        return sccs;
    }
    

    public void explore(List<Integer>[] adj, boolean[] visited, int firstVertex, List<List<Integer>> sccs) {
    	
    	Deque<Integer> explored = new LinkedList<Integer>();
    	
    	
    	explored.add(firstVertex);
    	
    	while(explored.isEmpty() == false)
    	{
    		int vertex = explored.poll();
    		
	    	sccs.get(sccs.size()-1).add(vertex);
	    	varScc[vertex] = sccs.size()-1;
	    	
	        visited[vertex] = true;
	        
	        
	        
	        for (int neighbor : adj[vertex]) {
	            if (!visited[neighbor]) {
	            	
	            	explored.addFirst(neighbor);
	            }
	        }
    	}
    }
    
    public static void explore(List<Integer>[] adj, boolean[] visited, int first, Deque<Integer> order) {
        // Stack to simulate the recursive function call stack
        Deque<Integer> stack = new ArrayDeque<>();
        
        // A helper stack to track the node processing state
        Deque<Boolean> onReturnStack = new ArrayDeque<>();

        // Push the starting node onto the stack
        stack.push(first);
        onReturnStack.push(false);

        while (!stack.isEmpty()) {
            int currentNode = stack.peek();
            boolean onReturn = onReturnStack.pop();

            if (onReturn) {
                // We're returning to this node after processing its neighbors
                order.addFirst(currentNode);
                stack.pop();
            } else if (!visited[currentNode]) {
                // Mark the node as visited
                visited[currentNode] = true;

                // Push the return marker for this node
                onReturnStack.push(true);

                // Push all unvisited neighbors onto the stack
                List<Integer> neighbors = adj[currentNode];
                for (int neighbor : neighbors) {
                    if (!visited[neighbor]) {
                        stack.push(neighbor);
                        onReturnStack.push(false);
                    }
                }
            } else {
                // Already visited, pop the node
                stack.pop();
            }
        }
    }
    
    
    /*
    public static void explore(List<Integer>[] adj, boolean[] visited, int first, Deque<Integer> order) {
    	
    	Deque<Integer> explored = new LinkedList<Integer>();
    	explored.add(first);
    	while(!explored.isEmpty()) {
    		int vertex = explored.poll();
    		
	        visited[vertex] = true;
	        for (int neighbor : adj[vertex]) {
	            if (!visited[neighbor]) {
	            	explored.addFirst(neighbor);
	            	order.addLast(vertex);
	            }
	        }
	        
	        
    	}
    }
    */
    
    
    
    /*
    public static void explore(List<Integer>[] adj, boolean[] visited, int vertex, Deque<Integer> order) {
        visited[vertex] = true;
        for (int neighbor : adj[vertex]) {
            if (!visited[neighbor]) {
                explore(adj, visited, neighbor, order);
            }
        }
        
        order.addFirst(vertex);
    }
    */
    private void assignLiterals(List<Integer> scc, int[] cnfVars, int nv, HashMap<Integer, Integer> mapping, HashMap<Integer, Integer> mappingFromIndexToKeys) {
    	for(int vertex : scc) {
    		int negIndex = mapping.get(-mappingFromIndexToKeys.get(vertex));
    		if (cnfVars[vertex] == 0 && cnfVars[negIndex] == 0)
    		{
    			cnfVars[vertex] = 1;
    			cnfVars[negIndex] = -1;
    			
    		}
    	}
    }
    
    private boolean checkSatisfiability(HashMap<Integer, Integer> mapping, HashMap<Integer, Integer> mappingFromIndexToKeys) {
    	    	
    	for (int i = 0; i < varScc.length; i++) {
    		if (varScc[i] == varScc[mapping.get(-mappingFromIndexToKeys.get(i))]) {
    			System.out.println("UNSATISFIABLE");
    			System.exit(0);
    		}
    	}
    	return true;
    }
    
    private static Deque<Integer> dfs(List<Integer>[] adj) {
        Deque<Integer> order = new LinkedList<Integer>();
        // write your code here
        boolean[] visited = new boolean[adj.length];
        for (int i = 0; i < adj.length; i++) {
            if (visited[i] == false) {
                explore(adj, visited, i, order);
            }

        }

        return order;
    }
    
    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new CircuitDesignWithDeque(reader, writer).run();
        writer.writer.flush();
    }

    class Clause {
        int firstVar;
        int secondVar;
    }

    class TwoSatisfiability {
        int numVars;
        Clause[] clauses;

        TwoSatisfiability(int n, int m) {
            numVars = n;
            clauses = new Clause[m];
            for (int i = 0; i < m; ++i) {
                clauses[i] = new Clause();
            }
        }

        /*
        boolean isSatisfiable(int[] result) {
            // This solution tries all possible 2^n variable assignments.
            // It is too slow to pass the problem.
            // Implement a more efficient algorithm here.
            for (int mask = 0; mask < (1 << numVars); ++mask) {
                for (int i = 0; i < numVars; ++i) {
                    result[i] = (mask >> i) & 1;
                }

                boolean formulaIsSatisfied = true;

                for (Clause clause: clauses) {
                    boolean clauseIsSatisfied = false;
                    if ((result[Math.abs(clause.firstVar) - 1] == 1) == (clause.firstVar < 0)) {
                        clauseIsSatisfied = true;
                    }
                    if ((result[Math.abs(clause.secondVar) - 1] == 1) == (clause.secondVar < 0)) {
                        clauseIsSatisfied = true;
                    }
                    if (!clauseIsSatisfied) {
                        formulaIsSatisfied = false;
                        break;
                    }
                }

                if (formulaIsSatisfied) {
                    return true;
                }
            }
            return false;
        }
        */
    }

    public static boolean verifySatisfiability(ArrayList<Integer> satisfyingAssignment, TwoSatisfiability twoSat) {
    	for (int i = 0; i < twoSat.clauses.length; ++i) {
    		
    		if(!satisfyingAssignment.contains(twoSat.clauses[i].firstVar) && !satisfyingAssignment.contains(twoSat.clauses[i].secondVar)){
    			return false;
    		}
    		
    	}
    	
    	return true;
    	
    }
    
    public void run() {
    	//int n = 0;
    	HashMap<Integer, Integer> mapping = new HashMap<>();
    	HashMap<Integer, Integer> mappingFromIndexToKeys = new HashMap<>();
    	
    	HashMap<Integer, List<Integer>> implicationGraph = new HashMap<Integer, List<Integer>>();
    	
    	
    	
        int n = reader.nextInt();
        int m = reader.nextInt();

    	
        TwoSatisfiability twoSat = new TwoSatisfiability(n, m);
        
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        
        int maxVarNumber = 0;
        
        for (int i = 0; i < m; ++i) {
        	
            twoSat.clauses[i].firstVar = reader.nextInt();
            twoSat.clauses[i].secondVar = reader.nextInt();
            
            
            if ( n < Math.abs(twoSat.clauses[i].firstVar)) {
            	n = Math.abs(twoSat.clauses[i].firstVar);
            }
            
            if ( n < Math.abs(twoSat.clauses[i].secondVar)) {
            	n = Math.abs(twoSat.clauses[i].secondVar);
            }
            
            //maxVarNumber = Math.max(maxVarNumber, Math.max(twoSat.clauses[i].firstVar+n, twoSat.clauses[i].secondVar+n));
            
        }

        /*
        if (m==1) {
        	System.out.println("SATISFIABLE");
        	System.out.println(twoSat.clauses[0].firstVar + " " + twoSat.clauses[0].secondVar);
        	System.exit(0);
        }
        */
        
         
        
        
        maxVarNumber = 2 * n;

        for (int i = 0; i < n; i++) {
        	mapping.put(i-n, i);
        	mapping.put(i+1, i+n);
        	mappingFromIndexToKeys.put(i, i-n);
        	mappingFromIndexToKeys.put(i+n, i+1);
        }
        
        for (int i = 0; i < n; i++) {
        	implicationGraph.put(i, new ArrayList<Integer>());
        	implicationGraph.put(i+n, new ArrayList<Integer>());
        }
        
        for (Clause clause : twoSat.clauses) {
        	implicationGraph.get(mapping.get(-clause.firstVar)).add(mapping.get(clause.secondVar));
        	implicationGraph.get(mapping.get(-clause.secondVar)).add(mapping.get(clause.firstVar));
        }
        
        List<Integer>[] adj = new ArrayList[2*n];
        List<Integer>[] adjR = new ArrayList[2*n];
        
        varScc = new int[2*n];
        
        for (int i = 0; i < adjR.length; i++) {
    		adjR[i] = new ArrayList<Integer>();
    	}
        
        for (Entry<Integer,List<Integer>> keyValuePair : implicationGraph.entrySet()) {
        	
        	adj[keyValuePair.getKey()] = keyValuePair.getValue();
        	
        	for (int nodeDest : keyValuePair.getValue()) {
        		adjR[nodeDest].add(keyValuePair.getKey());
        	}
        	
        }
        
        List<List<Integer>> sccs =  findStronglyConnectedComponents(adj, adjR);
        checkSatisfiability(mapping, mappingFromIndexToKeys);
        
        int cnfVars[] = new int[2*n];
        
        for (List<Integer> scc : sccs) {
        	assignLiterals(scc, cnfVars, n, mapping, mappingFromIndexToKeys);
        }
        
        System.out.println("SATISFIABLE");
        /*
        for (int i = 0; i < m; i++) {
        	System.out.print(twoSat.clauses[i].firstVar + "  ");
        	System.out.println(twoSat.clauses[i].secondVar);
        	
        }
        */
        
        ArrayList<Integer> satisfyingAssignment = new ArrayList<Integer>(); 
        
        
       
        
        for (int i = 1; i <= n; i++) {
        	System.out.print(cnfVars[mapping.get(i)]*i + " ");
        	
        	satisfyingAssignment.add(cnfVars[mapping.get(i)]*i);
        }
        
        
        /*
        for (int i = 0; i < m; i++) {
        	System.out.println( cnfVars[ mapping.get(twoSat.clauses[i].firstVar)] + "  " + cnfVars[ mapping.get(twoSat.clauses[i].secondVar) ]);
        	System.out.println(mapping.get(twoSat.clauses[i].firstVar));
        	System.out.println(mapping.get(twoSat.clauses[i].secondVar));
        	//System.out.println(twoSat.clauses[i].secondVar);
        	
        }
        */
        
        /*System.out.println(verifySatisfiability(satisfyingAssignment, twoSat));*/
    }
    
    
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    static class OutputWriter {
        public PrintWriter writer;

        OutputWriter(OutputStream stream) {
            writer = new PrintWriter(stream);
        }

        public void printf(String format, Object... args) {
            writer.print(String.format(Locale.ENGLISH, format, args));
        }
    }
}
