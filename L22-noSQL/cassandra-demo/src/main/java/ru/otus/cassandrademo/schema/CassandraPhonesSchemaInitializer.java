package ru.otus.cassandrademo.schema;

import com.datastax.driver.core.Session;
import lombok.RequiredArgsConstructor;
import ru.otus.cassandrademo.db.CassandraConnection;

@RequiredArgsConstructor
public class CassandraPhonesSchemaInitializer implements CassandraSchemaInitializer {
    private final CassandraConnection cassandraConnection;

    @Override
    public void initSchema() {
        Session session = cassandraConnection.getSession();
        createKeySpace(session);
        createTable(session);
    }

    @Override
    public void dropSchemaIfExists() {
        String query = "DROP KEYSPACE IF EXISTS Products";
        cassandraConnection.getSession().execute(query);
    }

    private void createKeySpace(Session session) {
        String query = "CREATE KEYSPACE IF NOT EXISTS Products" +
                " WITH replication = {" +
                "'class':'SimpleStrategy','replication_factor':1};";
        session.execute(query);
    }

    private void createTable(Session session) {
        String query = "CREATE TABLE IF NOT EXISTS Products.Phones (" +
                "id uuid PRIMARY KEY, " +
                "model text," +
                "color text," +
                "serialNumber text," +
                "operatingSystem text);";
        session.execute(query);
    }
}
