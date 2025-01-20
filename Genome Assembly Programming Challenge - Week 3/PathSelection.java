import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PathSelection {

	static HashMap<String, Integer> merCounter = new HashMap<String, Integer>();
	static HashMap<String, ArrayList<String>> graph = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> reverseGraph = new HashMap<String, ArrayList<String>>();
	
	public static void main(String... args) throws IOException {
	
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String read;
		
		int merLength = 20;
		int numOfReads = 1618;
		
		for (int i = 0; i < numOfReads; i++) {
			read = reader.readLine();
			for (int j = 0; j < read.length()-merLength+1; j++) {
				String prefix = read.substring(j, j+merLength-1);
				String suffix = read.substring(j+1, j+merLength);
				
				merCounter.put(prefix, merCounter.getOrDefault(prefix, 0)+1);
				
    			
				addEdge(graph, prefix, suffix);
				addEdge(reverseGraph, suffix, prefix);
				
			}
			//merCounter.put(read.substring(read.length()-15), merCounter.getOrDefault(read.substring(read.length()-15), 0)+1);
			
		}
		boolean atLeastOneBubblePruned = false;
		
		do {
			removeEnds();
			List<String> startingVertices = findCandidatesForStartingNodes();
			atLeastOneBubblePruned = false;
			for (int i = 0; i < startingVertices.size(); i++) {
				String startingVertex = startingVertices.get(i);
			    if (removeBubblesFromStartingVertex(startingVertex, 20)) {
			    	atLeastOneBubblePruned = true;
			    	
			    	i--;
			    	
			    }
			}
			
		}while(atLeastOneBubblePruned);
		
		assembleGenome();
		
		
	}
	
	static List<String> findCandidatesForStartingNodes() {
		
		List<String> candidatesForStartingNodes = new ArrayList<String>();
		
		for (Entry<String, ArrayList<String>> keyValuePair : graph.entrySet()) {
			ArrayList<String> value = keyValuePair.getValue();
			if (value.size() >= 2) {
				candidatesForStartingNodes.add((String)keyValuePair.getKey());
			}
		}
		return candidatesForStartingNodes;
	}
	
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
	
	static void retainOnlyMaxCoveragePath(ArrayList<HashSet<String>> pathsToVertex, String startingNode) {
		
		int indexOfPathWithBestCoverage = 0;
		double bestCoverageScore = 0;
		
		for (int i = 0; i < pathsToVertex.size(); i++) {
			double currentPCS = calculateCoverage(pathsToVertex.get(i));
			if (currentPCS > bestCoverageScore) {
				bestCoverageScore = currentPCS;
				indexOfPathWithBestCoverage = i;
			}
		}
		
		for (int i = 0; i < pathsToVertex.size(); i++) {
			if (i == indexOfPathWithBestCoverage) {
				continue;
			}
			removePathFromOrigin(pathsToVertex.get(i), startingNode, pathsToVertex.get(indexOfPathWithBestCoverage));
		}
		
	}
	
	static void removePathFromOrigin(HashSet<String> path, String startingNode, HashSet<String> preservedPath) {
		for (String vertex : path) {
			if (preservedPath.contains(vertex)) {
				continue;
			}
			ArrayList<String> neighborsInReverseGraph = reverseGraph.get(vertex);
			ArrayList<String> neighborsInGraph = graph.get(vertex);
			
			for (String nirg : neighborsInReverseGraph) {
				graph.get(nirg).remove(vertex);
			}
			
			for (String neighborInGraph : neighborsInGraph) {
				reverseGraph.get(neighborInGraph).remove(vertex);
			}
			
			graph.remove(vertex);
			reverseGraph.remove(vertex);
		}
		
	}
	
	static void removeEnds() {

		
		
		
		List<String> tipNoOutgoingEdges;
		List<String> tipNoIncomingEdges;
		
		do {
			tipNoIncomingEdges = new ArrayList<>();
			tipNoOutgoingEdges = new ArrayList<>();
			for (Map.Entry<String, ArrayList<String>> em : graph.entrySet()) {
				if (em.getValue().isEmpty()) {
					tipNoOutgoingEdges.add(em.getKey());
				}
			}
			for (Map.Entry<String, ArrayList<String>> em : reverseGraph.entrySet()) {
				if (em.getValue().isEmpty()) {
					tipNoIncomingEdges.add(em.getKey());
				}
			}
			for (String k : tipNoOutgoingEdges ) {
				
				for (String n : reverseGraph.get(k)) {
					graph.get(n).remove(k);
				}
				
				graph.remove(k);
				reverseGraph.remove(k);
			}
			
			for (String k : tipNoIncomingEdges ) {
				for (String n : graph.get(k)) {
					reverseGraph.get(n).remove(k);
				}
				
				reverseGraph.remove(k);
				graph.remove(k);
			}
			
			//tr.graph.remove(tipNoIncomingEdges)
		}while(!(tipNoOutgoingEdges.size() == 0 && tipNoIncomingEdges.size() == 0));
		
	}
	
	static void assembleGenome() {
		// test this
		//graph.keySet().iterator().next();
		graph.keySet().iterator().next();
		
		String nextVertex = graph.keySet().iterator().next();
		String firstVertex = nextVertex;
		String genome = "";
		do {
			genome += nextVertex.substring(nextVertex.length()-1);
			nextVertex = graph.get(nextVertex).get(0);
			
		}while(nextVertex.equals(firstVertex) == false);
		System.out.println(genome);
		
		
	}
	
	static double calculateCoverage(HashSet<String> path) {
		
		double total = 0;
		
		for (String v : path) {
			total += merCounter.get(v);
		}
		
		total /= path.size();
		return total;
	}
	
	static class PathAndDistance {
		String vertex;
		HashSet<String> path;
		int distance;
		int numOfVisitedAdj;
	}
	
	static boolean removeBubblesFromStartingVertex(String startingVertexKey, int bubbleLengthThreshold) {
		
		
		
		if (!graph.containsKey(startingVertexKey) || graph.get(startingVertexKey).size() <= 1) {
			return false;
		}
		
		HashMap<String, ArrayList<HashSet<String>>> pathToVertex = new HashMap<String, ArrayList<HashSet<String>>>(); 
		HashSet<String> candidatesForBubbleChecking = new HashSet<String>();
		
		Deque<PathAndDistance> paths = new LinkedList<PathAndDistance>();
		PathAndDistance start = new PathAndDistance();
		start.path = new HashSet<String>();
		start.vertex = startingVertexKey;
		
		paths.add(start);
		boolean pathIsEmpty = paths.isEmpty();
		while(pathIsEmpty == false) {
		
			PathAndDistance currPath = paths.getFirst();
			ArrayList<String> neighborsOfCurrentVertex;
			String neighbor = "";
			do {
				
				while (!graph.containsKey(currPath.vertex) || currPath.numOfVisitedAdj >= graph.get(currPath.vertex).size()) {
					paths.pop();
					if (paths.size() == 0) {
						pathIsEmpty = true;
						break;
					}
					currPath = paths.getFirst();
				}
				
				
				if (pathIsEmpty) {
					break;
				}
				neighborsOfCurrentVertex = graph.get(currPath.vertex);
				neighbor = neighborsOfCurrentVertex.get(currPath.numOfVisitedAdj);
				currPath.numOfVisitedAdj++;
			}
			while (currPath.path.contains(neighbor));
			
			if (pathIsEmpty) {
				break;
			}
		
			if (pathToVertex.containsKey(neighbor)) {
				
				
				ArrayList<HashSet<String>> pathsToVertex = pathToVertex.get(neighbor);
				
				if (reverseGraph.get(neighbor).size() >= 2) {
					candidatesForBubbleChecking.add(neighbor);
				}
				
				
				
				HashSet<String> newPath = (HashSet<String>) currPath.path.clone();
				newPath.add(currPath.vertex);
				pathsToVertex.add(newPath);
				 
				if (currPath.distance <= bubbleLengthThreshold-1-1) {
					PathAndDistance newTrack = new PathAndDistance();
					newTrack.distance = currPath.distance + 1;
					newTrack.vertex = neighbor;
					newTrack.path = newPath;
					paths.add(newTrack);
				}
				
			}
			else {
				ArrayList<HashSet<String>> pathsToVertex = new ArrayList<HashSet<String>>();
				
				HashSet<String> newPath = (HashSet<String>) currPath.path.clone();
				newPath.add(currPath.vertex);
				pathsToVertex.add(newPath);
				pathToVertex.put(neighbor, pathsToVertex);
				
				if (currPath.distance <= bubbleLengthThreshold-1-1) {
					PathAndDistance newTrack = new PathAndDistance();
					newTrack.distance = currPath.distance + 1;
					newTrack.vertex = neighbor;
					newTrack.path = newPath;
					paths.add(newTrack);
				}
				
			}
			
		
		
		
		}
		
		int maxLength = 0;
		
		
		
		String nodeOfMaxLength = "";
		
		
		for (String node : candidatesForBubbleChecking) {
			retainOnlyMaxCoveragePath(pathToVertex.get(node), startingVertexKey);
			/*
			int maxPathSizeForThisPath = pathToVertex.get(node).stream().mapToInt(HashSet::size).max().getAsInt();
			if (maxPathSizeForThisPath > maxLength && pathToVertex.get(node).size() > 1) {
				maxLength = maxPathSizeForThisPath;
				nodeOfMaxLength = node;
			}
			
			*/
		}
		
		if (candidatesForBubbleChecking.isEmpty() == false) {
			return true;
		}
		
		/*
		if (nodeOfMaxLength.isEmpty() == false) {
			retainOnlyMaxCoveragePath(pathToVertex.get(nodeOfMaxLength), startingVertexKey);
			return true;
		}
		*/
		
		
		
	
		return false;
	}
	
}
