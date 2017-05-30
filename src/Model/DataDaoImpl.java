package Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by tom on 11-5-2017.
 */
public class DataDaoImpl extends  AbstractCrudDao implements DataDao {
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
    public void setPatientName(int idWristband, String newName) {
        updatePatientName(new CrudAction() {
            @Override
            public void doAction() {
                for(int i = 0; i < getNumberOfPatients(); i++){
                    if(Data.getInstance().getPatients().get(i).getIdWristband() == idWristband){
                        Data.getInstance().getPatients().get(i).setName(newName);
                    }
                }
            }
        });
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
        newPatient(new CrudAction() {
            @Override
            public void doAction() {
                Data.getInstance().getPatients().add(patient);
            }
        });
    }

    @Override
    public void clearPatients() {
        dataCleared(new CrudAction() {
            @Override
            public void doAction() {
                Data.getInstance().getPatients().clear();
            }
        });
    }

    @Override
    public void addNewPatientHeartRateData(String data) {
        newData(new CrudAction() {
            int idWristband;
            int heartbeat;
            Date date;
            boolean hasPatient;

            @Override
            public void doAction() {
                try {
                    StringTokenizer stringTokenizer;
                    hasPatient = false;
                    stringTokenizer = new StringTokenizer(data);
                    if(stringTokenizer.countTokens() == 3) {
                        setIdWristband(stringTokenizer.nextToken());
                        for (Patient patient : getAllPatients()) {
                            if (patient.getIdWristband() == idWristband) {
                                setHeartbeat(stringTokenizer.nextToken());
                                setDate((String)stringTokenizer.nextElement());
                                getPatientHeartRateList(idWristband).add(new HeartRate(date, heartbeat));
                                hasPatient = true;
                                break;
                            }
                        }
                        if (!hasPatient) {
                            setHeartbeat(stringTokenizer.nextToken());
                            setDate(stringTokenizer.nextToken());
                            addNewPatient(new Patient(idWristband, Integer.toString(idWristband)));
                            getPatientHeartRateList(idWristband).add(new HeartRate(date, heartbeat));
                        }
                    }
                }catch (ParseException | NumberFormatException e){
                    e.printStackTrace();
                }
            }

            private void setIdWristband(String stringIdWristband){
                idWristband = Integer.parseInt(stringIdWristband);
            }

            private void setHeartbeat(String stringHeartbeat){
                heartbeat = Integer.parseInt(stringHeartbeat);
            }

            private void setDate(String stringDate)throws ParseException{
                DateFormat timeFormat = new SimpleDateFormat("MM:dd:yyyy:HH:mm:ss");
                date =  timeFormat.parse(stringDate);
            }
        });
    }
}
