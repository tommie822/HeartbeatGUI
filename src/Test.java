import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

/**
 * Created by ttigg on 18/05/2017.
 */
public class Test {
    public static void main(String[] args){
            SerialPort[] serialPorts = SerialPort.getCommPorts();
            for(SerialPort serialPort : serialPorts){
                System.out.println(serialPort.getDescriptivePortName());
                if(serialPort.getSystemPortName().equals("COM3")){
                    System.out.println(serialPort.getSystemPortName());
                    serialPort.openPort();
                    serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
                    String string = "";
                    try {
                        char c = (char) serialPort.getInputStream().read();
                        while (c != 0) {
                            string = string + (c);
                            c = (char) serialPort.getInputStream().read();

                        }
                    }catch (IOException e){

                    }
                    System.out.println("String: "+string);
                }
            }
    }
}
