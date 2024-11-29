package blankcrop;

import blankcrop.cli;
import java.util.ArrayList;

public class main {
  public static void main(String[] args) {
    if (cli.askedForHelp(args)) {help.printHelp(); return;}

    var files = cli.getInputFiles(args);
    if (files.size() == 0) {help.printHelp_small(); return;}
  }

  static void encodeImage(String path) {
    var image = pngio.openImage(path);
    int[] coordinates = imgcrop.getCroppedCoordinates(image); 
    if (coordinates[0] == 0 && coordinates[1] == image.getWidth()-1 && coordinates[2] == 0 && coordinates[3] == image.getHeight()-1) {return;}
    var croppedImage = pngio.createCroppedImage(image, coordinates, image.getBitDepths()); //todo: safecheck bit depths
    pngio.writeImage(croppedImage, "Autocropped-"+path); //improve filename generation
  }
}
