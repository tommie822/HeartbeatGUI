package Model;

import java.io.Serializable;
import java.util.Date;

public class HeartRate implements Serializable{
    private Date date;
    private int heartBeat;
    HeartRate(Date date, int heartBeat){
        this.date = date;
        this.heartBeat = heartBeat;
    }

    public Date getDate() {
        return date;
    }

    public int getHeartBeat() {
        return heartBeat;
    }

}
