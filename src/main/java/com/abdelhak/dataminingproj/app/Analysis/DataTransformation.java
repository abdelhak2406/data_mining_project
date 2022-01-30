package com.abdelhak.dataminingproj.app.Analysis;

import javafx.print.Collation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class DataTransformation extends DataAnalysis{

        public DataTransformation(String path) throws Exception{
               super(path);
        }

        public DataTransformation(ArrayList<ArrayList<Double>> data){
               super(data);
        }

        public Double getNormalizedMinMaxVal(Double value, Double min, Double max) {
                /**
                 * get the normalized value
                 */
                return (value - min) / (max - min);
        }

        public ArrayList<Double> getNormalizeMinMaxCol(int colNum) {
                /*
                 * Normalize value of the column @param column
                 * @param data the dataset (matrix)
                 * @param column the columns from which we want to compute the normalization
                 * @return ArrayList<Double> the new values for the column column
                 */
                ArrayList<Double> normalizedColumn = this.data.getColumn(colNum);
                Double min, max;

                min = Collections.min(normalizedColumn);
                max = Collections.max(normalizedColumn);
                for (int i = 0; i < normalizedColumn.size(); i++) {
                        //normalize the thing
                        normalizedColumn.set(i, (normalizedColumn.get(i) - min) / (max - min));
                }

                return normalizedColumn;
        }

        public void minMaxNorm(){
                /**
                 * Normalize all the dataset  using the minMax method
                 * @return the normalized dataset!
                 * @note i know the solution is kindof stupid now and it takes a little bit too much ram
                but since the dataset is small we can work with that, and any changes are welcome.
                 * @note2 it was tested and the value of the first instances are
                [0.40509915014164316, 0.44628099173553726, 0.6624319419237747, 0.3688063063063065,
                0.5010691375623664, 0.03288301759221938, 0.21516494337764666, 1.0]
                it matches the teacher's result
                 */
                int maxNumCol = 8;

                Double[] minList = new Double[maxNumCol];
                Double[] maxList = new Double[maxNumCol];
                for (int i = 0; i < maxNumCol; i++) {
                        minList[i] =  Collections.min(this.data.getColumn(i));
                        maxList[i] =  Collections.max(this.data.getColumn(i));
                }

                for (int i = 0; i < this.data.dataset.size(); i++) {
                        ArrayList<Double> normalizedLine;
                        for (int j = 0; j < this.data.dataset.get(i).size()-1 ; j++) {//the -1 is here because we don't need the last column
                                double normalizedVal ;
                                normalizedVal = getNormalizedMinMaxVal(this.data.getElement(i,j), minList[j], maxList[j]);
                                this.data.setElement(i,j,normalizedVal);
                        }
                }
        }

        /*TODO:implement this one!
        public  ArrayList<ArrayList<Double>> minMaxNorm(){
        }
         */

        public Double getSValue(double moyenne, int colNum) {
                /*
                 * compute the S value for zsocre normalization
                 */
                double sValue = 0.0;
                int n = this.data.dataset.size();
                for (int i = 0; i < this.data.dataset.size(); i++) {
                        sValue = sValue + (this.data.getElement(i,colNum) - moyenne);
                }
                sValue = sValue / n;
                return sValue;
        }

        public static Double getNormalizedzScoreValue(Double value, Double moyenne, Double sValue) {
                /**
                 * get the normalized zscore value
                 */
                return (value - moyenne) / sValue;
        }

        public void zScoreNorm(){
                /*
                 * Normalize all the dataset  using the zScore method
                 * @param data the dataset returned by @see readData
                 * @return the normalized dataset!
                 *
                 */
                int maxNumCol = 8;
                Double[] sList = new Double[maxNumCol];
                Double[] meanList = new Double[maxNumCol];
                for (int i = 0; i < maxNumCol; i++) {
                        meanList[i] = this.getMean(i);
                        sList[i] = getSValue(meanList[i], i);
                }
                for (int i = 0; i < this.data.dataset.size(); i++) {
                        for (int j = 0; j < this.data.dataset.get(i).size() - 1; j++) {//the -1 is here because we don't need the last column
                                double normalizedVal ;
                                normalizedVal=  getNormalizedzScoreValue(this.data.getElement(i,j), meanList[j], sList[j]);
                                this.data.setElement(i,j,normalizedVal);

                        }
                }
        }

        /*TODO implemnet
        public ArrayList<ArrayList<Double>> zScoreNorm(){

        }
        */

        public int getIntervalEqual(ArrayList<double[]> listInt, double value){
                /**
                 * gets in which interval a value is
                 */
                int i=0;
                for ( i = 0; i < listInt.size(); i++) {
                        if ( (value >= listInt.get(i)[0] ) && (value < listInt.get(i)[1] ) ){
                                return i;

                        }
                }
                return i-1;
        }

        public ArrayList<String> getDiscretisEqualCol(int q, int colNum){
                /*
                 * Compute new discretized values for a column in the dataset using the equal method
                 * @param column the column in ArrayList<Double> type which means that we need to
                make an extra step to loop through the data and then transform and get each
                column in this new type
                 * TODO:create a function to do this transformation to have a better dry code.
                 * @param q number of intervals
                 * @param c column number
                 */
                ArrayList<Double> column = this.data.getColumn(colNum);
                double min = Collections.min(column);
                double max = Collections.max(column);
                double intervalLength = (max - min) / q;

                //chaque element de listeIntervale continet un intervale [x,y[
                ArrayList<double[]> listeInvervales = new ArrayList<>() ;

                // remplir la liste des intervalles
                double debutIntervalle = min;
                for (int i = 0; i < q ; i++) {
                        double [] tmp = {debutIntervalle , debutIntervalle + intervalLength};
                        listeInvervales.add(tmp);
                        debutIntervalle = debutIntervalle +intervalLength;
                }


                ArrayList<String> result = new ArrayList<>(column.size());
                for (Double element : column) {
                        int category = getIntervalEqual(listeInvervales, element) + 1;
                        if (category == -1) {
                                System.out.println("error in getDiscretisEqualCol");
                                System.exit(0);
                        }
                        result.add("I" + String.valueOf(colNum) + String.valueOf(category));
                }
                return result;

        }

        public void discretiseEqual(int q){

                //" -1 " because the last column is the target, we don't want to discretize it.
                int maxCol = this.data.dataset.get(0).size() -1;
                this.data.discretizeData = new ArrayList<>();

                for (int i = 0; i < maxCol; i++) {
                        ArrayList<String> discretizedI = this.getDiscretisEqualCol(q,i);
                       this.data.setColumnD(i,discretizedI);
                }
                // add the target column it's the last column soo that's why the -1 ( the 0 is just a random choice)
                this.data.addColumnDataToNormalized(this.data.dataset.get(0).size() - 1);

        }

        public void discretiEffectif(int q){

        }

}
