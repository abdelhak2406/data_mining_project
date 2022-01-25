package com.abdelhak.dataminingproj.app.controller;

import com.abdelhak.dataminingproj.app.Condidate;
import com.abdelhak.dataminingproj.app.Instance;
import com.abdelhak.dataminingproj.app.Utilities;
import app.functions.Knn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static app.functions.MainFct.splitNormalizedData;
import static app.functions.MainFct.split_data_knn;
import static app.functions.NaiveBays.*;

public class BayesKnn  implements Initializable {

        //window fiels
        private Stage stage;
        private Scene scene;
        private Parent root;


        @FXML
        public Spinner<Integer> knn_k_value = new Spinner<>();

        @FXML
        private Button btn_dashboard;

        @FXML
        private Button btn_home;

        @FXML
        private Button btn_pretraitement;

        public Button btn_algos;

        public TableView<Condidate> confusion_mat = new TableView<>();

        @FXML
        public TableView<Instance> datasetTable = new TableView<>();


        @FXML
        private TableView<Condidate> mesures_table  = new TableView<>();

        @FXML
        public RadioButton naiv_b_radio;

        @FXML
        public RadioButton knn_radio;

        @FXML
        private Button run_knn_nb;

        //useful attributes
        private static String filePath = "";
        private static ArrayList<Double[]> data = null;
        private static ArrayList<Double[]> normalizedData = null;
        private static ArrayList<ArrayList<String>> discretizedData = null;



        public void naivBSelected(ActionEvent event) {
                this.knn_radio.selectedProperty().setValue(false);
                this.naiv_b_radio.selectedProperty().setValue(true);
        }
        public void knnSelected(ActionEvent event) {
                this.naiv_b_radio.selectedProperty().setValue(false);
                this.knn_radio.selectedProperty().setValue(true);
        }


