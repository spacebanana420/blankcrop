import blankcrop.pngio;
import blankcrop.imgcrop;

import io.nayuki.png.image.BufferedRgbaImage;


public class readwidth {
  public static void main(String[] args) {
    var imagedata = pngio.openImage("kogger.png"); //read valid file
    System.out.println(imagedata.getWidth());
    int[] coords = imgcrop.getCroppedCoordinates(imagedata);
    for (int i : coords) {System.out.println(i);}

    imagedata = pngio.openImage("readwidth.java"); //read invalid file
    System.out.println(imagedata.getWidth());
  }
}
