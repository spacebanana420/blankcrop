import blankcrop.pngio;

import io.nayuki.png.image.BufferedRgbaImage;


public class readwidth {
  public static void main(String[] args) {
    var imagedata = pngio.openImage("kogger.png");
    System.out.println(imagedata.getWidth());
  }
}
