package GMM2D;

import java.util.Random;

public class GaussianMixtureModel {

    public int components;
    public double[] weights;
    public PVector[] params;

    public GaussianMixtureModel(int n) {
        this.components = n;
        this.weights = new double[n];
        this.params = new PVector[n];
    }

    public void normalizeWeights() {
        double sum = 0;
        int i;
        for (i = 0; i < this.components; i++) {
            sum += this.weights[i];
        }
        for (i = 0; i < this.components; i++) {
            this.weights[i] /= sum;
        }
    }

    public double density(Point p) {
        double cum =  10e-300;
        //double cum = 0;
        for (int i = 0; i < this.components; i++) {
            cum = cum + this.weights[i] * densityOfGaussian(p, params[i]);
        }
        return cum;
    }

    public static double densityOfGaussian(Point p, PVector param) {
        double Px = Math.exp(-(p.x - param.array[0]) * (p.x - param.array[0]) / (2.0d * param.array[1]));
        double Py = Math.exp(-(p.y - param.array[2]) * (p.y - param.array[2]) / (2.0d * param.array[3]));
        return (Px * Py) / (2.0d * Math.PI * Math.sqrt(param.array[1] * param.array[3]));
    }

    public static Point drawRandomPointFromGaussian(PVector L) {
        Random rand = new Random();
        return new Point(L.array[0] + rand.nextGaussian() * Math.sqrt(L.array[1]), L.array[2] + rand.nextGaussian() * Math.sqrt(L.array[3]));
    }

    public Point[] drawRandomPoints(int m) {
        Point[] points = new Point[m];
        double[] f = new double[this.components];
        double sum = 0.0;
        for (int i = 0; i < this.components; i++) {
            sum = sum + this.weights[i];
            f[i] = sum;
        }
        for (int i = 0; i < m; i++) {
            double r = Math.random();
            int idx = 0;
            while (f[idx] < r && idx < this.components - 1) {
                idx++;
            }
            points[i] = drawRandomPointFromGaussian(params[idx]);
        }
        return points;
    }

    public String toString() {
        String output = String.format("Mixture containing %d components\n", components);
        for (int i = 0; i < this.components; i++) {
            output += String.format("Weight = %8.6f ", weights[i]);
            output += String.format("Parameters = %s\n", params[i]);
        }
        return output;
    }

    public GaussianMixtureModel clone() {
        GaussianMixtureModel mm = new GaussianMixtureModel(this.components);
        mm.weights = this.weights.clone();
        for (int i = 0; i < this.components; i++) {
            mm.params[i] = (PVector) this.params[i].clone();
        }
        return mm;
    }
}
