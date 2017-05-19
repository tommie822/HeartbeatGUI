package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 11-5-2017.
 */
public class DataDaoImpl implements DataDao {
    private static final DataDaoImpl instance = new DataDaoImpl();
    private DataDaoImpl(){

    }

    public static DataDaoImpl getInstance() {
        return instance;
    }

    @Override
    public List<HeartRate> getPatientHeartRateList(int idWristband) {
        for(int i = 0; i < getNumberOfPatients(); i++){
            if(Data.getInstance().getPatients().get(i).getIdWristband() == idWristband){
                return Data.getInstance().getPatients().get(i).getHeartRateList();
            }
        }
        return new ArrayList<HeartRate>();
    }

    @Override
    public int getPatientID(int index) {
        return Data.getInstance().getPatients().get(index).getIdWristband();
    }

    @Override
    public List<HeartRate> getPatientHeartRateList(String name) {
        for(int i = 0; i < getNumberOfPatients(); i++){
            if(Data.getInstance().getPatients().get(i).getName().equals(name)){
                return Data.getInstance().getPatients().get(i).getHeartRateList();
            }
        }
        return null;
    }

    @Override
    public String getPatientName(int idWristband) {
        for(int i = 0; i < getNumberOfPatients(); i++){
            if(Data.getInstance().getPatients().get(i).getIdWristband() == idWristband){
                return Data.getInstance().getPatients().get(i).getName();
            }
        }
        return null;
    }

    @Override
    public int getNumberOfPatients() {
        return Data.getInstance().getPatients().size();
    }

    @Override
    public List<Patient> getAllPatients() {
        return Data.getInstance().getPatients();
    }

    @Override
    public void addNewPatient(Patient patient) {
        Data.getInstance().getPatients().add(patient);
    }

    @Override
    public void clearPatients() {
        Data.getInstance().getPatients().clear();
    }

    @Override
    public void addNewPatientHeartRateData(int idWristband, HeartRate heartRate) {
        getPatientHeartRateList(idWristband).add(heartRate);
    }
}
