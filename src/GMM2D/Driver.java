package GMM2D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Driver extends JPanel {

    static int k = 0;
    static int n = 100, m = 3;
    static int state = 0;
    public static Point[] points;
    public static Point hull[][] = new Point[m][];
    static int[] results = new int[n];
    static double cx[] = new double[m], cy[] = new double[m], l1[] = new double[m], l2[] = new double[m];

    public static void plot(Point[] points) {
        Driver.points = points;
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new Driver());
        f.setSize(400, 400);
        f.setLocation(600, 200);
        f.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();

        Point max = getMax(points);
        double scalex = 8 * max.x / w;
        double scaley = 8 * max.y / h;



        if (state == 1) {
            for (int i = 0; i < points.length; i++) {
                if (results[i] == 0) {
                    g2.setPaint(Color.red);
                } else if (results[i] == 1) {
                    g2.setPaint(Color.GREEN);
                } else if (results[i] == 2) {
                    g2.setPaint(Color.gray);
                } else if (results[i] == 3) {
                    g2.setPaint(Color.blue);
                } else if (results[i] == 4) {
                    g2.setPaint(Color.cyan);
                } else if (results[i] == 5) {
                    g2.setPaint(Color.black);
                } else {
                    g2.setPaint(Color.magenta);
                }
                double x = points[i].x * scalex + 100;
                double y = h - points[i].y * scaley - 100;
                g2.fill(new Ellipse2D.Double(x - 2, y - 2, 4, 4));
            }
        }

        if (state == 0) {
            for (int i = 0; i < points.length; i++) {
                g2.setPaint(Color.magenta);
                double x = points[i].x * scalex + 100;
                double y = h - points[i].y * scaley - 100;
                g2.fill(new Ellipse2D.Double(x - 2, y - 2, 4, 4));
            }
        }

        if (state == 2) {
            for (int i = 0; i < points.length; i++) {
                if (results[i] == 0) {
                    g2.setPaint(Color.red);
                } else if (results[i] == 1) {
                    g2.setPaint(Color.GREEN);
                } else if (results[i] == 2) {
                    g2.setPaint(Color.gray);
                } else if (results[i] == 3) {
                    g2.setPaint(Color.blue);
                } else if (results[i] == 4) {
                    g2.setPaint(Color.cyan);
                } else if (results[i] == 5) {
                    g2.setPaint(Color.black);
                } else {
                    g2.setPaint(Color.magenta);
                }
                double x = points[i].x * scalex + 100;
                double y = h - points[i].y * scaley - 100;
                g2.fill(new Ellipse2D.Double(x - 2, y - 2, 4, 4));
            }
            for (int k = 0; k < m; k++) {
                if (k == 0) {
                    g2.setPaint(Color.red);
                } else if (k == 1) {
                    g2.setPaint(Color.GREEN);
                } else if (k == 2) {
                    g2.setPaint(Color.gray);
                } else if (k == 3) {
                    g2.setPaint(Color.blue);
                } else if (k == 4) {
                    g2.setPaint(Color.cyan);
                } else if (k == 5) {
                    g2.setPaint(Color.black);
                } else {
                    g2.setPaint(Color.magenta);
                }
               // System.out.println((cx[k] - l1[k] / 2) * scalex + 100 + "  "+(cy[k] - l2[k] / 2) * scaley + 100);
                g2.draw(new Ellipse2D.Double((cx[k] - l1[k] / 2) * scalex + 100, (cy[k] - l2[k] / 2) * scaley + 100, l1[k], l2[k]));
            }

        }



    }

    private Point getMax(Point[] points) {
        double maxx = -Double.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            if (points[i].x > maxx) {
                maxx = points[i].x;
            }
        }
        double maxy = -Double.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            if (points[i].y > maxy) {
                maxy = points[i].y;
            }
        }
        return new Point(maxx, maxy);
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("num of points : " + n);
        System.out.println("numof components in mixture : " + m);

        // state 0

        state = 0;
        GaussianMixtureModel model = new GaussianMixtureModel(m);
        Random r = new Random();
        for (int i = 0; i < m; i++) {
            PVector param = new PVector(4);
            param.array[0] = r.nextInt(100);
            param.array[1] = r.nextFloat() * 100.0;
            param.array[2] = r.nextInt(100);
            param.array[3] = r.nextFloat() * 100.0;
            model.params[i] = param;
            model.weights[i] = r.nextInt(10) + 1;
        }
        model.normalizeWeights();
        System.out.println("model from which point are generated:\n" + model + "\n");
        Point points[] = model.drawRandomPoints(n);
        plot(points);
        Thread.sleep(2000);

        // state 1

        state = 1;
        GaussianMixtureModel learnedmodel = ExpectationMaximization.initialize(points, m);
        System.out.println("Initial mixture model:\n" + learnedmodel + "\n");

        System.out.println("** Ready to run EM **\n");
        learnedmodel = ExpectationMaximization.run(points, learnedmodel);
        System.out.println("\nMixure model estimated using EM: \n" + learnedmodel + "\n");

        Classification c = new Classification();
        for (int i = 0; i < points.length; i++) {
            results[i] = c.classify_point(learnedmodel, points[i]);
        }

        plot(points);
        Thread.sleep(2000);


        // state 2

        state = 2;
        ArrayList<Integer>[] hulls = new ArrayList[m];
        for (int i = 0; i < m; i++) {
            hulls[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < n; i++) {
            hulls[results[i]].add(i);
        }
        for (int i = 0; i < m; i++) {
            Point[] t = new Point[hulls[i].size()];
            for (int j = 0; j < hulls[i].size(); j++) {
                t[j] = points[(int) (hulls[i].get(j))];
            }
            hull[i] = t.clone();
        }

        // hull[i] is the set of points that are classified in ith component

        /*for (k = 0; k < m; k++) {
            PCA pca = new PCA(hull[k]);
            pca = pca.compute();
            double[] ev1 = pca.evec[0].evec;
            double[] ev2 = pca.evec[1].evec;
            double[] vp1 = new double[hull[k].length];
            double[] vp2 = new double[hull[k].length];
            for (int i = 0; i < hull[k].length; i++) {
                vp1[i] = ev1[0] * hull[k][i].x + ev1[1] * hull[k][i].y;
                vp2[i] = ev2[0] * hull[k][i].x + ev2[1] * hull[k][i].y;
            }
            double min1 = Double.MAX_VALUE, min2 = Double.MAX_VALUE, max1 = Double.MIN_VALUE, max2 = Double.MIN_VALUE;
            for (int i = 0; i < hull[k].length; i++) {
                if (vp1[i] < min1) {
                    min1 = vp1[i];
                }
                if (vp2[i] < min2) {
                    min2 = vp2[i];
                }
                if (vp1[i] > max1) {
                    max1 = vp1[i];
                }
                if (vp2[i] > max2) {
                    max2 = vp2[i];
                }
            }
            double t;
            t = (min1 + max1) / 2;
            l1[k] = t - min1;
            cx[k] = (ev1[0] + ev2[0]) * t;
            t = (min2 + max2) / 2;
            l2[k] = cy[k] - min2;
            cy[k] = (ev1[1] + ev2[1]) * t;
            
        }
                */
        plot(points);
    }
}
