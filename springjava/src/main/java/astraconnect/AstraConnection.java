package astraconnect;

import com.datastax.oss.driver.api.core.CqlSession;

/*
Below we use the literal "token" and a valid Astra token to authenticate.
An equally valid choice is to supply the "Client ID" and the "Client Secret".
*/

public class AstraConnection extends CassandraConnection {
    static final String ASTRA_DB_SECURE_BUNDLE_PATH = System.getenv("ASTRA_DB_SECURE_BUNDLE_PATH");
    static final String ASTRA_DB_CLIENT_ID = "token";
    static final String ASTRA_DB_CLIENT_SECRET = System.getenv("ASTRA_DB_APPLICATION_TOKEN");
    static final String ASTRA_DB_KEYSPACE = System.getenv("ASTRA_DB_KEYSPACE");
    
    public AstraConnection() {
        super(ASTRA_DB_CLIENT_ID, ASTRA_DB_CLIENT_SECRET, ASTRA_DB_SECURE_BUNDLE_PATH, ASTRA_DB_KEYSPACE);
    }
    
    public CqlSession getCqlSession() {
        return super.getCqlSession();
    }
    
    public void finalize() {
        super.finalize();
    }
}
