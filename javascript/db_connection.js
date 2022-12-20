// Connection to an Astra DB database.

const { Client } = require('cassandra-driver');

const config = {
  cloud: {
    secureConnectBundle: process.env.ASTRA_DB_SECURE_BUNDLE_PATH
  },
  credentials: {
    username: process.env.ASTRA_DB_CLIENT_ID,
    password: process.env.ASTRA_DB_CLIENT_SECRET
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
