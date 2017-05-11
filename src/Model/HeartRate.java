package Model;

import java.util.Date;

/**
 * Created by ttigg on 11/05/2017.
 */
public class HeartRate {
    private Date date;
    private int heartBeat;
    public HeartRate(Date date, int heartBeat){
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
