// Connection to an Astra DB database.

require('dotenv').config({path: '../.env'});
const { Client } = require('cassandra-driver');

/*
Below we use the literal "token" and a valid Astra token to authenticate.
An equally valid choice is to supply the "Client ID" and the "Client Secret".
*/

const config = {
  cloud: {
    secureConnectBundle: process.env.ASTRA_DB_SECURE_BUNDLE_PATH
  },
  credentials: {
    username: "token",
    password: process.env.ASTRA_DB_APPLICATION_TOKEN
  },
  keyspace: process.env.ASTRA_DB_KEYSPACE
};

// either 'credentials' or an 'authProvider' to the above
// config, when connecting to an auth-enabled cluster.
// (https://docs.datastax.com/en/developer/nodejs-driver/4.6/api/type.ClientOptions/)

let session;

exports.getSession = () => {

  if (! session){
    console.log('Creating session.')
    session = new Client(config);
  }

  return session;

}
