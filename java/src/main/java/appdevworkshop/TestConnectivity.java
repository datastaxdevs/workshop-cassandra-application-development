package appdevworkshop;

import astraconnect.AstraConnection;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

public class TestConnectivity {

	public static void main(String[] args) {

		AstraConnection conn = new AstraConnection();
		CqlSession session = conn.getCqlSession();
		
		try {
			ResultSet rs = session.execute("SELECT key, cluster_name, data_center from system.local");
			Row row = rs.one();
			System.out.printf("** Connected to cluster '%s' at data center '%s' **\n", row.getString("cluster_name"), row.getString("data_center"));
		} catch (Exception ex) {
			System.out.println("Error: could not read 'system.local' table!");
		}

		System.exit(0);
	}
}
