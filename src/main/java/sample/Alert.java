package sample;

// Alert has two alert types
public interface Alert {
   String getMessage();
}
class UploadAlert implements Alert{
    public String getMessage(){
        return "You haven't uploaded your images yet";
    }
}

class SavePathAlert implements Alert{
    public String getMessage(){
        return "You haven't choose save path yet";
    }
}
