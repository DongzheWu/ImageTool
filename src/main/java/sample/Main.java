package sample;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

public class Main extends Application {
    Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/tool.fxml"));
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Image Management Tool");
        primaryStage.setScene(scene);
        primaryStage.show();

    }






    public static void main(String[] args) {
        launch(args);
    }
}
