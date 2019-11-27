package sample;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
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
import org.opencv.core.CvType;

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
    ImageView singleView;
    ToggleGroup mode;
    ScrollBar sbar;

    public showIMG(List<File> fileList, GridPane grid, Button preview, ImageView singleView, ToggleGroup mode, ScrollBar sbar){
        this.fileList = fileList;
        this.grid = grid;
        this.preview = preview;
        this.singleView = singleView;
        this.mode = mode;
        this.sbar = sbar;
    }

    public void getshow(Label uploadLabel){

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
            RadioButton selectedRadioButton = (RadioButton) mode.getSelectedToggle();
            String modeFormat = selectedRadioButton.getText();

            if(currentFile == null){
                System.out.println("file is none");
            }else{
                System.out.println("....");
                Mat previewIMG = preResize(currentFile);
                int level = (int)sbar.getValue();
                previewIMG = preChange(previewIMG, modeFormat, level);
                MatOfByte byteMat = new MatOfByte();
                Imgcodecs.imencode(".jpg", previewIMG, byteMat);
                Image img = new Image(new ByteArrayInputStream(byteMat.toArray()));
                singleView.setImage(img);
                singleView.setFitHeight(300);
                singleView.setFitWidth(400);
            }
        });
    }
    public Mat preResize(File currentFile){
        String path = currentFile.toURI().toString();
        path = path.substring(6);
        Mat source = Imgcodecs.imread(path);
        Mat destination = new Mat();
        Imgproc.resize(source, destination, new Size(400, 300));
        return destination;
    }

    public Mat preChange(Mat source, String type, int level){
        switch(type){
            case "Line": return channel(source, level);
            case "Origin": return source;
        }
        return source;

    }

    public Mat line(Mat source, int level){
        Mat destination = new Mat();

        Size size = new Size(21, 21);
        Mat temp = new Mat();

        int low_threshold = 50;
        int high_threshold = level;

        Imgproc.cvtColor(source, temp, Imgproc.COLOR_RGB2GRAY);
        Imgproc.medianBlur(temp, source, 7);
        Mat edge = new Mat();
        Imgproc.Laplacian(source, edge, CvType.CV_8U, 7);
//        int cols = temp.cols();
//        int rows = temp.rows();
//        int ch = temp.channels();

//        for(int i = 0; i < rows; i++){
//            for(int j = 0; j < cols; j++){
//                double[] data = temp.get(i, j);
//                for(int k = 0; k < ch; k++){
//                    data[k] = 255 - data[k];
//                }
//                temp.put(i, j, data);
//            }
//        }
        Imgproc.threshold(edge, destination, high_threshold, 255, Imgproc.THRESH_BINARY_INV);
//        Imgproc.GaussianBlur(temp, source, size,0);
//
//        Imgproc.Canny(source, destination, low_threshold, high_threshold);
        return destination;

    }
    public Mat channel(Mat source, int level) {

        int rows = source.rows();
        int cols = source.cols();
        Mat res = new Mat(rows, cols, source.type());
        System.out.println(source.type());
        System.out.println(rows + cols + source.channels());
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                double[] data = new double[3];
                data[0] = 10*Math.sqrt(source.get(i, j)[0]);
                data[1] = source.get(i, j)[1];
                data[2] = source.get(i, j)[2];

                res.put(i, j, data);


            }
        }
        return res;

    }



}
