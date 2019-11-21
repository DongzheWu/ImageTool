package sample;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("tool.fxml"));
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Image Management Tool");
        primaryStage.setScene(scene);
        primaryStage.show();

    }






    public static void main(String[] args) {
        launch(args);
    }
}
