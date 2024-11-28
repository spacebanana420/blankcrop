package blankcrop;

import io.nayuki.png.ImageDecoder;
import io.nayuki.png.PngImage;
import io.nayuki.png.image.BufferedRgbaImage;

import java.io.File;
import java.io.IOException;

public class pngio {
  public static Object openImage(String path) {return openImage(new File(path));}

  public static Object openImage(File path) {
    try {
      var img = PngImage.read(path);
      Object imageData = ImageDecoder.toImage(img);
      return imageData;
    }
    catch (IOException e) {return null;}
  }
}
