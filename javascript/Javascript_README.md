# Python instructions

2. [Sensor App](#2-sensor-app)
3. [Sensor API](#3-sensor-api)

## 2. Sensor App

You will be running the following steps from the command line.

### Setup

First (in the GitPod terminal), do `source .env` to set environment variables for your app.

Next `cd javascript`.

Install packages, `npm install`.

The connection to the database is provided by `db_connection.js`, which
provides a (Singleton) Cassandra session by reading connection parameters
from environment variables. Check out [the code](db_connection.js).

### Run stuff

💻 Run connectivity test:
```bash
node ex_00_connectivity.js
```
<details><summary>Show expected result</summary>

```
$> node ex_00_connectivity.js 
Creating session.
    ** Connected to cluster 'cndb' at data center 'us-east1' **
Session closed.
```

</details>

💻 Run query Q3 as standalone exercise:
```bash
node ex_01_query_Q3.js volcano-net
```
<details><summary>Show expected result</summary>

```
$ node ex_01_query_Q3.js volcano-net
Creating session.
    ** Querying sensors for network 'volcano-net' ...
      - Sensor    s2001 (LAT=44.46, LON=-110.83): accuracy = high, sensitivity = medium
      - Sensor    s2002 (LAT=44.46, LON=-110.83): accuracy = high, sensitivity = medium
Session closed.
```

</details>

> _Note_: you will see that the `latitude` and `longitude` column values
> (of type `DECIMAL` in the table)
> need to be coerced, here and in the upcoming API code, into a number explicitly.

💻 Try to run the improved form of the same exercise, which uses prepared statements:
```bash
node ex_01B_query_Q3.js volcano-net
```
> If you plan to run the same CQL statements over and over in your driver-based application
> (possibly with variable arguments), you should always employ prepared statements as they improve
> the performance by reducing the overhead. Thus this version of query Q3 is to be preferred.

💻 Run the callback-based version
```bash
node ex_01C_query_Q3.js volcano-net
```

The only difference is that this time, instead of a promise-based style,
the code passes and explicit [callback](https://docs.datastax.com/en/developer/nodejs-driver/4.6/api/class.Client/#execute) to the `execute` method.

## 3. Sensor API

💻 Start the API:
```bash
node api.js
```
<details><summary>Show expected result</summary>

```
$> node api.js
Creating session.
API ready on port 5000
```

</details>

💻 With the API running, _in the other shell_ try to call the "Q3" endpoint (GET):
```bash
curl -s localhost:5000/sensors_by_network/volcano-net | jq
```
<details><summary>Show expected result</summary>

```
$> curl -s localhost:5000/sensors_by_network/volcano-net | jq
[
  {
    "network": "volcano-net",
    "sensor": "s2001",
    "characteristics": {
      "accuracy": "high",
      "sensitivity": "medium"
    },
    "latitude": 44.460321,
    "longitude": -110.828151
  },
  {
    "network": "volcano-net",
    "sensor": "s2002",
    "characteristics": {
      "accuracy": "high",
      "sensitivity": "medium"
    },
    "latitude": 44.463195,
    "longitude": -110.830124
  }
]
```

</details>

This endpoint is a GET and its parameter is a path component in the URL.
Try to find, in the API code, where the URL path is parsed to obtain the `network` name.

💻 With the API running, _in the other shell_ try to call the "Q4" endpoint (POST):
```bash
curl -s -XPOST localhost:5000/measurements_by_sensor_date \
    -d '{"sensor":"s1001", "date":"2020-07-04"}' \
    -H 'Content-Type: application/json' | jq
```
<details><summary>Show expected result</summary>

```
$> curl -s -XPOST localhost:5000/measurements_by_sensor_date \
>     -d '{"sensor":"s1001", "date":"2020-07-04"}' \
>     -H 'Content-Type: application/json' | jq
[
  {
    "timestamp": "2020-07-04T12:59:59.000Z",
    "value": 98
  },
  {
    "timestamp": "2020-07-04T12:00:01.000Z",
    "value": 97
  },
  {
    "timestamp": "2020-07-04T00:59:59.000Z",
    "value": 79
  },
  {
    "timestamp": "2020-07-04T00:00:01.000Z",
    "value": 80
  }
]
```

In this case, the parameters are passed as POST payload: you can check, in the API
code, the way these are parsed and used within the endpoint function body.
This makes use of `moment.js` for parsing and a subsequent conversion with
`toDate()` to comply with the allowed [representation for date/time](https://docs.datastax.com/en/developer/nodejs-driver/4.6/features/datatypes/datetime/) in the Node drivers.
