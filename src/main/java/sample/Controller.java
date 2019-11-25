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
    File currentFile;

    ObservableList<String> formatList = FXCollections.observableArrayList(
            "jpg", "jpeg", "jpe", "png", "bmp", "pbm", "ppm","tiff","tif");

    List<File> fileList = new ArrayList<File>();

    public void initialize(){
        format.setValue("jpg");
        format.setItems(formatList);
        grid = new GridPane();
        scrollPane.setContent(grid);


    }

    public void uploadClick(){
        FileChooser fc2 = new FileChooser();
        FileChooser.ExtensionFilter addextension = new FileChooser.ExtensionFilter("image", "*.png", "*.jpg");
        fc2.getExtensionFilters().add(addextension);
        List<File> fileList = fc2.showOpenMultipleDialog(newWindow);

        showIMG show = new showIMG(fileList, grid, preview);
        show.preshow();
        show.getshow(uploadLabel, singleView);



//        if(list != null){
//            int row = 0;
//            int col = 0;
//            grid.setPadding(new Insets(10, 10, 10, 10));
//
//
//
//            for(File file: list){
//                String path = file.toURI().toString();
//                ReadIMG readimg = new ReadIMG(file);
//                Map<String, String> info = readimg.getInfoIMG();
//                Image img = new Image(path);
//
//
//                ImageView imgView = new ImageView(img);
//                imgView.setFitHeight(100);
//                imgView.setFitWidth(100);//                String[] splits = path.split("/");
//
//
//                VBox vbinside = new VBox();
//                vbinside.setPadding(new Insets(0, 0, 0, 15));
//                fileList.add(file);
//
//
//
//                for(String s: info.keySet()){
//                    Label l = new Label(s + ": " + info.get(s));
//                    vbinside.getChildren().add(l);
//                }
//                HBox hbox = new HBox(imgView, vbinside);
//                hbox.setMargin(imgView, new Insets(3, 3, 3, 3));
//
//                hbox.setStyle("-fx-border-color: gray;" +
//                        "-fx-border-width: 3;" +
//                        "-fx-border-style: solid;");
//
//                GridPane.setConstraints(hbox, col, row);
//                grid.getChildren().add(hbox);
//                col++;
//                if(col == 3){
//                    row++;
//                    col = 0;
//                    }
//                }
//            uploadLabel.setText("Upload finished !");
//        } else{
//            uploadLabel.setText("Something wrong !");
//        }
    }

    public void convertClick(){
        RadioButton selectedRadioButton = (RadioButton) mode.getSelectedToggle();
        modeFormat = selectedRadioButton.getText();
        System.out.println(modeFormat);
        fileFormat = (String)format.getSelectionModel().getSelectedItem();
        if(checkfile()){
            if(checkPath()){
                CovertIMG covertIMG = new CovertIMG(fileList, savePath, fileFormat, modeFormat);
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
