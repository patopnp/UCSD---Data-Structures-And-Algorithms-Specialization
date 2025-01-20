import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


class GaussianElimination {
    static double[][] readInput() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();
        
        double a[][] = new double[size][size+1];
        
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size+1; column++)
                a[row][column] = scanner.nextInt();
            //b[row] = scanner.nextInt();
        }
        return a;
    }

    static void normalizeRow(double[][] a, int row) {

        for (int i = 0; i < a[row].length-1; i++) {
            if (a[row][i] != 0) {
                double denominator = a[row][i];
                a[row][i] = 1;

                for (int j = i+1; j < a[row].length; j++) {
                    a[row][j] /= denominator;
                }
                break;
            }
        }

    }

    static void substractRow(double[][] a, int from, int to, int col) {

       

        if (a[to][col] == 0) return;

        double value = a[to][col];

        for (int o = col; o < a[from].length; o++) {
            a[to][o] -= a[from][o] * value;
            //System.out.println("Ejecuto iteracion");
        }

        //print(a);
    }

    static void swapRows(double[][] a, int i, int pivotRow) {
        double[] aux = a[i];
        a[i] = a[pivotRow];
        a[pivotRow] = aux;
    }    

    static double[][] solve(double[][] a) {
        
        int pivotRow = 0;

        for (int c = 0; c < a[0].length-1; c++) {
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

        return a;
    }

    static void printTheSolution(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.printf("%.20f\n",a[i][a[i].length - 1] );
        }
    }

    static void print(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(" | ");
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(a[i][j] + " | ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        double[][] values = readInput();

        if (values.length == 0) {
            return;
        }
        
        double[][] yes = solve(values);
        printTheSolution(yes);
    }
}
