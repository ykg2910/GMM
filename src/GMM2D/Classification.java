
package GMM2D;


public class Classification {
    
    public  int classify_point( GaussianMixtureModel mm, Point p){
        double currentprob = 0.0;
        double bestprob = -1;
        int responsible = 0;
        for( int i = 0; i < mm.components; i++){
            currentprob = mm.weights[i]*mm.densityOfGaussian(p,mm.params[i]);
            if(currentprob > bestprob){
                responsible = i;
                bestprob = currentprob;
            }
        }
        return responsible;
    }
}
