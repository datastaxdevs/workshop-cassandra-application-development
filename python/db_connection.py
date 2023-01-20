import os
import atexit

from dotenv import load_dotenv
from cassandra.cluster import Cluster
from cassandra.auth import PlainTextAuthProvider

'''
Below we use the literal "token" and a valid Astra token to authenticate.
An equally valid choice is to supply the "Client ID" and the "Client Secret".
'''

# read .env file for connection params
load_dotenv()
ASTRA_DB_SECURE_BUNDLE_PATH = os.environ["ASTRA_DB_SECURE_BUNDLE_PATH"]
ASTRA_DB_CLIENT_ID = "token"
ASTRA_DB_CLIENT_SECRET = os.environ["ASTRA_DB_APPLICATION_TOKEN"]
ASTRA_DB_KEYSPACE = os.environ["ASTRA_DB_KEYSPACE"]

# global cache variables to re-use a single Session
cluster = None
session = None


def get_session():
    #
    global session
    global cluster
    #
    if session is None:
        print("[get_session] Creating session")
        cluster = Cluster(
            cloud={
                "secure_connect_bundle": ASTRA_DB_SECURE_BUNDLE_PATH,
            },
            auth_provider=PlainTextAuthProvider(
                ASTRA_DB_CLIENT_ID,
                ASTRA_DB_CLIENT_SECRET,
            ),
        )
        session = cluster.connect(ASTRA_DB_KEYSPACE)
    else:
        print("[get_session] Reusing session")
    #
    return session


@atexit.register
def shutdown_driver():
    if session is not None:
        print("[shutdown_driver] Closing connection")
        cluster.shutdown()
        session.shutdown()
