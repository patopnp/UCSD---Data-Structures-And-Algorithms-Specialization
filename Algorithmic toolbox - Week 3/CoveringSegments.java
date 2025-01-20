import java.util.*;

public class CoveringSegments {

    private static int[] optimalPoints(Segment[] segments) {

        int smallestSegmentEnd = -1;
        List<Integer> pointsInList = new ArrayList<Integer>();

        do{
            smallestSegmentEnd = smallestSegmentEnd(segments);
            pointsInList.add(smallestSegmentEnd);
        } while (smallestSegmentEnd != -1);

        pointsInList.remove(pointsInList.size() - 1);

        int[] points = new int[pointsInList.size()];
        int i = 0;

        for (int point : pointsInList){
           points[i] = point;
           i++;
        }


        return points;
    }

    private static int smallestSegmentEnd(Segment[] segments) {

        int smallestSegmentEnd = -1;
        int indexOfSmallestSegmentEnd = -1;

        for (int s = 0; s < segments.length; s++) {
            if (segments[s] != null) {
                if (smallestSegmentEnd == -1) {
                    smallestSegmentEnd = segments[s].end;
                    indexOfSmallestSegmentEnd = s;
                } else {
                    if (segments[s].end < smallestSegmentEnd) {
                        smallestSegmentEnd = segments[s].end;
                        indexOfSmallestSegmentEnd = s;
                    }
                }
            }
        }
        if (indexOfSmallestSegmentEnd != -1) {
            segments[indexOfSmallestSegmentEnd] = null;
            removeAllSegmentsTouchedByPoint(segments, smallestSegmentEnd);
        }
	    return smallestSegmentEnd;
    }

    private static void removeAllSegmentsTouchedByPoint(Segment[] segments, int point) {
        for (int s = 0; s < segments.length; s++) {
            if (segments[s] != null) {
                if (segments[s].end >= point && segments[s].start <= point) {
                    segments[s] = null;
                }
            }
        }
    }

    private static class Segment {
        int start, end;

        Segment(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        for (int i = 0; i < n; i++) {
            int start, end;
            start = scanner.nextInt();
            end = scanner.nextInt();
            segments[i] = new Segment(start, end);
        }
        int[] points = optimalPoints(segments);
        System.out.println(points.length);
        for (int point : points) {
            System.out.print(point + " ");
        }
    }
}
