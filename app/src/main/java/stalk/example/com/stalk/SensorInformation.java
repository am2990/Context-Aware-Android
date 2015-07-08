package stalk.example.com.stalk;

/**
 * Created by Srishti Sengupta on 7/8/2015.
 */
public class SensorInformation{
    //sensor information model
    private long id;
    private String sensorName;
    private String sensorValue;
    private String location;

    public SensorInformation(long id, String sensorName, String sensorValue, String location) {
        this.id = id;
        this.sensorName = sensorName;
        this.sensorValue = sensorValue;
        this.location = location;
    }

    public SensorInformation() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(String sensorValue) {
        this.sensorValue = sensorValue;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
