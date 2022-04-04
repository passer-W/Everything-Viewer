package everything;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stage;
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/resources/main.fxml"));
        stage.setTitle("Everything Viewer");
        stage.getIcons().add(new Image("/resources/favicon.png"));
        stage.setScene(new Scene(root, 1000, 700));
        stage.show();
        this.stage = stage;
    }

    public static Stage getStage(){
        return stage;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
