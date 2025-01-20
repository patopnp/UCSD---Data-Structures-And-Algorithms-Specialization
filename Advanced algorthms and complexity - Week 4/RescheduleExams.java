import java.io.*;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.StringTokenizer;

public class RescheduleExams {
    private final InputReader reader;
    private final OutputWriter writer;

    int varScc[];
    
    public RescheduleExams(InputReader reader, OutputWriter writer) {
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
    
    public void explore(List<Integer>[] adj, boolean[] visited, int vertex, List<List<Integer>> sccs) {
    	
    	sccs.get(sccs.size()-1).add(vertex);
    	varScc[vertex] = sccs.size()-1;
    	
        visited[vertex] = true;
        for (int neighbor : adj[vertex]) {
            if (!visited[neighbor]) {
                explore(adj, visited, neighbor, sccs);
            }
        }
    }
    
    public static void explore(List<Integer>[] adj, boolean[] visited, int vertex, Deque<Integer> order) {
        visited[vertex] = true;
        for (int neighbor : adj[vertex]) {
            if (!visited[neighbor]) {
                explore(adj, visited, neighbor, order);
            }
        }

        order.addFirst(vertex);
    }
    
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
    			System.out.println("Impossible");
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
        
        // This is to avoid stack overflow issues
        new Thread(null, new Runnable() {
                      public void run() {
                    	  new RescheduleExams(reader, writer).run();
                          
                      }
                  }, "1", 1 << 26).start();
        
        
        
        writer.writer.flush();
    }

    static class Clause {
        int firstVar;
        int secondVar;
        
        Clause(int firstVar, int secondVar){
        	this.firstVar = firstVar;
        	this.secondVar = secondVar;
        }
        
        Clause(int firstVar) {
        	this.firstVar = firstVar;
        }
        
    }


    public static boolean verifySatisfiability(ArrayList<Integer> satisfyingAssignment, List<Clause> clauses) {
    	for (int i = 0; i < clauses.size(); ++i) {
    		
    		if(!satisfyingAssignment.contains(clauses.get(i).firstVar) && !satisfyingAssignment.contains(clauses.get(i).secondVar)){
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
    	
        int numVars = n*3;
        int numClauses = n*6;
        
        
        String colorsLine = reader.nextLine();
        char[] colors = colorsLine.toCharArray();
        ArrayList<Clause> clausesList = new ArrayList<>();
        ArrayList<Clause> singleLiteralClausesList = new ArrayList<>();
        
        for (int j = 0; j < colors.length; j++) {
        	int c = j*3+getColorIndexFromChar(colors[j]);
        	int firstdifferentc = j*3+ ((getColorIndexFromChar(colors[j])-1+1)%3)+1;
        	int seconddifferentc = j*3+ ((getColorIndexFromChar(colors[j])-1+2)%3)+1;
        	
        	Clause notSameColor = new Clause(-c);
        	Clause atLeastOneColor = new Clause(firstdifferentc, seconddifferentc);
        	Clause atMostOneColor = new Clause(-firstdifferentc, -seconddifferentc);

        	singleLiteralClausesList.add(notSameColor);
        	clausesList.add(atLeastOneColor);
        	clausesList.add(atMostOneColor);
        }        
        
        for (int i = 0; i < m; i++) {
            int u = reader.nextInt();
            int v = reader.nextInt();
            
            int ru = (u-1)*3+1;
            int gu = (u-1)*3+2;
            int bu = (u-1)*3+3;
            int rv = (v-1)*3+1;
            int gv = (v-1)*3+2;
            int bv = (v-1)*3+3;
            
            Clause rAdj = new Clause(-ru,-rv);
            
            Clause gAdj = new Clause(-gu,-gv);
            
            Clause bAdj = new Clause(-bu,-bv);
            
            clausesList.add(rAdj);
        	clausesList.add(gAdj);
        	clausesList.add(bAdj);
            
        }
        
        
        
         

        for (int i = 0; i < numVars; i++) {
        	mapping.put(i-numVars, i);
        	mapping.put(i+1, i+numVars);
        	mappingFromIndexToKeys.put(i, i-numVars);
        	mappingFromIndexToKeys.put(i+numVars, i+1);
        }
        
        for (int i = 0; i < numVars; i++) {
        	implicationGraph.put(i, new ArrayList<Integer>());
        	implicationGraph.put(i+numVars, new ArrayList<Integer>());
        }
        
        for (Clause clause : clausesList) {
        	implicationGraph.get(mapping.get(-clause.firstVar)).add(mapping.get(clause.secondVar));
        	implicationGraph.get(mapping.get(-clause.secondVar)).add(mapping.get(clause.firstVar));
        }
        
        for (Clause clause : singleLiteralClausesList) {
        	implicationGraph.get(mapping.get(-clause.firstVar)).add(mapping.get(clause.firstVar));
        }
        
        
        List<Integer>[] adj = new ArrayList[2*numVars];
        List<Integer>[] adjR = new ArrayList[2*numVars];
        
        varScc = new int[2*numVars];
        
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
        
        int cnfVars[] = new int[2*numVars];
        
        for (List<Integer> scc : sccs) {
        	assignLiterals(scc, cnfVars, numVars, mapping, mappingFromIndexToKeys);
        }
        
        //System.out.println("SATISFIABLE");
        
        ArrayList<Integer> satisfyingAssignment = new ArrayList<Integer>(); 
        
        
       
        
        for (int i = 1; i <= numVars; i++) {
        	//System.out.print(cnfVars[mapping.get(i)]*i + " ");
        	
        	satisfyingAssignment.add(cnfVars[mapping.get(i)]*i);
        }
        
        for (int i = 0; i < satisfyingAssignment.size(); i+=3) {
        	
        	if (satisfyingAssignment.get(i) > 0) {
        		System.out.print("R");
        	}
        	else if (satisfyingAssignment.get(i+1) > 0) {
        		System.out.print("G");
        	}
        	else if (satisfyingAssignment.get(i+2) > 0) {
        		System.out.print("B");
        	}
        }
        
        
        /*
        for (int i = 0; i < m; i++) {
        	System.out.println( cnfVars[ mapping.get(twoSat.clauses[i].firstVar)] + "  " + cnfVars[ mapping.get(twoSat.clauses[i].secondVar) ]);
        	System.out.println(mapping.get(twoSat.clauses[i].firstVar));
        	System.out.println(mapping.get(twoSat.clauses[i].secondVar));
        	//System.out.println(twoSat.clauses[i].secondVar);
        	
        }
        */
        
    }
    static int getColorIndexFromChar(char c) {
		if (c == 'R') {
			return 1;
		}
		else if (c== 'G') {
			return 2;
		}
		else if (c == 'B') {
			return 3;
		}
		return 1;
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

        public String nextLine() {
        	try {
				return reader.readLine();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
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
