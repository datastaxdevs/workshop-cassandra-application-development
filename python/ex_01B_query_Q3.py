"""
Using prepared statements for query Q3
"""

import sys

from db_connection import get_session

if __name__ == '__main__':
    session = get_session()
    #
    if len(sys.argv) > 1:
        network = sys.argv[1]
    else:
        network = 'forest-net'
    #
    q3_statement = session.prepare("SELECT * FROM sensors_by_network WHERE network = ?;")
    #
    print("    ** Querying sensors for network '%s' ..." % network)
    rows = session.execute(q3_statement, (network,) )
    for row in rows:
        print("      - Sensor %-8s (LAT=%+5.2f, LON=%+5.2f): %s" % (
            "'%s'" % row.sensor,
            row.latitude,
            row.longitude,
            ", ".join(
                "%s = %s" % (k, v)
                for k, v in row.characteristics.items()
            ),
        ))
