/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> t;

    public PointSET() {
        t = new TreeSet<>();
    }

    public boolean isEmpty() {
        return t.isEmpty();
    }

    public int size() {
        return t.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("cannot insert null into tree");
        t.add(p);
    }

    public boolean contains(Point2D p) {
        return t.contains(p);
    }

    public void draw() {
        for (Point2D p : t) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> range = new ArrayList<Point2D>();
        for (Point2D p : t) {
            if (rect.contains(p)) range.add(p);
        }
        return range;
    }

    public Point2D nearest(Point2D p) {
        double shortestDist = Double.POSITIVE_INFINITY;
        Point2D nearest = null;
        for (Point2D q : t) {
            double distance = p.distanceTo(q);
            if (distance < shortestDist) {
                shortestDist = distance;
                nearest = q;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {

    }
}
