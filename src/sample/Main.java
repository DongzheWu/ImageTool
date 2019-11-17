package sample;


import com.sun.glass.ui.CommonDialogs;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.event.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import javax.swing.*;

import org.opencv.core.Core;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main extends Application {
    Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        window = primaryStage;

        FileChooser fc = new FileChooser();
        FileChooser fc2 = new FileChooser();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select directory");
//        directoryChooser.setInitialDirectory(new File("e:\\"));


        FileChooser.ExtensionFilter addextension = new FileChooser.ExtensionFilter("image", "*.png", "*.jpg");
        fc.getExtensionFilters().add(addextension);
        fc2.getExtensionFilters().add(addextension);

        Button imageButton = new Button("Load your image");
        Button mutiButton = new Button("Load mutiple images");
        Button grayButton = new Button("change to gray");
        Button saveButton = new Button("Download");


        ChoiceBox formatChoice = new ChoiceBox(FXCollections.observableArrayList(
                "jpg", "jpeg", "jpe", "png", "bmp", "pbm", "ppm","tiff","tif"
        ));
        formatChoice.setTooltip(new Tooltip("Select a format to save"));
        Label formatLabel = new Label("Save Format");


        HBox hb = new HBox(10, imageButton, mutiButton, grayButton, formatLabel, formatChoice, saveButton);
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(20);
        grid.setHgap(20);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(hb);
        borderPane.setCenter(grid);


        imageButton.setOnAction(e -> {
            File file = fc.showOpenDialog(window);
            if(file != null){
                Image img = new Image(file.toURI().toString());
                ImageView imgView = new ImageView(img);
                imgView.setFitHeight(100);
                imgView.setFitWidth(100);
                hb.getChildren().add(imgView);
                System.out.println(img);
            }else{
                System.out.println("The file is wrong!");
            }

        });
        List<File> fileList = new ArrayList<File>();


        mutiButton.setOnAction(e -> {
            List<File> list = fc2.showOpenMultipleDialog(window);
            if(list != null){
                int row = 0;
                int col = 0;
                for(File file: list){
                    String path = file.toURI().toString();
                    Image img = new Image(path);
                    String[] splits = path.split("/");
                    String fileName = splits[splits.length - 1];


                    path = path.substring(6);
                    Mat imgcv = Imgcodecs.imread(path);
                    int height = imgcv.height();
                    int width = imgcv.width();
                    Label label1 = new Label("Image Name : " + fileName);
                    Label label2 = new Label(" Height : " + height + " Width: " + width);

                    ImageView imgView = new ImageView(img);
                    imgView.setFitHeight(100);
                    imgView.setFitWidth(100);

                    fileList.add(file);
                    VBox vbox = new VBox(imgView, label1, label2);
                    GridPane.setConstraints(vbox, col, row);
                    grid.getChildren().add(vbox);
                    col++;
                    if(col == 3){
                        row++;
                        col = 0;
                    }
                }
            } else{
                System.out.println("Something is wrong!");
            }
        });

        grayButton.setOnAction(e -> {
            if(fileList.size() == 0){
                AlertBox ab = new AlertBox();
                ab.display("Alert", "You haven't upload your images yet!");
            }
            CovertIMG gray = new CovertIMG(fileList);
            gray.start();

        });

        saveButton.setOnAction(e -> {
            System.out.println(formatChoice.getValue());
            File file = directoryChooser.showDialog(primaryStage);
        });



        ScrollPane sp = new ScrollPane();
        sp.setContent(borderPane);
        Scene scene = new Scene(sp, 800, 300);


//        sp.setHbarPolicy(ScrollBarPolicy.NEVER);
//        sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);

        primaryStage.setTitle("Image Management Tool");
        primaryStage.setScene(scene);
        primaryStage.show();

    }






    public static void main(String[] args) {
        launch(args);
    }
}
//package sample;
//import org.opencv.core.Core;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.core.Core;
//import org.opencv.core.Mat;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.Imgproc;

//public class Main {
//
//    public static void main(String[] args){
////        String filename = "C:\\Users\\wdz19\\Desktop\\test.jpg";
////        smooth(filename);
//        System.out.println(Core.VERSION);
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        String input = "C:/Users/wdz19/Desktop/test/01_mid.jpg";
//
//        // To Read the image
//        Mat source = Imgcodecs.imread(input);
//
//        // Creating the empty destination matrix
//        Mat destination = new Mat();
//
//        // Converting the image to gray scale and
//        // saving it in the dst matrix
//        Imgproc.cvtColor(source, destination, Imgproc.COLOR_RGB2GRAY);
//
//        // Writing the image
//        Imgcodecs.imwrite("C:/Users/wdz19/Desktop/test2.jpg", destination);
//        System.out.println("The image is successfully to Grayscale");
//    }
//}