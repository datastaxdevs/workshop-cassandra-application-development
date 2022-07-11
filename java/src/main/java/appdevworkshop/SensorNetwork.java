package appdevworkshop;

import astraconnect.AstraConnection;

import java.util.List;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

public class SensorNetwork {

	public static void main(String[] args) {

		AstraConnection conn = new AstraConnection();
		CqlSession session = conn.getCqlSession();
		
		
		try {
			
			if (args.length > 0 && args[0].equals("ex_01_query_Q3")) {
				// ex_01_query_Q3
				String network = args[1];
				ResultSet rs = session.execute("SELECT * FROM sensors_by_network WHERE network = " + network);
				List<Row> rows = rs.all();
				
				
			} else if (args.length > 0 && args[0].equals("ex_01B_query_Q3")) {
				// ex_01B_query_Q3 (prepared statement)
				
			} else {
				// test connection
				ResultSet rs = session.execute("SELECT key, cluster_name, data_center from system.local");
				Row row = rs.one();
				System.out.printf("** Connected to cluster '%s' at data center '%s' **\n",
						row.getString("cluster_name"), row.getString("data_center"));
			}
			
		} catch (Exception ex) {
			System.out.println("Error: " + ex.toString());
		}

		System.exit(0);
	}
}
