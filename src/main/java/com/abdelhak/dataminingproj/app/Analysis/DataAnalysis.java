package com.abdelhak.dataminingproj.app.Analysis;

import com.abdelhak.dataminingproj.app.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class DataAnalysis {
        public Data data ;

        public DataAnalysis(String path) throws Exception {
               this.data = new Data(path);
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

                ArrayList<Double> column = data.getColumn(colNum);
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

                ArrayList<Double> column = data.getColumn(colNum);
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
                ArrayList<Double> sorted = data.getColumn(colNum);
                ArrayList<Double> quartileList = new ArrayList<>();
                Collections.sort(sorted);
                double result = 0;
                int i, index;
                int size = sorted.size();

                //Q1
                if (size % 4==0){//pas de reste de division
                        index = size / 4;
                        //-1 because we start from the index 0!
                        quartileList.add(sorted.get(index) -1) ;
                }else{
                        index = Integer.parseInt(String.valueOf(size / 4)) ;
                        quartileList.add(sorted.get(index))  ;
                }
                //Q2
                quartileList.add(this.getMedian(colNum) );
                //Q3
                if ( ( (size*3) % 4 ) == 0 ) {//pas de reste de division
                        index = size / 4;
                        //-1 because we start from the index 0!
                        quartileList.add(sorted.get(index) -1) ;
                }else{
                        index = Integer.parseInt(String.valueOf( (size*3) / 4 )) ;
                        quartileList.add(sorted.get(index))  ;
                }

                /*
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

        getOutliers(){

        }

        getRange(){
                /** Etenque
                 *
                 */

        }

        getFiveNumbers(){

        }

        public  Double getIqr(int colNum){
               ArrayList<Double> quartiList = getQuartiles(colNum) ;
                return  (quartiList.get(2) - quartiList.get(0))  ;
        }

        getStd(){
                /* ecart-type
                 *
                 */

        }

        getVariance(){

        }

        getAllMetrics(){

        }

        getCorrelationCoef(){

        }

        getCovariance(){

        }
}
