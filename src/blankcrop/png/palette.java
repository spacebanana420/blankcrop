package blankcrop.png;

import io.nayuki.png.ImageDecoder;
import io.nayuki.png.ImageEncoder;
import io.nayuki.png.PngImage;
import io.nayuki.png.image.BufferedRgbaImage;
import io.nayuki.png.image.RgbaImage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class palette {
  public static void convertImage(BufferedRgbaImage img, String palette1, String palette2) {
    long[] colors1 = readPalette(palette1);
    long[] colors2 = readPalette(palette2);
    if (colors1 == null || colors2 == null) {return;}
    if (colors1.length > colors2.length) {return;}
    
    int palette_length = colors1.length;
    int width = img.getWidth();
    int height = img.getHeight();
    
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++)
      {
        long pixel = img.getPixel(x, y);
        for (int i = 0; i < palette_length; i++) {if (pixel == colors1[i]) {pixel = colors2[i];}}
        img.setPixel(x, y, pixel);
      }
    }
  }
  
  static long[] readPalette(String path) {
    var file = new File(path);
    if (!file.isFile() || !file.canRead()) {return null;}
    
    String data = readFile(path);
    if (data == null) {return null;}
    
    String[] rgb_str = new String[4];
    int rgb_i = 0;
    var colors = new ArrayList<RGBA>();
    for (int i = 0; i < data.length(); i++)
    {
      char c = data.charAt(i);
      if (c == '\n' || rgb_i > 3) {
        colors.add(new RGBA(rgb_str));
        rgb_str = new String[4];
        rgb_i = 0;
      }
      else if (c == ' ') {rgb_i++;}
      else {rgb_str[rgb_i] += c;}
    }
    
    long[] palette_colors = new long[colors.size()];
    for (int i = 0; i < palette_colors.length; i++) {
      byte[] pixel = colors.get(i).getChannels();
      palette_colors[i] = pixelToLong(pixel);
    }
    return palette_colors;
  }
  
  static long pixelToLong(byte[] pixel) {
    long[] unsigned = new long[]{toUnsigned(pixel[0]), toUnsigned(pixel[1]), toUnsigned(pixel[2]), toUnsigned(pixel[3])};
    long value = (unsigned[0] << 48) + (unsigned[1] << 32) + (unsigned[2] << 16) + unsigned[3];
    return value;
  }
  
  static long toUnsigned(byte b) {return b & 255;}
  
  static String readFile(String path) {
    try {
      return new String(Files.readAllBytes(Path.of(path)));
    }
    catch (IOException e) {return null;}
  }
}

class RGBA {
  byte red = 0;
  byte green = 0;
  byte blue = 0;
  byte alpha = (byte)255;
  
  public RGBA(byte[] channels) {
    if (channels.length >= 4) {
      red = channels[0];
      green = channels[1];
      blue = channels[2];
      alpha = channels[3];
    }
  }
  public RGBA(String[] channels) {
    if (channels.length < 4) {return;}
    byte[] rgba = new byte[4];
    for (int i = 0; i < 4; i++) {
      try {rgba[i] = Byte.parseByte(channels[i]);}
      catch (NumberFormatException e) {rgba[i] = 0;} 
    }
    red = rgba[0];
    green = rgba[1];
    blue = rgba[2];
    alpha = rgba[3];
  }
  
  public byte[] getChannels() {return new byte[]{red, green, blue, alpha};}
}
