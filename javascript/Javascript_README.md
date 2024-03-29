# Javascript instructions

2. [Sensor App](#2-sensor-app)
3. [Sensor API](#3-sensor-api)

## 2. Sensor App

You will be running the following steps from the command line of your Gitpod: all commands except the HTTP requests are to be run on the left-hand panel ("main-console", the one saying "Ready to rock!").

> Note: since the code uses the `dotenv` package, sourcing the dot-env file is not even necessary for Javascript.

### Setup

💻 Change directory:

```bash
cd javascript
```

💻 Install dependencies:

```bash
npm install
```

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
> (of type `DECIMAL` in the table) are
> [returned as `BigDecimal` objects in the drivers](https://docs.datastax.com/en/developer/nodejs-driver/4.6/features/datatypes/), hence
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

💻 With the API running, _in the request shell_ ("request-console", on the right-hand side), try to call the "Q3" endpoint (GET) using [`HTTPie`](https://httpie.io/) (see below if you prefer `curl`):

```bash
http :5000/sensors_by_network/volcano-net
```

<details><summary>Show expected result</summary>

```
$> http :5000/sensors_by_network/volcano-net
HTTP/1.1 200 OK
Connection: keep-alive
Content-Length: 299
Content-Type: application/json; charset=utf-8
Date: Wed, 11 Jan 2023 14:05:50 GMT
ETag: W/"12b-0shzYwfOdo5tSPRHAQdyPu/PoQE"
Keep-Alive: timeout=5
X-Powered-By: Express

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
curl -s localhost:5000/sensors_by_network/volcano-net | jq
```

Expected result:

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

💻 With the API running, _in the request shell_ ("request-console", on the right-hand side) try to call the "Q4" endpoint (POST):

```bash
http --json POST \
    :5000/measurements_by_sensor_date \
    "sensor"="s1001" "date"="2020-07-04"
```

<details><summary>Show expected result</summary>

```
$> http --json POST \
>     :5000/measurements_by_sensor_date \
>     "sensor"="s1001" "date"="2020-07-04"
HTTP/1.1 200 OK
Connection: keep-alive
Content-Length: 209
Content-Type: application/json; charset=utf-8
Date: Wed, 11 Jan 2023 14:08:20 GMT
ETag: W/"d1-xcCHVjBxvBg19/9NGLyyd0s8jf8"
Keep-Alive: timeout=5
X-Powered-By: Express

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

</details>

<details><summary>Click for "curl" equivalent instructions</summary>

```bash
curl -s -XPOST localhost:5000/measurements_by_sensor_date \
    -d '{"sensor":"s1001", "date":"2020-07-04"}' \
    -H 'Content-Type: application/json' | jq
```

Expected result:

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

</details>

In this case, the parameters are passed as POST payload: you can check, in the API
code, the way these are parsed and used within the endpoint function body.
This makes use of `moment.js` for parsing and a subsequent conversion with
`toDate()` to comply with the allowed [representation for date/time](https://docs.datastax.com/en/developer/nodejs-driver/4.6/features/datatypes/datetime/) in the Node drivers.

## Well done!

Congratulations, your Astra-DB-backed API is running all right!

Now head back to the [main README](../README.md#homework-instructions) and ... it's time for a little assignment!
