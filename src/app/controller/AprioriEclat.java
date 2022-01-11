package app.controller;

import app.Condidate;
import app.Instance;
import app.Utilities;
import app.functions.Apriori;
import app.functions.Eclat;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

public class AprioriEclat implements Initializable {

    //window fiels
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public TableView<Instance> datasetTable = new TableView<>();
    public TableView<Condidate> apriori_condidates_table = new TableView<>();
    public TableView<Condidate> apriori_result_table = new TableView<>();
    public TableView<Condidate> eclat_condidates_table = new TableView<>();
    public TableView<Condidate> eclat_result_table = new TableView<>();

    public RadioButton min_max_radio = new RadioButton();
    public RadioButton z_score_radio = new RadioButton();
    public RadioButton effective_discretization_radio = new RadioButton();
    public RadioButton equal_discretization_radio = new RadioButton();

    @FXML
    public Spinner<Integer> apriori_support_spinner = new Spinner<>();
    public Spinner<Integer> eclat_support_spinner = new Spinner<>();
    public TextField discretization_q_field = new TextField();



    //useful attributes
    public static String filePath = "";
    public static ArrayList<Double[]> data = null;
    public static ArrayList<Double[]> normalizedData = null;
    public static ArrayList<ArrayList<String>> discretizedData = null;

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

        //Initialize discretization
        this.discretization_q_field.setText("4");

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

