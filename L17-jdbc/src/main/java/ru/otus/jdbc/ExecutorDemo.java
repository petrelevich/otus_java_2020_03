package ru.otus.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.User;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class ExecutorDemo {
    private static final String URL = "jdbc:h2:mem:";
    private static final Logger logger = LoggerFactory.getLogger(ExecutorDemo.class);

    public static void main(String[] args) throws SQLException {
        ExecutorDemo demo = new ExecutorDemo();

        try (Connection connection = getConnection()) {
            demo.createTable(connection);

            DbExecutorImpl<User> executor = new DbExecutorImpl<>();
            long userId = executor.executeInsert(connection, "insert into user(name) values (?)",
                    Collections.singletonList("testUserName"));
            logger.info("created user:{}", userId);
            connection.commit();

            Optional<User> user = executor.executeSelect(connection, "select id, name from user where id  = ?",
                    userId, rs -> {
                        try {
                            if (rs.next()) {
                                return new User(rs.getLong("id"), rs.getString("name"));
                            }
                        } catch (SQLException e) {
                            logger.error(e.getMessage(), e);
                        }
                        return null;
                    });
            logger.info("user:{}", user);
        }
    }

    private static Connection getConnection() throws SQLException {
        var connection = DriverManager.getConnection(URL);
        connection.setAutoCommit(false);
        return connection;
    }

    private void createTable(Connection connection) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement("create table user(id long auto_increment, name varchar(50))")) {
            pst.executeUpdate();
        }
    }
}
