from fastapi import FastAPI, Depends

from db_connection import get_session


### Helpers

async def get_db_session():
    yield get_session()

### API code

app = FastAPI()

@app.get('/sensors_by_network/{network}')
async def get_sensors(network, session=Depends(get_db_session)):
    return [
        {
            'network': sensor.network,
            'sensor': sensor.sensor,
            'latitude': sensor.latitude,
            'longitude': sensor.longitude,
            'characteristics': sensor.characteristics,
        }
        for sensor in session.execute("SELECT * FROM sensors_by_network WHERE network = %s;", (network,) )
    ]