        @FXML
        void runKnnOrNb(ActionEvent event) {
                //TODO: do something here pls
                // get what shit is selected!

                // clear confusion matrix
                this.confusion_mat.getItems().clear();
                this.mesures_table.getItems().clear();

                ArrayList<Float> allMetrics = new ArrayList<>();


                if (this.knn_radio.selectedProperty().get()){
                        //
                        // get the dataset!

                        // data normalized, or not??
                        //TODO:maybe check if the data is discretized or not
                        //TODO: take the data from the previous Windows(AprioriEclat)


                        ArrayList<ArrayList<Double[]>> total = split_data_knn(data);

                        Knn knn_instance = new Knn(total.get(0), total.get(1), this.knn_k_value.getValue());

                        Double[][] result = knn_instance.calcul_knn();


                        ArrayList<Double> predicted_classes = knn_instance.predict_class();

                        ArrayList<String > yTest = new ArrayList<String>();

                        ArrayList<Double[]> test  = total.get(1);//1 le test! 0:train
                        for (int i=0; i<test.size();i++)
                        {
                                yTest.add(String.valueOf(test.get(i)[7]));
                        }

                        int[][] mat = getConfusion_Matrix(predicted_classes, yTest);

                        //TODO: display the matrix
                        //this.confusion_mat
                        for (int i =0; i<3;i++)
                        {
                                String[] row = new String[7];
                                for(int j=0; j<7; j++){
                                        if ( j < 3 ) {
                                                row[j] = String.valueOf(mat[i][j]);
                                        }else{
                                                row[j] = String.valueOf("");
                                        }
                                }
                                this.confusion_mat.getItems().add(new Condidate(row) ) ;

                        }
                        float sensitiv = ( sensitivity(1, mat) + sensitivity(2,mat) + sensitivity(3,mat ) ) /3;
                        allMetrics.add(sensitiv) ;
                        for (int i = 1; i < 4 ; i++) {
                                allMetrics.add(sensitivity(i, mat));
                        }


                        float specific = ( specificity(1, mat) + specificity(2,mat) + specificity(3,mat ) ) /3;
                        allMetrics.add(specific) ;
                        for (int i = 1; i < 4 ; i++) {
                                allMetrics.add(specificity(i, mat));
                        }

                        float   acc = ( accuracy(1, mat) + accuracy(2,mat) + accuracy(3,mat ) ) /3;
                        allMetrics.add(acc) ;
                        for (int i = 1; i < 4 ; i++) {
                                allMetrics.add(accuracy(i, mat));
                        }

                        float precisio  = ( precision(1, mat) + precision(2,mat) + precision(3,mat ) ) /3;
                        allMetrics.add(precisio) ;
                        for (int i = 1; i < 4 ; i++) {
                                allMetrics.add(precision(i, mat));
                        }




                        float reca = ( recall(1, mat) + recall(2,mat) + recall(3,mat ) ) /3;
                        allMetrics.add(reca);
                        for (int i = 1; i < 4 ; i++) {
                                allMetrics.add(recall(i, mat));
                        }


                        float fsco = ( fScore(1, mat) + fScore(2,mat) + fScore(3,mat ) ) /3;
                        allMetrics.add(fsco);
                        for (int i = 1; i < 4 ; i++) {
                                allMetrics.add(fScore(i, mat));
                        }





                        System.out.println("k value: "+this.knn_k_value.getValue());
                }else{
                        /// BAYESIAN rapsody


                        ArrayList<String> y = new ArrayList();

                        // add the labels(8th column) to the new dataset to be able to call NB fct
                        for (int j= 0; j < data.size(); j++) {
                                y.add(data.get(j)[7].toString());
                        }
                        discretizedData.add(y);
                        //concretement quand on fait discretizedData.get(i) i:fait reference au numero de  la colonne.




                        //////// USAGE OF NB, CONFUSION MATRIX AND ACCURACY////////////

                        ArrayList<ArrayList<ArrayList<String>>> totalnb = new ArrayList();

                        ArrayList<Double> predictions = new ArrayList<Double>();
                        ArrayList<ArrayList<String>> train;
                        ArrayList<ArrayList<String>> test ;
                        ArrayList<String> yTrain  = new ArrayList<String>();
                        ArrayList<String > yTest = new ArrayList<String>();

                        totalnb = splitNormalizedData(discretizedData);



                        train = totalnb.get(0);
                        System.out.println(train.get(0));

                        // get xtrainset and yTrainset
                        train = totalnb.get(0);
                        for (int i=0; i<train.size();i++)
                        {
                                yTrain.add(train.get(i).get(7));
                        }

                        // get xtestset and yTestset
                        test  = totalnb.get(1);
                        for (int i=0; i<test.size();i++)
                        {
                                yTest.add(test.get(i).get(7));
                        }

                        predictions = th_naiveBayes(train,test);
                        int[][] mat = getConfusion_Matrix(predictions, yTest);


                        //TODO: display the matrix
                        //this.confusion_mat
                        for (int i =0; i<3;i++)
                        {
                                String[] row = new String[7];
                                for(int j=0; j<7; j++){
                                        if ( j < 3 ) {
                                                row[j] = String.valueOf(mat[i][j]);
                                        }else{
                                                row[j] = String.valueOf("");
                                        }
                                }
                                this.confusion_mat.getItems().add(new Condidate(row) ) ;

                        }

                        float sensitiv = ( sensitivity(1, mat) + sensitivity(2,mat) + sensitivity(3,mat ) ) /3;
                        allMetrics.add(sensitiv) ;
                        for (int i = 1; i < 4 ; i++) {
                                allMetrics.add(sensitivity(i, mat));
                        }


                        float specific = ( specificity(1, mat) + specificity(2,mat) + specificity(3,mat ) ) /3;
                        allMetrics.add(specific) ;
                        for (int i = 1; i < 4 ; i++) {
                                allMetrics.add(specificity(i, mat));
                        }

                        float   acc = ( accuracy(1, mat) + accuracy(2,mat) + accuracy(3,mat ) ) /3;
                        allMetrics.add(acc) ;
                        for (int i = 1; i < 4 ; i++) {
                                allMetrics.add(accuracy(i, mat));
                        }

                        float precisio  = ( precision(1, mat) + precision(2,mat) + precision(3,mat ) ) /3;
                        allMetrics.add(precisio) ;
                        for (int i = 1; i < 4 ; i++) {
                                allMetrics.add(precision(i, mat));
                        }




                        float reca = ( recall(1, mat) + recall(2,mat) + recall(3,mat ) ) /3;
                        allMetrics.add(reca);
                        for (int i = 1; i < 4 ; i++) {
                                allMetrics.add(recall(i, mat));
                        }


                        float fsco = ( fScore(1, mat) + fScore(2,mat) + fScore(3,mat ) ) /3;
                        allMetrics.add(fsco);
                        for (int i = 1; i < 4 ; i++) {
                                allMetrics.add(fScore(i, mat));
                        }
                }

                // display the measures (les colonnes!)
                String [] labels = {"Sensitivity", "Specificity", "Accuracy", "Precision", "Recall", "F-Score"};
                int k =0;
                for (int i = 0; i < 7 ; i++) {
                        String[] raw = new String[7];
                        raw[0] = labels[i];
                        for (int j = 1; j < 7; j++) {
                                if(j < 5){
                                        raw[j] = String.valueOf(allMetrics.get(k));
                                        k++;

                                }else{
                                        raw[j] = "";
                                }

                        }
                        this.mesures_table.getItems().add(new Condidate(raw));
                }

        }

