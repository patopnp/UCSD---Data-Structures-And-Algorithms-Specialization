import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;
import java.util.StringTokenizer;

public class UniversalCircularString {

	public static void main(String[] args) throws IOException {
		
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int mers = Integer.parseInt(reader.readLine());
        int maxMerRepresentation = (int)Math.pow(2, mers);
        int numberOfVertex = maxMerRepresentation/2;
        Stack<Integer>[] vertexEdges = new Stack[numberOfVertex]; 
        
        for (int i = 0; i < numberOfVertex; i++) {
        	vertexEdges[i] = new Stack<>();
        }
        
        for (int c = 0; c < maxMerRepresentation; c++) {
        	int[] result = getPrefixAndSuffix(c, mers);
        	vertexEdges[result[0]].add(result[1]);
        	
        }
        
        Stack<Integer> vStack = new Stack<>();
        
		Deque<Integer> path = new LinkedList<>();
		vStack.add(0);
		while (!vStack.isEmpty()) {
			
			int currVertex = vStack.peek();
			
			if (vertexEdges[currVertex].isEmpty() == false) {
				vStack.add(vertexEdges[currVertex].pop());
				
			}
			else {
				path.addFirst(vStack.pop());
			}
		}
		
		path.removeLast();
		System.out.print(Integer.toBinaryString(path.getFirst()).substring(0, Integer.toBinaryString(path.getFirst()).length()-1));
		for (int next : path) {
			System.out.print( Integer.toBinaryString(next).substring(Integer.toBinaryString(next).length()-1) + "");
		}
	}

	public static int[] getPrefixAndSuffix(int num, int d) {
		int totalBits = Integer.SIZE;
        int leadingZeros = Integer.numberOfLeadingZeros(num);
        
        //int numOfdigitsInBinaryRepresentationOfNum = totalBits - leadingZeros;
        
        int[] result = new int[] {getFirstNBits(num, totalBits-1 ), getLastNBits(num, d-1)};
        return result;
	}
	
	public static int getLastNBits(int num, int n) {
        // Create a mask with the last n bits set to 1
        int mask = (1 << n) - 1;
        
        // Apply the mask to get the last n bits
        return num & mask;
    }
	
	public static int getFirstNBits(int num, int n) {
        // Total bits in an integer
        int totalBits = Integer.SIZE; // 32 for int
        
        // Right shift to isolate the first n bits
        int shifted = num >>> (totalBits - n);
        
        // Optional: Mask to ensure only n bits are kept
        int mask = (1 << n) - 1;
        return shifted & mask;
    }
}
