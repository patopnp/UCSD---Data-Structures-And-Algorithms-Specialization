import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class GenomeSequencing3 {
	/*
	static boolean visited[];
	static int weights[][] = new int[1618][1618];
	
	static PriorityQueue<Edge> edges[];
	*/
	
	static class Edge {
		int u,v,weight;
		
		Edge(int u, int v, int weight) {
			this.u = u;
			this.v = v;
			this.weight = weight;
		}
	}
	
	static int overlap(String a, String b) {

		for (int start = 0; start < a.length(); start++) {
			
			int numOfErrors = 0;
			boolean add = true;
			for (int i = 0; i < a.length()-start; i++) {
				if (a.charAt(start+i) != b.charAt(i)) {
					numOfErrors++;
				}
				
				if (numOfErrors*20 > a.length()-start/*2*/ ) {
					add = false;
					break;
				}
				
			}
			
			if (add) {
				return (a.length()-start);
			}
			
		}
		return 0;
	}
	
	static void generateRandomReads(String[] reads) {
		for (int p = 0; p < reads.length; p++) {
			reads[p] = generateRandomDNAString(100);
		}
	}
	
	public static String generateRandomDNAString(int length) {
        char[] dnaBases = {'A', 'C', 'G', 'T'};
        Random random = new Random();
        StringBuilder dnaString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            char base = dnaBases[random.nextInt(dnaBases.length)];
            dnaString.append(base);
        }

        return dnaString.toString();
    }
	
	public static void main(String... args) throws IOException {
		

		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		List<String> readsList = new ArrayList<String>();
		
		for (int i = 0; i < 1618; i++) {
			readsList.add(reader.readLine());
		}
		
		String[] reads = readsList.toArray(new String[0]);
		int weCanDoIt = 0;
		String genome = getGenomeSequence(reads, weCanDoIt);
		System.out.println(genome);
		
		//ACGAACTCGTT
		

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
	
	public static String getGenomeSequence(String[] reads, int firstIndex) {
		int[][] chars = new int[161800][4]; 
		PriorityQueue<Edge> edges[];
		edges = new PriorityQueue[reads.length];
		
		boolean visited[] = new boolean[reads.length];
		String genome = reads[0];
		
		for (int index = 0; index < 100; index++) {
			if (chars[index] == null) 					
				chars[index] = new int[4];
			chars[index][charToInt(reads[0].charAt(index))] += 1;
		}
		
		
		int visits = 1;
		int i = firstIndex;
		//30
		while (visits < reads.length/*-30*/) {
			
			visited[i] = true;
			edges[i] = new PriorityQueue<Edge>((a,b)->{ return Integer.compare(b.weight, a.weight);});
			
			for (int j = 0; j < reads.length; j++) {
				if (i==j) continue;
				if (visited[j]) continue;
				
				int weight = overlap(reads[i], reads[j]);
				if (weight >= 12) {
					edges[i].add(new Edge(i, j, weight));
				}
			}
			/*
			 * 			debe ser la cola mordiendo la primera parte no hay manera que se concatenen los 30 restantes sino, 
			 */
			//ERA LA COLA, AHORA DEBO MEJORAR LA CORRECCION DE ERRORES (PUEDE SER QUE LA COLA METIDA EN LA PRIMERA PARTE AYUDE A CORREGIR LOS 16 ERRORES RESTANTES, EN ESTE MOMENTO NO SE ESTA APROVEHCANDO
			
			Edge nextEdge = edges[i].poll();
			
			int startingPoint = genome.length()-nextEdge.weight;
			

			
			
			for (int index = 0; index < reads[nextEdge.v].length(); index++) {
				if (chars[startingPoint+index] == null) 					
					chars[startingPoint+index] = new int[4];
				chars[startingPoint+index][charToInt(reads[nextEdge.v].charAt(index))] += 1;
			}
			
			genome += reads[nextEdge.v].substring(nextEdge.weight);
			i = nextEdge.v;
			
			visits++;
		}
		
		int tailOverlappingLength = overlap(genome.substring(genome.length()-100),reads[0]);
		int genomeRealSize = genome.length()-tailOverlappingLength;
		
		char intToChar[] = new char[4];
		intToChar[0] = 'A';
		intToChar[1] = 'C';
		intToChar[2] = 'T';
		intToChar[3] = 'G';

		String realGenome = "";
		
		for (int index = 0; index < tailOverlappingLength; index++) {
			int bestCharacterIndex = 0;
			
			if (chars[index][1] + chars[index+genomeRealSize][1] > chars[index][bestCharacterIndex] + chars[index + genomeRealSize][bestCharacterIndex]) 
				bestCharacterIndex = 1;
			if (chars[index][2] + chars[index+genomeRealSize][2] > chars[index][bestCharacterIndex] + chars[index + genomeRealSize][bestCharacterIndex]) 
				bestCharacterIndex = 2;
			if (chars[index][3] + chars[index+genomeRealSize][3] > chars[index][bestCharacterIndex] + chars[index + genomeRealSize][bestCharacterIndex]) 
				bestCharacterIndex = 3;
			
			realGenome += intToChar[bestCharacterIndex];
			
		}
		
		
		for (int index = tailOverlappingLength; index < genomeRealSize; index++) {
			int bestCharacterIndex = 0;
			
			if (chars[index][1] > chars[index][bestCharacterIndex])
				bestCharacterIndex = 1;
			if (chars[index][2] > chars[index][bestCharacterIndex])
				bestCharacterIndex = 2;
			if (chars[index][3] > chars[index][bestCharacterIndex])
				bestCharacterIndex = 3;
			
			realGenome += intToChar[bestCharacterIndex];
		}

		return realGenome;
		
		////return genome.substring(0, genome.length()-overlap(genome.substring(genome.length()-100),reads[0]));
		
		
	}

	
    public static String changeCharacter(String str, int position, char newChar) {

    	 
    	
        StringBuilder sb = new StringBuilder(str);
        
    	sb.setCharAt(position, newChar);
        
        return sb.toString();
    }
	
    public static String changeCharacters(String str, List<Integer> positions, char newChar) {

    	 
    	
        StringBuilder sb = new StringBuilder(str);
        
        for (int pos : positions) {
        	sb.setCharAt(pos, newChar);
        
        }
        return sb.toString();
    }
    
	static int charToInt(char c) {
		if (c == 'A') return 0;
		if (c == 'C') return 1;
		if (c == 'T') return 2;
		if (c == 'G') return 3;
		return -1;
	}
	
	static List<Integer> findErrorPoints(String[] reads, Edge e, int startingPoint) {
		String a = reads[e.u];
		String b = reads[e.v];
		int start = a.length()-e.weight;
		List<Integer> errorPoints = new ArrayList<Integer>();
		for (int i = 0; i < a.length()-start; i++) {
			if (a.charAt(start+i) != b.charAt(i)) {
				errorPoints.add(start+i+startingPoint);
				if (errorPoints.size() == 2) break;
			}
		}
		return errorPoints;
	}
	
}
