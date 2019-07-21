/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

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
                root = new Node(p, Node.Orientation.X, new RectHV(0, 0, 1, 1));
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

    // public void draw() {}
    //
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
    }


    private static class Node {
        private final Point2D point;
        private Node lesser;
        private Node greater;
        private final Orientation orientation;
        private final RectHV boundingRectangle;

        public Node(Point2D p, Orientation o, RectHV boundingRectangle) {
             this.point = p;
             this.orientation = o;
             this.boundingRectangle = boundingRectangle;
        }

        public void insert(Point2D p) {
            if (this.orientation.compare(p, this.point) < 0) {
                if (this.lesser == null) {
                    this.lesser = new Node(
                            p,
                            this.orientation.nextLevelOrientation(),
                            this.orientation.lesserSplitRectangle(this.boundingRectangle, p)
                    );
                } else {
                    this.lesser.insert(p);
                }
            } else {
                if (this.greater == null) {
                    this.greater = new Node(
                            p,
                            this.orientation.nextLevelOrientation(),
                            this.orientation.greaterSplitRectangle(this.boundingRectangle, p)
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
            if (nearestSoFar == null) {
                nearestSoFar = this.point;
            } else {
                if (referencePoint.distanceTo(this.point) < referencePoint.distanceTo(nearestSoFar)) {
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
                double distanceToSubtree = subtree.boundingRectangle.distanceTo(referencePoint);
                if (distanceToSubtree < referencePoint.distanceTo(nearestSoFar)) {
                    Point2D nearestAfterSearchingSubtree = subtree.nearest(referencePoint, nearestSoFar);
                    if (nearestAfterSearchingSubtree != nearestSoFar) {
                        nearestSoFar = nearestAfterSearchingSubtree;
                    }
                }
            }
            return nearestSoFar;
        }

        private enum Orientation {
            X {
                @java.lang.Override
                public int compare(Point2D one, Point2D two) {
                    return Double.compare(one.x(), two.x());
                }

                @java.lang.Override
                public Orientation nextLevelOrientation() {
                    return Y;
                }

                @java.lang.Override
                public RectHV lesserSplitRectangle(RectHV container, Point2D splitPoint) {
                    return new RectHV(container.xmin(), splitPoint.x(), container.ymin(), container.ymax());
                }

                @java.lang.Override
                public RectHV greaterSplitRectangle(RectHV container, Point2D splitPoint) {
                    return new RectHV(splitPoint.x(), container.xmax(), container.ymin(), container.ymax());
                }
            }, Y {
                @java.lang.Override
                public int compare(Point2D one, Point2D two) {
                    return Double.compare(one.y(), two.y());
                }

                @java.lang.Override
                public Orientation nextLevelOrientation() {
                    return X;
                }

                @java.lang.Override
                public RectHV lesserSplitRectangle(RectHV container, Point2D splitPoint) {
                    return new RectHV(container.xmin(), container.xmax(), container.ymin(), splitPoint.y());
                }

                @java.lang.Override
                public RectHV greaterSplitRectangle(RectHV container, Point2D splitPoint) {
                    return new RectHV(container.xmin(), container.xmax(), splitPoint.y(), container.ymax());
                }
            };

            public abstract int compare(Point2D one, Point2D two);
            public abstract Orientation nextLevelOrientation();
            public abstract RectHV lesserSplitRectangle(RectHV container, Point2D splitPoint);
            public abstract RectHV greaterSplitRectangle(RectHV container, Point2D splitPoint);
       }
    }
}
