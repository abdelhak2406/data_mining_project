import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {


        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/MainWindow.fxml"));
        stage.setTitle("Data Mining Project");
        Scene scene = new Scene(root);

        // the css
        //TODO: find a way to apply the css to every scene(window)
        String cssPath = "gui/style.css";
        String css = this.getClass().getResource(cssPath).toExternalForm();


        Image icon = new Image("gui/assets/tuxou.png");


        scene.getStylesheets().add(css);
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){

        launch(args);

    }

}