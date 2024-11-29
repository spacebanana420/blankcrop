package blankcrop;

import io.nayuki.png.ImageDecoder;
import io.nayuki.png.ImageEncoder;
import io.nayuki.png.PngImage;
import io.nayuki.png.image.BufferedRgbaImage;
import io.nayuki.png.image.RgbaImage;
import io.nayuki.png.chunk.Ihdr;

import java.io.File;
import java.io.IOException;

public class pngio {
  public static BufferedRgbaImage openImage(String path) {return openImage(new File(path));}

  public static BufferedRgbaImage openImage(File path) {
    if (!path.isFile()) {return null;}
    try {
      var img = PngImage.read(path);
      var imageData = (BufferedRgbaImage) ImageDecoder.toImage(img);
      return imageData;
    }
    catch (IOException e) {return null;}
    catch (IllegalArgumentException e) {return null;}
  }
  

  public static void writeImage(RgbaImage imageData, String path) {writeImage(imageData, new File(path));}

  public static void writeImage(RgbaImage imageData, File path) {
    var img = ImageEncoder.toPng(imageData, Ihdr.InterlaceMethod.NONE);
    
    try {img.write(path);}
    catch (IOException e) {return;}
  }

  public static BufferedRgbaImage createCroppedImage(BufferedRgbaImage original, int[] coordinates, int[] bitDepths) {
    int width = coordinates[1] - coordinates[0];
    int height = coordinates[3] - coordinates[2];
    var croppedImage = new BufferedRgbaImage(width, height, bitDepths);
    
    for (int x = coordinates[0]; x <= coordinates[1]; x++) {
      for (int y = coordinates[2]; y <= coordinates[3]; y++) {
        long pixel = original.getPixel(x, y);
        croppedImage.setPixel(x, y, pixel);    
      }
    }
    return croppedImage;
  }
}
