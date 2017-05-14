package Model;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by tom on 14-5-2017.
 */
public class ConnectionHandler extends Thread{
    //TODO verbeteren van code meer comments erbij en duidelijker schrijven
    private SerialPort serialPort;
    private OutputStream outputStream;
    private InputStream inputStream;
    public ConnectionHandler(SerialPort serialPort){
        this.serialPort = serialPort;
        outputStream = serialPort.getOutputStream();
        inputStream = serialPort.getInputStream();
    }

    @Override
    public void run() {
        String text = "";
        DataDaoImpl dataDao = new DataDaoImpl();
        StringTokenizer stringTokenizer = new StringTokenizer(text);
        try {
            do {
                text = text + ((char) inputStream.read());
            } while (!text.contains("/n"));
            int devices = Integer.parseInt((String) stringTokenizer.nextElement());
            Data.getInstance().getPatients().clear();
            for (int index = 0; index < devices; index++) {
                dataDao.addNewPatient(new Patient(index, Integer.toString(index)));
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        while (serialPort.isOpen()){
            try {
                do {
                    text = text + ((char) inputStream.read());
                } while (!text.contains("/n"));
                int idWristband = Integer.parseInt((String) stringTokenizer.nextElement());
                DateFormat timeFormat = new SimpleDateFormat("MM:dd:HH:mm:ss");
                Date date = null;
                date = timeFormat.parse((String) stringTokenizer.nextElement());
                int heartbeat = Integer.parseInt((String) stringTokenizer.nextElement());
                dataDao.addNewPatientHeartRateData(idWristband, new HeartRate(date, heartbeat));
            }catch (IOException e){
                e.printStackTrace();
            }catch (ParseException e){
                e.printStackTrace();
            }
        }
    }
}
