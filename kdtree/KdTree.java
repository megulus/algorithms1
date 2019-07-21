/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

public class KdTree {
    private Node root;
    private int size = 0;

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void insert(Point2D p) {
        if (!contains(p)) {
            size++;
            if (root == null) {
                root = new Node(p, new Node.XOrientation(), new RectHV(0, 0, 1, 1));
            } else {
                root.insert(p);
            }
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return false;
        return root.contains(p);
    }

     public void draw() {
        root.drawTree();
        root.drawBoundingRectangle(root.boundingRectangle);
     }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("grump!");
        ArrayList<Point2D> result = new ArrayList<>();
        if (root != null) {
            root.addIntersectingPoints(rect, result);
        }
        return result;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("nagpoke");
        }

        if (root == null) {
            return null;
        }

        return root.nearest(p);
    }

    public static void main(String[] args) {
        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        kdtree.draw();
    }


    private static class Node {
        private final Point2D point;
        private Node lesser;
        private Node greater;
        private final Orientation orientation;
        private final RectHV boundingRectangle;

        public Node(Point2D p, Orientation nodeOrientation, RectHV nodeBoundingRectangle) {
             this.point = p;
             this.orientation = nodeOrientation;
             this.boundingRectangle = nodeBoundingRectangle;
        }

        public void drawBoundingRectangle(RectHV rectangle) {
            double xmin = rectangle.xmin();
            double xmax = rectangle.xmax();
            double ymin = rectangle.ymin();
            double ymax = rectangle.ymax();
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.setPenRadius(0.02);
            StdDraw.line(xmin, ymin, xmin, ymax);
            StdDraw.line(xmin, ymax, xmax, ymax);
            StdDraw.line(xmax, ymax, xmax, ymin);
            StdDraw.line(xmax, ymin, xmin, ymin);
        }

        public void drawTree() {
            this.drawPoint();
            this.drawSplitLine();
            if (this.lesser != null) this.lesser.drawTree();
            if (this.greater != null) this.greater.drawTree();
        }

        public void drawPoint() {
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(this.point.x(), this.point.y());
        }

        public void drawSplitLine() {
            this.orientation.drawSplitLine(this.point, this.boundingRectangle);
        }

        public void insert(Point2D p) {
            if (this.orientation.compare(p, this.point) < 0) {
                if (this.lesser == null) {
                    this.lesser = new Node(
                            p,
                            this.orientation.nextLevelOrientation(),
                            this.orientation.lesserSplitRectangle(this.boundingRectangle, this.point)
                    );
                } else {
                    this.lesser.insert(p);
                }
            } else {
                if (this.greater == null) {
                    this.greater = new Node(
                            p,
                            this.orientation.nextLevelOrientation(),
                            this.orientation.greaterSplitRectangle(this.boundingRectangle, this.point)
                    );
                } else {
                    this.greater.insert(p);
                }
            }
        }

        public boolean contains(Point2D p) {
            if (p.equals(this.point)) return true;
            if (this.orientation.compare(p, this.point) < 0) {
                if (this.lesser == null) return false;
                return this.lesser.contains(p);
            }
            if (this.greater == null) return false;
            return this.greater.contains(p);
        }

        public void addIntersectingPoints(RectHV range, Collection<Point2D> result) {
            if (!range.intersects(this.boundingRectangle)) {
                return;
            }
            if (range.contains(this.point)) {
                result.add(point);
            }
            if (this.lesser != null) {
                this.lesser.addIntersectingPoints(range, result);
            }
            if (this.greater != null) {
                this.greater.addIntersectingPoints(range, result);
            }
        }

        public Point2D nearest(Point2D referencePoint) {
            return nearest(referencePoint, null);
        }

        private Point2D nearest(Point2D referencePoint, Point2D nearestSoFar) {
            if (referencePoint == null) {
                throw new IllegalArgumentException();
            }

            if (nearestSoFar == null) {
                nearestSoFar = this.point;
            } else {
                if (referencePoint.distanceSquaredTo(this.point) < referencePoint.distanceSquaredTo(nearestSoFar)) {
                    nearestSoFar = this.point;
                }
            }

            if (this.orientation.compare(referencePoint, this.point) < 0) {
                nearestSoFar = nearestAfterSearchingSubtree(this.lesser, referencePoint, nearestSoFar);
                nearestSoFar = nearestAfterSearchingSubtree(this.greater, referencePoint, nearestSoFar);
            } else {
                nearestSoFar = nearestAfterSearchingSubtree(this.greater, referencePoint, nearestSoFar);
                nearestSoFar = nearestAfterSearchingSubtree(this.lesser, referencePoint, nearestSoFar);
            }
            return nearestSoFar;
        }

        private Point2D nearestAfterSearchingSubtree(Node subtree, Point2D referencePoint, Point2D nearestSoFar) {
            if (subtree != null) {
                double distanceSquaredToSubtree = subtree.boundingRectangle.distanceSquaredTo(referencePoint);
                if (distanceSquaredToSubtree < referencePoint.distanceSquaredTo(nearestSoFar)) {
                    Point2D nearestAfterSearchingSubtree = subtree.nearest(referencePoint, nearestSoFar);
                    if (nearestAfterSearchingSubtree != nearestSoFar) {
                        nearestSoFar = nearestAfterSearchingSubtree;
                    }
                }
            }
            return nearestSoFar;
        }

        private interface Orientation {
            int compare(Point2D first, Point2D second);
            Orientation nextLevelOrientation();
            RectHV lesserSplitRectangle(RectHV container, Point2D splitPoint);
            RectHV greaterSplitRectangle(RectHV container, Point2D splitPoint);
            void drawSplitLine(Point2D splitPoint, RectHV boundingRectangle);
        }

        private static class XOrientation implements Orientation {
            @Override
            public int compare(Point2D first, Point2D second) {
                return Double.compare(first.x(), second.x());
            }

            @Override
            public Orientation nextLevelOrientation() {
                return new YOrientation();
            }

            @Override
            public RectHV lesserSplitRectangle(RectHV container, Point2D splitPoint) {
                return new RectHV(container.xmin(), container.ymin(), splitPoint.x(),  container.ymax());
            }

            @Override
            public RectHV greaterSplitRectangle(RectHV container, Point2D splitPoint) {
                return new RectHV(splitPoint.x(), container.ymin(), container.xmax(),  container.ymax());
            }

            @Override
            public void drawSplitLine(Point2D splitPoint, RectHV boundingRectangle) {
                StdDraw.setPenColor(Color.RED);
                StdDraw.setPenRadius();
                StdDraw.line(splitPoint.x(), boundingRectangle.ymin(), splitPoint.x(), boundingRectangle.ymax());
            }

        }

        private static class YOrientation implements Orientation {
            @Override
            public int compare(Point2D first, Point2D second) {
                return Double.compare(first.y(), second.y());
            }

            @Override
            public Orientation nextLevelOrientation() {
                return new XOrientation();
            }

            @Override
            public RectHV lesserSplitRectangle(RectHV container, Point2D splitPoint) {
                return new RectHV(container.xmin(), container.ymin(), container.xmax(),  splitPoint.y());
            }

            @Override
            public RectHV greaterSplitRectangle(RectHV container, Point2D splitPoint) {
                return new RectHV(container.xmin(), splitPoint.y(), container.xmax(), container.ymax());
            }

            @Override
            public void drawSplitLine(Point2D splitPoint, RectHV container) {
                StdDraw.setPenColor(Color.BLUE);
                StdDraw.setPenRadius();
                StdDraw.line(container.xmin(), splitPoint.y(), container.xmax(), splitPoint.y());
            }

        }
    }
}
