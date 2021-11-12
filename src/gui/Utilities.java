package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/*
    this class is aimed to contain useful methods that we will need
    in the gui.

 */
public class Utilities {

    public  static  void acceptOnlyNumbersTextField(TextField txt){
      /*
         just accept Numbers in a specific textField
       */
        txt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txt.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }



    public static void addBlancChoice(ComboBox cbx){
       /*
            when a user selects an item in the choice box and wants to go back
            we need to add a "" item that's what this function do
        */

        cbx.getItems().add("");
        /*
        cbx_softwareNeeds.getItems().add("");
        cbx_desktopEnvirnment.getItems().add("");
        cbx_linuxDistro.getItems().add("");
        cbx_os.getItems().add("");
        */

    }

    public static Boolean isFilled(TextField input){
        //works !
        return  !(input.getText() == null || input.getText().trim().isEmpty()) ;
    }

    public void switchWindow(ActionEvent event, String fxmlWindow, Parent root, Stage stage, Scene scene) throws IOException {

        String cssPath = "style.css";
        String css = this.getClass().getResource(cssPath).toExternalForm();

        root = FXMLLoader.load(getClass().getResource(fxmlWindow));
        stage =(Stage) ((Node) event.getSource()).getScene().getWindow();

        scene = new Scene(root);
        scene.getStylesheets().add(css);

        stage.setScene(scene);

        stage.show();
    }

}
