# Java instructions

2. [Sensor App](#2-sensor-app)
3. [Sensor API](#3-sensor-api)

## 2. Sensor App

You will be running the following steps from the command line of your Gitpod: all commands except the HTTP requests are to be run on the left-hand panel ("main-console", the one saying "Ready to rock!").

### Setup

💻 Make sure you exported the environment variables for your app earlier with the command _(note the `set -a`, which marks all variables as exported to child processes for later consumption within the Java application code)_:

```bash
set -a ; source .env
```

💻 Next change directory: 

```bash
cd java
```

💻 Now bring in the dependencies and build everything:

```bash
mvn clean install
```

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

## 3. Sensor API

💻 Start the API (which is in a separate directory):

```bash
cd ../springjava
mvn clean install
mvn spring-boot:run
```

<details><summary>Show expected result</summary>

```
2022-07-11 17:27:18 INFO 815 [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2022-07-11 17:27:18 INFO 815 [main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2022-07-11 17:27:18 INFO 815 [main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.62]
2022-07-11 17:27:18 INFO 815 [main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2022-07-11 17:27:18 INFO 815 [main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 446 ms
2022-07-11 17:27:19 INFO 815 [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2022-07-11 17:27:19 INFO 815 [main] springexamples.SensorNetworkSpringApp    : Started SensorNetworkSpringApp in 0.865 seconds (JVM running for 4.094)
```

</details>

💻 With the API running, _in the request shell_ ("request-console", on the right-hand side), try to call the "Q3" endpoint (GET) using [`HTTPie`](https://httpie.io/) (see below if you prefer `curl`):

```bash
http :8080/sensors_by_network/volcano-net
```

<details><summary>Show expected result</summary>

```
$> http :8080/sensors_by_network/volcano-net
HTTP/1.1 200 
Connection: keep-alive
Content-Length: 299
Content-Type: text/plain;charset=UTF-8
Date: Wed, 11 Jan 2023 14:14:03 GMT
Keep-Alive: timeout=60

[
    {
        "characteristics": {
            "accuracy": "high",
            "sensitivity": "medium"
        },
        "latitude": 44.460321,
        "longitude": -110.828151,
        "network": "volcano-net",
        "sensor": "s2001"
    },
    {
        "characteristics": {
            "accuracy": "high",
            "sensitivity": "medium"
        },
        "latitude": 44.463195,
        "longitude": -110.830124,
        "network": "volcano-net",
        "sensor": "s2002"
    }
]
```

</details>

<details><summary>Click for "curl" equivalent instructions</summary>

```bash
curl -s localhost:8080/sensors_by_network/volcano-net | jq
```

Expected result:

```
$> curl -s localhost:8080/sensors_by_network/volcano-net | jq
[
  {
    "network": "volcano-net",
    "sensor": "s2001",
    "latitude": 44.460321,
    "longitude": -110.828151,
    "characteristics": {
      "accuracy": "high",
      "sensitivity": "medium"
    }
  },
  {
    "network": "volcano-net",
    "sensor": "s2002",
    "latitude": 44.463195,
    "longitude": -110.830124,
    "characteristics": {
      "accuracy": "high",
      "sensitivity": "medium"
    }
  }
]
```

</details>

This endpoint is a GET and its parameter is a path component in the URL.
Try to find, in the API code, where the URL path is parsed to obtain the `network` name.

💻 With the API running, _in the request shell_ ("request-console", on the right-hand side), try to call the "Q4" endpoint (POST):

```bash
http --json POST \
    :8080/measurements_by_sensor_date \
    "sensor"="s1001" "date"="2020-07-04"
```

<details><summary>Show expected result</summary>

```
$> http --json POST \
>     :8080/measurements_by_sensor_date \
>     "sensor"="s1001" "date"="2020-07-04"
HTTP/1.1 200 
Connection: keep-alive
Content-Length: 201
Content-Type: application/json
Date: Wed, 11 Jan 2023 14:15:31 GMT
Keep-Alive: timeout=60

[
    {
        "timestamp": "2020-07-04T12:59:59Z",
        "value": 98.0
    },
    {
        "timestamp": "2020-07-04T12:00:01Z",
        "value": 97.0
    },
    {
        "timestamp": "2020-07-04T00:59:59Z",
        "value": 79.0
    },
    {
        "timestamp": "2020-07-04T00:00:01Z",
        "value": 80.0
    }
]
```

</details>

<details><summary>Click for "curl" equivalent instructions</summary>

```bash
curl -s -XPOST localhost:8080/measurements_by_sensor_date \
    -d '{"sensor":"s1001", "date":"2020-07-04"}' \
    -H 'Content-Type: application/json' | jq
```

Expected result:

```
$> curl -s -XPOST localhost:8080/measurements_by_sensor_date \
>     -d '{"sensor":"s1001", "date":"2020-07-04"}' \
>     -H 'Content-Type: application/json' | jq
[
  {
    "value": 98,
    "timestamp": "2020-07-04T12:59:59Z"
  },
  {
    "value": 97,
    "timestamp": "2020-07-04T12:00:01Z"
  },
  {
    "value": 79,
    "timestamp": "2020-07-04T00:59:59Z"
  },
  {
    "value": 80,
    "timestamp": "2020-07-04T00:00:01Z"
  }
]
```

</details>

In this case, the parameters are passed as POST payload: you can check, in the API code, the way these are parsed and used within the endpoint function body.

## Well done!

Congratulations, your Astra-DB-backed API is running all right!

Now head back to the [main README](../README.md#homework-instructions) and ... it's time for a little assignment!
