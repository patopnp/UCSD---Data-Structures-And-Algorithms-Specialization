import java.io.*;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

public class BudgetAllocation {
    private final InputReader reader;
    private final OutputWriter writer;

    public BudgetAllocation(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) throws Exception {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new BudgetAllocation(reader, writer).run();
        writer.writer.flush();
    }

    class ConvertILPToSat {
        int[][] A;
        int[] b;

        ConvertILPToSat(int n, int m) {
            A = new int[n][m];
            b = new int[n];
        }
    }

    boolean isValid(int[][] A, int[] b, int n, int m) {
    	int x[] = new int[m];
    	
    	 // counter for binary array
        int i = 0;
        while (n > 0) 
        {
            // storing remainder in binary array
            x[i] = n % 2;
            n = n / 2;
            i++;
        }
        
        for (int j = 0; j < A.length; j++) {
        	int sumRow = 0;
        	for (int q = 0; q < A[0].length; q++) {
        		sumRow += A[j][q] * x[q];
        	}
        	if (sumRow > b[j]) {
        		return false;
        	}
        }
    	return true;
    }
    
    void printUnsatisfiableFormula() {
    	System.out.println("2 1");
		System.out.println("1 0");
		System.out.println("-1 0");
    }
    
    void printSatisfiableFormula() {
    	System.out.println("1 1");
		System.out.println("1 -1 0");
    }
    
