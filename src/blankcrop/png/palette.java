package blankcrop.png;

import blankcrop.stdout;

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
  public static BufferedRgbaImage convertImage(BufferedRgbaImage img, String palette1, String palette2) {
    long[] colors1 = readPalette(palette1);
    long[] colors2 = readPalette(palette2);
    if (colors1 == null || colors2 == null) {return img;}
    if (colors1.length > colors2.length) {return img;}
    stdout.print_debug("Palette 1:", colors1);
    stdout.print_debug("Palette 2:", colors2);
    
    int palette_length = colors1.length;
    int width = img.getWidth();
    int height = img.getHeight();
    int[] bitDepths = img.getBitDepths();
    var newimg = new BufferedRgbaImage(width, height, bitDepths);
    
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++)
      {
        long pixel = img.getPixel(x, y);
        for (int i = 0; i < palette_length; i++) {
          if (pixel == colors1[i]) {
            stdout.print_debug("Found pixel match: " + pixel);
            pixel = colors2[i];
            newimg.setPixel(x, y, pixel);
            break;
          }
        }
      }
    }
    return newimg;
  }
  
  static long[] readPalette(String path) {
    var file = new File(path);
    if (!file.isFile() || !file.canRead()) {return null;}
    
    String data = readFile(path);
    if (data == null) {return null;}
    
    String[] rgb_str = new String[]{"", "", "", ""};
    int rgb_i = 0;
    var colors = new ArrayList<RGBA>();
    for (int i = 0; i < data.length(); i++)
    {
      char c = data.charAt(i);
      if (c == '\n' || rgb_i > 3) {
        colors.add(new RGBA(rgb_str));
        rgb_str = new String[]{"", "", "", ""};
        rgb_i = 0;
      }
      else if (c == ' ') {rgb_i++;}
      else {rgb_str[rgb_i] += c;}
    }
    
    long[] palette_colors = new long[colors.size()];
    for (int i = 0; i < palette_colors.length; i++) {
      palette_colors[i] = colors.get(i).getLong();
    }
    return palette_colors;
  }
  
  static String readFile(String path) {
    try {
      return new String(Files.readAllBytes(Path.of(path)));
    }
    catch (IOException e) {return null;}
  }
}

class RGBA {
  short red = 0;
  short green = 0;
  short blue = 0;
  short alpha = 255;
  
  public RGBA(short[] channels) {
    if (channels.length >= 4) {
      red = channels[0];
      green = channels[1];
      blue = channels[2];
      alpha = channels[3];
    }
  }
  public RGBA(String[] channels) {
    if (channels.length < 4) {return;}
    short[] rgba = new short[4];
    for (int i = 0; i < 4; i++) {
      try {rgba[i] = Short.parseShort(channels[i]);}
      catch (NumberFormatException e) {
        stdout.print_debug("Failed to parse RGB value: " + channels[i]);
        rgba[i] = 0;
      } 
    }
    red = rgba[0];
    green = rgba[1];
    blue = rgba[2];
    alpha = rgba[3];
  }
  
  public short[] getChannels() {return new short[]{red, green, blue, alpha};}
  
  public long getLong() {
    return ((long)red << 48) + ((long)green << 32) + ((long)blue << 16) + (long)alpha;
  }
  
  //long toUnsigned(short b) {return b & 255;}
}
