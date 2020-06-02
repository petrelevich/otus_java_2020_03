package ru.otus.h2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sergey
 * created on 01.10.18.
 */
public class H2demo {
    private static final String URL = "jdbc:h2:mem:";
    private static final Logger logger = LoggerFactory.getLogger(H2demo.class);

    public static void main(String[] args) throws SQLException {
        H2demo demo = new H2demo();
        try (var connection = getConnection()) {
            demo.createTable(connection);
            int id = 1;
            demo.insertRecord(connection, id);
            demo.selectRecord(connection, id);
        }
    }

    private static Connection getConnection() throws SQLException {
        var connection = DriverManager.getConnection(URL);
        connection.setAutoCommit(false);
        return connection;
    }

    private void createTable(Connection connection) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement("create table test(id int, name varchar(50))")) {
            pst.executeUpdate();
        }
    }

    private void insertRecord(Connection connection, int id) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement("insert into test(id, name) values (?, ?)")) {
            Savepoint savePoint = connection.setSavepoint("savePointName");
            pst.setInt(1, id);
            pst.setString(2, "NameValue");
            try {
                int rowCount = pst.executeUpdate(); //Блокирующий вызов
                connection.commit();
                logger.info("inserted rowCount: {}", rowCount);
            } catch (SQLException ex) {
                connection.rollback(savePoint);
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    private void selectRecord(Connection connection, int id) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement("select name from test where id  = ?")) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    var name = rs.getString("name");
                    logger.info("name:{}", name);
                }
            }
        }
    }
}
