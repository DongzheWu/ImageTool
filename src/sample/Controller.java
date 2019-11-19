package sample;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.util.List;

public class Controller {
    Stage newWindow;
    public void uploadClick(){
        FileChooser fc2 = new FileChooser();
        FileChooser.ExtensionFilter addextension = new FileChooser.ExtensionFilter("image", "*.png", "*.jpg");
        fc2.getExtensionFilters().add(addextension);
        List<File> list = fc2.showOpenMultipleDialog(newWindow);

        if(list != null){
            for(File file: list){
                String path = file.toURI().toString();
                System.out.println(path);
            }
        }
//            int row = 0;
//            int col = 0;
//            for(File file: list){
//                String path = file.toURI().toString();
//                Image img = new Image(path);
//                String[] splits = path.split("/");
//                String fileName = splits[splits.length - 1];
//
//
//                path = path.substring(6);
//                Mat imgcv = Imgcodecs.imread(path);
//                int height = imgcv.height();
//                int width = imgcv.width();
//                Label label1 = new Label("Image Name : " + fileName);
//                Label label2 = new Label(" Height : " + height + " Width: " + width);
//
//                ImageView imgView = new ImageView(img);
//                imgView.setFitHeight(100);
//                imgView.setFitWidth(100);
//
//                fileList.add(file);
//                VBox vbox = new VBox(imgView, label1, label2);
//                GridPane.setConstraints(vbox, col, row);
//                grid.getChildren().add(vbox);
//                col++;
//                if(col == 3){
//                    row++;
//                    col = 0;
//                    }
//                }
//        } else{
//            System.out.println("Something is wrong!");
//        }
    }
}
