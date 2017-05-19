package Util;

import Controller.HomePageController;
import Model.DataDaoImpl;
import Model.HeartRate;
import Model.Patient;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by tom on 19-5-2017.
 */
public class Connection {
    private static Connection instance = new Connection();
    private List<HomePageController> listeners = new ArrayList<>();
    private DataDaoImpl dataDao = DataDaoImpl.getInstance();

    private Connection(){

    }

    public static Connection getInstance() {
        return instance;
    }

    public void addListener(HomePageController add){
        listeners.add(add);
    }

    public void addPatientData(String data){
        try {
            StringTokenizer stringTokenizer;
            /**Will import all the data from the file and will put it in the correct patient*/
            boolean hasPatient = false;
            stringTokenizer = new StringTokenizer(data);
            int idWristband = Integer.parseInt((String) stringTokenizer.nextElement());
            for(Patient patient : dataDao.getAllPatients()){
                if(patient.getIdWristband() == idWristband){
                    DateFormat timeFormat = new SimpleDateFormat("MM:dd:HH:mm:ss");
                    Date date = null;
                    date = timeFormat.parse((String) stringTokenizer.nextElement());
                    int heartbeat = Integer.parseInt((String) stringTokenizer.nextElement());
                    dataDao.addNewPatientHeartRateData(idWristband,new HeartRate(date, heartbeat));
                    hasPatient = true;
                    break;
                }
            }
            if(!hasPatient) {
                dataDao.addNewPatient(new Patient(idWristband, Integer.toString(idWristband)));
                DateFormat timeFormat = new SimpleDateFormat("MM:dd:HH:mm:ss");
                Date date = null;
                date = timeFormat.parse((String) stringTokenizer.nextElement());
                int heartbeat = Integer.parseInt((String) stringTokenizer.nextElement());
                dataDao.addNewPatientHeartRateData(idWristband,new HeartRate(date, heartbeat));
            }

            for(HomePageController dataListener : listeners){
                dataListener.updateLineChart(idWristband);
                dataListener.updatePatientIDListView();
            }
        }catch (ParseException e){

        }catch (NumberFormatException e){

        }
    }
}
