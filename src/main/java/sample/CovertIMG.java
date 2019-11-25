package sample;

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
    public CovertIMG(List<File> fileList, String savePath, String fileFormat, String modeFormat){
        this.fileList = fileList;
        this.savePath = savePath;
        this. fileFormat = fileFormat;
        this.modeFormat = modeFormat;
        destination = new Mat();
    }
    public void run(){
        if(fileList.size() == 0){
            System.out.println("Please upload your images");

        }else {
            switch (modeFormat){
                case "Gray": changeGray();
                case "Line": changeLine();

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
            double kernel_size = 3;
            Size size = new Size(3, 3);
//            Imgproc.GaussianBlur(source, destination, size,0);
            int low_threshold = 50;
            int high_threshold = 200;
            Imgproc.cvtColor(source, destination, Imgproc.COLOR_RGB2GRAY);
            Imgproc.Canny(source, destination, low_threshold, high_threshold);
            Imgcodecs.imwrite(savePath + count + "." + fileFormat, destination);

        }
    }


}
