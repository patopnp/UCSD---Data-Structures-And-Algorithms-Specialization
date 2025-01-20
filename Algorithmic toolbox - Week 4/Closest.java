import java.io.*;
import java.util.*;

import static java.lang.Math.*;

public class Closest {

    static class Point implements Comparable<Point> {
        long x, y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point o) {
            return o.y == y ? Long.signum(x - o.x) : Long.signum(y - o.y);
        }

        public double distanceTo(Point o) {
            double distanceSquared = Math.pow((o.x-this.x), 2) + Math.pow((o.y-this.y), 2);
            return Math.sqrt(distanceSquared);
        }

        public boolean isContainedInInterval(double left, double right) {
            return (this.x >= left && this.x <= right);
        }

        public static double minimalDistanceBetweenPoints(Point... ps) {
            double minimalDistance = ps[0].distanceTo(ps[1]);
            for (int i = 0; i < ps.length; i++) {
                for (int j = i + 1; j < ps.length; j++) {
                    double distaneBetweenPoints = ps[i].distanceTo(ps[j]);
                    if (distaneBetweenPoints < minimalDistance) {
                        minimalDistance = distaneBetweenPoints;
                    }
                }
            }
            return minimalDistance;
        }



        public static double minimalDistanceBetweenPoints(int[] psx, int[] psy) {

            Point[] ps = new Point[psx.length];

            for(int i = 0; i < psx.length; i++) {
                ps[i] = new Point(psx[i], psy[i]);
            }

            double minimalDistance = ps[0].distanceTo(ps[1]);
            for (int i = 0; i < ps.length; i++) {
                for (int j = i + 1; j < ps.length; j++) {
                    double distaneBetweenPoints = ps[i].distanceTo(ps[j]);
                    if (distaneBetweenPoints < minimalDistance) {
                        minimalDistance = distaneBetweenPoints;
                    }
                }
            }
            return minimalDistance;
        }
    }

    static double minimalDistance(int[] x, int y[]) {
        double ans = Double.POSITIVE_INFINITY;

        Point points[] = getPointsFromCoordinatesSorted(x, y);

        ans = getMinimalDistanceOnInterval(points, 0, points.length);

        return ans;
    }

    static double getMinimalDistanceOnInterval(Point points[], int left, int right) {

        if (right - left == 2) {
            return Point.minimalDistanceBetweenPoints(points[left], points[left + 1]);
        }

        if (right - left == 3) {
            return Point.minimalDistanceBetweenPoints(points[left], points[left+1], points[left+2]);
        }

        int s = Math.floorDiv(right + left, 2);
        
        double d1 = getMinimalDistanceOnInterval(points, left, s);
        double d2 = getMinimalDistanceOnInterval(points, s, right);

        double d = d1<d2?d1:d2;

        Point[] pointsForCalculation = new Point[right - left];
        
        int i = 0;

        for(int h = left; h < right; h++){
            if (points[h].isContainedInInterval(points[s].x - d, points[s].x + d)) {
                pointsForCalculation[i] = points[h];
                i++;
            }
        }

        Point[] trimmedPointsForCalculation = new Point[i];

        for (int l = 0; l < i; l++){
            trimmedPointsForCalculation[l] = pointsForCalculation[l];
        }

        Arrays.sort(trimmedPointsForCalculation);

        double minimalDistance = d;

        for (int m = 0; m < trimmedPointsForCalculation.length; m++) {
            for (int m2 = m + 1; m2 < trimmedPointsForCalculation.length && m2 < m + 9; m2++) {
                double distanceBetweenPoints = trimmedPointsForCalculation[m].distanceTo(trimmedPointsForCalculation[m2]);

                if (distanceBetweenPoints < minimalDistance) {
                    minimalDistance = distanceBetweenPoints;
                }
                
            }
        }

        return minimalDistance;

    }

    static Point[] getPointsFromCoordinatesSorted(int[] x, int[] y) {

        Point points[] = new Point[x.length];

        for (int i = 0; i < x.length; i++) {
            points[i] = new Point(x[i], y[i]);
        }

        Arrays.sort(points, new Comparator<Point>() {
            @Override
            public int compare(Point p, Point o) {
                return o.x == p.x ? Long.signum(p.y - o.y) : Long.signum(p.x - o.x);
            }
        });

        return points;
    }

    public static void main(String[] args) throws Exception {

        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = new PrintWriter(System.out);
        int n = nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = nextInt();
            y[i] = nextInt();
        }
        System.out.println(minimalDistance(x, y));

        
        //writer.close();
    }

    static BufferedReader reader;
    static PrintWriter writer;
    static StringTokenizer tok = new StringTokenizer("");


    static String next() {
        while (!tok.hasMoreTokens()) {
            String w = null;
            try {
                w = reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (w == null)
                return null;
            tok = new StringTokenizer(w);
        }
        return tok.nextToken();
    }

    static int nextInt() {
        return Integer.parseInt(next());
    }

}
