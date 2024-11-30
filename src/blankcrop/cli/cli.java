package blankcrop.cli;

import java.util.ArrayList;
import java.io.File;

import blankcrop.stdout;

public class cli {

  public static ArrayList<String> getInputFiles(String[] args) {
    var files = new ArrayList<String>();
    for (String a : args)
    {
      var f = new File(a);
      if (!isArgument(a) && f.isFile() && f.canRead() && isPNGFile(a)) {files.add(a);}  
    }
    return files;
  }

  public static boolean askedForHelp(String[] args) {return hasArgument(args, "-h") || hasArgument(args, "--help");}

  public static byte getVerbosityLevel(String[] args) {
    if (hasArgument(args, "-quiet")) {return 0;}
    else if (hasArgument(args, "-verbose")) {return 2;}
    else if (hasArgument(args, "-debug")) {return 3;}
    else return 1;    
  }

  public static boolean overwriteImage(String[] args) {return hasArgument(args, "-o") || hasArgument(args, "--overwrite");}

  public static boolean walfasMode(String[] args) {return hasArgument(args, "-walfa");}

  private static boolean hasArgument(String[] args, String argument) {return findArgument(args, argument) != -1;}
  
  private static int findArgument(String[] args, String argument) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals(argument)) {return i;}
    }
    return -1;
  }

  private static boolean isArgument(String arg) {return arg != null && arg.length() > 1 && arg.charAt(0) == '-';}

  private static boolean isPNGFile(String path) {
    if (path == null || path.length() <= 4) {return false;} // the 4 characters stand for ".png"
    for (int i = 0; i < 4; i++)
    {
      char c_png = ".png".charAt(3-i);
      char c_path = path.charAt(path.length()-1-i);
      if (c_png != c_path) {return false;}
    }
    return true;
  }
}
