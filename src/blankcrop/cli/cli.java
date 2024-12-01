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

  public static String getOutputFile(String[] args) {
    String file = getArgumentValue(args, "-out");
    if (file == null) {return null;}
    File f = new File(file);
    if (!f.getName().equals(file) && !new File(f.getParent()).isDirectory()) {return null;}
    return isPNGFile(file) ? file : null;
  }

  private static String getArgumentValue(String[] args, String argument) {
    if (args.length < 2) {return null;}
    int i = findArgument(args, argument);
    if (i == -1 || i == args.length-1) {return null;}
    return args[i+1];
  }

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
