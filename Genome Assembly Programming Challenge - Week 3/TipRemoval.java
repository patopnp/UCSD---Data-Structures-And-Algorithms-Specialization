import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TipRemoval {

	
	
	static int addEdge(HashMap<String, ArrayList<String>> graphForEdgeAddition, String prefix, String suffix) {

		if (graphForEdgeAddition.containsKey(prefix)) {
			ArrayList<String> neighbors= graphForEdgeAddition.get(prefix);
			
			if (!suffix.equals(prefix) && !neighbors.stream().anyMatch(s->s.equals(suffix))) {
				neighbors.add(suffix);
			}
			
		}	
		else {
			ArrayList<String> neighbors = new ArrayList<>();
			neighbors.add(suffix);
			if (!prefix.equals(suffix)) {
				graphForEdgeAddition.put(prefix, neighbors);
			}
		}
		
		graphForEdgeAddition.putIfAbsent(suffix, new ArrayList<String>());
		return 0;
		
		
	}
	
	public static void main(String... args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int totalNumOfEdgesRemoved = 0;
		TipRemoval tr = new TipRemoval();
		String read;
		
		
		for (int i = 0; i < 1618; i++) {
			read = reader.readLine();
			for (int j = 0; j < read.length()-15+1; j++) {
				String prefix = read.substring(j, j+15-1);
				String suffix = read.substring(j+1, j+15);
				
				addEdge(tr.graph, prefix, suffix);
				addEdge(tr.reverseGraph, suffix, prefix);
				
			}

		}
		
		
		
		int initialNumOfEdges = 0;
		for (ArrayList<String> kv : tr.graph.values()) {
			initialNumOfEdges += kv.size();
		}
		
		
		
		List<String> tipNoOutgoingEdges;
		List<String> tipNoIncomingEdges;
		
		do {
			tipNoIncomingEdges = new ArrayList<>();
			tipNoOutgoingEdges = new ArrayList<>();
			for (Map.Entry<String, ArrayList<String>> em : tr.graph.entrySet()) {
				if (em.getValue().isEmpty()) {
					tipNoOutgoingEdges.add(em.getKey());
				}
			}
			for (Map.Entry<String, ArrayList<String>> em : tr.reverseGraph.entrySet()) {
				if (em.getValue().isEmpty()) {
					tipNoIncomingEdges.add(em.getKey());
				}
			}
			for (String k : tipNoOutgoingEdges ) {
				
				for (String n : tr.reverseGraph.get(k)) {
					tr.graph.get(n).remove(k);
				}
				
				tr.graph.remove(k);
				tr.reverseGraph.remove(k);
			}
			
			for (String k : tipNoIncomingEdges ) {
				for (String n : tr.graph.get(k)) {
					tr.reverseGraph.get(n).remove(k);
				}
				
				tr.reverseGraph.remove(k);
				tr.graph.remove(k);
			}
			
			//tr.graph.remove(tipNoIncomingEdges)
		}while(!(tipNoOutgoingEdges.size() == 0 && tipNoIncomingEdges.size() == 0));
		
		/*
		do {
			tipNoIncomingEdges = new ArrayList<>();
			tipNoOutgoingEdges = new ArrayList<>();
			for (Map.Entry<String, ArrayList<String>> em : tr.graph.entrySet()) {
				if (em.getValue().isEmpty() && tr.reverseGraph.get(em.getKey()).size() == 1) {
					tipNoOutgoingEdges.add(em.getKey());
				}
			}
			for (Map.Entry<String, ArrayList<String>> em : tr.reverseGraph.entrySet()) {
				if (em.getValue().isEmpty() && tr.graph.get(em.getKey()).size() == 1) {
					tipNoIncomingEdges.add(em.getKey());
				}
			}
			for (String lastVertex : tipNoOutgoingEdges) {
				totalNumOfEdgesRemoved += tr.prunePathEnding(lastVertex);
			}
			for (String firstVertex : tipNoIncomingEdges) {
				totalNumOfEdgesRemoved += tr.prunePathBeginning(firstVertex);
			}
		}while(!(tipNoOutgoingEdges.size() == 0 && tipNoIncomingEdges.size() == 0));
		*/
		
		int finalNumOfEdges = 0;
		for (ArrayList<String> kv : tr.graph.values()) {
			finalNumOfEdges += kv.size();
		}
		System.out.println(initialNumOfEdges - finalNumOfEdges);
	}
	
	int prunePathBeginning(String firstVertex) {
		int numOfEdgesRemoved = 0;
		while(reverseGraph.get(firstVertex).size() == 0 && graph.get(firstVertex).size() == 1) {
			
			
			String nextVertex = graph.get(firstVertex).get(0);
			reverseGraph.get(nextVertex).remove(firstVertex);
			reverseGraph.remove(firstVertex);
			graph.remove(firstVertex);
			firstVertex = nextVertex;
			numOfEdgesRemoved++;
			
			
			
			
			
		}
		return numOfEdgesRemoved;
	}
	
	int prunePathEnding(String lastVertex) {
		int numOfEdgesRemoved = 0;
		while(graph.get(lastVertex).size() == 0 && reverseGraph.get(lastVertex).size() == 1) {
			
			
			String nextVertex = reverseGraph.get(lastVertex).get(0);
			graph.get(nextVertex).remove(lastVertex);
			graph.remove(lastVertex);
			reverseGraph.remove(lastVertex);
			lastVertex = nextVertex;
			
			numOfEdgesRemoved++;
			
			
			
			
		}
		return numOfEdgesRemoved;
	}
	
	HashMap<String, ArrayList<String>> graph = new HashMap<String, ArrayList<String>>();
	HashMap<String, ArrayList<String>> reverseGraph = new HashMap<String, ArrayList<String>>();
	
}
