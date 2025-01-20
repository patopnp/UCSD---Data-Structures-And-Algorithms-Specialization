import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.StringTokenizer;

public class TravelingSalesman {

	List<Integer> path[][];
    private static int INF = 1000 * 1000 * 1000;

    
    
    public static void main(String[] args) throws IOException {
        new TravelingSalesman().
    	HeldKarp(readData());
        
    }
	
    void HeldKarp(int[][] graph) {  
    	int n = graph.length;
    	path = new ArrayList[(int)Math.pow(2, n)-1][n];
    	
    	for (int i = 0; i < path.length; i++) {
    		for (int j = 0; j < path[0].length; j++) {
    			path[i][j] = new ArrayList<Integer>();
    		}
    	}
    	
    	int c[][] = new int[(int)Math.pow(2, n)-1][n];
    
    	c[0][0] = 0;
    	
    	for (int size = 2; size <= n; size++) {
    		
    		List<Integer> setsOfSizeN = generateNumbersWithNOnes(n, size);
    		
    		for (int s : setsOfSizeN) {
    			c[s-1][0] = INF;
    			List<Integer> elementsFromS = new ArrayList<>();
    			String binaryRepresentation =  Integer.toBinaryString(s);
    			//System.out.println(binaryRepresentation);
    			for (int place = 0; place < n && place < binaryRepresentation.length(); place++) {
    				
    				if (binaryRepresentation.charAt(binaryRepresentation.length()-place-1) == '1') {
    					//use a hashmap
    					elementsFromS.add(place+1);
    				}
    				
    			}
    			for (int dest : elementsFromS) {
    				if (dest == 1) continue;
    				
    				int minC = INF;
    				int minp = -1;
    				int destToBinary = (int)Math.pow(2,dest-1);
    				int index = (s^destToBinary)-1;
    				for (int p : elementsFromS) {
    					if (p==dest) continue;
    					
    					
    					int auxC = c[index][p-1] + graph[p-1][dest-1];
    					
    					if (auxC < minC) {
    						minC = auxC;
    						minp = p;
    					}
    					
    				}
    				path[s-1][dest-1] = new ArrayList<>();
    				if (minp != -1) {
						path[s-1][dest-1].addAll(path[index][minp-1]);
						path[s-1][dest-1].add(dest);
    				}
    				c[s-1][dest-1] = minC;
    				
    				
    			}
    		}
    		
    	}
    	
    	int lastS = ((int)Math.pow(2, n)) - 1;
    	int cmin = INF;
    	List<Integer> bestPath = null;
    	for (int i = 0; i < n; i++) {
    		int auxC = c[lastS-1][i] + graph[i][0];
    		if (cmin > auxC) {
    			cmin = auxC;
    			bestPath = path[lastS-1][i];
    		}
    		
    	}
    	if (cmin >= INF) {
    		System.out.println("-1");
    	}
    	else {
	    	System.out.println(cmin);
	    	
	    	System.out.print("1 ");
	    	
	    	for (int stop : bestPath) {
	    		System.out.print(stop + " ");
	    	}
    	
    	}
    }
    
    
    
    private static int[][] readData() throws IOException {
    	FastScanner in = new FastScanner();
        int n = in.nextInt();
        int m = in.nextInt();
        int[][] graph = new int[n][n];

        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                graph[i][j] = INF;

        for (int i = 0; i < m; ++i) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;
            int weight = in.nextInt();
            graph[u][v] = graph[v][u] = weight;
        }
        return graph;
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

	    public static List<Integer> generateNumbersWithNOnes(int numBits, int numOnes) {
	        List<Integer> result = new ArrayList<>();
	        generateCombinations(result, 1, numBits, numOnes-1, 1);
	        return result;
	    }

	    private static void generateCombinations(List<Integer> result, int currentNumber, int numBits, int remainingOnes, int start) {
	        if (remainingOnes == 0) {
	            // Add the constructed number to the result list
	            result.add(currentNumber);
	            return;
	        }

	        for (int i = start; i < numBits; i++) {
	            // Set the bit at position `i` and proceed recursively
	            generateCombinations(result, currentNumber | (1 << i), numBits, remainingOnes - 1, i + 1);
	        }
	    }

	
}
