package app.controller;

import app.Condidate;
import app.FrequentItemsRow;
import app.Instance;
import app.Utilities;
import app.functions.Apriori;
import app.functions.MainFct;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
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

public class AprioriEclat implements Initializable {

    //window fiels
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public TableView<Instance> datasetTable = new TableView<>();
    public TableView<Condidate> apriori_condidates_table = new TableView<>();
    public TableView<FrequentItemsRow> apriori_result_table = new TableView<>();

    public RadioButton min_max_radio = new RadioButton();
    public RadioButton z_score_radio = new RadioButton();
    @FXML
    public Spinner<Integer> apriori_support_spinner = new Spinner<>();



    //useful attributes
    private static String filePath = "";
    private static ArrayList<Double[]> data = null;
    private static ArrayList<Double[]> normalizedData = null;

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

        //Initialize apriori parameters

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

    public void minMaxNormalizationSelected(ActionEvent event) {
        this.z_score_radio.selectedProperty().setValue(false);
    }

    public void zScoreNormalizationSelected(ActionEvent event) {
        this.min_max_radio.selectedProperty().setValue(false);
    }

    public void normalizeData(ActionEvent event) {
        if (this.min_max_radio.selectedProperty().get()){
            //Min max normalization
            normalizedData = MainFct.minMaxNormalization(data);
        }else{
            //Z-score normalization
            normalizedData = MainFct.zScoreNormalization(data);
        }
        this.addDataToTable(normalizedData);
    }

    public void resetTable(ActionEvent event){
        this.addDatasetToTable(filePath);
        normalizedData = null;
    }

    public void runApriori(ActionEvent event){
        if (data != null){
            Apriori aprioriInstance = new Apriori(data, this.apriori_support_spinner.getValue());
            ArrayList<String[]> frequentItems = aprioriInstance.calculateFrequentItems();
            ArrayList<ArrayList<String>> condidatesList = aprioriInstance.getCondidatesList();

            this.addDataToAprioriCondidates(condidatesList);
            this.addDataToAprioriFrequentItems(frequentItems);
        }

    }

    private void addDataToAprioriFrequentItems(ArrayList<String[]> frequentItems) {
        //initialize table form
        this.apriori_result_table.getColumns().clear();
        this.apriori_result_table.getItems().clear();

        TableColumn column = new TableColumn<>("L");
        column.setCellValueFactory(new PropertyValueFactory<>("value"));
        this.apriori_result_table.getColumns().add(column);

        for (String[] itemSet: frequentItems) {
            String itemString = "";
            for (String item: itemSet) {
                itemString += item +",";
            }
            itemString = itemString.substring(0, itemString.length()-1);
            System.out.println(itemString);
            this.apriori_result_table.getItems().add(new FrequentItemsRow(itemString));
        }

    }

    private void addDataToAprioriCondidates(ArrayList<ArrayList<String>> condidatesList) {
        /**
         * each line i in condidateslist represents the condidates list at iteration i
         */

        //Initialize table form
        this.apriori_condidates_table.getItems().clear();
        this.apriori_condidates_table.getColumns().clear();

        double columnWidth = 1/condidatesList.size();
        for (int i = 0; i < condidatesList.size(); i++) {
            TableColumn column = new TableColumn<>("C" + (i+1));
            column.setCellValueFactory(new PropertyValueFactory<>("c" + (i+1)));
            this.apriori_condidates_table.getColumns().add(column);
        }

        int maxSize = 0;
        for (int i = 0; i < condidatesList.size(); i++) {
            if (condidatesList.get(i).size() > maxSize){
                maxSize = condidatesList.get(i).size();
            }
        }

        String[] rows;
        for (int i = 0; i < maxSize; i++) {
            rows = new String[7];
            for (int j = 0; j < condidatesList.size(); j++) {
                if (i < condidatesList.get(j).size()){
                    rows[j] = condidatesList.get(j).get(i);
                }

                if (j == condidatesList.size()-1){
                    for (int k = j+1; k < 7; k++) {
                        rows[k] = "";
                    }
                }
            }

            Condidate condidate = new Condidate(rows);
            this.apriori_condidates_table.getItems().add(condidate);

        }


    }

    @FXML
    public void switchToHomeWin(ActionEvent event) throws Exception{
        Utilities u = new Utilities();
        u.switchWindow(event, "/resources/views/MainWindow.fxml", root, stage, scene);
    }

    public void switchToDashboardWin(ActionEvent event) throws Exception {
        Utilities u = new Utilities();
        u.switchWindow(event, "/resources/views/DashboardWindow.fxml", root, stage, scene);
    }

    private void refreshScene(ActionEvent event) throws IOException {
        Utilities u = new Utilities();
        u.switchWindow(event, "/resources/views/AprioriEclat.fxml", root, stage, scene);
    }
}
