package Model;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Data implements Serializable{
    private static Data instance = null;
    private List<Patient> patients = new ArrayList<Patient>();
    private boolean import1 = false;

    private Data(){    }

    public static Data getInstance(){
        if(instance == null){
            try {
                String dataFolderPath = getLocalApplicationDataFolderPath();
                loadSavedDataIntoInstance(dataFolderPath);
            }catch (ClassNotFoundException | IOException e){
                //If there isn't any saved data, an exception will be throwed so then a fress Data object will be made.
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

    private static void loadSavedDataIntoInstance(String folderPathForSavedData) throws ClassNotFoundException, IOException{
        File savedData = new File(folderPathForSavedData);
        FileInputStream fileInputStream = new FileInputStream(savedData);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        instance = (Data) objectInputStream.readObject();
        objectInputStream.close();
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

    public void setImport1(boolean import1) {
        this.import1 = import1;
    }

    public boolean isImport1() {
        return import1;
    }
}
