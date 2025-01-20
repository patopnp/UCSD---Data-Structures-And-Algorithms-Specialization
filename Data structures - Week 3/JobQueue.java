import java.io.*;
import java.util.StringTokenizer;
import java.util.*;

public class JobQueue {
    private int numWorkers;
    private int[] jobs;

    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new JobQueue().solve();
    }

    private void readData() throws IOException {
        numWorkers = in.nextInt();
        int m = in.nextInt();
        jobs = new int[m];
        for (int i = 0; i < m; ++i) {
            jobs[i] = in.nextInt();
        }
    }

    private void generateData() throws IOException {

        Random rand = new Random();

        numWorkers = rand.nextInt(100000) + 1;
        int m = 1 + rand.nextInt(100000);

        jobs = new int[m];
        for (int i = 0; i < m; ++i) {
            jobs[i] = rand.nextInt(1000000000);
        }
        
        /*System.out.println("n is " + numWorkers + " m is " + m);*/
    }

    private void assignJobs() {

        //writeResponse();

        //System.out.println("fast correct algorithm");

        Heap threadsHeap = new Heap(numWorkers);

        int i = 0;
        int i2 = 0;

        //fijarse si esto considera cuando el numero de trabajadores es superior al numero de trabajos
        while (i < numWorkers && i2 < jobs.length) {

            if (jobs[i2] == 0) {
                System.out.println(i + " " + 0);
                /*if (assignedWorker[i2] != i || startTime[i2] != 0) {
                    System.out.println("ERROR ERROR");
                    success = false;
                }*/
                i2++;
                continue;
            }
            threadsHeap.insert(jobs[i2]);

            /*
            if (assignedWorker[i2] != i || startTime[i2] != 0) {
                System.out.println("ERROR ERROR");
                success = false;
            }
            */
            /*
            if (assignedWorker[i] != i || startTime[i] != 0) {
                System.out.println("ERROR ERROR");
                success = false;
            }
            */

            System.out.println(i + " " + 0);

            i++;
            i2++;
        }
                

        i = i2;
        /*
        System.out.println("binary tree is ");
        
        for (int j = 1; j <= numWorkers; j++) {
            System.out.println("index " + threadsHeap.nextFreeTime[j][1] + " time " + threadsHeap.nextFreeTime[j][0]);
        }
        
        System.out.println("min index is " + threadsHeap.getMinIndex());
        threadsHeap.changePriorityOfMin(10);
        System.out.println("min index is " + threadsHeap.getMinIndex());
        */

        
        while (i < jobs.length) {
            //System.out.println("before: " + (threadsHeap.getMinIndex() - 1) + " " + threadsHeap.getMinTime());
            /*if (assignedWorker[i] != threadsHeap.getMinIndex() - 1 || startTime[i] != threadsHeap.getMinTime()) {
                System.out.println("ERROR: " + (threadsHeap.getMinIndex() - 1) + "is not equal to " + assignedWorker[i]);
                success = false;
            }*/
            System.out.println(threadsHeap.getMinIndex() - 1 + " " + threadsHeap.getMinTime());
            threadsHeap.changePriorityOfMin(threadsHeap.getMinTime() + jobs[i]);
            i++;
        }
        /*
        if (success == true) {
            System.out.println("SUCCESS");
        }
        */


    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        //
        readData();
        //generateData();
        assignJobs();
        //writeResponse();
    
        out.close();
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

    class Heap {
        int maxSize;
        int size;
        long[][] nextFreeTime;

        Heap(int maxSize){
            this.maxSize = maxSize;
            nextFreeTime = new long[maxSize + 1][2];
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
            while (i > 1 && (nextFreeTime[parent(i)][0] > nextFreeTime[i][0] || (nextFreeTime[parent(i)][0] == nextFreeTime[i][0] && nextFreeTime[parent(i)][1] > nextFreeTime[i][1]))) {
                    long[] aux = nextFreeTime[parent(i)];
                    nextFreeTime[parent(i)] = nextFreeTime[i];
                    nextFreeTime[i] = aux;
                    i = parent(i);
            }
        }

        void siftDown(int i) {
            int maxIndex = i;
            int l = leftChild(i);

            if (l <= size && (nextFreeTime[l][0] < nextFreeTime[maxIndex][0]
                    || (nextFreeTime[l][0] == nextFreeTime[maxIndex][0]
                            && nextFreeTime[l][1] < nextFreeTime[maxIndex][1]))) {
                maxIndex = l;
            }
            int r = rightChild(i);

            if (r <= size && (nextFreeTime[r][0] < nextFreeTime[maxIndex][0]
                    || (nextFreeTime[r][0] == nextFreeTime[maxIndex][0]
                            && nextFreeTime[r][1] < nextFreeTime[maxIndex][1]))) {
                maxIndex = r;
            }

            if (i != maxIndex) {
                long[] aux = nextFreeTime[i];
                nextFreeTime[i] = nextFreeTime[maxIndex];
                nextFreeTime[maxIndex] = aux;
                siftDown(maxIndex);
            }
        }
        
        void insert(int p) {
            size += 1;
            nextFreeTime[size][0] = p;
            nextFreeTime[size][1] = size;
            siftUp(size);
        }

        void changePriorityOfMin(long newP) {
            long oldP = nextFreeTime[1][0];
            nextFreeTime[1][0] = newP;

            if (newP <= oldP) {
                siftUp(1);
            } else {
                siftDown(1);
            }
        }
        
        long getMinIndex() {
            return nextFreeTime[1][1];
        }

        long getMinTime() {
            return nextFreeTime[1][0];
        }
    }
}
