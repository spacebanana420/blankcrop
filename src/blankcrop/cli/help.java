package blankcrop.cli;

public class help {
  public static void printHelp() {
    System.out.println
    (
      "==Blankcrop v0.1=="
      + "+\nUsage: blankcrop [options] [png images...]"
      + "\n"
      + "\n==Options=="
      + "\n  * -h (--help) - prints this menu"
      + "\n  * -o (--overwrite) - overwrites cropped image"
      + "\n  * -quiet - disables all printing to standard output"
      + "\n  * -verbose - prints more information on the progam's progress"
      + "\n  * -debug - prints debugging information"
    );
  }
  public static void printHelp_small() {
    System.out.println("No PNG images specified!\nRun \"blankcrop -h\" for more information");
  }
}
