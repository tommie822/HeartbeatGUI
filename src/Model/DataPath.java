package Model;

import com.sun.istack.internal.NotNull;

/**
 * Created by tom on 1-6-2017.
 */
public class DataPath {
  public static Data data;
  public static DaoImpl dao;

  @NotNull
  public static String getLocalApplicationDataFolderPath() {
    String workingDirectory;
    String OS = (System.getProperty("os.name")).toUpperCase();
    if (OS.contains("WIN")) {
      workingDirectory = System.getenv("AppData");
    } else {
      workingDirectory = System.getProperty("user.home");
      workingDirectory += "/Library/Application Support";
    }
    return workingDirectory + "\\data.txt";
  }
}
