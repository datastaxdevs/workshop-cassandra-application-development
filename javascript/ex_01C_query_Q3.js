/*
Callback-form for 'execute' (as opposed to promise-based).
See https://docs.datastax.com/en/developer/nodejs-driver/4.6/api/class.Client/#execute
*/

const {getSession} = require('./db_connection');

async function main() {

  const session = getSession();
  await session.connect();

  const network = process.argv.concat(['forest-net'])[2];

  console.log(`    ** Querying sensors for network '${network}' ...`);
  session.execute(
    "SELECT * FROM sensors_by_network WHERE network = ?;",
    [network],
    // The next line is enough to prepare and re-use the statement.
    // The driver will know when to get the cached prepared statement
    // if the statement string ("SELECT ...") is seen again:
    {prepare: true},
    (error, result) => {
      if (! error){
        const rows = result.rows;
        rows.forEach( row => {
          console.log(
            '      - Sensor %s (LAT=%s, LON=%s): %s',
            row['sensor'].padStart(8),
            // The 'decimal' CQL data type becomes a "require('cassandra-driver').types.BigDecimal" in Node:
            // see https://docs.datastax.com/en/developer/nodejs-driver/4.6/features/datatypes/
            // and https://docs.datastax.com/en/developer/nodejs-driver/4.6/features/datatypes/numerical/#decimal
            // So this "1.0 * ..." is there to coerce the values to ordinary numbers.
            (1.0 * row['latitude']).toFixed(2),
            (1.0 * row['longitude']).toFixed(2),
            (Object.entries(row['characteristics']).map( (kv) => `${kv[0]} = ${kv[1]}`)).join(', ')
          );
        });
      }else{
        console.error('ERROR reading the table:', error);
      }
      session.shutdown().then( () => console.log('Session closed.'));
    }
  );

}

main();
