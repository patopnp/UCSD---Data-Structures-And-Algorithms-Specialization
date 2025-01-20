import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class BuildHeap {
  private int[] data;
    int size;
    private List<Swap> swaps;

    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new BuildHeap().solve();
    }

    private void readData() throws IOException {
        int n = in.nextInt();
        data = new int[n+1];
        for (int i = 1; i <= n; ++i) {
          data[i] = in.nextInt();
        }
    }

    private void writeResponse() {
        out.println(swaps.size());
        for (Swap swap : swaps) {
          out.println((swap.index1-1) + " " + (swap.index2-1));
        }
    }

    private void generateSwaps() {
      swaps = new ArrayList<Swap>();

      buildHeap();

      /*
      for (int j = 0; j < size - 1; j++) {
        swaps.add(new Swap(1, size));
        int aux = data[1];
        data[1] = data[size];
        data[size] = aux;
        size--;
        siftDown(1);
      }
      */

      // The following naive implementation just sorts 
      // the given sequence using selection sort algorithm
      // and saves the resulting sequence of swaps.
      // This turns the given array into a heap, 
      // but in the worst case gives a quadratic number of swaps.
      //
      // TODO: replace by a more efficient implementation
      /*for (int i = 0; i < data.length; ++i) {
        for (int j = i + 1; j < data.length; ++j) {
          if (data[i] > data[j]) {
            swaps.add(new Swap(i, j));
            int tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
          }
        }
      }*/
    }

    public void buildHeap() {
      size = data.length-1;
      for (int i = (int)(size/2); i >= 1; i--) {
        siftDown(i);
      }
    }

    int parent(int i) {
        return (int)(i/2);
    }

    int leftChild(int i) {
        return 2*i;
    }

    int rightChild(int i) {
        return 2 * i + 1;
    }

    void siftUp(int i) {
      while (i > 1 && data[parent(i)] > data[i]) {
                    swaps.add(new Swap(parent(i), i));
                    int aux = data[parent(i)];
                    data[parent(i)] = data[i];
                    data[i] = aux;
                    i = parent(i);
        }
    }

    public void siftDown(int i) {
      int maxIndex = i;
      int l = leftChild(i);

      if (l <= size && data[l] < data[maxIndex]) {
        maxIndex = l;
      }
      int r = rightChild(i);

      if (r <= size && data[r] < data[maxIndex]) {
        maxIndex = r;
      }

      if (i != maxIndex) {
        swaps.add(new Swap(i, maxIndex));
        int aux = data[i];
        data[i] = data[maxIndex];
        data[maxIndex] = aux;
        siftDown(maxIndex);
      }
    } 

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        readData();
        generateSwaps();
        writeResponse();
        out.close();
    }

    static class Swap {
        int index1;
        int index2;

        public Swap(int index1, int index2) {
            this.index1 = index1;
            this.index2 = index2;
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
