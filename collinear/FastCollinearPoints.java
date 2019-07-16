/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LSLite> segments = new ArrayList<LSLite>();
    private final LineSegment[] result;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Must supply an array of 4 points");
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("Cannot supply null values");
            }
        }
        points = Arrays.copyOf(points, points.length);
        Arrays.sort(points);
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            Point[] otherPoints = Arrays.copyOfRange(points, i + 1, points.length);
            Arrays.sort(otherPoints, p.slopeOrder());
            addSegments(p, otherPoints);
        }
        filterOutSubsegments();
        result = new LineSegment[segments.size()];
        for (int i = 0; i < numberOfSegments(); i++) {
            result[i] = segments.get(i).toLineSegment();
        }
        segments = null;
    }

    private void addSegments(Point p, Point[] otherPoints) {
        if (otherPoints.length == 0) return;
        double slope = p.slopeTo(otherPoints[0]);
        if (p.equals(otherPoints[0]))
            throw new IllegalArgumentException("Duplicate points not allowed");
        int numberOfPoints = 2;
        for (int i = 1; i < otherPoints.length; i++) {
            if (p.equals(otherPoints[i]))
                throw new IllegalArgumentException("Duplicate points not allowed");
            double curSlope = p.slopeTo(otherPoints[i]);
            if (curSlope == slope) {
                numberOfPoints++;
            }
            else {
                if (numberOfPoints >= 4) {
                    segments.add(new LSLite(p, otherPoints[i - 1]));
                }
                numberOfPoints = 2;
                slope = curSlope;
            }
        }
        if (numberOfPoints >= 4) segments.add(new LSLite(p, otherPoints[otherPoints.length - 1]));
    }

    public int numberOfSegments() {
        return result.length;
    }

    public LineSegment[] segments() {
        return result;
    }

    private void filterOutSubsegments() {
        if (segments.isEmpty()) return;
        segments.sort((ls1, ls2) -> {
            int qCompare = ls1.q().compareTo(ls2.q());
            if (qCompare != 0) {
                return qCompare;
            }
            if (ls1.slope() < ls2.slope()) {
                return -1;
            }
            else if (ls2.slope() < ls1.slope()) {
                return 1;
            }
            return ls1.p().compareTo(ls2.p());
        });
        ArrayList<LSLite> newSegments = new ArrayList<LSLite>();
        newSegments.add(segments.get(0));
        double slope = segments.get(0).slope();
        Point q = segments.get(0).q();
        for (int i = 1; i < segments.size(); i++) {
            if (segments.get(i).slope() != slope || !segments.get(i).q().equals(q)) {
                newSegments.add(segments.get(i));
                slope = segments.get(i).slope();
                q = segments.get(i).q();
            }
        }
        LSLite lastSegment = segments.get(segments.size() - 1);
        if (lastSegment.slope() != slope || !lastSegment.q().equals(q))
            newSegments.add(lastSegment);
        segments = newSegments;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    private class LSLite {
        private final Point p;
        private final Point q;

        public LSLite(Point p, Point q) {
            if (p == null || q == null) {
                throw new NullPointerException("argument is null");
            }
            if (p.compareTo(q) < 0) {
                this.p = p;
                this.q = q;
            }
            else {
                this.p = q;
                this.q = p;
            }
        }

        public Point p() {
            return this.p;
        }

        public Point q() {
            return this.q;
        }

        public double slope() {
            return this.p.slopeTo(this.q);
        }

        public LineSegment toLineSegment() {
            return new LineSegment(p(), q());
        }
    }
}
