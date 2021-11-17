package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

import static functions.base_fct.src.MainFct.readFile;

public class DashboardWindowController {

    //window fiels
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private TableView<Tendances_centrales> table_mcentrale=new TableView<Tendances_centrales>();


    @FXML private TableView<String> table_mdispersion;



    @FXML
    private Button btn_submit;

    @FXML
    private ComboBox<String> combo_firstCol;


    @FXML
    private ComboBox<String> combo_secondCol;

    @FXML
    private BarChart<?, ?> chart_histogram;

    @FXML
    private Pane chart_boxPlot;

    @FXML
    private Pane chart_otherGraph;

    @FXML
    private Pane chart_qPlot;

    @FXML
    private Pane chart_otherGraph2;

    @FXML
    private GridPane chart_tables;




    @FXML
    void submitAction(ActionEvent event) throws Exception{
        Utilities utilities= new Utilities();
        ArrayList<Double[]> data= readFile("seeds_dataset.txt");
        Double[] table_centrale= utilities.return_centrale(data,combo_firstCol.getSelectionModel().getSelectedIndex());
        ObservableList<Tendances_centrales> tendance= FXCollections.observableArrayList(
                new Tendances_centrales(table_centrale[0],table_centrale[1],table_centrale[2],
                        table_centrale[3],table_centrale[4]));

        table_mcentrale.setItems(tendance);


    }
    @FXML
    void switchToHomeWin(ActionEvent event) throws Exception{
        Utilities u = new Utilities();
        u.switchWindow(event, "MainWindow.fxml", root, stage, scene);
    }

    public void initialize() {
        Utilities ut= new Utilities();
        ut.addChoice(combo_firstCol);
        ut.addChoice(combo_secondCol);
    }




}



