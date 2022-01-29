package com.abdelhak.dataminingproj.app.Analysis;

import com.abdelhak.dataminingproj.app.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class DataAnalysis {
        public Data data ;

        public DataAnalysis(String path) throws Exception {
               this.data = new Data(path);
        }

        public DataAnalysis(ArrayList<ArrayList<Double>> data){
                this.data = new Data(data);
        }

        public  double getMean(int colNum){
                /*TODO: test

                 */

                double mean = 0.0;
                ArrayList<Double> column = data.getColumn(colNum);
                for (Double elem:column) {
                        mean = mean + elem;
                }
                return  mean / column.size();
        }

        public double getTrimmedMean(int colNum){
                /*
                 *
                 */
                //sort the values ascending order( small to big)
                ArrayList<Double> column = data.getColumn(colNum);
                Collections.sort(column);
                //
                double dbl = column.size() * 0.025;
                int min = (int) dbl + 1;
                dbl = column.size() * 0.975 ;
                int max = (int) dbl;
                double mean = 0.0 ;
                for (int i = min; i < max ; i++) {
                        mean += column.get(i);
                }

                return mean;
        }

        public double getMedian(int colNum){
                /*TODO:test
                 *
                 */

                ArrayList<Double> column = this.data.getColumn(colNum);
                Collections.sort(column);
                if(column.size() % 2 == 1 ){
                        return column.get(column.size() / 2);
                }else{
                        return (column.get( (column.size() - 1) /2 ) + column.get( (column.size() + 1)/2 ) ) / 2;

                }
        }

        private int getMaxOccurences(ArrayList<Double> list){
                /*TODO:test
                 *
                 */
                Collections.sort(list);
                int i = 0,  max = 0 ;
                while ( i < list.size()) {
                        int counter = 0;
                        while ((i < list.size()) && (Objects.equals(list.get(i), list.get(i + 1)))) {
                                counter++;
                                i++;
                        }
                        if (counter > max) {
                                max = counter;
                        }
                        i++;
                }
                return max;
        }

        public ArrayList<Double> getMode(int colNum){
                /* TODO:test!
                 *
                 */
                ArrayList<Double> column = data.getColumn(colNum);
                Collections.sort(column);
                ArrayList<Double> modesList = new ArrayList<>();
                int i = 0;
                int maxOccur = 0;

                //0:when we're looking for the maxOccur and 1 if we are looking for the mode
                int typeTreat = 0;
                while ( i < column.size()) {
                        int counter = 0;
                        while ((i < column.size()) && (Objects.equals(column.get(i), column.get(i + 1))) ) {
                                counter++;
                                i++;
                        }
                        if(typeTreat == 0){//we search for the maxOccurence
                                if (counter > maxOccur) {
                                        maxOccur = counter;
                                }

                        }else{//we add the mode value to the modesList
                                if ( ( maxOccur > 1 ) && (counter == maxOccur) ) {
                                        modesList.add(column.get(i));
                                }
                        }
                        i++;
                        // we get all the modes here
                        if(i == column.size()) {
                                i = 0;
                                typeTreat = 1;
                        }
                }

                return modesList;

        }

        public  Double getMidRange(int colNum){

                ArrayList<Double> column = this.data.getColumn(colNum);
                Collections.sort(column);

                double max = column.get(column.size() - 1);
                double min = column.get(0);
                return (max + min) / 2;
        }

        public  String getAttributeSymetry(int colNum){
                /* TODO:test (technicaly i didn't realy change anything in comarison to raouf's implemntation)
                 * we say if its symmetric positively  or negatively skewed!
                 *
                 */
                double moyenne = this.getMean(colNum);
                double median = this.getMedian(colNum);
                ArrayList<Double> modeList = this.getMode(colNum);
                if (Math.abs(moyenne - median) <= 0.3){
                        for (double mode: modeList) {
                                if (Math.abs(moyenne - mode) <= 0.3 && Math.abs(median - mode) <= 0.3){
                                        return "symmetric";
                                }
                        }
                        return "none";
                }else{
                        if (moyenne < median){
                                for (double mode: modeList) {
                                        if (median  > mode)
                                                return "none";
                                }
                                return "negatively skewed data";
                        }
                        if (moyenne > median){
                                for (double mode: modeList) {
                                        if (median < mode)
                                                return "none";
                                }
                                return "positively skewed data";
                        }
                }
                return "none";

        }

        public ArrayList<Double> getQuartiles(int colNum){
                /*TODO:test
                 * get the Q1 Q2 and Q3 quartiles
                 * toujours arondir a la valeur entiere superieur!
                 */
                ArrayList<Double> dataColumn = this.data.getColumn(colNum);
                ArrayList<Double> quartileList = new ArrayList<>();
                Collections.sort(dataColumn);
                int  index;
                int size = dataColumn.size();

                //Q1
                if (size % 4==0){//pas de reste de division
                        index = size / 4;
                        //-1 because we start from the index 0!
                        quartileList.add(dataColumn.get(index) -1) ;
                }else{
                        index = size / 4  ;//we will only get the int part.
                        quartileList.add(dataColumn.get(index))  ;
                }
                //Q2
                quartileList.add(this.getMedian(colNum) );
                //Q3
                if ( ( (size*3) % 4 ) == 0 ) {//pas de reste de division
                        index = size / 4;
                        //-1 because we start from the index 0!
                        quartileList.add(dataColumn.get(index) -1) ;
                }else{
                        index = (size*3) / 4  ;
                        quartileList.add(dataColumn.get(index))  ;
                }

                /* youcef's implementation!
                int taille = sorted.size(), index;
                        index = taille / 4;
                        System.out.println("index = " + index);
                        /// pair
                        if (taille % 4 == 0) {
                                 quartileList.add( (sorted.get(index) + sorted.get(index - 1) ) / 2);
                        } else {
                                quartileList.add(sorted.get(index - 1) );
                        }


                                index = taille / 2;
                                System.out.println("index = " + index);
                                /// pair
                                if (taille % 2 == 0) {
                                         quartileList.add((sorted.get(index) + sorted.get(index - 1)) / 2);
                                } else {
                                        quartileList.add(sorted.get(index - 1));
                                }

                                index = taille * 3 / 4;
                                System.out.println("index = " + index);
                                /// pair
                                if (taille * 3 % 4 == 0) {
                                        quartileList.add((sorted.get(index) + sorted.get(index - 1)) / 2);
                                } else {
                                        quartileList.add(sorted.get( index - 1 ) );
                                }
                */

                return quartileList;
        }

        public ArrayList<Double> getOutliers(int colNum){
                /*TODO: test
                 * @return all the outlier values of the @param colNum
                 */
                ArrayList<Double> column = this.data.getColumn(colNum);
                ArrayList<Double> outliersList = new ArrayList<>();
                Collections.sort(column);
                ArrayList<Double> quartiList = this.getQuartiles(colNum);
                double iqr = this.getIqr(colNum);
                double lowLimit = quartiList.get(0) - (1.5 * iqr) ;
                double highLimit = quartiList.get(2) + (1.5 * iqr) ;
                int i = 0;
                //low values!
                while ( (i < column.size()) && (column.get(i) < lowLimit ) ){
                        outliersList.add(column.get(i));
                        i++;
                }
                // high values!
                i = column.size() - 1;
                while ( (i >= 0) && (column.get(i) > highLimit) ){
                        outliersList.add(column.get(i));
                        i--;
                }
                return outliersList;
        }

        public Double getRange(int colNum){
                /* Etendue
                 *
                 */
                ArrayList<Double>column = this.data.getColumn(colNum);
                Collections.sort(column);
                return ( column.get(column.size() - 1 ) - column.get(0) );
        }

        public ArrayList<Double> getFiveNumbers(int colNum){
                /*
                 * get the five numbers (Min, Q1, Q2, Q3, Max)
                 */
                ArrayList<Double> fiveNum = new ArrayList<>();
                ArrayList<Double> column = this.data.getColumn(colNum);
                ArrayList<Double> quartiList = this.getQuartiles(colNum);
                Collections.sort(column);
                fiveNum.add(column.get(0));
                fiveNum.addAll(quartiList);
                fiveNum.add( column.get( column.size() - 1 ) );
                return  fiveNum;
        }

        public  Double getIqr(int colNum){
                /*Ecart interquartile
                 *
                 */
                ArrayList<Double> quartiList = getQuartiles(colNum) ;
                return  (quartiList.get(2) - quartiList.get(0))  ;
        }

        public Double getStd(int colNum){
                /* ecart-type
                 *
                 */
                double sum = 0.0, mean = this.getMean(colNum);
                ArrayList<Double> column = this.data.getColumn(colNum);
                for (double element: column) {
                        double tmp = Math.pow( (element - mean) , 2);
                        sum = sum + tmp;
                }
                sum = Math.sqrt( sum / column.size() );

                return sum;
        }

        public  Double getVariance(int colNum){
                double std = this.getStd(colNum) ;
                return (std * std);
        }

        public Double getCorrelationCoef(int colNum1, int colNum2){

                ArrayList<Double> column1 = this.data.getColumn(colNum1);
                ArrayList<Double> column2 = this.data.getColumn(colNum2);
                double mean1  = this.getMean(colNum1);
                double mean2  = this.getMean(colNum2);
                double std1 = this.getStd(colNum1);
                double std2 = this.getStd(colNum2);
                double n12 =  column2.size() * mean1 * mean2;
                double sum12 = 0.0;
                for (int i = 0; i < column1.size(); i++) {
                        sum12 = sum12 + (column1.get(i) * column2.get(2));
                }

                return ( ( sum12 - n12 ) / ( ( column1.size() - 1 ) * std1 * std2 ) ) ;


        }

        /*TODO:implement this 2 methods!when i know what they should do
        getAllMetrics(){

        }

        getCovariance(){


        }

         */

}
