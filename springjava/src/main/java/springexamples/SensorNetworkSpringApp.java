package springexamples;

import astraconnect.AstraConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.google.gson.Gson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SensorNetworkSpringApp {
	static AstraConnection conn;
	static CqlSession session;
	
	public static void main(String[] args) {
		conn = new AstraConnection();
		session = conn.getCqlSession();
		
		SpringApplication.run(SensorNetworkSpringApp.class, args);
	}
	 
	@GetMapping("/sensors_by_network/{network}")
	public String getSensorsByNetwork(@PathVariable(value = "network") String network) {
		PreparedStatement q3Prepared = session.prepare("SELECT * FROM sensors_by_network WHERE network = ?");
		BoundStatement q3Bound = q3Prepared.bind(network);
		ResultSet rs = session.execute(q3Bound);
		
		String sensorJSON = new Gson().toJson(processSensorsByNetworkData(network,rs.all()));
		return sensorJSON;
	}

	@PostMapping("/measurements_by_sensor_date")
	public String getMeasurementsBySensorDate(@RequestBody SensorByDateRequest request) {
		PreparedStatement q4Prepared = session.prepare(
				"SELECT timestamp, value FROM temperatures_by_sensor WHERE sensor=? AND date=?");
		BoundStatement q4Bound = q4Prepared.bind(request.getSensor(),request.getDate());
		ResultSet rs = session.execute(q4Bound);
		
		String sensorJSON = new Gson().toJson(processSensorByDateData(rs.all()));
		return sensorJSON;
	}
	
	private static List<SensorByDateResponse> processSensorByDateData(List<Row> rows) {
		List<SensorByDateResponse> returnVal = new ArrayList<SensorByDateResponse>();
		
		for (Row row : rows) {
			SensorByDateResponse sensorReading = new SensorByDateResponse();
			
			sensorReading.setValue(row.getFloat("value"));
			sensorReading.setTimestamp(row.get("timestamp", GenericType.INSTANT).toString());
			
			returnVal.add(sensorReading);
		}
		
		return returnVal;
	}

	private static List<SensorByNetworkResponse> processSensorsByNetworkData(String network, List<Row> rows) {
		List<SensorByNetworkResponse> returnVal = new ArrayList<SensorByNetworkResponse>();
		
		for (Row row : rows) {
			SensorByNetworkResponse sensorReading = new SensorByNetworkResponse();
			
			sensorReading.setNetwork(network);
			sensorReading.setSensor(row.getString("sensor"));
			sensorReading.setLatitude(row.getBigDecimal("latitude"));
			sensorReading.setLongitude(row.getBigDecimal("longitude"));

			Map<String,String> characteristics = row.getMap("characteristics", String.class, String.class);
			sensorReading.setCharacteristics(characteristics);
			
			returnVal.add(sensorReading);
		}
		
		return returnVal;
	}
}
