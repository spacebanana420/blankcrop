package blankcrop.cli;

public class help {
  public static void printHelp() {
    System.out.println
    (
      "==Blankcrop v0.2=="
      + "\nUsage: blankcrop [options] [png images...]"
      + "\n"
      + "\n==Commands=="
      + "\n  * crop - crops the image"
      + "\n  * palette - changes the color palette of the image (requires passing 2 .plt files as arguments)"
      + "\n  * generate-palette - generates a .plt file based on the pixels of an image"
      + "\n"
      + "\n==Options=="
      + "\n  * -h (--help) - prints this menu"
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
  public static void printNoCommands() {
    System.out.println("No commands specified!\nRun \"blankcrop -h\" for more information");
  }
  public static void printNoImages() {
    System.out.println("No PNG images specified!\nRun \"blankcrop -h\" for more information");
  }
}
