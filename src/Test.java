import java.io.IOException;
import java.io.OutputStream;
import com.fazecast.jSerialComm.SerialPort;

/**
 * Created by ttigg on 13/06/2017.
 */
public class Test {
    public static void main(String[] args){
        SerialPort comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
        OutputStream output = comPort.getOutputStream();
        try
        {
            for (int j = 0; j < 1000; ++j){
                output.write('z');
                Thread.sleep(3000);
            }
        } catch (Exception e) { e.printStackTrace(); }
        comPort.closePort();
    }

}
