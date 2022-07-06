# Python instructions

### Setup

First `cd python`

Install packages, `pip install -r requirements.txt`

### Run stuff

Run connectivity test, `python ex_00_connectivity.py`:
```
$> python ex_00_connectivity.py
[get_session] Creating session
    ** Connected to cluster 'cndb' at data center 'eu-west-1' **
[shutdown_driver] Closing connection
```

Run query Q3 as standalone exercise, `python ex_01_query_Q3.py volcano-net`:
```
$> python ex_01_query_Q3.py volcano-net
[get_session] Creating session
    ** Querying sensors for network 'volcano-net' ...
      - Sensor 's2001'  (LAT=+44.46, LON=-110.83): accuracy = high, sensitivity = medium
      - Sensor 's2002'  (LAT=+44.46, LON=-110.83): accuracy = high, sensitivity = medium
[shutdown_driver] Closing connection
```

Try to run the improved form of the same exercise, which uses prepared statements:
`python ex_01B_query_Q3.py volcano-net`. If you plan to run the same statements over and over,
possibly with other arguments, you should always employ prepared statements as they improve
the performance by reducing the overhead.

Start the API, `uvicorn api:app`:
```
$> uvicorn api:app
INFO:     Started server process [68610]
INFO:     Waiting for application startup.
INFO:     Application startup complete.
INFO:     Uvicorn running on http://127.0.0.1:8000 (Press CTRL+C to quit)
```

With the API running, _in the other shell_ try to call it, `curl -s localhost:8000/sensors_by_network/volcano-net | jq`:
```
$> curl -s localhost:8000/sensors_by_network/volcano-net | jq
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

### TODOs

- other endpoints
- better fastAPI structure (but: don't overdo, would confuse)
