package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 28-3-2017.
 */
public class Data implements Serializable{
    private static Data instance = null;
    private List<Patient> patients = new ArrayList<Patient>();
    private boolean import1 = false;
    private Data(){

    }

    public void setImport1(boolean import1) {
        this.import1 = import1;
    }

    public boolean isImport1() {
        return import1;
    }

    public static Data getInstance(){
        if(instance == null){
            try {
                String workingDirectory;
                //here, we assign the name of the OS, according to Java, to a variable...
                String OS = (System.getProperty("os.name")).toUpperCase();
                //to determine what the workingDirectory is.
                //if it is some version of Windows
                if (OS.contains("WIN"))
                {
                    //it is simply the location of the "AppData" folder
                    workingDirectory = System.getenv("AppData");
                }
                //Otherwise, we assume Linux or Mac
                else
                {
                    //in either case, we would start in the user's home directory
                    workingDirectory = System.getProperty("user.home");
                    //if we are on a Mac, we are not done, we look for "Application Support"
                    workingDirectory += "/Library/Application Support";
                }
                File file = new File(workingDirectory+"\\data.txt");
                file.createNewFile();
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                instance = (Data) objectInputStream.readObject();
                objectInputStream.close();
                System.out.println("DIT MAG MAAR EEN KEER GEDAAN WORDEN");
            }catch (ClassNotFoundException | IOException e){
                instance = new Data();
                e.printStackTrace();
            }
        }
        return instance;
    }

    public List<Patient> getPatients() {
        return patients;
    }
}