        private void initDatasetTable(){
                //Change dataset table columns names
                TableColumn id = new TableColumn<>("N°");
                id.setCellValueFactory(new PropertyValueFactory<>("id"));
                TableColumn field1 = new TableColumn<>("Field1");
                field1.setCellValueFactory(new PropertyValueFactory<>("field1"));
                TableColumn field2 = new TableColumn<>("Field2");
                field2.setCellValueFactory(new PropertyValueFactory<>("field2"));
                TableColumn field3 = new TableColumn<>("Field3");
                field3.setCellValueFactory(new PropertyValueFactory<>("field3"));
                TableColumn field4 = new TableColumn<>("Field4");
                field4.setCellValueFactory(new PropertyValueFactory<>("field4"));
                TableColumn field5 = new TableColumn<>("Field5");
                field5.setCellValueFactory(new PropertyValueFactory<>("field5"));
                TableColumn field6 = new TableColumn<>("Field6");
                field6.setCellValueFactory(new PropertyValueFactory<>("field6"));
                TableColumn field7 = new TableColumn<>("Field7");
                field7.setCellValueFactory(new PropertyValueFactory<>("field7"));
                TableColumn classe = new TableColumn<>("Class");
                classe.setCellValueFactory(new PropertyValueFactory<>("classe"));

                //removing columns
                while (this.datasetTable.getColumns().size() > 0){
                        this.datasetTable.getColumns().remove(0);
                }

                id.prefWidthProperty().bind(datasetTable.widthProperty().multiply(0.07));
                field1.prefWidthProperty().bind(datasetTable.widthProperty().multiply(0.107));
                field2.prefWidthProperty().bind(datasetTable.widthProperty().multiply(0.107));
                field3.prefWidthProperty().bind(datasetTable.widthProperty().multiply(0.107));
                field4.prefWidthProperty().bind(datasetTable.widthProperty().multiply(0.107));
                field5.prefWidthProperty().bind(datasetTable.widthProperty().multiply(0.107));
                field6.prefWidthProperty().bind(datasetTable.widthProperty().multiply(0.107));
                field7.prefWidthProperty().bind(datasetTable.widthProperty().multiply(0.107));
                classe.prefWidthProperty().bind(datasetTable.widthProperty().multiply(0.14));

                //Add new columns to table
                this.datasetTable.getColumns().addAll(id, field1, field2, field3, field4, field5, field6, field7, classe);
        }

        private void addDatasetToTable(String filePath){
                data =  AprioriEclat.data;
                if (AprioriEclat.discretizedData != null){
                        discretizedData = AprioriEclat.discretizedData;
                        this.addDiscretizedDataToTable(discretizedData);
                }
               else{
                        if (AprioriEclat.normalizedData == null){
                                data =  AprioriEclat.data;
                        }else{
                                data = AprioriEclat.normalizedData ;

                        }
                        this.addDataToTable(data);
                }
        }

