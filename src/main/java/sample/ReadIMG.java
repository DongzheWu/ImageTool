package sample;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.jpeg.JpegDirectory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ReadIMG{
    File file;
    Map<String, String> info;
    public ReadIMG(File file){
        this.file = file;
        info = new HashMap<>();
    }

    public Map<String, String> getInfoIMG(){
        try{
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            FileSystemDirectory d5 = metadata.getFirstDirectoryOfType(FileSystemDirectory.class);
            System.out.println(d5.getString(FileSystemDirectory.TAG_FILE_NAME));
            System.out.println(d5.getString(FileSystemDirectory.TAG_FILE_SIZE));
            String name = d5.getString(FileSystemDirectory.TAG_FILE_NAME);
            info.put("name", name);
            String size = d5.getString(FileSystemDirectory.TAG_FILE_SIZE);
            info.put("size", size);

            ExifSubIFDDirectory d1 = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            System.out.println(d1.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
            String date = d1.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            info.put("date", date);

            ExifIFD0Directory d2 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            System.out.println(d2.getString(ExifIFD0Directory.TAG_MAKE));
            System.out.println(d2.getString(ExifIFD0Directory.TAG_MODEL));

            String make = d2.getString(ExifIFD0Directory.TAG_MAKE);
            info.put("make", make);
            String model = d2.getString(ExifIFD0Directory.TAG_MODEL);
            info.put("model", model);

            GpsDirectory d3 = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            System.out.println(d3.getString(GpsDirectory.TAG_LATITUDE));
            System.out.println(d3.getString(GpsDirectory.TAG_LONGITUDE));

            String latitude = d3.getString(GpsDirectory.TAG_LATITUDE);
            String longtitude = d3.getString(GpsDirectory.TAG_LONGITUDE);
            info.put("latitude", latitude);
            info.put("longtitude", longtitude);

            JpegDirectory d4 = metadata.getFirstDirectoryOfType(JpegDirectory.class);


            System.out.println("height: " + d4.getInteger(JpegDirectory.TAG_IMAGE_HEIGHT));
            String width = d4.getString(JpegDirectory.TAG_IMAGE_WIDTH);
            String height = d4.getString(JpegDirectory.TAG_IMAGE_HEIGHT);

            info.put("width", width);
            info.put("height", height);



//            for (Directory directory : metadata.getDirectories()) {
//
//                for (Tag tag : directory.getTags()) {
//                    System.out.println(tag);
//                }
//            }
        }catch (Exception e){
            System.out.println("Cannot read this image!");
        }
        return info;




    }
}
