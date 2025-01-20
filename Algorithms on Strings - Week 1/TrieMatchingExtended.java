import java.io.*;
import java.util.*;

class Node
{
	public boolean isEndOfWord;
	public static final int Letters =  4;
	public Node next [];

	Node ()
	{
		isEndOfWord = false;
		next = new Node [Letters];
	}
}

public class TrieMatchingExtended implements Runnable {
	int letterToIndex (char letter)
	{
		switch (letter)
		{
			case 'A': return 0;
			case 'C': return 1;
			case 'G': return 2;
			case 'T': return 3;
			default: assert (false); return -1;
		}
	}

	List <Integer> solve (String text, List <String> patterns) {
		List <Integer> result = new ArrayList <Integer> ();
		
		Node root = buildTrie(patterns);
		result = findPositions(text, root);
		
		return result;
	}

	List<Integer> findPositions(String text, Node root){
		List <Integer> result = new ArrayList <Integer>();

		for (int i = 0; i < text.length(); i++) {
			String textForSearch = text.substring(i);
			if (matchString(textForSearch, root)) {
				result.add(i);
			}
		}

		return result;
	}

	boolean matchString(String text, Node root) {
		Node currentNode = root;
		for (int i = 0; i < text.length(); i++) {
			char symbol = text.charAt(i);

			if (currentNode.next[letterToIndex(symbol)] != null) {
				currentNode = currentNode.next[letterToIndex(symbol)];
			} 
			else {
				return false;
			}
			if(currentNode.isEndOfWord) {
				return true;
			}
		}
		return false;
	}

	Node buildTrie(List<String> patterns) {

		//add root
		Node root = new Node();
		Node currentNode = root;

		for (String pattern : patterns) {
			currentNode = root;
			for (int i = 0; i < pattern.length(); i++) {
				char symbol = pattern.charAt(i);
				if (currentNode.next[letterToIndex(symbol)] != null) {
					currentNode = currentNode.next[letterToIndex(symbol)];
				} else {
					currentNode.next[letterToIndex(symbol)] = new Node();
					currentNode = currentNode.next[letterToIndex(symbol)];
				}
			}
			currentNode.isEndOfWord = true;
		}

		return root;
	}
	
	public void run () {
		try {	
		
			BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
			String text = in.readLine ();
		 	int n = Integer.parseInt (in.readLine ());
		 	List <String> patterns = new ArrayList <String> ();
			for (int i = 0; i < n; i++) {
				patterns.add (in.readLine ());
			}
			
			List <Integer> ans = solve(text, patterns);

			for (int j = 0; j < ans.size (); j++) {
				System.out.print ("" + ans.get (j));
				System.out.print (j + 1 < ans.size () ? " " : "\n");
			}
		}
		catch (Throwable e) {
			e.printStackTrace ();
			System.exit (1);
		}
	}

	public static void main (String [] args) {
		new Thread (new TrieMatchingExtended ()).start ();
	}
	
}
