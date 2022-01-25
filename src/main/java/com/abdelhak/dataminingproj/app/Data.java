package com.abdelhak.dataminingproj.app;

import com.abdelhak.dataminingproj.controller.MainWindowController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Data {
         public ArrayList<ArrayList<Double>> dataset ;

        Data(String path) throws Exception {
                this.dataset  = this.readData(path);
        }

        public  ArrayList<ArrayList<Double>> readData(String path)throws  Exception{
                /**
                 * Method used to read the file as txt or csv
                 * @param path This is the path to the file
                 * @return THE DATASET!
                 */
                ArrayList <ArrayList<Double>> matrice =  new ArrayList<ArrayList<Double>>();

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
        public  String displayData(){
                String dataDisplayed =  "aaa";
                return dataDisplayed;
        }
        public  void putData(){

        }
        public  ArrayList<Double> getColumn(int colNum){

        }

        public ArrayList<Double> getLine(int lineNum){

        }

}
