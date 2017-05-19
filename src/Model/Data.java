package Model;

import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 28-3-2017.
 */
public class Data {
    private static Data instance = new Data();
    private List<Patient> patients = new ArrayList<Patient>();
    private SerialPort connectedPort;
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
        return instance;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setConnectedPort(SerialPort connectedPort) {
        this.connectedPort = connectedPort;
    }

    public SerialPort getConnectedPort() {
        return connectedPort;
    }
}
