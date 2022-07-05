import os
import atexit

from dotenv import load_dotenv
from cassandra.cluster import Cluster
from cassandra.auth import PlainTextAuthProvider


# read .env file for connection params
load_dotenv()
ASTRA_DB_SECURE_BUNDLE_PATH = os.environ["ASTRA_DB_SECURE_BUNDLE_PATH"]
ASTRA_DB_CLIENT_ID = os.environ["ASTRA_DB_CLIENT_ID"]
ASTRA_DB_CLIENT_SECRET = os.environ["ASTRA_DB_CLIENT_SECRET"]
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
