package blankcrop.png;

import io.nayuki.png.ImageDecoder;
import io.nayuki.png.ImageEncoder;
import io.nayuki.png.PngImage;
import io.nayuki.png.image.BufferedRgbaImage;
import io.nayuki.png.image.RgbaImage;

public class imgcrop {
  public static int[] getCroppedCoordinates(BufferedRgbaImage img) {
    int width = img.getWidth();
    int height = img.getHeight();
    int[] coordinates = new int[]{0, width-1, 0, height-1}; //min x, max x, min y, max y
    
    int currentValue = width;

    //Find start X coordinate
    for (int line_y = 0; line_y < height; line_y++) {
      currentValue = lowestX(img, line_y, currentValue);
    }
    if (currentValue != width) {coordinates[0] = currentValue;}

    //Find end X coordinate
    currentValue = 0;
    for (int line_y = 0; line_y < height; line_y++) {
      currentValue = highestX(img, width, line_y, currentValue);
    }
    if (currentValue != 0) {coordinates[1] = currentValue;}

    //Find start Y coordinate
    currentValue = height;
    for (int line_x = 0; line_x < width; line_x++) {
      currentValue = highestY(img, line_x, currentValue);
    }
    if (currentValue != height) {coordinates[2] = currentValue;}

    //Find end Y coordinate
    currentValue = 0;
    for (int line_x = 0; line_x < width; line_x++) {
      currentValue = lowestY(img, height, line_x, currentValue);
    }
    if (currentValue != 0) {coordinates[3] = currentValue;}

    return coordinates;
  }

  private static int lowestX(BufferedRgbaImage img, int line_y, int currentLowest) {
    for (int line_x = 0; line_x < currentLowest; line_x++) {
      long pixel = img.getPixel(line_x, line_y);
      if (pixel != 0) {return line_x;}
    }
    return currentLowest;
  }

  private static int highestX(BufferedRgbaImage img, int width, int line_y, int currentHighest) {
    for (int line_x = width-1; line_x > currentHighest; line_x--) {
      long pixel = img.getPixel(line_x, line_y);
      if (pixel != 0) {return line_x;}
    }
    return currentHighest;
  }

  private static int highestY(BufferedRgbaImage img, int line_x, int currentHighest) {
    for (int line_y = 0; line_y < currentHighest; line_y++) {
      long pixel = img.getPixel(line_x, line_y);
      if (pixel != 0) {return line_y;}
    }
    return currentHighest;
  }


  private static int lowestY(BufferedRgbaImage img, int height, int line_x, int currentLowest) {
    for (int line_y = height-1; line_y > currentLowest; line_y--) {
      long pixel = img.getPixel(line_x, line_y);
      if (pixel != 0) {return line_y;}
    }
    return currentLowest;
  }
}
