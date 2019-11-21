package sample;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CovertIMG extends Thread{
    List<File> fileList;
    List<File> newfileList;
    String savePath;
    String fileFormat;
    public CovertIMG(List<File> fileList, String savePath, String fileFormat){
        this.fileList = fileList;
        this.savePath = savePath;
        this. fileFormat = fileFormat;
    }
    public void run(){
        if(fileList.size() == 0){
            System.out.println("Please upload your images");

        }else {

            int count = 0;
            for (File file : fileList) {
                String path = file.toURI().toString();
                path = path.substring(6);
                Mat source = Imgcodecs.imread(path);
                Mat destination = new Mat();
                Imgproc.cvtColor(source, destination, Imgproc.COLOR_RGB2GRAY);
                count++;
                Imgcodecs.imwrite(savePath + count + "." + fileFormat, destination);
            }
        }
    }
}
