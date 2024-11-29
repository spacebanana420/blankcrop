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
    );
  }
  public static void printHelp_small() {
    System.out.println("No PNG images specified!\nRun \"blankcrop -h\" for more information");
  }
}