    public void addDiscretizedDataToTable(ArrayList<String[]> matrix){
        this.datasetTable.getItems().clear();
        int i = 1;
        String className = "";
        for (String[] data: matrix) {
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

    public void effectiveDiscretizationSelected(ActionEvent event) {
        this.effective_discretization_radio.selectedProperty().setValue(true);
        this.equal_discretization_radio.selectedProperty().setValue(false);
    }

    public void equalDiscretizationSelected(ActionEvent event) {
        this.equal_discretization_radio.selectedProperty().setValue(true);
        this.effective_discretization_radio.selectedProperty().setValue(false);
    }

    public void discretizeData(ActionEvent event){
        int q = Integer.parseInt(this.discretization_q_field.getText());
        ArrayList<ArrayList<String>> descritized_data = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < 7; i++) {
            if (this.equal_discretization_radio.selectedProperty().get()) {
                descritized_data.add(MainFct.discretisationEqual(this.getColumn(data, i), q, i + 1));
            }else{
                descritized_data.add(MainFct.discretisationEffectif(this.getColumn(data, i), q, i + 1));
            }
        }
        ArrayList<String[]> newData = this.transposeData(descritized_data);
        discretizedData =  descritized_data;
        this.addDiscretizedDataToTable(newData);
    }

    public void resetTable(ActionEvent event){
        this.addDatasetToTable(filePath);
        normalizedData = null;
        discretizedData = null;
    }

    public void runApriori(ActionEvent event){
        if (data != null){
            // if discretizationType = 1 we use equal discretization else we use effective discretization
            int discretizationType = 1;
            if (this.effective_discretization_radio.selectedProperty().get()){
                discretizationType = 2;
            }

            Apriori aprioriInstance = new Apriori(data, this.apriori_support_spinner.getValue(), Integer.parseInt(this.discretization_q_field.getText()), discretizationType);
            ArrayList<String[]> frequentItems = aprioriInstance.calculateFrequentItems();
            ArrayList<ArrayList<String>> condidatesList = aprioriInstance.getCondidatesList();

            this.addDataToAprioriCondidates(condidatesList);
            this.addDataToAprioriFrequentItems(frequentItems);
        }

    }

    private void addDataToAprioriFrequentItems(ArrayList<String[]> frequentItems) {
        //Preprocess data
        //Transform frequentItems list of list to simple list of strings using concatenation of the elements
        ArrayList<String> frequentItemsList = new ArrayList<>();
        for (String[] items: frequentItems) {
            String newItem = "";
            for (String item: items) {
                newItem += item + ", ";
            }
            newItem = newItem.substring(0, newItem.length()-2);
            frequentItemsList.add(newItem);
        }

        //Sort list by length of string
        Collections.sort(frequentItemsList, Comparator.comparing(String::length));
        //Get number of lists by checking the length of the last item
        int nbLists = frequentItemsList.get(frequentItems.size()-1).split(",").length;

        //initialize table form
        this.apriori_result_table.getColumns().clear();
        this.apriori_result_table.getItems().clear();

        for (int i = 0; i < nbLists; i++) {
            TableColumn column = new TableColumn<>("L" + (i+1));
            column.setCellValueFactory(new PropertyValueFactory<>("c" + (i+1)));
            this.apriori_result_table.getColumns().add(column);
        }

        // Transform sorted list to a matrix in which each line i represents list Li
        ArrayList<ArrayList<String>> listOfFrequentItemsList = new ArrayList<>();
        ArrayList<String> listOfFrequentItems = new ArrayList<>();
        String tmpItem = frequentItemsList.get(0);
        for (int i = 0; i < frequentItemsList.size(); i++) {
            if (frequentItemsList.get(i).length() > tmpItem.length()){
                listOfFrequentItemsList.add(listOfFrequentItems);
                listOfFrequentItems = new ArrayList<>();
                tmpItem = frequentItemsList.get(i);
            }

            listOfFrequentItems.add(frequentItemsList.get(i));

            if (i == frequentItemsList.size()-1){
                listOfFrequentItemsList.add(listOfFrequentItems);
            }
        }

        //Add data to table
        int maxSize = 0;
        for (int i = 0; i < listOfFrequentItemsList.size(); i++) {
            if (listOfFrequentItemsList.get(i).size() > maxSize){
                maxSize = listOfFrequentItemsList.get(i).size();
            }
        }

        String[] rows;
        for (int i = 0; i < maxSize; i++) {
            rows = new String[7];
            for (int j = 0; j < listOfFrequentItemsList.size(); j++) {
                if (i < listOfFrequentItemsList.get(j).size()){
                    rows[j] = listOfFrequentItemsList.get(j).get(i);
                }

                if (j == listOfFrequentItemsList.size()-1){
                    for (int k = j+1; k < 7; k++) {
                        rows[k] = "";
                    }
                }
            }

            Condidate condidate = new Condidate(rows);
            this.apriori_result_table.getItems().add(condidate);

        }


    }

    private void addDataToAprioriCondidates(ArrayList<ArrayList<String>> condidatesList) {
        /**
         * each line i in condidateslist represents the condidates list at iteration i
         */

        //Initialize table form
        this.apriori_condidates_table.getItems().clear();
        this.apriori_condidates_table.getColumns().clear();


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

    public void runEclat(ActionEvent event){
        if (data != null){
            // if discretizationType = 1 we use equal discretization else we use effective discretization
            int discretizationType = 1;
            if (this.effective_discretization_radio.selectedProperty().get()){
                discretizationType = 2;
            }

            Eclat eclatInstance = new Eclat(data, this.eclat_support_spinner.getValue(), Integer.parseInt(this.discretization_q_field.getText()), discretizationType);
            eclatInstance.executEclat();
            ArrayList<String[]> frequentItems = eclatInstance.frequentItemsList;
            ArrayList<ArrayList<String>> condidatesList = eclatInstance.condidatesList;

            this.addDataToEclatCondidates(condidatesList);
            this.addDataToEclatFrequentItems(frequentItems);
        }
    }

    private void addDataToEclatFrequentItems(ArrayList<String[]> frequentItems) {
        //Preprocess data
        //Transform frequentItems list of list to simple list of strings using concatenation of the elements
        ArrayList<String> frequentItemsList = new ArrayList<>();
        for (String[] items: frequentItems) {
            String newItem = "";
            for (String item: items) {
                newItem += item + ", ";
            }
            newItem = newItem.substring(0, newItem.length()-2);
            frequentItemsList.add(newItem);
        }

        //Sort list by length of string
        Collections.sort(frequentItemsList, Comparator.comparing(String::length));
        //Get number of lists by checking the length of the last item
        int nbLists = frequentItemsList.get(frequentItems.size()-1).split(",").length;

        //initialize table form
        this.eclat_result_table.getColumns().clear();
        this.eclat_result_table.getItems().clear();

        for (int i = 0; i < nbLists; i++) {
            TableColumn column = new TableColumn<>("L" + (i+1));
            column.setCellValueFactory(new PropertyValueFactory<>("c" + (i+1)));
            this.eclat_result_table.getColumns().add(column);
        }

        // Transform sorted list to a matrix in which each line i represents list Li
        ArrayList<ArrayList<String>> listOfFrequentItemsList = new ArrayList<>();
        ArrayList<String> listOfFrequentItems = new ArrayList<>();
        String tmpItem = frequentItemsList.get(0);
        for (int i = 0; i < frequentItemsList.size(); i++) {
            if (frequentItemsList.get(i).length() > tmpItem.length()){
                listOfFrequentItemsList.add(listOfFrequentItems);
                listOfFrequentItems = new ArrayList<>();
                tmpItem = frequentItemsList.get(i);
            }

            listOfFrequentItems.add(frequentItemsList.get(i));

            if (i == frequentItemsList.size()-1){
                listOfFrequentItemsList.add(listOfFrequentItems);
            }
        }

        //Add data to table
        int maxSize = 0;
        for (int i = 0; i < listOfFrequentItemsList.size(); i++) {
            if (listOfFrequentItemsList.get(i).size() > maxSize){
                maxSize = listOfFrequentItemsList.get(i).size();
            }
        }

        String[] rows;
        for (int i = 0; i < maxSize; i++) {
            rows = new String[7];
            for (int j = 0; j < listOfFrequentItemsList.size(); j++) {
                if (i < listOfFrequentItemsList.get(j).size()){
                    rows[j] = listOfFrequentItemsList.get(j).get(i);
                }

                if (j == listOfFrequentItemsList.size()-1){
                    for (int k = j+1; k < 7; k++) {
                        rows[k] = "";
                    }
                }
            }

            Condidate condidate = new Condidate(rows);
            this.eclat_result_table.getItems().add(condidate);

        }


    }

    private void addDataToEclatCondidates(ArrayList<ArrayList<String>> condidatesList) {
        /**
         * each line i in condidateslist represents the condidates list at iteration i
         */

        //Initialize table form
        this.eclat_condidates_table.getItems().clear();
        this.eclat_condidates_table.getColumns().clear();


        for (int i = 0; i < condidatesList.size(); i++) {
            TableColumn column = new TableColumn<>("C" + (i+1));
            column.setCellValueFactory(new PropertyValueFactory<>("c" + (i+1)));
            this.eclat_condidates_table.getColumns().add(column);
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
            this.eclat_condidates_table.getItems().add(condidate);

        }


    }

    //Useful but in general useless functions
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

    public ArrayList<Double> getColumn(ArrayList<Double[]> data, int column){
        ArrayList<Double> columnArray = new ArrayList<>();
        for(int i=0 ;i<data.size();i++) {
            columnArray.add(data.get(i)[column]);
        }
        return  columnArray;
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
    public void  switchToAlgoWin(ActionEvent event) throws Exception {
        Utilities u = new Utilities();
        u.switchWindow(event, "/resources/views/BayesKnn.fxml", root, stage, scene);
    }
}
