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
    public Label showPath;
    public GridPane grid;
    public ToggleGroup mode;
    public String savePath;
    public ChoiceBox format;
    public String fileFormat;
    public String modeFormat;
    public ScrollPane scrollPane;
    public ProgressIndicator cProgress;
    public ImageView singleView;
    public Button preview;
    public ScrollBar sbar;

    //Set file format extensions can be read by FileChooser
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
        cProgress.setVisible(false);


    }
    // Set images folder path and read images from the path
    public void uploadClick(){
        // read images folder path
        FileChooser fc2 = new FileChooser();
        FileChooser.ExtensionFilter addextension = new FileChooser.ExtensionFilter("image", "*.png", "*.jpg");
        fc2.getExtensionFilters().add(addextension);
        fileList = fc2.showOpenMultipleDialog(newWindow);

        //Initiate showIMG class and show images in the Javafx GUI
        showIMG show = new showIMG(fileList, grid, preview, singleView, mode, sbar, sblevel, filterLabel);
        show.preshow();
        show.getshow(uploadLabel);

    }

    // when convert button is clicked, convertClick function will be called.
    public void convertClick(){
        // read selected format(jpg, png ...)
        fileFormat = (String)format.getSelectionModel().getSelectedItem();
        if(checkfile()){
            if(checkPath()){
                cProgress.setVisible(true);
                CovertIMG covertIMG = new CovertIMG(fileList, savePath, fileFormat, mode, sbar, cProgress);
                covertIMG.start();
            }
        }

    }


    //check if files have been uploaded already, before starting to covert imagtes.
    public boolean checkfile(){
        if(fileList.size() == 0){
            AlertBox uploadAlert = new UploadAlertBox();
            uploadAlert.display();
            return false;
        }
        return true;
    }

    //check if the save path has been set already, before starting to covert images.
    public boolean checkPath(){
        if(savePath == null || savePath.length() == 0){
            convertLabel.setVisible(true);
            convertLabel.setText("Please select save path first !");
            AlertBox pathAlert = new SaveAlertBox();
            pathAlert.display();
            return false;
        }
        return true;
    }

    // set save path for converted images.
    public void setSavePath(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File savePathFile = directoryChooser.showDialog(newWindow);
        savePath = savePathFile.toURI().toString();
        savePath = savePath.substring(6);
        showPath.setText(savePath);
        convertLabel.setVisible(false);
    }
}
