package GMM2D;

import java.io.InputStream;
import java.util.Arrays;

public class ConvexHull {

    public static Point[] findHull(Point[] points) {
        int n = points.length;
        Arrays.sort(points);
        Point[] ans = new Point[2 * n]; // In between we may have a 2n points
        int k = 0;
        int start = 0; // start is the first insertion point
        for (int i = 0; i < n; i++) // Finding lower layer of hull
        {
            Point p = points[i];
            while (k - start >= 2
                    && p.sub(ans[k - 1]).cross(p.sub(ans[k - 2])) > 0) {
                k--;
            }
            ans[k++] = p;
        }
        k--; // drop off last point from lower layer
        start = k;
        for (int i = n - 1; i >= 0; i--) // Finding top layer from hull
        {
            Point p = points[i];
            while (k - start >= 2
                    && p.sub(ans[k - 1]).cross(p.sub(ans[k - 2])) > 0) {
                k--;
            }
            ans[k++] = p;
        }
        k--; // drop off last point from top layer
        return Arrays.copyOf(ans, k); // convex hull is of size k

    }
}