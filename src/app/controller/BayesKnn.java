package app.controller;

import app.Instance;
import app.Utilities;
import app.functions.MainFct;
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

public class BayesKnn  implements Initializable {

        //window fiels
        private Stage stage;
        private Scene scene;
        private Parent root;


        @FXML
        private Spinner<?> Knn_k_value;

        @FXML
        private Button btn_dashboard;

        @FXML
        private Button btn_home;

        @FXML
        private Button btn_pretraitement;

        public Button btn_algos;

        @FXML
        private TableView<?> confusion_mat;

        @FXML
        public TableView<Instance> datasetTable = new TableView<>();

        @FXML
        private RadioButton knn_radio;

        @FXML
        private TableView<?> mesures_table;

        @FXML
        private RadioButton naiv_b_radio;

        @FXML
        private Button run_knn_nb;

        //useful attributes
        private static String filePath = "";
        private static ArrayList<Double[]> data = null;
        private static ArrayList<Double[]> normalizedData = null;



        @FXML
        void runKnnOrNb(ActionEvent event) {

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
                ArrayList<Double[]> matrix;
                try {
                        matrix = MainFct.readFile(filePath);
                        data = matrix;
                        this.addDataToTable(matrix);

                } catch (Exception e) {
                        e.printStackTrace();
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
