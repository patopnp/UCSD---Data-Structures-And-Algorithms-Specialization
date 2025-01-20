import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;


public class HeavyHitters {

    private final InputReader reader;
	
	long signHashFunctionParameters[][];
	long bucketHashFunctionParameters[][]; 
	int b;	
	long counters[][];
	
	public HeavyHitters(InputReader reader) {
        this.reader = reader;
    }
	
	public static void main(String... args) {
		
		InputReader reader = new InputReader(System.in);
		new HeavyHitters(reader).run();
		
	}
	
	public void run() {
		long numOfDataItems = reader.nextInt();
		long threshold = reader.nextInt();
		
		defineHashParameters(numOfDataItems);
		
		for (long i = 0; i < numOfDataItems; i++) {
			long dataItem = reader.nextInt();
			long frecuency = reader.nextInt();
			
			for (int row = 0; row < counters.length; row++) {
				updateCellCounter(dataItem, frecuency, row, true);
			}
		}
		
		for (long i = 0; i < numOfDataItems; i++) {
			long dataItem = reader.nextInt();
			long frecuency = reader.nextInt();
			
			for (int row = 0; row < counters.length; row++) {
				updateCellCounter(dataItem, frecuency, row, false);
			}
		}
		
		long numOfQueries = reader.nextInt();
		for (int i = 0; i < numOfQueries; i++) {
			long dataItem = reader.nextInt();
			System.out.print(getEstimatedFrecuency(dataItem)>= threshold?"1 " : "0 ");
		}
	}
	
	private void defineHashParameters(long n) {
		int numOfRows;
		
	    numOfRows = 10*((int)Math.log(n)+1);
		
		signHashFunctionParameters = new long[numOfRows][2];
		bucketHashFunctionParameters = new long[numOfRows][2];
		
		for (int i = 0; i < numOfRows; i++) {
			Random rnd = new Random();
			signHashFunctionParameters[i][0] = rnd.nextInt(100)+1;
			signHashFunctionParameters[i][1] = rnd.nextInt(100)+1;
			bucketHashFunctionParameters[i][0] = rnd.nextInt(1000);
			bucketHashFunctionParameters[i][1] = rnd.nextInt(1000);
		}
		
		// not much of a memory save, standard deviation of data set is small and probably not queried for k largest either on test cases
		b = ((int)Math.log(n)+1)*15000;
		counters = new long[numOfRows][b];
	}
	
	
	
	long getEstimatedFrecuency(long dataItem) {
		
		int totalNumOfRows = counters.length;
		
		long estimatedFrecuencyPerRow[] = new long[totalNumOfRows];
		
		for (int row = 0; row < totalNumOfRows; row++) {
			estimatedFrecuencyPerRow[row] = counters[row][findBucket(dataItem, row)]*sign(dataItem, row);
			
		}
		
		return findMedian(estimatedFrecuencyPerRow);
	}
	
	
	public static long findMedian(long a[])
    {
		int n = a.length;
        Arrays.sort(a);
 
        if (n % 2 != 0)
            return a[n / 2];
        else return a[(n / 2)-1];
    }
	
	public void updateCellCounter(long dataItem, long frecuency, int row, boolean sum) {
		counters[row][findBucket(dataItem, row)] += (sum?1:-1) * frecuency * sign(dataItem, row);
	}
	
	
	int findBucket(long dataItem, int row) {
		return (int) (((bucketHashFunctionParameters[row][0]*dataItem+bucketHashFunctionParameters[row][1])% 1000000007) % b);
	}
	
	int sign(long dataItem, int row) {
			return (int)((((signHashFunctionParameters[row][0]*dataItem+signHashFunctionParameters[row][1]) % 1000000007) % 2) * 2 -1);
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

        public long nextInt() {
            return Integer.parseInt(next());
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
