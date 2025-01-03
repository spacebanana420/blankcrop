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
    global.PALETTE_GENERATE = cli.doGenPalette(args);
    
    boolean issuedAnyCommands = global.CROP_IMAGE || global.PALETTE_CONVERT || global.PALETTE_GENERATE;
    if (!issuedAnyCommands) {help.printNoCommands(); System.exit(1);}
    
    var files = cli.getInputFiles(args);
    if (files.size() == 0) {help.printNoImages(); System.exit(2);}
    if (!global.PALETTE_GENERATE) {
      for (String file : files) {encodeImage(file, args);}
    }
    else {
      for (String file : files) {generatePalette(file, args);}
    }
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
    if (global.PALETTE_CONVERT) {image = changePalette(image, args);}
    
    String newname = misc.getFilename(args, path, false);
    boolean result = pngio.writeImage(image, newname);

    if (result) {stdout.print("Converted image " + path);}
    else {stdout.print("Error: failed to create converted image at path " + newname);}
  }
  
  static void generatePalette(String path, String[] args) {
    var image = pngio.openPalette(path);
    if (image == null) {
      stdout.print("The image " + path + " cannot be read or is an invalid PNG image! Skipping.");
      return;
    }
    String palette_data = palette.generatePalette(image);
    String newname = misc.getFilename(args, path, true);
    boolean result = pngio.writePalette(palette_data, newname);
    if (result) {stdout.print("Generated palette file " + newname + " from " + path);}
    else {stdout.print("Error: failed to generate palette file from file " + path);}
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
  
  static BufferedRgbaImage changePalette(BufferedRgbaImage image, String[] args) {
    String[] palette_files = cli.getPaletteFiles(args);
    boolean approximate = cli.approximateMatch(args);
    if (palette_files[0] == null || palette_files[1] == null) {
      stdout.print("You must specify 2 .plt files to convert the image's color palette!");
      return image;
    }
    return palette.convertImage(image, palette_files[0], palette_files[1], approximate);
  }
}

class misc {
  static String getFilename(String[] args, String input_file, boolean isPalette) {
    String custom_filename = cli.getOutputFile(args);
    String extension; if (isPalette) {extension = ".plt";} else {extension = "-blankcrop.png";} 
  
    if (custom_filename != null) {return custom_filename;}
    else if (global.OVERWRITE_IMAGE) {return input_file;}
    else {return removeExtension(input_file) + extension;}
  }

  static String removeExtension(String path) {
    String newname = "";
    int nameEnd_i = -1;

    for (int i = path.length()-1; i >= 0; i--) {
      if (path.charAt(i) == '.') {nameEnd_i = i; break;}
    }
    if (nameEnd_i == -1) {return path;}
    for (int i = 0; i < nameEnd_i; i++) {newname += path.charAt(i);}
    return newname;
  }
}
