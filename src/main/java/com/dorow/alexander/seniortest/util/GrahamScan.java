package com.dorow.alexander.seniortest.util;

import java.util.Arrays;
import java.util.Stack;

/**
 * The {@code GrahamScan} data type provides methods for computing the convex hull of a set of <em>n</em> points in the plane.
 * <p>
 * The implementation uses the Graham-Scan convex hull algorithm. It runs in O(<em>n</em> log <em>n</em>) time in the worst case and uses O(<em>n</em>) extra memory.
 * <p>
 * For additional documentation, see <a href="http://algs4.cs.princeton.edu/99scientific">Section 9.9</a> of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class GrahamScan {
    private Stack<Point2D> hull = new Stack<Point2D>();

    public GrahamScan(Point2D[] points) {
        if (points == null)
            throw new IllegalArgumentException("argument is null");
        if (points.length == 0)
            throw new IllegalArgumentException("array is of length 0");

        // defensive copy
        int n = points.length;
        Point2D[] a = new Point2D[n];
        for (int i = 0; i < n; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("points[" + i + "] is null");
            a[i] = points[i];
        }

        // preprocess so that a[0] has lowest y-coordinate; break ties by x-coordinate
        // a[0] is an extreme point of the convex hull
        // (alternatively, could do easily in linear time)
        Arrays.sort(a);

        // sort by polar angle with respect to base point a[0],
        // breaking ties by distance to a[0]
        Arrays.sort(a, 1, n, a[0].polarOrder());

        hull.push(a[0]); // a[0] is first extreme point

        // find index k1 of first point not equal to a[0]
        int k1;
        for (k1 = 1; k1 < n; k1++)
            if (!a[0].equals(a[k1]))
                break;
        if (k1 == n)
            return; // all points equal

        // find index k2 of first point not collinear with a[0] and a[k1]
        int k2;
        for (k2 = k1 + 1; k2 < n; k2++)
            if (Point2D.ccw(a[0], a[k1], a[k2]) != 0)
                break;
        hull.push(a[k2 - 1]); // a[k2-1] is second extreme point

        // Graham scan; note that a[n-1] is extreme point different from a[0]
        for (int i = k2; i < n; i++) {
            Point2D top = hull.pop();
            while (Point2D.ccw(hull.peek(), top, a[i]) <= 0) {
                top = hull.pop();
            }
            hull.push(top);
            hull.push(a[i]);
        }

        assert isConvex();
    }

    public Iterable<Point2D> hull() {
        Stack<Point2D> s = new Stack<Point2D>();
        for (Point2D p : hull)
            s.push(p);
        return s;
    }

    // check that boundary of hull is strictly convex
    private boolean isConvex() {
        int n = hull.size();
        if (n <= 2)
            return true;

        Point2D[] points = new Point2D[n];
        int k = 0;
        for (Point2D p : hull()) {
            points[k++] = p;
        }

        for (int i = 0; i < n; i++) {
            if (Point2D.ccw(points[i], points[(i + 1) % n], points[(i + 2) % n]) <= 0) {
                return false;
            }
        }
        return true;
    }

}

