package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    Stage newWindow;
    public Label uploadLabel;
    public Label convertLabel;
    public Label sblevel;
    public Label filterLabel;
    public GridPane grid;
    public ToggleGroup mode;
    public String savePath;
    public ChoiceBox format;
    public String fileFormat;
    public String modeFormat;
    public ScrollPane scrollPane;
    public ProgressIndicator progressIndicator;
    public ImageView singleView;
    public Button preview;
    public ScrollBar sbar;

    ObservableList<String> formatList = FXCollections.observableArrayList(
            "jpg", "jpeg", "jpe", "png", "bmp", "pbm", "ppm","tiff","tif");

    List<File> fileList = new ArrayList<File>();

    public void initialize(){
        format.setValue("jpg");
        format.setItems(formatList);
        grid = new GridPane();
        scrollPane.setContent(grid);
        sbar.setVisible(false);
        filterLabel.setVisible(false);

    }

    public void uploadClick(){
        FileChooser fc2 = new FileChooser();
        FileChooser.ExtensionFilter addextension = new FileChooser.ExtensionFilter("image", "*.png", "*.jpg");
        fc2.getExtensionFilters().add(addextension);
        fileList = fc2.showOpenMultipleDialog(newWindow);

        showIMG show = new showIMG(fileList, grid, preview, singleView, mode, sbar, sblevel, filterLabel);
        show.preshow();
        show.getshow(uploadLabel);

    }

    public void convertClick(){
        fileFormat = (String)format.getSelectionModel().getSelectedItem();
        if(checkfile()){
            if(checkPath()){
                CovertIMG covertIMG = new CovertIMG(fileList, savePath, fileFormat, mode, sbar);
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
