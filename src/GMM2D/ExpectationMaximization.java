package GMM2D;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ExpectationMaximization {

    public static GaussianMixtureModel initialize(Point[] points, int components) {
        GaussianMixtureModel mm = new GaussianMixtureModel(components);
        Integer[] arr = sampleNUniquePoints(components, points.length);
        for (int i = 0; i < components; i++) {
            mm.weights[i] = (double) 1 / components;
            PVector param = new PVector(4);
            param.array[0] = points[arr[i]].x;
            param.array[1] = 1;
            param.array[2] = points[arr[i]].y;
            param.array[3] = 1;
            mm.params[i] = param;
        }
        return mm;
    }

    public static final Integer[] sampleNUniquePoints(int components, int numpoints) {
        Random rand = new Random();
        Set<Integer> set = new HashSet();
        while (set.size() < components) {
            int r = rand.nextInt(numpoints);
            if (!set.contains(r)) {
                set.add(r);
            }
        }
        return set.toArray(new Integer[set.size()]);
    }

    public static double logLikelihood(Point[] points, GaussianMixtureModel f) {
        double v = 0.0;
        for (int i = 0; i < points.length; i++) {
            //System.out.println(f.density(points[i]));
            v = v + Math.log(f.density(points[i]));
        }
        return v;
    }

    public static GaussianMixtureModel run(Point[] points, GaussianMixtureModel mm) {
        GaussianMixtureModel mixtureModel = mm.clone();

        // Variables
        int numComponents = mixtureModel.components;
        int numPoints = points.length;
        int n, k;
        int iterations = 0;
        double[][] p = new double[numPoints][numComponents];

        // Initial log likelihood
        double logLikelihoodNew = logLikelihood(points, mixtureModel);
        double logLikelihoodThreshold = 10e-10; //Math.abs(logLikelihoodNew) * 0.01;
        double logLikelihoodOld;

        //System.out.printf("Iteration %2d: LL = %12.6f\n", iterations, logLikelihoodNew);

        do {

            logLikelihoodOld = logLikelihoodNew;

            // E-step: computation of matrix P (fast version, we don't compute 1/f(x) for all P[i][j])
            for (n = 0; n < numPoints; n++) {
                double sum = 10e-300;
                //double sum = 0;
                for (k = 0; k < numComponents; k++) {
                    double tmp = mixtureModel.weights[k]
                            * GaussianMixtureModel.densityOfGaussian(points[n], mixtureModel.params[k]);
                    p[n][k] = tmp;
                    sum += tmp;
                }
                for (k = 0; k < numComponents; k++) {
                    p[n][k] /= sum;
                }
            }

            // M-step: computation of new Gaussians and the new weights
            for (k = 0; k < numComponents; k++) {

                // Variables
                double sum = 10e-300;
                //double sum = 0;
                double mux = 0;
                double muy = 0;
                double sigmax = 0;
                double sigmay = 0;

                // First step of the computation of new mu
                for (n = 0; n < numPoints; n++) {
                    double w = p[n][k];
                    sum += w;
                    mux += points[n].x * w;
                    muy += points[n].y * w;
                }
                mux /= sum;
                muy /= sum;

                // Computation of new sigma
                for (n = 0; n < numPoints; n++) {
                    double diffx = points[n].x - mux;
                    double diffy = points[n].y - muy;
                    sigmax += p[n][k] * diffx * diffx;
                    sigmay += p[n][k] * diffy * diffy;
                }
                sigmax /= sum;
                sigmay /= sum;

                // Set new mu and sigma
                PVector param = new PVector(4);
                param.array[0] = mux;
                param.array[1] = sigmax;
                param.array[2] = muy;
                param.array[3] = sigmay;
                mixtureModel.params[k] = param;
                mixtureModel.weights[k] = sum / numPoints;
            }

            // Update of iterations and log likelihood value
            iterations++;
            logLikelihoodNew = logLikelihood(points, mixtureModel);

            System.out.printf("Iteration %2d: LL = %12.6f\n", iterations, logLikelihoodNew);
        } while (Math.abs((logLikelihoodNew - logLikelihoodOld) / logLikelihoodOld) > logLikelihoodThreshold
                && iterations < 30);
         
        return mixtureModel;
    }
    
}
