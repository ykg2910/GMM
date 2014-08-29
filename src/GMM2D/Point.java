package GMM2D;

public class Point implements Comparable<Point> { 

    double x;
    double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point clone() {
        return new Point(this.x, this.y);
    }

    public String toString() {
        String output = "( " + this.x + " , " + this.y + " )";
        return output;
    }

    public int compareTo(Point other) {
        if (x == other.x) {
            return (int) (y - other.y);
        } else {
            return (int) (x - other.x);
        }
    }

    public double cross(Point p) {
        return x * p.y - y * p.x;
    }

    public Point sub(Point p) {
        return new Point(x - p.x, y - p.y);
    }

}
