package gui;

import functions.base_fct.src.MainFct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    //window fiels
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private Button btn_upload;

    @FXML
    private Button btn_add;

    @FXML
    private Button btn_edit;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_reset;

    @FXML
    public TableView<Field> fieldsTable = new TableView<>();
    public TableView<Instance> datasetTable = new TableView<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initFieldsTable();
        this.initDatasetTable();
    }

    private void initFieldsTable(){

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

        //Add new columns to table
        this.datasetTable.getColumns().addAll(id, field1, field2, field3, field4, field5, field6, field7, classe);
    }

    public void uploadFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Ressource File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        String path = fileChooser.showOpenDialog(stage).getAbsolutePath();
        this.addDatasetToTable(path);

    }

    private void addDatasetToTable(String filePath){
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
                this.datasetTable.getItems().add(instance);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addAction(ActionEvent event) {

    }

    @FXML
    void deleteAction(ActionEvent event) {

    }

    @FXML
    void editAction(ActionEvent event) {

    }

    @FXML
    void resetAction(ActionEvent event) {

    }

    @FXML
    void uploadAction(ActionEvent event) {

    }


    public void switchToDashboardWin(ActionEvent event) throws Exception {
        Utilities u = new Utilities();
        u.switchWindow(event, "DashboardWindow.fxml", root, stage, scene);

    }


}



















