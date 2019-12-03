package sample;

import javafx.scene.control.*;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.List;

public class CovertIMG extends Thread{
    List<File> fileList;
    List<File> newfileList;
    String savePath;
    String fileFormat;
    String modeFormat;
    Mat destination;
    int level;
    Label convertLabel;
    ProgressIndicator cProgress;

    public CovertIMG(List<File> fileList, String savePath, String fileFormat, ToggleGroup mode, ScrollBar sbar, ProgressIndicator cProgress){
        this.fileList = fileList;
        this.savePath = savePath;
        this.fileFormat = fileFormat;
        this.cProgress = cProgress;
        this.convertLabel = convertLabel;
        destination = new Mat();

        RadioButton selectedRadioButton = (RadioButton) mode.getSelectedToggle();
        modeFormat = selectedRadioButton.getText();
        level = (int)sbar.getValue();
    }
    public void run(){
        if(fileList.size() == 0){
            System.out.println("Please upload your images");

        }else {
            switch (modeFormat) {
                case "Gray":
                    changeGray();
                    break;
                case "Line":
                    changeLine();
                    break;
                case "Origin":
                    imgsave();
                    break;
                case "Lomo":
                    changeLomo();
                    break;

            }
        }
    }

    public void changeGray(){
        int count = 0;
        for (File file : fileList) {
            String path = file.toURI().toString();
            path = path.substring(6);
            Mat source = Imgcodecs.imread(path);
            Mat destination = new Mat();
            Imgproc.cvtColor(source, destination, Imgproc.COLOR_RGB2GRAY);
            count++;
            cProgress.setProgress((double)count/(double)fileList.size());
            Imgcodecs.imwrite(savePath + count + "." + fileFormat, destination);
        }
    }

    public void changeLine(){
        int count = 0;
        for (File file : fileList) {
            String path = file.toURI().toString();
            path = path.substring(6);
            Mat source = Imgcodecs.imread(path);

            Mat destination = new Mat();

            Size size = new Size(21, 21);
            Mat temp = new Mat();

            Imgproc.cvtColor(source, temp, Imgproc.COLOR_RGB2GRAY);
            Imgproc.medianBlur(temp, source, 7);
            Mat edge = new Mat();
            Imgproc.Laplacian(source, edge, CvType.CV_8U, 7);
            Imgproc.threshold(edge, destination, level, 255, Imgproc.THRESH_BINARY_INV);
            count++;
            cProgress.setProgress((double)count/(double)fileList.size());

            Imgcodecs.imwrite(savePath + count + "." + fileFormat, destination);
        }
    }

    public void changeLomo(){
        int count = 0;
        for (File file : fileList){
            String path = file.toURI().toString();
            path = path.substring(6);
            Mat source = Imgcodecs.imread(path);

            int rows = source.rows();
            int cols = source.cols();
            Mat destination = new Mat(rows, cols, source.type());
            for(int i = 0; i < rows; i++){
                for(int j = 0; j < cols; j++){
                    double[] data = new double[3];
                    data[0] = level*Math.sqrt(source.get(i, j)[0]);
                    data[1] = source.get(i, j)[1];
                    data[2] = source.get(i, j)[2];

                    destination.put(i, j, data);
                }
            }
            count++;
            cProgress.setProgress((double)count/(double)fileList.size());
            Imgcodecs.imwrite(savePath + count + "." + fileFormat, destination);
        }
    }

    public void imgsave(){
        int count = 0;
        for (File file : fileList) {
            count++;
            cProgress.setProgress((double)count/(double)fileList.size());
            Imgcodecs.imwrite(savePath + count + "." + fileFormat, destination);
        }
    }


}
