import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.*;

public class BubbleDetection {

	public static void main(String[] args) throws IOException{
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    	BubbleDetection bd = new BubbleDetection();
    	
    	String input = reader.readLine();
    	String[] stringMandT = input.split(" ");
    	
    	int m = Integer.parseInt(stringMandT[0]);
    	int t = Integer.parseInt(stringMandT[1]);
    	String read = reader.readLine();
    	bd.bubbleLengthThreshold = t;
    	
    	do {
    		
    		
    		
    		
            
    		
    		for (int j = 0; j < read.length()-m+1; j++) {
    			
    			String prefix = read.substring(j, j+m-1);
    			String suffix = read.substring(j+1, j+m);
    			bd.numOfIncomingEdges.put(suffix, bd.numOfIncomingEdges.getOrDefault(suffix, 0)+1);
    			
    			if (bd.graph.containsKey(prefix)) {
        			
    				ArrayList<String> neighbors= bd.graph.get(prefix);
    				
    				if (!suffix.equals(prefix) && !neighbors.stream().anyMatch(s->s.equals(suffix))) {
    					neighbors.add(suffix);
    				}
    				
        		}	
    			else {
    				ArrayList<String> neighbors = new ArrayList<>();
    				neighbors.add(suffix);
    				
    				
    				
    				if (!prefix.equals(suffix)) 
    					bd.graph.put(prefix, neighbors);
    				
    				
					
    				
    			}
    		}
    		
    		
    		
    		read = reader.readLine();
    	}
    	while(read != null && !read.isEmpty());
    	
    	
    	List<String> v = bd.findCandidatesForStartingNodes();
    	
    	int numOfBubbles = 0;
    	
    	for (String startingVertex : v) {
    		numOfBubbles += bd.findBubblesFromStartingVertex(startingVertex);
    	}
    	
    	System.out.println(numOfBubbles);
	}

	HashMap<String, Integer> numOfIncomingEdges = new HashMap<String, Integer>();
	HashMap<String, ArrayList<String>> graph = new HashMap<String, ArrayList<String>>();
	int bubbleLengthThreshold;
	
	List<String> findCandidatesForStartingNodes() {
		
		List<String> candidatesForStartingNodes = new ArrayList<String>();
		
		for (Entry<String, ArrayList<String>> keyValuePair : graph.entrySet()) {
			ArrayList<String> value = keyValuePair.getValue();
			if (value.size() >= 2) {
				candidatesForStartingNodes.add((String)keyValuePair.getKey());
			}
		}
		return candidatesForStartingNodes;
	}
	
	
	int findBubblesFromStartingVertex(String startingVertexKey) {
		
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
				
				if (numOfIncomingEdges.get(neighbor) >= 2) {
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
		int sumOfNumOfBubbles = 0;
		
		for (String vertex : candidatesForBubbleChecking) {
			sumOfNumOfBubbles = sumOfNumOfBubbles + numOfBubbles(vertex, pathToVertex.get(vertex), startingVertexKey);
		}
		
		
		
		
		
		
		return sumOfNumOfBubbles;
	}
	
	int numOfBubbles(String vertex, ArrayList<HashSet<String>> pathsToVertex, String startingNode) {
		int numOfBubbles = 0;
		for (int i = 0; i < pathsToVertex.size(); i++) {
			for (int j = i+1; j < pathsToVertex.size(); j++) {
				if (!hasElementInCommon(pathsToVertex.get(i), pathsToVertex.get(j), startingNode)) {
					numOfBubbles++;
				}
			}
		}
		
		return numOfBubbles;
	}
	
	
	static class PathAndDistance {
		String vertex;
		HashSet<String> path;
		int distance;
		int numOfVisitedAdj;
	}
	
	boolean hasElementInCommon(HashSet<String> set1, HashSet<String> set2, String startingNode) {
		for (String nodeSetOne : set1) {
			if (set2.contains(nodeSetOne) && nodeSetOne.equals(startingNode) == false) {
				return true;
			}
		}
		return false;
	}
 }
