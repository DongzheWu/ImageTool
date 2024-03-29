package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.opencv.core.CvType;
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
    ImageView singleView;
    ToggleGroup mode;
    ScrollBar sbar;
    int level;
    String type;
    Label sblevel;
    Label filterLabel;

    //Initialize showIMG class
    public showIMG(List<File> fileList, GridPane grid, Button preview, ImageView singleView, ToggleGroup mode, ScrollBar sbar, Label sblevel, Label filterLabel){
        this.fileList = fileList;
        this.grid = grid;
        this.preview = preview;
        this.singleView = singleView;
        this.mode = mode;
        this.sbar = sbar;
        this.sblevel = sblevel;
        this.filterLabel = filterLabel;
        level = 0;
        // addListener to scrollbar for adjusting the level of tint/filter effect.
        sbar.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            double max = sbar.getMax();

            level = newValue.intValue();
            int sbl = (int)(((double)level) / max * 100);

            sblevel.setText(sbl + "%");
        });

        //addListener to read which(gray, lomo, line or origin) mode is selected
        mode.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton rb = (RadioButton)mode.getSelectedToggle();
                if(rb != null){
                    type = rb.getText();
                    switch(type){
                        // mode Line, show and set scrollbar
                        case "Line": {
                            sbar.setVisible(true);
                            filterLabel.setVisible(true);
                            sblevel.setVisible(true);
                            sbar.setMax(255);
                            break;
                        }
                        // mode origin, hide scrollbar
                        case "Origin":{
                            sbar.setVisible(false);
                            filterLabel.setVisible(false);
                            sblevel.setVisible(false);
                            break;
                        }
                        // mode lomo, show and set scrollbar
                        case "Lomo": {
                            sbar.setVisible(true);
                            sbar.setMin(5);
                            sbar.setMax(15);
                            filterLabel.setVisible(true);
                            sblevel.setVisible(true);
                            break;
                        }
                        // mode gray, hide scrollbar
                        case "Gray": {
                            sbar.setVisible(false);
                            filterLabel.setVisible(false);
                            sblevel.setVisible(false);
                            break;
                        }
                    }

                }
            }
        });

    }
    //read images from path folder and show them in the Javafx GUI.
    public void getshow(Label uploadLabel){
        grid.getChildren().clear();

        if(fileList != null){
            int row = 0;
            int col = 0;
            grid.setPadding(new Insets(50, 50, 50, 50));



            for(File file: fileList){
                String path = file.toURI().toString();
                // initialize readIMG class and read data(location, size, camera) from images
                ReadIMG readimg = new ReadIMG(file);
                Map<String, String> info = readimg.getInfoIMG();


                path = path.substring(6);
                // user opencv read images from path
                Mat source = Imgcodecs.imread(path);
                Mat destination = new Mat();
                // resize images
                Imgproc.resize(source, destination, new Size(100, 100));

                MatOfByte byteMat = new MatOfByte();
                Imgcodecs.imencode(".bmp", destination, byteMat);
                Image img = new Image(new ByteArrayInputStream(byteMat.toArray()));

                ImageView imgView = new ImageView(img);
                // resize the imgView
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
                // When an image is clicked, preview it by 500*300 size
                imgView.setOnMouseClicked(e -> {
                    String singlePath = file.toURI().toString();
                    currentFile = file;
                    Image singleImg = new Image(singlePath);
                    singleView.setImage(singleImg);
                    singleView.setFitHeight(300);
                    singleView.setFitWidth(500);
                    // add border to the image that is clicked.
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


                // Put all images in the gridpane
                GridPane.setConstraints(hbox, col, row);
                grid.getChildren().add(hbox);
                col++;
                if(col == 3){
                    row++;
                    col = 0;
                }
            }
            // change label
            uploadLabel.setText("Upload finished !");
        } else{
            // change label
            uploadLabel.setText("Something wrong !");
        }

    }
    // preview image
    public void preshow(){
        // preview is clicked
        preview.setOnAction(e -> {
            // check if any image is clicked
            if(currentFile == null){
                System.out.println("file is none");
            }else{
                //read image and apply tint/filter mode to the image
                Mat previewIMG = preResize(currentFile);
                previewIMG = preChange(previewIMG, sbar);
                MatOfByte byteMat = new MatOfByte();
                Imgcodecs.imencode(".jpg", previewIMG, byteMat);
                Image img = new Image(new ByteArrayInputStream(byteMat.toArray()));
                singleView.setImage(img);
                singleView.setFitHeight(300);
                singleView.setFitWidth(400);
            }
        });
    }
    // resize the image for preview
    public Mat preResize(File currentFile){
        String path = currentFile.toURI().toString();
        path = path.substring(6);
        Mat source = Imgcodecs.imread(path);
        Mat destination = new Mat();
        Imgproc.resize(source, destination, new Size(800, 600));
        return destination;
    }
    // apply the filter to preview image
    public Mat preChange(Mat source, ScrollBar sbar){
        switch(type){
            case "Line": {
                return line(source);
            }
            case "Origin":{
                return source;
            }
            case "Lomo": {
                return lomo(source);
            }
            case "Gray": {
                return gray(source);
            }
        }
        return source;

    }
    // line filter effect function
    public Mat line(Mat source){
        Mat destination = new Mat();

        Size size = new Size(21, 21);
        Mat temp = new Mat();

        Imgproc.cvtColor(source, temp, Imgproc.COLOR_RGB2GRAY);
        Imgproc.medianBlur(temp, source, 7);
        Mat edge = new Mat();
        Imgproc.Laplacian(source, edge, CvType.CV_8U, 7);
        Imgproc.threshold(edge, destination, level, 255, Imgproc.THRESH_BINARY_INV);

        return destination;

    }
    // lomo filter effect function
    public Mat lomo(Mat source) {

        int rows = source.rows();
        int cols = source.cols();
        Mat res = new Mat(rows, cols, source.type());
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                double[] data = new double[3];
                data[0] = level*Math.sqrt(source.get(i, j)[0]);
                data[1] = source.get(i, j)[1];
                data[2] = source.get(i, j)[2];

                res.put(i, j, data);


            }
        }
        return res;
    }
    // gray filter effect function
    public Mat gray(Mat source){
        Mat res = new Mat();
        Imgproc.cvtColor(source, res, Imgproc.COLOR_RGB2GRAY);
        return res;
    }



}
