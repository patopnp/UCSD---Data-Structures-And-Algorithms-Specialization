import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class FunParty {
	
	private int[] d;
	private int[] weights;
    private final InputReader reader;
    private final OutputWriter writer;
	
    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        
        
     // This is to avoid stack overflow issues
        new Thread(null, new Runnable() {
                      public void run() {
                        	  new FunParty(reader, writer).run();
                          
                      }
                  }, "1", 1 << 26).start();
        
        
       
        writer.writer.flush();
    }
	
    public FunParty(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }
    
    public void run() {
        int n = reader.nextInt();
        
        weights = new int[n+1];
        d = new int[n+1];
        
        for (int t = 1; t <= n; t++) {
        	weights[t] = reader.nextInt();
        }
        
        Node[] treeNodes = new Node[n+1];
        
        for (int t = 1; t <= n; t++) {
        	treeNodes[t] = new Node(t);
        }
        
        for (int t = 1; t < n; t++) {
        
	        int key = reader.nextInt();
	        int child = reader.nextInt();
	        
	        treeNodes[key].add(treeNodes[child]);
	        
	        if (key != 1)
	        	treeNodes[child].add(treeNodes[key]);
        }
        trimTree(treeNodes[1], null);
        int result =  maxWeightValue(treeNodes[1]);
        System.out.println(result);
        
        
        
    }
    
    
    public void trimTree(Node currNode, Node parent) {
    	
    	for (int i = 0; i < currNode.children.size(); i++) {
    		if (currNode.children.get(i) == parent) {
    			currNode.children.remove(i);
    			i--;
    		}
    		else {
    			trimTree(currNode.children.get(i), currNode);
    		}
    	}
    	
    	
    }
    
    //fix stack overflow error
    int maxWeightValue(Node root) {
    	if (d[root.key] == 0) {
    		
    		if (root.children.size() == 0) {
    			d[root.key] = weights[root.key];
    		}
    		else {
    			int m1 = weights[root.key];
    			for (Node child : root.children)
    				for (Node grandchild : child.children)
    					m1 +=  maxWeightValue(grandchild);
    			int m0 = 0;
    			
    			for (Node child : root.children) {
    				m0 += maxWeightValue(child);
    			}
    			d[root.key] = Math.max(m0, m1);
    		}
    	}
    	return d[root.key];
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
    
   class Node {
	   List<Node> children;
	   int key;
	   
	   
	   public Node(int key) {
		   this.key = key;
		   this.children = new ArrayList<Node>();
	   }
	   
	   void add(Node c) {
		   children.add(c);
	   }
	   
   }
}
