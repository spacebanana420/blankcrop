package blankcrop;

import blankcrop.cli.cli;
import blankcrop.cli.help;
import java.util.ArrayList;

public class main {
  public static int main(String[] args) {
    if (cli.askedForHelp(args)) {help.printHelp(); return 0;}

    var files = cli.getInputFiles(args);
    if (files.size() == 0) {help.printHelp_small(); return 1;}
    global.VERBOSITY_LEVEL = cli.getVerbosityLevel(args);
    global.OVERWRITE_IMAGE = cli.overwriteImage(args);
    for (String file : files) {encodeImage(file);}
    
    return 0;
  }

  static void encodeImage(String path) {
    var image = pngio.openImage(path);
    if (image == null) {
      stdout.print("The image " + path + " cannot be read or is an invalid PNG image! Skipping.");
      return;
    }
    int[] coordinates = imgcrop.getCroppedCoordinates(image); 
    if (coordinates[0] == 0 && coordinates[1] == image.getWidth()-1 && coordinates[2] == 0 && coordinates[3] == image.getHeight()-1) {
      stdout.print_verbose("Image " + path + " does not have redundant transparent pixels. Skipping.");
      return;
    }
  
    var croppedImage = pngio.createCroppedImage(image, coordinates, image.getBitDepths()); //todo: safecheck bit depths
    pngio.writeImage(croppedImage, "Autocropped-"+path); //improve filename generation
  }
}
