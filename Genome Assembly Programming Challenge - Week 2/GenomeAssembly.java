import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

public class GenomeAssembly {

	public static void main(String[] args) throws IOException{
		
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Stack<Integer>[] vertexEdges = new Stack[262144]; 
        
        for (int i = 0; i < vertexEdges.length; i++) {
        	vertexEdges[i] = new Stack<>();
        }
        //5396
        int firstIndex = -1;
        for (int c = 0; c < 5396 ; c++) {
        	String tenMer = reader.readLine();
        	
        	//String tenMer = input.substring(c, 10+c);
        	
        	int decTMRepr = convertStringToDecimal(tenMer);
        	String sdsa = getGenomeFromNumericRepresentation(convertDecimalToBase4(decTMRepr)+"", 10);
        	int[] result = getPrefixAndSuffix(decTMRepr, 20);
        	String prefixS = getGenomeFromNumericRepresentation(convertDecimalToBase4(result[0])+"", 9);
        	String suffixS = getGenomeFromNumericRepresentation(convertDecimalToBase4(result[1])+"", 9);
        	
        	vertexEdges[result[0]].add(result[1]);
        	if (firstIndex == -1) {
        		firstIndex = result[0];
        	}
        }
        
        Stack<Integer> vStack = new Stack<>();
        
		Deque<Integer> path = new LinkedList<>();
		vStack.add(firstIndex);
		while (!vStack.isEmpty()) {
			
			int currVertex = vStack.peek();
			
			if (vertexEdges[currVertex].isEmpty() == false) {
				vStack.add(vertexEdges[currVertex].pop());
				
			}
			else {
				path.addFirst(vStack.pop());
			}
		}
		
		//path.removeLast();
		
		String result = getGenomeFromNumericRepresentation(convertDecimalToBase4(path.getFirst()/4),8);
		
		//System.out.print(result);		
		
		
		String genome = result;
		for (int next : path) {
			result = getGenomeFromNumericRepresentation(convertDecimalToBase4(next),9);
			genome += result.substring(8);
		}

		System.out.println(genome.substring(0, genome.length()-computePrefixFunction(genome)));
	}

	
	
	public static int[] getPrefixAndSuffix(int num, int d) {
		int totalBits = Integer.SIZE;
        int leadingZeros = Integer.numberOfLeadingZeros(num);
        
        //int numOfdigitsInBinaryRepresentationOfNum = totalBits - leadingZeros;
        
        int[] result = new int[] {getFirstNBits(num, totalBits-2 ), getLastNBits(num, d-2)};
        return result;
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
	
	public static int getLastNBits(int num, int n) {
        // Create a mask with the last n bits set to 1
        int mask = (1 << n) - 1;
        
        // Apply the mask to get the last n bits
        return num & mask;
    }
	

	
    public static int convertStringToDecimal(String tenMer) {
        int decimal = 0;
        int length = tenMer.length();

        for (int i = 0; i < length; i++) {
            // Get the current digit as an integer
            int digit = charToNum(tenMer.charAt(i));
            
            // Validate that the digit is valid for base 4
            if (digit < 0 || digit >= 4) {
                throw new IllegalArgumentException("Invalid digit for base 4: " + digit);
            }

            // Calculate the decimal value of the current digit
            decimal += digit * Math.pow(4, length - 1 - i);
        }

        return decimal;
    }
	
	static int charToNum(char c){
		switch(c) {
			case 'A':
				return 0;
			case 'G':
				return 1;				
			case 'C':
				return 2;
			case 'T':
				return 3;	
			default:
				return -1;
		}
	}
	
	public static String convertDecimalToBase4(int number) {
        if (number == 0) return "0"; // Special case for 0
        StringBuilder base4 = new StringBuilder();

        while (number > 0) {
            int remainder = number % 4;
            base4.append(remainder);
            number /= 4;
        }

        return base4.reverse().toString(); // Reverse the string to get the correct order
    }
	
	public static int computePrefixFunction(String pattern) {
        int n = pattern.length();
        int[] lps = new int[n]; // Array to store the prefix function values
        int j = 0; // Length of the previous longest prefix suffix

        // Iterate over the pattern starting from the second character
        for (int i = 1; i < n; i++) {
            // If the current character does not match, move j back
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = lps[j - 1];
            }

            // If there's a match, increment j
            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;
            }

            // Update the LPS array
            lps[i] = j;
        }

        return lps[n-1];
    }
	
	public static String getGenomeFromNumericRepresentation(String numRepr, int trueLength) {
		char[] numToChar = new char[4];
		numToChar[0] = 'A';
		numToChar[1] = 'G';
		numToChar[2] = 'C';
		numToChar[3] = 'T';
		
		String result = "";
		
		
		int numOfLeadingAs = trueLength - numRepr.length();
		
		for (int i = 0; i < numOfLeadingAs; i++) {
			result += "A";
		}
		
		for (int i = 0; i < numRepr.length(); i++) {
			result += numToChar[Integer.parseInt(numRepr.charAt(i)+"")]+"";
		}
		return result;
	}
 }
