package GMM2D;

import java.util.Locale;

public class PVector {

    int dim;
    double[] array;

    public PVector(int dim) {
        this.dim = dim;
        this.array = new double[dim];
    }

    public PVector clone() {
        PVector param = new PVector(this.dim);
        param.array = this.array.clone();
        return param;
    }

    public String toString() {
        String output = "( ";

        for (int i = 0; i < dim; i++) {
            output += String.format(Locale.ENGLISH, "%13.6f ", array[i]);
        }

        return output + ")";
    }
    
}
