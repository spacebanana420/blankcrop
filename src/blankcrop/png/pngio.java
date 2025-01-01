package blankcrop.png;

import io.nayuki.png.ImageDecoder;
import io.nayuki.png.ImageEncoder;
import io.nayuki.png.PngImage;
import io.nayuki.png.image.BufferedRgbaImage;
import io.nayuki.png.image.RgbaImage;
import io.nayuki.png.chunk.Ihdr;

import java.io.File;
import java.io.FileOutputStream;
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
  

  public static boolean writeImage(RgbaImage imageData, String path) {return writeImage(imageData, new File(path));}

  public static boolean writeImage(RgbaImage imageData, File path) {
    var img = ImageEncoder.toPng(imageData, Ihdr.InterlaceMethod.NONE);
    
    try {img.write(path); return true;}
    catch (IOException e) {return false;}
  }

  public static BufferedRgbaImage createCroppedImage(BufferedRgbaImage original, int[] coordinates, int[] bitDepths) {
    int width = coordinates[1] - coordinates[0];
    int height = coordinates[3] - coordinates[2];
    var croppedImage = new BufferedRgbaImage(width, height, bitDepths);
    
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {      
        long pixel = original.getPixel(coordinates[0]+x, coordinates[2]+y);
        croppedImage.setPixel(x, y, pixel);    
      }
    }
    return croppedImage;
  }
  
  public static boolean writePalette(String data, String path) {
    byte[] data_bytes = data.getBytes();
    try {
      var fo = new FileOutputStream(path);
      fo.write(data_bytes);
      fo.close();
      return true;
    }
    catch (IOException e) {return false;}
  }
}
