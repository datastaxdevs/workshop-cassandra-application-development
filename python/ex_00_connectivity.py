from db_connection import get_session

if __name__ == '__main__':
    session = get_session()
    #
    rows = session.execute("SELECT key, cluster_name, data_center FROM system.local;")
    local = rows.one()
    if local:
        print("    ** Connected to cluster '%s' at data center '%s' **" % (
            local.cluster_name,
            local.data_center,
        ))
    else:
        print("Error: could not read 'system.local' table!")
