package sample;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Map;

public class showIMG {
    List<File> fileList;
    GridPane grid;
    HBox prev;
    File currentFile;
    Button preview;
    public showIMG(List<File> fileList, GridPane grid, Button preview){
        this.fileList = fileList;
        this.grid = grid;
        this.preview = preview;
    }

    public void getshow(Label uploadLabel, ImageView singleView){

        if(fileList != null){
            int row = 0;
            int col = 0;
            grid.setPadding(new Insets(10, 10, 10, 10));



            for(File file: fileList){
                String path = file.toURI().toString();
                ReadIMG readimg = new ReadIMG(file);
                Map<String, String> info = readimg.getInfoIMG();


                path = path.substring(6);
                Mat source = Imgcodecs.imread(path);
                Mat destination = new Mat();
                Imgproc.resize(source, destination, new Size(100, 100));

                MatOfByte byteMat = new MatOfByte();
                Imgcodecs.imencode(".bmp", destination, byteMat);
                Image img = new Image(new ByteArrayInputStream(byteMat.toArray()));

//                Image img = new Image(path);


                ImageView imgView = new ImageView(img);
                imgView.setFitHeight(100);
                imgView.setFitWidth(100);//                String[] splits = path.split("/");


                VBox vbinside = new VBox();
                vbinside.setPadding(new Insets(0, 0, 0, 15));


                for(String s: info.keySet()){
                    Label l = new Label(s + ": " + info.get(s));
                    vbinside.getChildren().add(l);
                }

                HBox hbox = new HBox(imgView, vbinside);
                imgView.setOnMouseEntered(e -> {
                    imgView.setCursor(Cursor.HAND);
                });
                hbox.setStyle("-fx-border-color: gray;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-style: solid;");
                imgView.setOnMouseClicked(e -> {
                    String singlePath = file.toURI().toString();
                    currentFile = file;
                    Image singleImg = new Image(singlePath);
                    singleView.setImage(singleImg);
                    singleView.setFitHeight(300);
                    singleView.setFitWidth(500);
                    if(prev == null){
                        hbox.setStyle("-fx-border-color: blue;" +
                                "-fx-border-width: 1.5;" +
                                "-fx-border-style: solid;");
                        prev = hbox;
                    }else{
                        prev.setStyle("-fx-border-color: gray;" +
                                "-fx-border-width: 1.5;" +
                                "-fx-border-style: solid;");
                        hbox.setStyle("-fx-border-color: blue;" +
                                "-fx-border-width: 1.5;" +
                                "-fx-border-style: solid;");
                        prev = hbox;
                    }

                });

                hbox.setMargin(imgView, new Insets(3, 3, 3, 3));



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

    public void preshow(){
        preview.setOnAction(e -> {
            if(currentFile == null){
                System.out.println("file is none");
            }else{
                System.out.println(currentFile);
            }
        });
    }

}
