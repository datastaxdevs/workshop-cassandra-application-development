/*
We implement Q3 on the sensor-network data-modeling example:
    'Find information about all sensors in a specified network'
*/

const {getSession} = require('./db_connection');

async function main() {

  const session = getSession();
  await session.connect();

  const network = process.argv.concat(['forest-net'])[2];

  console.log(`    ** Querying sensors for network '${network}' ...`);
  session.execute("SELECT * FROM sensors_by_network WHERE network = ?;", [network]).then( result => {
    const rows = result.rows;
    rows.forEach( row => {
      console.log(
        '      - Sensor %s (LAT=%s, LON=%s): %s',
        row['sensor'].padStart(8),
        // coerce to ordinary numbers with "1.0 * ..."
        (1.0 * row['latitude']).toFixed(2),
        (1.0 * row['longitude']).toFixed(2),
        (Object.entries(row['characteristics']).map( (kv) => `${kv[0]} = ${kv[1]}`)).join(', ')
      );
    });
    session.shutdown().then( () => console.log('Session closed.'));
  });
}

main();
