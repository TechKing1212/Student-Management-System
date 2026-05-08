package project;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TestDB {

    // ===== This class is used ONLY to test the database connection =====
    // Run it separately to verify MySQL is working before running the main app.

    public static void main(String[] args) {

        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                System.out.println("Connection failed. Check DBConnection.java settings.");
                return;
            }

            System.out.println("Connection successful!");

            // Insert a sample student using the correct column names
            String query = "INSERT INTO students (name, attendance, sub1, sub2, sub3, sub4, sub5) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, "Test Student");
            ps.setDouble(2, 85.0);  // attendance
            ps.setDouble(3, 78.0);  // sub1
            ps.setDouble(4, 82.0);  // sub2
            ps.setDouble(5, 90.0);  // sub3
            ps.setDouble(6, 74.0);  // sub4
            ps.setDouble(7, 88.0);  // sub5

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Test student inserted successfully!");
            } else {
                System.out.println("Insertion failed.");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
