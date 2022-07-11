package springexamples;

import java.math.BigDecimal;
import java.util.Map;

public class SensorByNetworkResponse {

	private String network;
	private String sensor;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private Map<String,String> characteristics;
	
	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}
	
	public String getSensor() {
		return sensor;
	}
	
	public void setSensor(String sensor) {
		this.sensor = sensor;
	}
	
	public BigDecimal getLatitude() {
		return latitude;
	}
	
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	
	public BigDecimal getLongitude() {
		return longitude;
	}
	
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	
	public Map<String, String> getCharacteristics() {
		return characteristics;
	}
	
	public void setCharacteristics(Map<String, String> characteristics) {
		this.characteristics = characteristics;
	}
}
