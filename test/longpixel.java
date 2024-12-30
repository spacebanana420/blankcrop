import blankcrop.png.pngio;
import blankcrop.png.imgcrop;

import io.nayuki.png.image.BufferedRgbaImage;


public class longpixel {
  public static void main(String[] args) {
    var imagedata = pngio.openImage("kogger.png");
    long pixel = imagedata.getPixel(200, 300);
    System.out.println(pixel);
    System.out.println("Actual RGBA value: 106 168 174 255");
    byte[] converted = pixelToBytes(pixel);
    for (byte channel : converted) {System.out.println(channel);}
    long reconverted = pixelToLong(converted);
    System.out.println(reconverted);
  }
  
  public static byte[] pixelToBytes(long num) {
    return new byte[] {(byte)(num >> 48), (byte)(num >> 32), (byte)(num >> 16), (byte)num};
  }
  public static long pixelToLong(byte[] pixel) {
    long[] unsigned = new long[]{toUnsigned(pixel[0]), toUnsigned(pixel[1]), toUnsigned(pixel[2]), toUnsigned(pixel[3])};
    long value = (unsigned[0] << 48) + (unsigned[1] << 32) + (unsigned[2] << 16) + unsigned[3];
    return value;
  }
  
  public static long toUnsigned(byte b) {return b & 255;}
}
