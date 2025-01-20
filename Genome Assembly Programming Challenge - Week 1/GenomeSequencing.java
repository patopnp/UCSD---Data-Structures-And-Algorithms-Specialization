import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class GenomeSequencing {
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
		int start = 0;
		
		while(true){
			start = a.indexOf(b.substring(0, 12), start);
			if (start == -1) {
				return 0;
			}
			if (b.substring(0, a.length()-start).equals(a.substring(start))) {
				return a.length()-start;
			}
			start++;
		}
		
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
		for (weCanDoIt = 1; weCanDoIt < 4; weCanDoIt++) {
			String candidate = getGenomeSequence(reads, weCanDoIt);
			
			if (candidate.length() < genome.length()) {
				genome = candidate;
			}
		}
		
		
		
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
		PriorityQueue<Edge> edges[];
		edges = new PriorityQueue[reads.length];
		
		boolean visited[] = new boolean[reads.length];
		//generateRandomReads(reads);
		
		for (int i = 0; i < reads.length; i++) {
			edges[i] = new PriorityQueue<Edge>((a,b)->{ return Integer.compare(b.weight, a.weight);});
			
			for (int j = 0; j < reads.length; j++) {
				if (i==j) continue;
				int weight = overlap(reads[i], reads[j]);
				//if (weight >= 12) {
					edges[i].add(new Edge(i, j, weight));
				//}
			}
			
		}
		
		
		ArrayList<Edge> genomeSequence;
		
		genomeSequence = new ArrayList<>();
		
		
		
		String genome = "";
		
		int visits = 1;
		int i = firstIndex;
		visited[i] = true;
		
		while (visits < reads.length) {
			
			Edge nextEdge = edges[i].poll();
			while(visited[nextEdge.v]) {
				nextEdge = edges[i].poll();
			}
			genomeSequence.add(nextEdge);
			i = nextEdge.v;
			visited[i] = true;
			visits++;
		}
		
		//genomeSequence.stream().forEach(e->System.out.print(reads[e.v].substring(e.weight)));
		
		
		genome += reads[genomeSequence.get(0).u];

		for (Edge e : genomeSequence) {
		
			//System.out.println(e.u + "->" + e.v + " w=" + e.weight);
			genome += reads[e.v].substring(e.weight);
		}
		
		return genome.substring(0, genome.length()-computePrefixFunction(genome));
		
		
	}
	
}
