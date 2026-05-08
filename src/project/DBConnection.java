package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // ===== DATABASE SETTINGS =====
    // Change these if your MySQL setup is different
    private static final String URL      = "jdbc:mysql://localhost:3306/oopproject";
    private static final String USER     = "root";
    private static final String PASSWORD = "Smj@123456";

    // ===== getConnection() =====
    // Called by every other class that needs the database.
    // Returns a fresh Connection each time.
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
