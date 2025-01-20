import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class OptimalMer {

	
	public static void main(String... args) throws IOException{
		
		
		//String read = "yeah";//readLine();
		List<String> allReads = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		for (int i = 0; i < 400; i++) {
			allReads.add(reader.readLine());
		}
		
		
		
		for (int lengthOfMer = allReads.get(0).length()-1/*99*/; lengthOfMer > 0; lengthOfMer--) {
			HashMap<String, String> prefixToSuffix = new HashMap<String, String>();
			boolean skip = false;
			for (String read : allReads) {
				skip = false;
				
				for (int i = 0; i < 100-lengthOfMer; i++) {
					
					//the prefix can be the suffix of the suffix from one of the unconnected
					
					String prefix = read.substring(i,i+lengthOfMer);
					String suffix = read.substring(i+1, i+lengthOfMer+1);
					
					if (prefixToSuffix.containsKey(prefix)) {
						if (prefixToSuffix.get(prefix).equals(suffix) == false) {
							skip = true;
							break;
						}
					}
					else {
						prefixToSuffix.put(prefix, suffix);
					}
					
				}
				
				
				
				
				if (skip) {
					break;
				}
				

			}
			if (!skip)
			{
				String prefix = allReads.get(0).substring(0,lengthOfMer);
				int t = prefixToSuffix.size();
				
				
				String nextMer = prefix;
				
				String beginning = nextMer;
				
				for (int i = 0; i < t; i++) {
					
					
					nextMer = prefixToSuffix.remove(nextMer);
					if (nextMer == null) {
						skip = true;
						break;
					}
					
				}
				
				if (skip == false && nextMer.equals(beginning)) {
					System.out.println(lengthOfMer+1);
					return;
				}
			}
			/*
			for (int i = 0; i < ; i++) {
				
				
			}
			*/
		}
		
		
		
	}
}
