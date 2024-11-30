package blankcrop;

import blankcrop.cli.cli;
import blankcrop.cli.help;
import blankcrop.png.pngio;
import blankcrop.png.imgcrop;

import java.util.ArrayList;

public class main {
  public static void main(String[] args) {
    if (cli.askedForHelp(args)) {help.printHelp(); System.exit(0);}

    
    global.VERBOSITY_LEVEL = cli.getVerbosityLevel(args);
    global.OVERWRITE_IMAGE = cli.overwriteImage(args);
    var files = cli.getInputFiles(args);
    if (files.size() == 0) {help.printHelp_small(); System.exit(1);}
    
    for (String file : files) {encodeImage(file, args);}
  }

  static void encodeImage(String path, String[] args) {
    var image = pngio.openImage(path);
    if (image == null) {
      stdout.print("The image " + path + " cannot be read or is an invalid PNG image! Skipping.");
      return;
    }
    
    int[] bitDepths = image.getBitDepths();
    int width = image.getWidth();
    int height = image.getHeight();
    if (bitDepths[3] == 0) {
      stdout.print_verbose("Image " + path + " does not have an alpha channel. SKipping.");
      return;
    }
    
    if (cli.walfasMode(args)) {
      stdout.print_verbose("Walfa mode is enabled, images are cropped horizontally from the left by 355 pixels");
      int[] preCrop = new int[]{355, width-1, 0, height-1}; //skip the UI elements on the left for Walfas
      image = pngio.createCroppedImage(image, preCrop, image.getBitDepths());
      width = image.getWidth();
      height = image.getHeight();
    }
    
    int[] coordinates = imgcrop.getCroppedCoordinates(image); 
    if (coordinates[0] == 0 && coordinates[1] == width-1 && coordinates[2] == 0 && coordinates[3] == height-1) {
      stdout.print_verbose("Image " + path + " does not have redundant transparent pixels. Skipping.");
      return;
    }
    
    var croppedImage = pngio.createCroppedImage(image, coordinates, bitDepths);
    String newname = (global.OVERWRITE_IMAGE) ? path : removeExtension(path) + "-autocropped.png";
    boolean result = pngio.writeImage(croppedImage, newname); //improve filename generation
    if (result) {stdout.print("Cropped image " + path);}
    else {stdout.print("Error: failed to create cropped image at path " + newname);}
  }

  static String removeExtension(String path) {
    String newname = "";
    boolean copy = false;
    int nameEnd_i = -1;

    for (int i = path.length(); i >= 0; i++) {
      if (path.charAt(i) == '.') {nameEnd_i = i; break;}
    }
    if (nameEnd_i == -1) {return path;}
    for (int i = 0; i < nameEnd_i; i++) {newname += path.charAt(i);}
    return newname;
  }
}
