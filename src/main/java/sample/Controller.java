package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller {
    Stage newWindow;
    public Label uploadLabel;
    public Label convertLabel;
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
                ReadIMG readimg = new ReadIMG(file);
                Map<String, String> info = readimg.getInfoIMG();
                Image img = new Image(path);

//                String[] splits = path.split("/");
//                String fileName = splits[splits.length - 1];
//                path = path.substring(6);
//                Mat imgcv = Imgcodecs.imread(path);
//                int height = imgcv.height();
//                int width = imgcv.width();
//                Label label1 = new Label("Image Name : " + fileName);
//                Label label2 = new Label(" Height : " + height + " Width: " + width);
//                VBox vbinside = new VBox(label1, label2);

                ImageView imgView = new ImageView(img);
                imgView.setFitHeight(100);
                imgView.setFitWidth(100);
                VBox vbinside = new VBox();
                fileList.add(file);
                for(String s: info.keySet()){
                    Label l = new Label(s + ": " + info.get(s));
                    vbinside.getChildren().add(l);
                }
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
        if(checkfile()){
            if(checkPath()){
                CovertIMG covertIMG = new CovertIMG(fileList, savePath, fileFormat);
                covertIMG.start();
                convertLabel.setText("Your images have been coverted!");
            }
        }

    }


    //check if files have been uploaded already, before starting to covert imagtes.
    public boolean checkfile(){
        if(fileList.size() == 0){
            AlertBox ab = new AlertBox();
            ab.display("Alert", "You haven't upload your images yet!");
            return false;
        }
        return true;
    }

    //check if the save path has been set already, before starting to covert images.
    public boolean checkPath(){
        if(savePath == null || savePath.length() == 0){
            convertLabel.setText("Please select a save path first !");
            return false;
        }
        return true;
    }

    public void setSavePath(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File savePathFile = directoryChooser.showDialog(newWindow);
        savePath = savePathFile.toURI().toString();
        savePath = savePath.substring(6);
    }
}
