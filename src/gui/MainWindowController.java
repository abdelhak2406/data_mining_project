package gui;

import functions.base_fct.src.MainFct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainWindowController implements Initializable {

    //window fiels
    private Stage stage;
    private Scene scene;
    private Parent root;


    //MainWindow scene fxml attributes
    @FXML
    public TableView<Field> fieldsTable = new TableView<>();
    public TableView<Instance> datasetTable = new TableView<>();
    public TextField nbInstancesField = new TextField();
    public TextField nbAttributesField = new TextField();
    public TextField nbClassesField = new TextField();
    public TextField nbMissingValuesField = new TextField();
    public TextField fstClassField = new TextField();
    public TextField sndClassField = new TextField();
    public TextField thrdClassField = new TextField();

    //AddInstance scene fxml attributes
    public TextField f1Field = new TextField();
    public TextField f2Field = new TextField();
    public TextField f3Field = new TextField();
    public TextField f4Field = new TextField();
    public TextField f5Field = new TextField();
    public TextField f6Field = new TextField();
    public TextField f7Field = new TextField();
    public TextField classField = new TextField();
    public TextField fieldId = new TextField();

    //controller attributes
    private static String filePath = "";
    private static ActionEvent tempEvent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initFieldsTable();
        this.initDatasetTable();
        if (!filePath.equals(""))
            this.addDatasetToTable(filePath);
    }

    private void initFieldsTable(){
        TableColumn id = new TableColumn<>("N°");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn description = new TableColumn<>("Description");
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn type = new TableColumn<>("Type");
        type.setCellValueFactory(new PropertyValueFactory<>("type"));

        //remove exiting columns
        while (this.fieldsTable.getColumns().size() > 0){
            this.fieldsTable.getColumns().remove(0);
        }

        //adding the new columns
        this.fieldsTable.getColumns().addAll(id, name, description, type);

        Field field1 = new Field(1, "field1", "area A", "Numeric");
        Field field2 = new Field(2, "field2", "Perimeter P", "Numeric");
        Field field3 = new Field(3, "field3", "Compactness", "Numeric");
        Field field4 = new Field(4, "field4", "length of kernel", "Numeric");
        Field field5 = new Field(5, "field5", "width of kernel", "Numeric");
        Field field6 = new Field(6, "field6", "Asymmetry coef", "Numeric");
        Field field7 = new Field(7, "field7", "Kernel groove length", "Numeric");
        Field classe = new Field(8, "class", "Class", "Numeric");

        this.fieldsTable.getItems().addAll(field1, field2, field3, field4, field5, field6, field7, classe);

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

    public void uploadFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Ressource File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        String path = fileChooser.showOpenDialog(stage).getAbsolutePath();
        filePath = path;
        this.addDatasetToTable(path);

    }

    private void addDatasetToTable(String filePath){
        this.datasetTable.getItems().clear();
        ArrayList<Double[]> matrix;
        try {
            matrix = MainFct.readFile(filePath);
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
            this.initCards(matrix);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCards(ArrayList<Double[]> matrix){
        this.nbInstancesField.setText(String.valueOf(matrix.size()));
        this.nbAttributesField.setText(String.valueOf(matrix.get(0).length));
        //calculate the number of classes
        ArrayList<Double> tempList = new ArrayList<Double>();
        for (Double[] instance: matrix) {
            tempList.add(instance[instance.length-1]);
        }
        Set<Double> tempSet = new HashSet<>(tempList);
        this.nbClassesField.setText(String.valueOf(tempSet.size()));

        //calculate the number of instances per class
        ArrayList<Double> classesList = new ArrayList<>(tempSet);
        Collections.sort(classesList);
        Integer[] nbClassesPerInstance = new Integer[classesList.size()];
        Arrays.fill(nbClassesPerInstance, 0);
        for (int i = 0; i < classesList.size(); i++) {
            for (Double[] instance : matrix) {
                if (instance[7].equals(classesList.get(i))){
                    nbClassesPerInstance[i] += 1;
                }
            }
        }
        this.fstClassField.setText(String.valueOf(nbClassesPerInstance[0]));
        this.sndClassField.setText(String.valueOf(nbClassesPerInstance[1]));
        this.thrdClassField.setText(String.valueOf(nbClassesPerInstance[2]));

        //Number of missing values
        int nbMissingValues = 0;
        for (Double[] instance: matrix) {
            for (int i = 0; i < instance.length; i++) {
                if (instance[i] == null)
                    nbMissingValues += 1;
            }
        }
        this.nbMissingValuesField.setText(String.valueOf(nbMissingValues));
    }

    @FXML
    void addBtnClick(ActionEvent event) {
        tempEvent = event;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddInstance.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addInstance(ActionEvent event){
        if (this.f1Field.getText().equals("")
                || this.f2Field.getText().equals("")
                || this.f3Field.getText().equals("")
                || this.f4Field.getText().equals("")
                || this.f5Field.getText().equals("")
                || this.f6Field.getText().equals("")
                || this.f7Field.getText().equals("")
                || this.classField.getText().equals("")
        ){
            this.errorAlert("All fields are required.");
        }else{
            try {
                BufferedWriter output = new BufferedWriter(new FileWriter(filePath, true));
                output.newLine();
                output.append(""
                        + this.f1Field.getText() + "\t"
                        + this.f2Field.getText() + "\t"
                        + this.f3Field.getText() + "\t"
                        + this.f4Field.getText() + "\t"
                        + this.f5Field.getText() + "\t"
                        + this.f6Field.getText() + "\t"
                        + this.f7Field.getText() + "\t"
                        + this.classField.getText()
                );
                output.close();
                this.closePopup(event);
                this.refreshScene(tempEvent);
                this.addDatasetToTable(filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void deleteInstance(ActionEvent event) {
        Instance selectedInstance = this.datasetTable.getSelectionModel().getSelectedItem();
        if (selectedInstance != null){
            int lineNumber = selectedInstance.getId();
            try {
                ArrayList<Double[]> matrix = MainFct.readFile(filePath);
                BufferedWriter output = new BufferedWriter(new FileWriter(filePath));
                output.append("");
                matrix.remove(lineNumber-1);
                for (Double[] data: matrix) {
                    output.append(""
                            + data[0] + "\t"
                            + data[1] + "\t"
                            + data[2] + "\t"
                            + data[3] + "\t"
                            + data[4] + "\t"
                            + data[5] + "\t"
                            + data[6] + "\t"
                            + data[7]
                    );
                    if (matrix.indexOf(data) < matrix.size()-1)
                        output.newLine();
                }
                output.close();
                this.refreshScene(event);
                this.addDatasetToTable(filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            this.errorAlert("You have to select an instance from the table.");
        }
    }

    @FXML
    public void editBtnClick(ActionEvent event) {
        tempEvent = event;
        Instance selectedInstance = this.datasetTable.getSelectionModel().getSelectedItem();
        if (selectedInstance != null){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddInstance.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(root));

                int classNumber = 0;
                if (selectedInstance.getClasse().equals("Kama"))
                    classNumber = 1;
                if (selectedInstance.getClasse().equals("Rosa"))
                    classNumber = 2;
                if (selectedInstance.getClasse().equals("Canadian"))
                    classNumber = 3;

                //Insert data to text fields
                ((TextField) root.lookup("#f1Field")).setText(String.valueOf(selectedInstance.getField1()));
                ((TextField) root.lookup("#f2Field")).setText(String.valueOf(selectedInstance.getField2()));
                ((TextField) root.lookup("#f3Field")).setText(String.valueOf(selectedInstance.getField3()));
                ((TextField) root.lookup("#f4Field")).setText(String.valueOf(selectedInstance.getField4()));
                ((TextField) root.lookup("#f5Field")).setText(String.valueOf(selectedInstance.getField5()));
                ((TextField) root.lookup("#f6Field")).setText(String.valueOf(selectedInstance.getField6()));
                ((TextField) root.lookup("#f7Field")).setText(String.valueOf(selectedInstance.getField7()));
                ((TextField) root.lookup("#classField")).setText(String.valueOf(classNumber));
                ((TextField) root.lookup("#fieldId")).setText(String.valueOf(selectedInstance.getId()));

                root.lookup("#editBtn").setVisible(true);

                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            this.errorAlert("You have to select an instance from the table.");
        }
    }

    public void editInstance(ActionEvent event){
        if (this.f1Field.getText().equals("")
                || this.f2Field.getText().equals("")
                || this.f3Field.getText().equals("")
                || this.f4Field.getText().equals("")
                || this.f5Field.getText().equals("")
                || this.f6Field.getText().equals("")
                || this.f7Field.getText().equals("")
                || this.classField.getText().equals("")
        ){
            this.errorAlert("All fields are required.");
        }else{
            try {
                ArrayList<Double[]> matrix = MainFct.readFile(filePath);
                int lineNumber = Integer.parseInt(this.fieldId.getText());

                BufferedWriter output = new BufferedWriter(new FileWriter(filePath));
                //clear file
                output.append("");

                //replacing the instance values with the new ones
                Double[] newLine = {
                        Double.parseDouble(this.f1Field.getText()),
                        Double.parseDouble(this.f2Field.getText()),
                        Double.parseDouble(this.f3Field.getText()),
                        Double.parseDouble(this.f4Field.getText()),
                        Double.parseDouble(this.f5Field.getText()),
                        Double.parseDouble(this.f6Field.getText()),
                        Double.parseDouble(this.f7Field.getText()),
                        Double.parseDouble(this.classField.getText())
                };
                matrix.set(lineNumber-1, newLine);
                //Writing new data in the file
                for (Double[] data: matrix) {
                    output.append(""
                            + data[0] + "\t"
                            + data[1] + "\t"
                            + data[2] + "\t"
                            + data[3] + "\t"
                            + data[4] + "\t"
                            + data[5] + "\t"
                            + data[6] + "\t"
                            + data[7]
                    );
                    if (matrix.indexOf(data) < matrix.size()-1)
                        output.newLine();
                }
                output.close();
                this.closePopup(event);
                this.refreshScene(tempEvent);
                this.addDatasetToTable(filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void resetAction(ActionEvent event) {
        filePath = "";
        try {
            this.refreshScene(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToDashboardWin(ActionEvent event) throws Exception {
        Utilities u = new Utilities();
        u.switchWindow(event, "DashboardWindow.fxml", root, stage, scene);
    }

    private void refreshScene(ActionEvent event) throws IOException {
        Utilities u = new Utilities();
        u.switchWindow(event, "MainWindow.fxml", root, stage, scene);
    }

    public void closePopup(ActionEvent event){
        Button btn = (Button) event.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }

    public void errorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
