package app.functions;

import java.util.ArrayList;
import java.util.Arrays;

public class Knn {
    ArrayList<Double[]> training_data, testing_data;
    int k;

    public Knn(ArrayList training_data, ArrayList testing_data, int k) {
        this.training_data = training_data;
        this.testing_data = testing_data;
        this.k = k;
    }

    public Double distance_eucledienne(Double[] ins1, Double[] ins2){
        Double distance=0.0;
        for (int i = 0; i < ins1.length; i++) {
            distance+=Math.pow(ins1[i]-ins2[i],2);
        }
        return Math.sqrt(distance);
    }

    public Double[][] calcul_knn(){
        Double[][] nearest_neighb= new Double[testing_data.size()][k];
        for (int i = 0; i < testing_data.size(); i++) {
            Double[] distances= new Double[training_data.size()];
            for (int j = 0; j < training_data.size(); j++) {
                distances[j]=distance_eucledienne(testing_data.get(i),training_data.get(j));
            }
            Arrays.sort(distances);
            nearest_neighb[i][0]=distances[0];
            nearest_neighb[i][1]=distances[1];
            nearest_neighb[i][2]=distances[2];
        }

        return nearest_neighb;
    }
}
