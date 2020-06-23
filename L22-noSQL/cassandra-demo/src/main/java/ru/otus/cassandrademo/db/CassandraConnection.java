package ru.otus.cassandrademo.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraConnection implements AutoCloseable {

    private Cluster cluster;
    private Session session;

    public CassandraConnection(String nodeAddress, int port) {
        cluster = Cluster.builder().addContactPoint(nodeAddress).withPort(port).build();
        session = cluster.connect();
    }

    public Session getSession() {
        return this.session;
    }

    @Override
    public void close() {
        session.close();
        cluster.close();
    }
}
