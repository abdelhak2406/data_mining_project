package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainWindowController {

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



















