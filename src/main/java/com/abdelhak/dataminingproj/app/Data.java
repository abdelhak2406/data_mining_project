package com.abdelhak.dataminingproj.app;


import  org.apache.commons.io.FilenameUtils;
import java.io.*;
import java.util.ArrayList;

public class Data {
        public ArrayList<ArrayList<Double>> dataset ;
        public ArrayList<ArrayList<String>> discretizeData ;

        public Data(String path) throws Exception {
                this.dataset  =this.readData(path);
        }

        public Data(ArrayList<ArrayList<Double>> data){
               this.dataset = data;
        }

        public ArrayList<ArrayList<Double>> readData(String path)throws  Exception{
                /*TODO:test
                 * Method used to read the file as txt or csv
                 * @param path This is the path to the file
                 * @return THE DATASET! when we do dataset.get(i) the i refers to the line!
                 */
                ArrayList <ArrayList<Double>> matrice = new ArrayList<>();

                BufferedReader reader = new BufferedReader(new FileReader(path));

                String line = reader.readLine();
                while (line != null) {
                        String[] line_table;
                        if (FilenameUtils.getExtension(path).equals("txt")){
                                line_table = line.split("\t+");
                        } else {
                                line_table = line.split(",");
                        }


                        ArrayList<Double> lineDataset = new ArrayList<>();

                        for (String s : line_table) {
                                lineDataset.add(Double.parseDouble(s));
                        }
                        matrice.add(lineDataset);

                        line = reader.readLine();
                }
                reader.close();

                return matrice;
        }

        public String displayData(){
                /* TODO: test
                 * display the content of the dataset line by line.
                 */

                String dataDisplayed =  "aaa";
                for (ArrayList<Double> line : this.dataset) {
                        for (Double value : line) {
                                dataDisplayed = dataDisplayed.concat(String.valueOf(value));
                        }
                }

                return dataDisplayed;
        }

        public void putData(ArrayList<ArrayList<Double>> dataset){
                /*
                 * replace the content of the dataset variable with a new dataset
                 */
                this.dataset = dataset;
        }

        public ArrayList<Double> getColumn(int colNum){
                /*
                 * get the column colNum
                 */
                ArrayList<Double> column = new ArrayList<>();
                for (ArrayList<Double> doubles : this.dataset) {
                        column.add(doubles.get(colNum));
                }
                return column;

        }

        public ArrayList<Double> getLine(int lineNum){
                return this.dataset.get(lineNum);
        }

        public Double getElement(int lineNum, int colNum ){
                return this.dataset.get(lineNum).get(colNum);
        }

        public void addColumnDataToNormalized(int colData){
                /*Add the column @param colData to the  @param colNormal
                 *
                 */
                ArrayList<Double> columnData = this.getColumn(colData);
                ArrayList<String> columnDataStr = new ArrayList<>();
                //convert every elemnt of columnData into String
                for (double elem:columnData) {
                        columnDataStr.add(String.valueOf(elem));
                }
                this.addColumnD(columnDataStr);

        }


        public void setColumnD(int colNum, ArrayList<String> discretizedCol) {
                /*
                 *D for discretized!
                 */
                if (discretizedCol.size() != this.discretizeData.size()){
                        //TODO: throw exception is better
                        System.out.println("error different length!");
                        System.exit(0);
                }
                else{
                        for (int i = 0; i < discretizedCol.size(); i++) {
                                this.discretizeData.get(i).set(colNum,discretizedCol.get(i));
                        }

                }

        }

        public void addColumnD(ArrayList<String>columnD){
               //we add a new
                //check if it has the good size
                if (columnD.size()!= this.discretizeData.size()){
                        System.out.println("ERROR");
                        System.exit(0);
                }else{
                        int i =0;
                        for (ArrayList<String> line:this.discretizeData) {
                               line.add(columnD.get(i)) ;
                               i++;
                        }

                }


        }


        public void setColumn(int colNum, ArrayList<Double> column){
                /*
                 * Change the column colNum values!
                 */
               // check if the new column have the right size!
                if (column.size() != this.dataset.size()){
                        //TODO: throw exception is better
                        System.out.println("error different length!");
                        System.exit(0);
                }
                else{
                        for (int i = 0; i < column.size(); i++) {
                               this.dataset.get(i).set(colNum,column.get(i));
                        }

                }

        }

        //TODO: add a method to cast an ArrayList<Double> into
        public void setLine(int lineNum, ArrayList<Double> line){

                if (line.size() != this.dataset.get(0).size()){
                        //TODO: throw exception is better
                        System.out.println("error different length!");
                        System.exit(0);
                }
                else{
                        this.dataset.set(lineNum,line);
                }
        }

        public void setElement(int lineNum, int colNum, Double newValue){
                this.dataset.get(lineNum).set(colNum,newValue);
        }

        public void deleteLine(int lineNum){
                this.dataset.remove(lineNum);
        }

        public void deleteColumn(int colNum){
                //remove an elemnt
                for (ArrayList<Double> row : this.dataset) {
                        row.remove(colNum);
                }

        }

        public void saveDataSet(String path){
                /*TODO: test
                 * save the dataset in path
                 * @param path and name of the file: exp: dataset/myFile.csv
                 */
                //i have no idea
                File csvFile = new File(path);
                try (PrintWriter csvWriter = new PrintWriter(new FileWriter(csvFile))){

                        for (ArrayList<Double> row : this.dataset) {
                                String lineStr = "";
                                for (Double lineElem:row) {
                                       lineStr = lineStr.concat(String.valueOf(lineElem));
                                }
                                csvWriter.println(lineStr);
                        }
                } catch (IOException e) {
                        //Handle exception
                        e.printStackTrace();
                }

        }

}
