package blankcrop.cli;

public class help {
  public static void printHelp() {
    System.out.println
    (
      "==Blankcrop v1.0=="
      + "\nUsage: blankcrop [options] [png images...]"
      + "\n"
      + "\n==Options=="
      + "\n  * -h (--help) - prints this menu"
      + "\n  * -crop - Crops the image"
      + "\n  * -palette - Changes the color palette of the image (requires passing 2 .palette files as arguments)"
      + "\n  * -o (--overwrite) - overwrites cropped image"
      + "\n  * -out - sets a manual output PNG filename/path"
      + "\n  * -d [path] (--directory [path]) - crops all PNG images inside a directory (not recursive)"
      + "\n  * -approx - verifies an approximate match to color palettes instead of exact"
      + "\n  * -quiet - disables all printing to standard output"
      + "\n  * -verbose - prints more information on the progam's progress"
      + "\n  * -debug - prints debugging information"
      + "\n  * -walfa - ignore this setting, it suits a specific workflow I need"
    );
  }
  public static void printHelp_small() {
    System.out.println("No PNG images specified!\nRun \"blankcrop -h\" for more information");
  }
}
