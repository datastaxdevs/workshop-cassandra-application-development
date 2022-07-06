from fastapi import FastAPI, Depends

from db_connection import get_session


### Helpers

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

### API code

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
