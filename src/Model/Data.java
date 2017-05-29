package Model;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Data implements Serializable{
    private static Data instance = null;
    private List<Patient> patients = new ArrayList<Patient>();

    private Data(){    }

    static Data getInstance(){
        if(instance == null){
            try {
                String ApplicationDataFolderPath = getLocalApplicationDataFolderPath();
                instance = getSavedDataFrom(ApplicationDataFolderPath);
            }catch (ClassNotFoundException | IOException e){
                //If there isn't any saved data, an exception will be throwed so then a fresh Data object will be made.
                instance = new Data();
                e.printStackTrace();
            }
        }
        return instance;
    }

    @NotNull
    private static String getLocalApplicationDataFolderPath(){
        String workingDirectory;
        String OS = (System.getProperty("os.name")).toUpperCase();
        if (OS.contains("WIN")){
            workingDirectory = System.getenv("AppData");
        }
        else{
            workingDirectory = System.getProperty("user.home");
            workingDirectory += "/Library/Application Support";
        }
        return workingDirectory+"\\data.txt";
    }

    private static Data getSavedDataFrom(String folderPathForSavedData) throws ClassNotFoundException, IOException{
        File savedDataFile = new File(folderPathForSavedData);
        FileInputStream fileInputStream = new FileInputStream(savedDataFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Data savedData = (Data) objectInputStream.readObject();
        objectInputStream.close();
        return savedData;
    }

    public static void saveStateOfDataInstance(){
        try{
            String dataFolderPath = getLocalApplicationDataFolderPath();
            saveInstanceInto(dataFolderPath);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void saveInstanceInto(String folderPath) throws IOException{
        File file = new File(folderPath);
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(Data.getInstance());
        objectOutputStream.close();
    }

    List<Patient> getPatients() {
        return patients;
    }
}
