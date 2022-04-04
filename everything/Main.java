package everything;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/resources/main.fxml"));
        stage.setTitle("Everything Viewer");
        stage.getIcons().add(new Image("/resources/favicon.png"));
        stage.setScene(new Scene(root, 1000, 700));
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}