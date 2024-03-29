# Python instructions

2. [Sensor App](#2-sensor-app)
3. [Sensor API](#3-sensor-api)

## 2. Sensor App

You will be running the following steps from the command line of your Gitpod: all commands except the HTTP requests are to be run on the left-hand panel ("main-console", the one saying "Ready to rock!").

> Note: since the code uses the `python-dotenv` package, sourcing the dot-env file is not even necessary for Python.

### Setup

💻 Change directory:

```bash
cd python
```

💻 Install dependencies:

```bash
pip install -r requirements.txt
```

The core module is `db_connection.py` which provides a (Singleton) Cassandra
session for use by the rest of the code. Take a minute to [inspect that](db_connection.py).

### Run stuff

💻 Run connectivity test:

```bash
python ex_00_connectivity.py
```

<details><summary>Show expected result</summary>

```
$> python ex_00_connectivity.py
[get_session] Creating session
    ** Connected to cluster 'cndb' at data center 'eu-west-1' **
[shutdown_driver] Closing connection
```

</details>

💻 Run query Q3 as standalone exercise:

```bash
python ex_01_query_Q3.py volcano-net
```

<details><summary>Show expected result</summary>

```
$> python ex_01_query_Q3.py volcano-net
[get_session] Creating session
    ** Querying sensors for network 'volcano-net' ...
      - Sensor 's2001'  (LAT=+44.46, LON=-110.83): accuracy = high, sensitivity = medium
      - Sensor 's2002'  (LAT=+44.46, LON=-110.83): accuracy = high, sensitivity = medium
[shutdown_driver] Closing connection
```

</details>

💻 Try to run the improved form of the same exercise, which uses prepared statements:

```bash
python ex_01B_query_Q3.py volcano-net
```
> If you plan to run the same CQL statements over and over in your driver-based application
> (possibly with variable arguments), you should always employ prepared statements as they improve
> the performance by reducing the overhead. Thus this version of query Q3 is to be preferred.

## 3. Sensor API

💻 Start the API:

```bash
uvicorn api:app
```

<details><summary>Show expected result</summary>

```
$> uvicorn api:app
INFO:     Started server process [68610]
INFO:     Waiting for application startup.
INFO:     Application startup complete.
INFO:     Uvicorn running on http://127.0.0.1:8000 (Press CTRL+C to quit)
```

</details>

💻 With the API running, _in the request shell_ ("request-console", on the right-hand side), try to call the "Q3" endpoint (GET) using [`HTTPie`](https://httpie.io/) (see below if you prefer `curl`):

```bash
http :8000/sensors_by_network/volcano-net
```

<details><summary>Show expected result</summary>

```
$> http :8000/sensors_by_network/volcano-net
HTTP/1.1 200 OK
content-length: 299
content-type: application/json
date: Wed, 11 Jan 2023 13:52:04 GMT
server: uvicorn

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
curl -s localhost:8000/sensors_by_network/volcano-net | jq
```

Expected result:

```
$> curl -s localhost:8000/sensors_by_network/volcano-net | jq
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
    :8000/measurements_by_sensor_date \
    "sensor"="s1001" "date"="2020-07-04"
```

<details><summary>Show expected result</summary>

```
$> http --json POST \
>     :8000/measurements_by_sensor_date \
>     "sensor"="s1001" "date"="2020-07-04"
HTTP/1.1 200 OK
content-length: 197
content-type: application/json
date: Wed, 11 Jan 2023 13:57:13 GMT
server: uvicorn

[
    {
        "timestamp": "2020-07-04T12:59:59",
        "value": 98.0
    },
    {
        "timestamp": "2020-07-04T12:00:01",
        "value": 97.0
    },
    {
        "timestamp": "2020-07-04T00:59:59",
        "value": 79.0
    },
    {
        "timestamp": "2020-07-04T00:00:01",
        "value": 80.0
    }
]
```

</details>

<details><summary>Click for "curl" equivalent instructions</summary>

```bash
curl -s -XPOST localhost:8000/measurements_by_sensor_date \
    -d '{"sensor":"s1001", "date":"2020-07-04"}' \
    -H 'Content-Type: application/json' | jq
```

Expected result:

```
$> curl -s -XPOST localhost:8000/measurements_by_sensor_date \
>     -d '{"sensor":"s1001", "date":"2020-07-04"}' \
>     -H 'Content-Type: application/json' | jq
[
  {
    "timestamp": "2020-07-04T12:59:59",
    "value": 98
  },
  {
    "timestamp": "2020-07-04T12:00:01",
    "value": 97
  },
  {
    "timestamp": "2020-07-04T00:59:59",
    "value": 79
  },
  {
    "timestamp": "2020-07-04T00:00:01",
    "value": 80
  }
]
```

</details>

In this case, the parameters are passed as POST payload: you can check, in the API
code, the way these are parsed and used within the endpoint function body.
This makes use of a `pydantic` model and the highly-automated dependency management
facilities offered by FastAPI.

## Well done!

Congratulations, your Astra-DB-backed API is running all right!

Now head back to the [main README](../README.md#homework-instructions) and ... it's time for a little assignment!
