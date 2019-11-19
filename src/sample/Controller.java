package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;


import javafx.geometry.Insets;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    Stage newWindow;
    public Label uploadLabel;
    public GridPane grid;
    public ToggleGroup mode;
    public String savePath;
    public ChoiceBox format;
    public String fileFormat;
    ObservableList<String> formatList = FXCollections.observableArrayList(
            "jpg", "jpeg", "jpe", "png", "bmp", "pbm", "ppm","tiff","tif");

    List<File> fileList = new ArrayList<File>();

    public void initialize(){
        format.setValue("jpg");
        format.setItems(formatList);
    }
    public void uploadClick(){
        FileChooser fc2 = new FileChooser();
        FileChooser.ExtensionFilter addextension = new FileChooser.ExtensionFilter("image", "*.png", "*.jpg");
        fc2.getExtensionFilters().add(addextension);
        List<File> list = fc2.showOpenMultipleDialog(newWindow);

        if(list != null){
            int row = 0;
            int col = 0;
            grid.setPadding(new Insets(10, 10, 10, 10));
            grid.setVgap(20);
            grid.setHgap(20);


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
                VBox vbinside = new VBox(label1, label2);
                HBox hbox = new HBox(imgView, vbinside);
                GridPane.setConstraints(hbox, col, row);
                grid.getChildren().add(hbox);
                col++;
                if(col == 3){
                    row++;
                    col = 0;
                    }
                }
            uploadLabel.setText("Upload finished !");
        } else{
            uploadLabel.setText("Something wrong !");
        }
    }

    public void convertClick(){
        RadioButton selectedRadioButton = (RadioButton) mode.getSelectedToggle();
        fileFormat = (String)format.getSelectionModel().getSelectedItem();
        System.out.println(fileFormat);
//        if(fileList.size() == 0){
//            AlertBox ab = new AlertBox();
//            ab.display("Alert", "You haven't upload your images yet!");
//        }
//        CovertIMG covertIMG = new CovertIMG(fileList);
//        covertIMG.start();
    }
}