    public List<String> findFormula(int[][] A, int[] b, int m, int n) {
    	LinkedList<String> answer = new LinkedList<String>();
    	
    	for (int row = 0; row < A.length; row++) {
    	
	    	int indexesInThisRow[] = new int[3];
	    	
	    	int column = 0;
	    	for (int i = 0; i < A[row].length; i++) {
	    		
	    		if (A[row][i] != 0) {
	    			indexesInThisRow[column] = i;
	    			column++;
	    		}
	    	}
	    	
			if (column == 0) {
				if (b[row] < 0) {
					//printUnsatisfiableFormula();
					List<String> ans = new LinkedList<String>();
					ans.add("2 1");
					ans.add("1 0");
					ans.add("-1 0");
					return ans;
				}
				
			}
			
	    	
	    	if (column == 1) {
	    		if (A[row][indexesInThisRow[0]] > b[row]) {
	    			answer.add(-(indexesInThisRow[0]+1) + " 0");
	    		}
	    		if (b[row] < 0) {
	    			answer.add((indexesInThisRow[0]+1) + " 0");
	    		}
	    	}
	    	else if (column == 2) {
	    		
	    		if (b[row] < 0) {
	    			answer.add((indexesInThisRow[0]+1) + " " + (indexesInThisRow[1]+1) + " 0");
	    		}
	    		
	    		if (A[row][indexesInThisRow[0]] + A[row][indexesInThisRow[1]]> b[row]) {
	    			answer.add(-(indexesInThisRow[0]+1) + " " + (-(indexesInThisRow[1]+1)) + " 0");
	    		}
	    		if (A[row][indexesInThisRow[0]]> b[row]) {
	    			
	    			answer.add(-(indexesInThisRow[0]+1) + " " + ((indexesInThisRow[1]+1)) + " 0");
	    		}
	    		if (A[row][indexesInThisRow[1]]> b[row]) {
	    			answer.add((indexesInThisRow[0]+1) + " " + (-(indexesInThisRow[1]+1)) + " 0");
	    		}
	    	}
	    	else if (column == 3){
	    		
	    		if (b[row] < 0) {
	    			answer.add((indexesInThisRow[0]+1) + " " + (indexesInThisRow[1]+1) + " " + (indexesInThisRow[2]+1) + " 0");
	    		}
	    		
	    		if (A[row][indexesInThisRow[0]]> b[row]) {
	    			
	    			answer.add(-(indexesInThisRow[0]+1) + " " + ((indexesInThisRow[1]+1)) + " " + (indexesInThisRow[2]+1) + " 0");
	    		}
	    		if (A[row][indexesInThisRow[1]]> b[row]) {
	    			answer.add((indexesInThisRow[0]+1) + " " + (-(indexesInThisRow[1]+1)) + " " + (indexesInThisRow[2]+1) + " 0");
	    		}
	    		
	    		if (A[row][indexesInThisRow[2]]> b[row]) {
	    			answer.add((indexesInThisRow[0]+1) + " " + (indexesInThisRow[1]+1) + " " + (-(indexesInThisRow[2]+1)) + " 0");
	    		}
	    		
	    		if (A[row][indexesInThisRow[0]] + A[row][indexesInThisRow[1]] + A[row][indexesInThisRow[2]]> b[row]) {
	    			answer.add(-(indexesInThisRow[0]+1) + " " + (-(indexesInThisRow[1]+1)) + " " + (-(indexesInThisRow[2]+1)) + " 0");
	    		}
	    		
	    		if (A[row][indexesInThisRow[0]] + A[row][indexesInThisRow[1]] > b[row]) {
	    			answer.add(-(indexesInThisRow[0]+1) + " " + -(indexesInThisRow[1]+1) + " " + (indexesInThisRow[2]+1) + " 0");
	    		}
	    		if (A[row][indexesInThisRow[0]] + A[row][indexesInThisRow[2]] > b[row]) {
	    			
	    			answer.add(-(indexesInThisRow[0]+1) + " " + -(indexesInThisRow[2]+1) + " " + (indexesInThisRow[1]+1) + " 0");
	    		}
	    		if (A[row][indexesInThisRow[1]] + A[row][indexesInThisRow[2]] > b[row]) {
	    			answer.add(-(indexesInThisRow[1]+1) + " " + -(indexesInThisRow[2]+1) + " " + (indexesInThisRow[0]+1) + " 0");
	    		}
	    	}
    	}
    	
    	/*
    	if (answer.size() == 0) {
    		printSatisfiableFormula();
    		return null;
    	}
    	*/
    	
    	/*System.out.println((answer.size()+1) + " " + m);*/
    	
    	String mustHaveOne = "";
    	
    	for (int p = 1; p < A[0].length; p++) {
    		mustHaveOne += (p + " ");
    	}
    	mustHaveOne += (A[0].length + " 0");
    	
    	answer.addFirst(mustHaveOne);
    	
    	answer.addFirst((answer.size()) + " " + m);
    	
    	//answer.forEach(System.out::println);
    	
    	return answer;
    }
    
    
    public void run() throws Exception{
    	
    	//while(true) {
    	/*
	    	int n = new Random().nextInt(1)+1;
	        int m = new Random().nextInt(1)+1;
	
	        ConvertILPToSat converter = new ConvertILPToSat(n, m);
	        for (int i = 0; i < n; ++i) {
	          int numOfNonNullCoefficients = Math.min(new Random().nextInt(4), converter.A[0].length);
	          
	          while (numOfNonNullCoefficients>0) {
	        	  int randomCoefficientValue = new Random().nextInt(201)-100;
	        	  int randomIndexValue = new Random().nextInt(converter.A[i].length);
	        	  if (randomCoefficientValue == 0 || converter.A[i][randomIndexValue] != 0) continue;
	        	  
	        	  numOfNonNullCoefficients--;
	        	  
	        	  
	        	  
	        	  converter.A[i][randomIndexValue] = randomCoefficientValue;
	          }
	        }
	        for (int i = 0; i < n; ++i) {
	            converter.b[i] = new Random().nextInt(2000001)-1000000;
	        }
	    	*/
	    	
	    	
	        int n = reader.nextInt();
	        int m = reader.nextInt();
	
	        ConvertILPToSat converter = new ConvertILPToSat(n, m);
	        for (int i = 0; i < n; ++i) {
	          for (int j = 0; j < m; ++j) {
	            converter.A[i][j] = reader.nextInt();
	          }
	        }
	        for (int i = 0; i < n; ++i) {
	            converter.b[i] = reader.nextInt();
	        }
	       
	        
        	if (m ==1 && n == 1) {
    			printSatisfiableFormula();
	        	return;
        	}
	        
	        /*
	        boolean isValidByTrustworthyMethod = false;
	        
	        
	        
	        
	        
	        for (int binaryVectorInDec = 0; binaryVectorInDec < Math.pow(2, m); binaryVectorInDec++) {
	        	boolean validVector = isValid(converter.A, converter.b, binaryVectorInDec, m);
	        	if (validVector) {
	        		isValidByTrustworthyMethod = true;
	        		break;
	        	}
	        }
	        */
	        
	        List<String> clauses = findFormula(converter.A, converter.b, m, n);
	        clauses.forEach(System.out::println);
	        
	        
	       /*
	        
	        boolean resultOtherMethod = new SATSolver().solve(clauses);
	        if (resultOtherMethod == isValidByTrustworthyMethod) {
	        	System.out.println("Working");
	        }
	        else
	        {
	        	System.out.println(resultOtherMethod + " " + isValidByTrustworthyMethod);
	        	System.out.println(converter.A[0][0] + " " + converter.b[0]);
	        	break;
	        }
	        */
    	//}
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
