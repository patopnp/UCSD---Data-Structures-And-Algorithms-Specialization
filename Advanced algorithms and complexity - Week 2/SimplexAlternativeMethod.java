import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class SimplexAlternativeMethod {
	
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static PrintWriter out = new PrintWriter(System.out);;
    static StringTokenizer st;
    static boolean eof;
	
	static String nextToken() {
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
	
	static PairRowTableau processPivotElement(double[][] sys, int[] pivotElement, boolean phaseOneOptimization, double[] phaseOneRow) {
		double priMult = sys[pivotElement[0]][pivotElement[1]];
		
		for (int j = 0; j < sys[pivotElement[0]].length; j++) {
			sys[pivotElement[0]][j] /= priMult;
		}
		
		sys[pivotElement[0]][pivotElement[1]] = 1;
		
		for (int j = 0; j < sys.length; j++) {
			if (j == pivotElement[0]) continue;
			double secMult = sys[j][pivotElement[1]];
			double priRow[] = new double[sys[pivotElement[0]].length];
			for (int l = 0; l < sys[pivotElement[0]].length; l++) {
				priRow[l] = sys[pivotElement[0]][l] * secMult;
			}
			for (int l = 0; l < sys[pivotElement[0]].length; l++) {
				sys[j][l] -= priRow[l];
			}
			sys[j][pivotElement[1]] = 0;
		}
		if (phaseOneOptimization) {
			double secMult = phaseOneRow[pivotElement[1]];
			double[] priRow = new double[sys[pivotElement[0]].length];
			for (int l = 0; l < sys[pivotElement[0]].length; l++) {
				priRow[l] = sys[pivotElement[0]][l] * secMult;
			}
			for (int l = 0; l < sys[pivotElement[0]].length; l++) {
				phaseOneRow[l] -= priRow[l];
			}
			phaseOneRow[pivotElement[1]] = 0;
		}
		return new PairRowTableau(phaseOneRow, sys);
	}
	
	static int nextInt() throws IOException {
        return Integer.parseInt(nextToken());
    }
	
	static void printColumn(double[] column) {
		if (column == null) {
			System.out.println("No solution");
		}
		else if (column[0] == Double.POSITIVE_INFINITY) {
			System.out.println("Infinity");
		}
		else {
			System.out.println("Bounded solution");
			int lengthMinusOne = column.length-1;
			for (int i = 0; i < column.length-1; i++) {
				System.out.print(column[i] + " ");
			}
			System.out.print(column[lengthMinusOne]);
		}
	}
	
	static double[] sumRows(double[] r1, double[] r2) {
		
		double[] row = new double[r1.length];
		
		for (int i = 0; i < r1.length; i++) {
			row[i] = r1[i] + r2[i];
		}
		
		return row;
	}
	
	static double[] generateRow(double[][] A, int i, double[] b, int sign, double[] slackVariables) {
		double[] newRow = new double[A[i].length+slackVariables.length+1];
		
		for (int j = 0; j < A[i].length; j++) {
			newRow[j] = sign * A[i][j];
		}
		
		for (int j = 0; j < slackVariables.length; j++) {
			newRow[A[i].length + j] = slackVariables[j];
		}
		
		newRow[A[i].length + slackVariables.length] = sign * b[i];
		
		return newRow;
	}
	
	static class PairOfAnswersYes {
		
		double[] ans1;
		double[] ans2;
		
		PairOfAnswersYes(double[] ans1, double[] ans2) {
			this.ans1 = ans1;
			this.ans2 = ans2;
		}
	}
	
	//en todos los casos reemplazar los menos y mas con comparacion con un diferencial porque no es exacta la division 
	
	public static PairRowTableau setUpTableau(double[][] A, double[] b, double[] c, int n, int m, boolean phaseOneOptimization) {
		
		double[][] tableau = new double[n+1][m+n+1];
		double[] phaseOneRow = new double[c.length + n + 2];
		
		for (int i = 0; i < n; i++) {
			
			if (phaseOneOptimization && b[i] < 0) {
				double[] slackVariables = new double[n];
				slackVariables[i] = -1;
				double[] tableauRow = generateRow(A, i, b, -1, slackVariables);
				tableau[i] = tableauRow;
				phaseOneRow = sumRows(tableauRow, phaseOneRow);
			}
			else {
				double[] slackVariables = new double[n];
				slackVariables[i] = 1;
				double[] tableauRow = generateRow(A, i, b, 1, slackVariables);
				tableau[i] = tableauRow;
			}
			
			
		}

		//generate final tableau row
		
		for (int j = 0; j < c.length; j++) {
			tableau[n][j] = -c[j];
		}
		
		
		
		for (int j = c.length; j < tableau.length-1; j++) {
			tableau[n][j] = 0;
		}
		
		tableau[n][tableau[0].length-1] = 0;
		return new PairRowTableau(phaseOneRow, tableau);
	}
	
	static boolean areThereNegativeColumns(double[][] tableau) {
		
		for (int i = 0; i < tableau[tableau.length-1].length-1; i++) {
			if (!epsilonGreaterThanEqualTo(tableau[tableau.length-1][i], 0)) {
				return true;
			}
		}
		return false;
		
	}
	
	static boolean areThereNoPositiveColumns(double[] row) {
		
		for (int i = 0; i < row.length-1; i++) {
			
			if (epsilonGreaterThan(row[i],0)) {
				return false;
			}
			
			
		}
		return true;
		
	}
	
	static int containsElement(int element, int[] slackRows) {
		
		int index = -1;
		
		for (int i = 0; i < slackRows.length; i++) {
			if (slackRows[i] == element) {
				return i;
			}
		}
		return index;
	}
	
	static double[] determineAnswer(double[][] tableau, int[] slackRows, int m, int n) {
		double[] ans = new double[m];
		for (int i = 0; i < n + m ; i++) {
			int indexInSlack = containsElement(i,slackRows);
			if (i < m && indexInSlack != -1) {
				ans[i] = tableau[indexInSlack][tableau[0].length-1];
			}
			else if (indexInSlack == -1 && tableau[tableau.length-1][i] == 0) {
				for (int j = 0; j < n-1; j++) {
					if (tableau[j][i] > 0) {
						return null;
					}
				}
			}
			else if (i < m) {
				ans[i] = 0;
			}
		}
		return ans;
	}
	
	static int[] selectPivotElement(double[][] a, int m, int[] slackRows, boolean phaseOneOptimization, double[] phaseOneRow) {
		int[] pivotElement = new int[2];
		pivotElement[1] = -1;
		
		
		
		//Select column
		
		if (phaseOneOptimization) {
			double valueMax = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < phaseOneRow.length-1; i++) {
				if (phaseOneRow[i] > valueMax) {
					valueMax = phaseOneRow[i];
					pivotElement[1] = i;
				}
			}
			
		}
		else {
			double valueMin = Double.POSITIVE_INFINITY;
			for (int i = 0; i < a[a.length-1].length-1; i++) {
				if (a[a.length-1][i] < valueMin) {
					valueMin = a[a.length-1][i];
					pivotElement[1] = i;
				}
			}
		}
		
		//Select row
		
		if (pivotElement[1] != -1) {
			List<Double> ratios = new ArrayList<Double>();
			for (int i = 0; i < a.length-1; i++) {
				if (a[i][pivotElement[1]] > 0) {
					ratios.add(Math.abs(a[i][a[i].length-1]/a[i][pivotElement[1]]));
				}
				else {
					ratios.add(Double.POSITIVE_INFINITY);
				}
			}
			
			if (ratios.stream().allMatch(r -> r == Double.POSITIVE_INFINITY)) {
				return null;
			}
			
			double minValue = Collections.min(ratios);
			int minIndex = 0;
			
			for (int j = 0; j < ratios.size(); j++) {
				if (ratios.get(j) == minValue) {
					minIndex = j;
					break;
				}
			}
			
			pivotElement[0] = minIndex;
			
		}
		else {
			return null;
		}
		return pivotElement;
	}
	
	static double[][] solveSystem(double[][] sys, double[][] a, double[] b, int m, int n, boolean phaseOneOptimization, double[] phaseOneRow) {
		
		int[] slackRows = new int[n];
		
		for (int i = 0; i < slackRows.length; i++) {
			slackRows[i] = m + i;
		}
		
		boolean phaseOneComplete = false;
		double[] phaseOneAnswer = new double[m];
		int i = 0;
		while (phaseOneOptimization || areThereNegativeColumns(sys)) {
			if (phaseOneOptimization && areThereNoPositiveColumns(phaseOneRow)) {
				phaseOneOptimization = false;
				phaseOneComplete = true;
				phaseOneAnswer = determineAnswer(sys, slackRows, m, n);
				if (!areThereNegativeColumns(sys)) {
					break;
				}
			}
			int[] selectedElement = selectPivotElement(sys, m, slackRows, phaseOneOptimization, phaseOneRow);
			if (selectedElement == null) {
				if (phaseOneComplete) {
					return new double[][] {null, phaseOneAnswer};
				}
				else {
					return new double[][] {new double[]{Double.POSITIVE_INFINITY}, phaseOneAnswer};
				}
			}
			slackRows[selectedElement[0]] = selectedElement[1];
			PairRowTableau tableauPhaseOneRow = processPivotElement(sys, selectedElement, phaseOneOptimization, phaseOneRow);
			sys = tableauPhaseOneRow.tableau;
			phaseOneRow = tableauPhaseOneRow.phaseOneRow;
			
			i++;
			

		}
		return new double[][]{determineAnswer(sys, slackRows, m, n), phaseOneAnswer};
	}
	
	public static boolean isInvalidAnswer(double[] ans, double a[][], double b[], int m, int n) {
		
		
		for (int i = 0; i < n; i++) {
			double validAnswer = 0;
			for (int j = 0; j < m; j++) {
				validAnswer += a[i][j] * ans[j];
			}
			if (epsilonGreaterThan(validAnswer, b[i])) {
				return true;
			}
		}
		for (int i = 0; i < ans.length; i++) {
			if (epsilonLessThan(ans[i], 0)) {
				return true;
			}
		}
		return false;
	}
	
	public static double[] solve(double[][] A, double[] b, double[] c, int n, int m) {
		
		PairRowTableau initialTableauAndPhaseOneRow = setUpTableau(A, b, c, n, m, false);
		double[][] solutions = solveSystem(initialTableauAndPhaseOneRow.tableau, A, b, m, n, false, initialTableauAndPhaseOneRow.phaseOneRow);
		if (solutions[0] == null || solutions[0][0] == Double.POSITIVE_INFINITY) {
			return solutions[0];
		}
		boolean invalidAns = isInvalidAnswer(solutions[0], A, b, m, n);
		boolean phaseOneAnswerInvalid = true;
		if (invalidAns) {
			initialTableauAndPhaseOneRow = setUpTableau(A, b, c, n, m, true);
			solutions = solveSystem(initialTableauAndPhaseOneRow.tableau, A, b, m, n, true, initialTableauAndPhaseOneRow.phaseOneRow);
			phaseOneAnswerInvalid = isInvalidAnswer(solutions[1], A, b, m, n);
			
			if (solutions[0] == null || solutions[0][0] == Double.POSITIVE_INFINITY) {
				return solutions[0];
			}
			invalidAns = isInvalidAnswer(solutions[0], A, b, m, n);
		}
		if (invalidAns) {
			if (!phaseOneAnswerInvalid) {
				return solutions[1];
			}
			else {
				return null;
			}
		}
		return solutions[0];
	}
	
	static class PairRowTableau {
		double[] phaseOneRow;
		double[][] tableau;
		
		PairRowTableau(double[] phaseOneRow ,double[][] tableau) {
			this.phaseOneRow = phaseOneRow;
			this.tableau = tableau;
		}
	}
	
	public static void main(String[] args) throws IOException {
    	int n = nextInt();
        int m = nextInt();
        double[][] A = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                A[i][j] = nextInt();
            }
        }
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = nextInt();
        }
        double[] c = new double[m];
        for (int i = 0; i < m; i++) {
            c[i] = nextInt();
        }
        
        double[] solution = solve(A, b, c, n, m);
        printColumn(solution);
	}
	
	static boolean epsilonGreaterThan(double a, double b) {
		return (a > b) && !isClose(a, b);
	}
	
	static boolean epsilonGreaterThanEqualTo(double a, double b) {
		return (a > b) || isClose(a, b);
	}
	
	static boolean epsilonLessThan(double a, double b) {
		return (a < b) && !isClose(a, b);
	}
	
	static boolean epsilonLessTanEqualTo(double a, double b) {
		return (a < b) || isClose(a, b);
	}
	
	static boolean isClose(double a, double b) {
		return Math.abs(a-b) <= 0.0001;
	}
}
