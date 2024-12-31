package blankcrop;

import blankcrop.cli.cli;
import blankcrop.cli.help;
import blankcrop.png.pngio;
import blankcrop.png.imgcrop;
import blankcrop.png.palette;

import io.nayuki.png.image.BufferedRgbaImage;
import java.util.ArrayList;

public class main {  
  public static void main(String[] args) {
    if (cli.askedForHelp(args)) {help.printHelp(); System.exit(0);}

    
    global.VERBOSITY_LEVEL = cli.getVerbosityLevel(args);
    global.OVERWRITE_IMAGE = cli.overwriteImage(args);
    global.CROP_IMAGE = cli.doCrop(args);
    global.WALFAS_MODE = cli.walfasMode(args);
    global.PALETTE_CONVERT = cli.doPalette(args);
    
    var files = cli.getInputFiles(args);
    boolean issuedAnyCommands = global.CROP_IMAGE || global.PALETTE_CONVERT;
    if (!issuedAnyCommands || files.size() == 0) {help.printHelp_small(); System.exit(1);}
    
    for (String file : files) {encodeImage(file, args);}
  }

  static void encodeImage(String path, String[] args) {
    var image = pngio.openImage(path);
    if (image == null) {
      stdout.print("The image " + path + " cannot be read or is an invalid PNG image! Skipping.");
      return;
    }
    
    int[] bitDepths = image.getBitDepths();
    if (bitDepths[3] == 0) {
      stdout.print_verbose("Image " + path + " does not have an alpha channel. Skipping.");
      return;
    }
    if (global.CROP_IMAGE) {image = cropImage(image, path);}
    if (global.PALETTE_CONVERT) {changePalette(image, args);}
    
    String newname = misc.getFilename(args, path);
    boolean result = pngio.writeImage(image, newname);

    if (result) {stdout.print("Converted image " + path);}
    else {stdout.print("Error: failed to create converted image at path " + newname);}
  }
  
  static BufferedRgbaImage cropImage(BufferedRgbaImage image, String path) {
    int width = image.getWidth();
    int height = image.getHeight();
    int[] bitDepths = image.getBitDepths();
    if (global.WALFAS_MODE)
    {
      stdout.print_verbose("Walfa mode is enabled, images are cropped horizontally from the left by 355 pixels");
      int[] preCrop = new int[]{355, width-1, 0, height-1}; //skip the UI elements on the left for Walfas
      image = pngio.createCroppedImage(image, preCrop, image.getBitDepths());
      width = image.getWidth();
      height = image.getHeight();
    }
    
    int[] coordinates = imgcrop.getCroppedCoordinates(image); 
    if (coordinates[0] == 0 && coordinates[1] == width-1 && coordinates[2] == 0 && coordinates[3] == height-1)
    {
      stdout.print_verbose("Image " + path + " does not have redundant transparent pixels. Skipping.");
      return image;
    }
    return pngio.createCroppedImage(image, coordinates, bitDepths);
  }
  
  static void changePalette(BufferedRgbaImage image, String[] args) {
    String[] palette_files = cli.getPaletteFiles(args);
    if (palette_files[0] == null || palette_files[1] == null) {return;}
    palette.convertImage(image, palette_files[1], palette_files[2]);
  }
}

class misc {
  static String getFilename(String[] args, String input_file) {
    String custom_filename = cli.getOutputFile(args);
  
    if (custom_filename != null) {return custom_filename;}
    else if (global.OVERWRITE_IMAGE) {return input_file;}
    else {return removeExtension(input_file) + "-autocropped.png";}
  }

  static String removeExtension(String path) {
    String newname = "";
    int nameEnd_i = -1;

    for (int i = path.length(); i >= 0; i++) {
      if (path.charAt(i) == '.') {nameEnd_i = i; break;}
    }
    if (nameEnd_i == -1) {return path;}
    for (int i = 0; i < nameEnd_i; i++) {newname += path.charAt(i);}
    return newname;
  }
}
