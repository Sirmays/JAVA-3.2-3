package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/new_schema",
                    "root",
                    "root"
            );

        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }

    }

 }

