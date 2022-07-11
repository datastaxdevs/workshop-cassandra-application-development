# Java instructions

### Setup

First (in the GitPod terminal) `cd java`.

Execute a `mvn clean install` to bring in the dependent packages and build everything.

The connection classes are `AstraConnection.java` and `CassandraConnection.java`.  The `AstraConnection` class extends the `CassandraConnection` super class, accounting for the Astra DB environment variables.  Take a minute to give those a look:
 - [AstraConnection](src/main/java/astraconnect/AstraConnection.java)
 - [CassandraConnection](src/main/java/astraconnect/CassandraConnection.java)

### Run stuff

💻 Run connectivity test:
```bash
java -jar target/examples-0.0.1-jar-with-dependencies.jar
```
<details><summary>Show expected result</summary>

```
$> java -jar target/examples-0.0.1-jar-with-dependencies.jar
[OK] Success
[OK] Welcome to Astra DB! Connected to Keyspace sensor_data
** Connected to cluster 'cndb' at data center 'us-east1' **
[shutdown_driver] Closing connection
```

_Note: You may see warnings for SLF4J.  These can be safely ignored._

</details>

💻 Run query Q3 as standalone exercise:
```bash
java -jar target/examples-0.0.1-jar-with-dependencies.jar ex_01_query_Q3 volcano-net
```
<details><summary>Show expected result</summary>

```
$> java -jar target/examples-0.0.1-jar-with-dependencies.jar ex_01_query_Q3 volcano-net
[OK] Success
[OK] Welcome to Astra DB! Connected to Keyspace sensor_data
** Querying sensors for network 'volcano-net' ...
      - Sensor s2001    (LAT=+44.46, LON=-110.83): accuracy = high  sensitivity = medium
      - Sensor s2002    (LAT=+44.46, LON=-110.83): accuracy = high  sensitivity = medium
[shutdown_driver] Closing connection
```

</details>

💻 Try to run the improved form of the same exercise, which uses prepared statements:
```bash
java -jar target/examples-0.0.1-jar-with-dependencies.jar ex_01B_query_Q3 volcano-net
```
> If you plan to run the same CQL statements over and over in your driver-based application
> (possibly with variable arguments), you should always employ prepared statements as they improve
> the performance by reducing the overhead. Thus this version of query Q3 is to be preferred.
