const {getSession} = require('./db_connection');

async function main() {

  const session = getSession();
  await session.connect();

  session.execute("SELECT key, cluster_name, data_center FROM system.local;")
    .then(result => {
      const local = result.first();
      console.log(`    ** Connected to cluster '${local['cluster_name']}' at data center '${local['data_center']}' **`);
      session.shutdown().then( () => console.log('Session closed.'));
    });

}

main();
