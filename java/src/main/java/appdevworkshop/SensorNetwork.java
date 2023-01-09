package appdevworkshop;

import astraconnect.AstraConnection;

import java.util.List;
import java.util.Map;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

public class SensorNetwork {

    public static void main(String[] args) {

        AstraConnection conn = new AstraConnection();
        CqlSession session = conn.getCqlSession();
      
        if (args.length > 0 && args[0].equals("ex_01_query_Q3")) {
            // ex_01_query_Q3

            // set network
            String network;
            if (args.length > 1) {
                network = args[1];
            } else {
                network = "forest-net";
            }
      
            ResultSet rs = session.execute("SELECT * FROM sensors_by_network WHERE network = '"
                + network + "'");
      
            printSensorsByNetworkData(network,rs.all());
      
        } else if (args.length > 0 && args[0].equals("ex_01B_query_Q3")) {
            // ex_01B_query_Q3 (prepared statement)

            // set network
            String network;
            if (args.length > 1) {
                network = args[1];
            } else {
                network = "forest-net";
            }

            PreparedStatement q3Prepared = session.prepare("SELECT * FROM sensors_by_network WHERE network = ?");
            BoundStatement q3Bound = q3Prepared.bind(network);
            ResultSet rs = session.execute(q3Bound);
      
              printSensorsByNetworkData(network,rs.all());

        } else {
            // test connection
            ResultSet rs = session.execute("SELECT key, cluster_name, data_center from system.local");
            Row row = rs.one();
            System.out.printf("** Connected to cluster '%s' at data center '%s' **\n",
                row.getString("cluster_name"), row.getString("data_center"));
        }
      
        conn.finalize();
        System.exit(0);
    }
  
    private static void printSensorsByNetworkData(String network, List<Row> rows) {
        System.out.printf("** Querying sensors for network '%s' ...\n", network);
        for (Row row : rows) {
            Map<String,String> characteristics = row.getMap("characteristics", String.class, String.class);
          
            System.out.printf("      - Sensor %-8s (LAT=%+5.2f, LON=%+5.2f):",
                row.getString("sensor"),
                row.getBigDecimal("latitude"),
                row.getBigDecimal("longitude"));
            for (Map.Entry<String, String> entry : characteristics.entrySet()) {
                System.out.printf(" %s = %s ", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
    }
}
