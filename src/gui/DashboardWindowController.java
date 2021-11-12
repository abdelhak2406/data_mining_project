package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DashboardWindowController {

    //window fiels
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button btn_submit;

    @FXML
    private ComboBox<?> combo_firstCol;

    @FXML
    private ComboBox<?> combo_secondCol;

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
    private Pane chart_tables;

    @FXML
    void submitAction(ActionEvent event) {

    }
    @FXML
    void switchToHomeWin(ActionEvent event) throws Exception{
        Utilities u = new Utilities();
        u.switchWindow(event, "MainWindow.fxml", root, stage, scene);
    }


}



