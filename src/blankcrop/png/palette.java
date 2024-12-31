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
  public static BufferedRgbaImage convertImage(BufferedRgbaImage img, String palette1, String palette2, boolean approximate_match) {
    long[] colors1 = readPalette(palette1);
    long[] colors2 = readPalette(palette2);
    if (colors1 == null || colors2 == null) {return img;}
    if (colors1.length > colors2.length) {return img;}
    stdout.print_debug("Palette 1:", colors1);
    stdout.print_debug("Palette 2:", colors2);
    
    int palette_length = colors1.length;
    int width = img.getWidth();
    int height = img.getHeight();
    var newimg = new BufferedRgbaImage(width, height, img.getBitDepths());
    
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++)
      {
        long pixel = img.getPixel(x, y);
        int i;
        if (approximate_match) {i = findMatch_approx(pixel, colors1);}
        else {i = findMatch_exact(pixel, colors1);}
        if (i != -1) {
          stdout.print_debug("Found pixel match: " + pixel);
          pixel = colors2[i];
        }
        newimg.setPixel(x, y, pixel);
      }
    }
    return newimg;
  }
  
  static int findMatch_exact(long pixel, long[] colors) {
    for (int i = 0; i < colors.length; i++)
    {
      if (pixel == colors[i]) {return i;}
    }
    return -1;
  }
  
  static int findMatch_approx(long pixel, long[] colors) {
    if (pixel == 0) {return -1;} //fully transparent pixel
    short[] pixel_color = RGBA.getChannels(pixel);
    float lowest_average = -1;
    int lowest_i = -1;
    
    for (int i = 0; i < colors.length; i++)
    {
      if (pixel == colors[i]) {return i;}
      short[] palette_color = RGBA.getChannels(colors[i]);
      float[] deviations = new float[4];
      for (int c = 0; c < 4; c++) {
        if (pixel_color[c] >= palette_color[c]) {deviations[c] = pixel_color[c]-palette_color[c];}
        else {deviations[c] = palette_color[c]-pixel_color[c];}
      }
      float average = (deviations[0] + deviations[1] + deviations[2] + deviations[3]) / 4;
      boolean match =
        (deviations[0] <= 8 && deviations[1] <= 8 && deviations[2] <= 8 && deviations[3] <= 2)
        && (lowest_average == -1 || average < lowest_average)
      ;
      if (match) {
        lowest_average = average;
        lowest_i = i;
      }
    }
    return lowest_i;
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
  
  public static short[] getChannels(long pixel) {
    return new short[] {(short)(pixel >> 48), (short)(pixel >> 32), (short)(pixel >> 16), (short)pixel};
  }
  
  public long getLong() {
    return ((long)red << 48) + ((long)green << 32) + ((long)blue << 16) + (long)alpha;
  }
  //long toUnsigned(short b) {return b & 255;}
}
