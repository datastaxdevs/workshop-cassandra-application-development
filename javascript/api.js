const express = require('express');
const moment = require('moment');

const api = express();

const {getSession} = require('./db_connection');


api.use(express.json());

api.listen(5000, () => {
  console.log('API ready on port 5000');
});


const session = getSession();
// 'client' will automatically connect when executing the first query

api.get('/sensors_by_network/:network', (req, res) => {
  /*
  Q3: Display all sensors in a network
  */
  session.execute(
    "SELECT * FROM sensors_by_network WHERE network = ?;",
    [req.params.network],
    {prepare: true}
  ).then( results => {
    res.send(
      results.rows.map( row => {
        return {
          ...row,
          ...{
            latitude: 1.0 * row.latitude,
            longitude: 1.0 * row.longitude,
          },
        };
      })
    );
  });
});

api.post('/measurements_by_sensor_date', (req, res) => {
  /*
  Q4: Find raw measurements for a particular sensor on a specified date: order by timestamp (desc)
  */
  const sensor = req.body.sensor;
  const date0 = req.body.date;
  // see: https://docs.datastax.com/en/developer/nodejs-driver/4.6/features/datatypes/datetime/
  const date = moment(date0, 'YYYY-MM-DD').toDate();

  session.execute(
    "SELECT timestamp, value FROM temperatures_by_sensor WHERE sensor=? AND date=?;",
    [sensor, date],
    {prepare: true}
  ).then( results => {
    res.send(results.rows);
  });
});
