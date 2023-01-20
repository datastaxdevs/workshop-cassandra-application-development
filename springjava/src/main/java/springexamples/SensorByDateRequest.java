package springexamples;

import java.time.LocalDate;

public class SensorByDateRequest {
    private String sensor;
    private LocalDate date;
    
    public String getSensor() {
        return sensor;
    }
    
    public void setSensor(String sensor) {
        this.sensor = sensor;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }    
}
