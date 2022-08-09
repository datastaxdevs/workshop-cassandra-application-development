import datetime

from fastapi import FastAPI, Depends
from pydantic import BaseModel

from db_connection import get_session


# Helpers
#   (this part, in a well-structured app, would go to separate modules...)

async def get_db_session():
    # this wraps getting the session in a way that works with the Depends injection
    yield get_session()

# global storage for prepared statements
prepared_Q3 = None
def get_prepared_Q3(session):
    global prepared_Q3
    if prepared_Q3 is None:
        print('[get_prepared_Q3] Preparing statement')
        prepared_Q3 = session.prepare("SELECT * FROM sensors_by_network WHERE network = ?;")
    else:
        print('[get_prepared_Q3] Reusing statement')
    return prepared_Q3


prepared_Q4 = None
def get_prepared_Q4(session):
    global prepared_Q4
    if prepared_Q4 is None:
        print('[get_prepared_Q4] Preparing statement')
        prepared_Q4 = session.prepare("SELECT timestamp, value FROM temperatures_by_sensor WHERE sensor=? AND date=?;")
    else:
        print('[get_prepared_Q4] Reusing statement')
    return prepared_Q4

class SensorDateStringQuery(BaseModel):
    sensor: str
    date: str

### API code proper

app = FastAPI()

@app.get('/sensors_by_network/{network}')
async def get_sensors(network, session=Depends(get_db_session)):
    prepared = get_prepared_Q3(session)
    return [
        {
            'network': sensor.network,
            'sensor': sensor.sensor,
            'latitude': sensor.latitude,
            'longitude': sensor.longitude,
            'characteristics': sensor.characteristics,
        }
        for sensor in session.execute(prepared, (network,) )
    ]

@app.post('/measurements_by_sensor_date')
async def get_measurements_by_sensor_date(params: SensorDateStringQuery, session=Depends(get_db_session)):
    """
    Q4: Find raw measurements for a particular sensor on a specified date: order by timestamp (desc)
    """
    q_sensor = params.sensor
    q_date = datetime.datetime.strptime(params.date, '%Y-%m-%d')
    prepared = get_prepared_Q4(session)
    return [
        {
            'timestamp': measurement.timestamp,
            'value': measurement.value,
        }
        for measurement in session.execute(prepared, (q_sensor, q_date))
    ]
