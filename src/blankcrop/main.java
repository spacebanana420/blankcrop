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
    if (bitDepths[3] == 0) {
      stdout.print_verbose("Image " + path + " does not have an alpha channel. SKipping.");
      return;
    }
    
    if (cli.walfasMode(args)) {
      stdout.print_verbose("Walfa mode is enabled, images are cropped horizontally from the left by 100 pixels");
      int[] preCrop = new int[]{100, image.getWidth()-1, 0, image.getWidth()-1}; //skip the UI elements on the left for Walfas
      image = pngio.createCroppedImage(image, preCrop, image.getBitDepths());
    }
    
    int[] coordinates = imgcrop.getCroppedCoordinates(image); 
    if (coordinates[0] == 0 && coordinates[1] == image.getWidth()-1 && coordinates[2] == 0 && coordinates[3] == image.getHeight()-1) {
      stdout.print_verbose("Image " + path + " does not have redundant transparent pixels. Skipping.");
      return;
    }
    
    var croppedImage = pngio.createCroppedImage(image, coordinates, bitDepths);
    pngio.writeImage(croppedImage, "Autocropped-"+path); //improve filename generation
    stdout.print("Cropped image " + path);
  }
}
