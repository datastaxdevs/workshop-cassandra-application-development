package astraconnect;

import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import com.datastax.oss.driver.api.core.CqlSession;

public class CassandraConnection {
    private CqlSession cqlSession;
        
    public CassandraConnection(String username, String pwd, List<InetSocketAddress> endpointList, String keyspace, String datacenter) {
        // Connect to open source Apache Cassandra
        try {
            cqlSession = CqlSession.builder()
            .addContactPoints(endpointList)
            .withAuthCredentials(username, pwd)
            .withKeyspace(keyspace)
            .withLocalDatacenter(datacenter)
            .build();

            System.out.println("[OK] Success");
            System.out.printf("[OK] Welcome to Apache Cassandra! Connected to Keyspace %s\n", cqlSession.getKeyspace().get());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
        
    public CassandraConnection(String username, String pwd, String secureBundleLocation, String keyspace) {
        // Connect to Astra DB with a secure bundle
        try {
            System.out.println(username);
            System.out.println(pwd);
            System.out.println(secureBundleLocation);
            System.out.println(keyspace);
            System.out.println("=======");
            Map<String, String> env = System.getenv();
            env.forEach((k, v) -> System.out.println(k + ":" + v));
            System.out.println("=======");
            cqlSession = CqlSession.builder()
            .withCloudSecureConnectBundle(Paths.get(secureBundleLocation))
            .withAuthCredentials(username, pwd)
            .withKeyspace(keyspace)
            .build();
                        
            System.out.println("[OK] Success");
            System.out.printf("[OK] Welcome to Astra DB! Connected to Keyspace %s\n", cqlSession.getKeyspace().get());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
        
    public CqlSession getCqlSession() {
        return cqlSession;
    }
        
    protected void finalize() {
        cqlSession.close();
        System.out.println("[shutdown_driver] Closing connection");
        System.out.println();
    }
}
