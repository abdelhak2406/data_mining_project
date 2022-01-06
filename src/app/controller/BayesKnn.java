package app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class BayesKnn  implements Initializable {

        @FXML
        private Spinner<?> Knn_k_value;

        @FXML
        private Button btn_dashboard;

        @FXML
        private Button btn_home;

        @FXML
        private Button btn_pretraitement;

        @FXML
        private TableView<?> confusion_mat;

        @FXML
        private TableView<?> datasetTable;

        @FXML
        private RadioButton knn_radio;

        @FXML
        private TableView<?> mesures_table;

        @FXML
        private RadioButton naiv_b_radio;

        @FXML
        private Button run_knn_nb;

        @FXML
        void runKnnOrNb(ActionEvent event) {

        }

        @FXML
        void switchToDashboardWin(ActionEvent event) {

        }

        @FXML
        void switchToHomeWin(ActionEvent event) {

        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
                //TODO: please
        }
}
