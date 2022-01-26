package com.abdelhak.dataminingproj.app;

import com.abdelhak.dataminingproj.controller.MainWindowController;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Data {
         public ArrayList<ArrayList<Double>> dataset ;

        Data(String path) throws Exception {
                this.dataset  =this.readData(path);
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
                        if (MainWindowController.fileExtension.equals("txt")) {
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
