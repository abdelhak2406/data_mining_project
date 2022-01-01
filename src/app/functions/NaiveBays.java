package app.functions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NaiveBays {

        public NaiveBays (ArrayList<Double[]> data) {
                /**
                 * TODO: write something here!
                 */
        }


        public static ArrayList<Double> th_naiveBayes(ArrayList<ArrayList<String>> train, ArrayList<ArrayList<String>> test )
        {
                float p_de_classes[] = {(float)50/150, (float)50/150,(float)50/150};

                double max;
                float  pX_givenC, Xk_given_Ci;
                int att, c, i;
                ArrayList <Double> predictions = new ArrayList  <Double> ();

                double classe = 0;
                // for each row of the test set, calculate the proba of belonging to the 3classes and choice max
                for ( i = 0; i < test.size(); i++)
                {
                        max =0;
                        for (c = 0; c<3; c++)
                        {
                                pX_givenC  = 1;
                                for (att =0; att<7 ;att++)
                                {

                                        Xk_given_Ci = find_prob_Xk_given_Ci(String.valueOf(Double.valueOf(c)),att,train.get(i).get(att), train);
                                        pX_givenC = pX_givenC* Xk_given_Ci;
                                }
                                pX_givenC = pX_givenC * p_de_classes[c];
                                if(pX_givenC> max){max = pX_givenC; classe = c;}

                        }

                        predictions.add( Double.valueOf(classe+1));
                }
                return predictions;
        }

        public static float find_prob_Xk_given_Ci(String classe, int att, String val, ArrayList<ArrayList<String>> data){
                int count = 0,i ;

                for ( i = 0; i < data.size(); i++) {
                        if((data.get(i).get(7).equals(classe) && (data.get(i).get(att).equals(val)))) { count++; } }

                float rslt = (float)count/50;
                return rslt;
        }

        public static float find_prob_Xk_given_Ci_laplace(int classe, int att, float val, ArrayList<Double[]> data){
                int count = 0,i ;
                ArrayList <Double> unique_vals = new ArrayList<Double>();

                for ( i = 0; i < data.size(); i++) {

                        if((data.get(i)[7] == classe) && (data.get(i)[att] == val)) { count++; }
                        // chercher # de valeurs possibles de l'attribut att
                        if ( unique_vals.contains(data.get(i)[att]) == false ) { unique_vals.add(data.get(i)[att]); }
                }
                return (float) (count+1)/(30+unique_vals.size());
        }


        // CONFUSION MATRIX

        public static int[][] getConfusion_Matrix (ArrayList <Double> predictions, ArrayList <String> y ){
                int[][] confusion_matrix = new int[3][3];
                int i,j,k;


                for(i=0;i<3;i++){
                        for(j=0;j<3;j++){
                                confusion_matrix[i][j] = 0;

                                // pour chaque celule, parcourir la liste des Y et Y'
                                for(k= 0; k<predictions.size(); k++)
                                {
                                        if ((y.get(k).equals( String.valueOf(Double.valueOf(i+1)))) && (predictions.get(k).equals(Double.valueOf(j+1))) ){
                                                confusion_matrix[i][j] +=1;
                                        }
                                }

                        }
                }

                return confusion_matrix;
        }


        public static float accuracy(int classe, int[][] confusion_matrix){
                int tp, fn = 0, fp = 0,tn = 0,i,j;
                float acc;

                // 1, TP
                tp = confusion_matrix[classe-1][classe-1];
                // 2, FN

                for(i= 0; i<3;i++){
                        fn = fn+ confusion_matrix[classe-1][i];
                        fp = fp +  confusion_matrix[i][classe-1];
                }
                fn = fn - tp;

                // 3, TP
                fp=  fp - tp;

                // 4, TN
                for(i= 0; i<3;i++){
                        for(j= 0; j<3;j++){
                                tn = tn+ confusion_matrix[i][j];
                        }
                }
                tn = tn -(tp+fn+fp);

                System.out.println("TP : "+tp);
                System.out.println("TN : "+tn);
                System.out.println("FP : "+fp);
                System.out.println("FN : "+fn);

                acc = (float)(tp+tn)/ 60;
                return acc;

        }

        public  static void mainTest(String[] args){
                /**
                 * Function to test the naiveBays things!
                 * confusion matrix,
                 * ect
                 * TODO:trouve comment executer sa!!
                 */



        }
}
