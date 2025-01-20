import java.io.*;
import java.util.*;

public class Diet {

	boolean solutionFoundIsUnbounded = false;
	
    BufferedReader br;
    PrintWriter out;
    StringTokenizer st;
    boolean eof;
    double[][] a;
    double[] solutionFound;

    double[] c;

    int solveDietProblem(int n, int m, double A[][], double[] b, double[] c, double[] x) {
      Arrays.fill(x, 1);
      // Write your code here
      return 0;
    }

    static void normalizeRow(double[][] a, int row) {

        for (int i = 0; i < a[row].length - 1; i++) {
            if (a[row][i] != 0) {
                double denominator = a[row][i];
                a[row][i] = 1;

                for (int j = i + 1; j < a[row].length; j++) {
                    a[row][j] /= denominator;
                }
                break;
            }
        }

    }

    static void substractRow(double[][] a, int from, int to, int col) {

        if (a[to][col] == 0)
            return;

        double value = a[to][col];

        for (int o = col; o < a[from].length; o++) {
            a[to][o] -= a[from][o] * value;
            // System.out.println("Ejecuto iteracion");
        }

        // print(a);
    }

    static void printTheSolution(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.printf("%.20f\n", a[i][a[i].length - 1]);
        }
    }

    static void swapRows(double[][] a, int i, int pivotRow) {
        double[] aux = a[i];
        a[i] = a[pivotRow];
        a[pivotRow] = aux;
    }

    static double[][] solve(double[][] a) {

        int pivotRow = 0;

        for (int c = 0; c < a[0].length - 1; c++) {
            for (int i = pivotRow; i < a.length; i++) {
                if (a[i][c] != 0) {

                    swapRows(a, i, pivotRow);
                    normalizeRow(a, pivotRow);

                    for (int to = 0; to < a.length; to++) {
                        if (to == pivotRow) {
                            continue;
                        }
                        substractRow(a, pivotRow, to, c);
                    }

                    pivotRow++;

                    break;

                }
            }
        }

        if (pivotRow < a.length) {
        	for (int i = pivotRow; i < a.length; i++) {
        		for (int j =0; j < a[pivotRow].length; j++) {
        			if (a[pivotRow][i] != 0) {
        				return null;
        			}
        		}
        	}
        }
        
        return a;
    }

    public boolean satisfiesOtherInequalities(double[] solutionsEqualities, List<Integer> mustSkip) {

        for (int p = 0; p < a.length-1; p++) {
        	
        	
        	//if (mustSkip.contains(p)) continue;
        	
            double sum = 0;

            for (int q = 0; q < a[p].length - 1; q++) {
                sum += a[p][q] * solutionsEqualities[q];
            }

            if (sum <= (a[p][a[p].length - 1] + Math.abs(sum/1000000)) == false) {
                return false;
            }
        }
        
        return true;
    }

    public double calculatePleasure(double[] solution) {
        double sum = 0;

        for (int i = 0; i < solution.length; i++) {
            sum += solution[i] * c[i];
        }

        return sum;
    }
/*
    public double calculateUnbounded(int n, int numVariables, double bestPleasure) {

        double solution[] = new double[numVariables];
        
        // Size of an integer is assumed to be 32 bits
        for (int i = numVariables // - 1; i >= 0; i--) {
            int k = n >> i;
            if ((k & 1) > 0) {
            	solution[i] = 1000000/8.0;
            }
        }


        if (satisfiesOtherInequalities(solution)) {
            double pleasure = calculatePleasure(solution);
            if (pleasure > bestPleasure) {
                solutionFound = solution;
                solutionFoundIsUnbounded = true;
            }
            return pleasure;
        }

        
        return Integer.MIN_VALUE;
    }
    */
    public double calculateCombination(int n, int numberOfInequalities, int numVariables, double bestPleasure) {

        List<Integer> selectedEquations = new ArrayList<>();
        int numberOfOnes = 0;

        boolean solutionIsUnbounded = false;
        
        // Size of an integer is assumed to be 32 bits
        for (int i = numVariables + numberOfInequalities /* - 1*/; i >= 0; i--) {
            int k = n >> i;
            if ((k & 1) > 0) {
            	if (i == numberOfInequalities+numVariables) {
            		solutionIsUnbounded = true;
            	}
                numberOfOnes++;
                //System.out.print("1");
                selectedEquations.add(i);
            } //else {
            	//selectedEquations.add(i);
                //System.out.print("0");
            // }
        }

        
        
        if (numberOfOnes == numVariables) {

            double[][] equations = new double[numVariables][numVariables + 1];
            
            int i = 0;

            for (int elem : selectedEquations) {
                equations[i] = a[elem].clone();
                i++;
            }

            double result[][] = solve(equations);
            
            if(result == null) {
            	return 0;
            }
            
            double solution[] = new double[result.length];

            for (int j = 0; j < result.length; j++) {
                solution[j] = result[j][result[j].length-1];
            }

            if (satisfiesOtherInequalities(solution, selectedEquations)) {
                double pleasure = calculatePleasure(solution);
                if (pleasure > bestPleasure) {
                    solutionFound = solution;
                    solutionFoundIsUnbounded = solutionIsUnbounded;
                }
                return pleasure;
            }
        }

        
        return Integer.MIN_VALUE;
    }

    void solve() throws IOException {
    	
        int n = nextInt(); // restrictions
        int m = nextInt(); // variables

        a = new double[n+m+1][m+1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                a[i][j] = nextInt();
            }
        }
        
        
        
        
        
        
        
        for (int i = 0; i < m; i++) {
        	a[n+i][i] = -1;
        }
        
        
        for (int i = 0; i < m; i++) {
        	a[n+m][i] = 1;
        }
        
        a[n+m][a[n+m].length - 1] = 1000000000;
        
        
        for (int i = 0; i < n; i++) {
            a[i][a[0].length - 1] = nextInt();
        }

        c = new double[m];

        for (int i = 0; i < m; i++) {
            c[i] = nextInt();
            a[n+m][i] = c[i];
        }

        a[n+m][a[n+m].length - 1] = 100000000;
        
        double bestPleasure = Integer.MIN_VALUE;

        for (int i = 0; i < Math.pow(2, n+m+1); i++) {
            
            double currPleasure = calculateCombination(i, n, m, bestPleasure);

            if (currPleasure > bestPleasure) {
                bestPleasure = currPleasure;
            }
            
        }

        /*
        for (int i = 0; i < Math.pow(2, m); i++) {
            
            double currPleasure = calculateUnbounded(i, m, bestPleasure);

            if (currPleasure > bestPleasure) {
                bestPleasure = currPleasure;
            }
            
        }
        */
        
        if (solutionFound == null)
        	System.out.println("No solution");
        else {
        	
        	if(solutionFoundIsUnbounded) {
        		System.out.println("Infinity");
        		return;
        	}
        	
        	
        	System.out.println("Bounded solution");
        	
        	for (int i = 0; i < m; i++) {
        		System.out.print(solutionFound[i] + " ");
        	}
        }
    }

    Diet() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out);
        solve();
        out.close();
    }

    public static void main(String[] args) throws IOException {
        new Diet();
    }

    String nextToken() {
        while (st == null || !st.hasMoreTokens()) {
            try {
                st = new StringTokenizer(br.readLine());
            } catch (Exception e) {
                eof = true;
                return null;
            }
        }
        return st.nextToken();
    }

    int nextInt() throws IOException {
        return Integer.parseInt(nextToken());
    }
}
