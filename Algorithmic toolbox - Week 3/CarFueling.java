import java.util.*;
import java.io.*;

public class CarFueling {

    static int computeMinRefills(int dist, int tank, int[] stops) {
	int numRefills = 0;
    int nextStop = -1;
    int travelledDistance = 0;

    while (travelledDistance + tank < dist) {
	    nextStop = getIndexOfHighestInRange(stops, nextStop, travelledDistance + tank);
	    if(nextStop == -1){
	       return -1;
	    }
        numRefills++;
	    travelledDistance = stops[nextStop];
	}

        return numRefills;
    }

    private static int getIndexOfHighestInRange(int[] stops, int fromIndex, int rightEnd){
        int farthestPossibleStop = -1;
        for(int i = fromIndex+1; i < stops.length && stops[i] <= rightEnd; i++){
            farthestPossibleStop = i;
        }
        return farthestPossibleStop;
	
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int dist = scanner.nextInt();
        int tank = scanner.nextInt();
        int n = scanner.nextInt();
        int stops[] = new int[n];
        for (int i = 0; i < n; i++) {
            stops[i] = scanner.nextInt();
        }
        System.out.println(computeMinRefills(dist, tank, stops));
    }
}