        public void addDataToTable(ArrayList<Double[]> matrix){
                this.datasetTable.getItems().clear();
                int i = 1;
                String className = "";
                for (Double[] data: matrix) {
                        if (data[data.length - 1] == 1)
                                className = "Kama";
                        if (data[data.length - 1] == 2)
                                className = "Rosa";
                        if (data[data.length - 1] == 3)
                                className = "Canadian";
                        Instance instance = new Instance(
                                i,
                                String.valueOf(data[0]),
                                String.valueOf(data[1]),
                                String.valueOf(data[2]),
                                String.valueOf(data[3]),
                                String.valueOf(data[4]),
                                String.valueOf(data[5]),
                                String.valueOf(data[6]),
                                className
                        );
                        i += 1;
                        this.datasetTable.refresh();
                        this.datasetTable.getItems().add(instance);
                }
        }

        public void addDiscretizedDataToTable(ArrayList<ArrayList<String>> matrix){
                ArrayList<String[]> transposedData = this.transposeData(matrix);
                this.datasetTable.getItems().clear();
                int i = 1;
                String className = "";
                for (String[] data: transposedData) {
                        if (data[data.length - 1].equals("1"))
                                className = "Kama";
                        if (data[data.length - 1].equals("2"))
                                className = "Rosa";
                        if (data[data.length - 1].equals("3"))
                                className = "Canadian";
                        Instance instance = new Instance(
                                i,
                                data[0],
                                data[1],
                                data[2],
                                data[3],
                                data[4],
                                data[5],
                                data[6],
                                className
                        );
                        i += 1;
                        this.datasetTable.refresh();
                        this.datasetTable.getItems().add(instance);
                }
        }

        public ArrayList<String[]> transposeData(ArrayList<ArrayList<String>> data){
                ArrayList<String[]> new_data = new ArrayList<>();
                for (int i = 0; i < data.get(0).size(); i++) {
                        ArrayList<String> tempItemset = new ArrayList<>();
                        String[] tmpArray = new String[data.size()];
                        for (int j = 0; j < data.size(); j++) {
                                tempItemset.add(data.get(j).get(i));
                        }
                        new_data.add(tempItemset.toArray(tmpArray));
                }
                return new_data;
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
                this.initDatasetTable();
                if (!MainWindowController.filePath.equals("")){
                        if (normalizedData != null){
                                this.addDataToTable(normalizedData);
                        }else {
                                this.addDatasetToTable(MainWindowController.filePath);
                        }
                        filePath = MainWindowController.filePath;
                }

                this.confusion_mat.getColumns().clear();
                for (int i = 0; i < 3; i++) {
                        TableColumn colId = new TableColumn<>("");
                        colId.setCellValueFactory(new PropertyValueFactory<>("c"+ (i + 1) ));
                        this.confusion_mat.getColumns().add(colId);


                }

                this.mesures_table.getColumns().clear();

                TableColumn colIzan = new TableColumn<>("");
                colIzan.setCellValueFactory(new PropertyValueFactory<>("c1" ));

                TableColumn colId = new TableColumn<>("Mean");
                colId.setCellValueFactory(new PropertyValueFactory<>("c2" ));
                this.mesures_table.getColumns().addAll(colIzan, colId );

                for (int i = 0; i < 3; i++) {
                        colId = new TableColumn<>("C" + (i + 1) );
                        colId.setCellValueFactory(new PropertyValueFactory<>("c"+ (i + 3) ));
                        this.mesures_table.getColumns().add(colId);


                }
        }





        @FXML
        public void switchToHomeWin(ActionEvent event) throws Exception{
                Utilities u = new Utilities();
                u.switchWindow(event, "/resources/views/MainWindow.fxml", root, stage, scene);
        }
        @FXML
        public void switchToDashboardWin(ActionEvent event) throws Exception {
                Utilities u = new Utilities();
                u.switchWindow(event, "/resources/views/DashboardWindow.fxml", root, stage, scene);
        }
        @FXML
        public void switchToPretraitWin(ActionEvent event) throws Exception {
                Utilities u = new Utilities();
                u.switchWindow(event, "/resources/views/AprioriEclat.fxml", root, stage, scene);
        }
        private void refreshScene(ActionEvent event) throws IOException {
                Utilities u = new Utilities();
                u.switchWindow(event, "/resources/views/BayesKnn.fxml", root, stage, scene);
        }
}
