package project;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterForm {

    public RegisterForm() {

        JFrame frame = new JFrame("Register - Student Management System");
        frame.setSize(420, 320);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen

        // ===== TITLE =====
        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(135, 20, 200, 30);
        frame.add(title);

        // ===== USERNAME =====
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(60, 75, 100, 28);
        frame.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(160, 75, 180, 28);
        frame.add(userField);

        // ===== PASSWORD =====
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(60, 120, 100, 28);
        frame.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(160, 120, 180, 28);
        frame.add(passField);

        // ===== MESSAGE LABEL =====
        JLabel msgLabel = new JLabel("");
        msgLabel.setBounds(60, 162, 300, 25);
        frame.add(msgLabel);

        // ===== REGISTER BUTTON =====
        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(100, 200, 110, 32);
        frame.add(registerBtn);

        // ===== BACK TO LOGIN BUTTON =====
        JButton backBtn = new JButton("Back to Login");
        backBtn.setBounds(225, 200, 120, 32);
        frame.add(backBtn);

        // ===== REGISTER ACTION =====
        registerBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            // Input validation
            if (username.isEmpty() || password.isEmpty()) {
                msgLabel.setForeground(Color.RED);
                msgLabel.setText("Both fields are required.");
                return;
            }

            if (password.length() < 4) {
                msgLabel.setForeground(Color.RED);
                msgLabel.setText("Password must be at least 4 characters.");
                return;
            }

            try {
                Connection con = DBConnection.getConnection();

                // Check if username already exists
                PreparedStatement check = con.prepareStatement(
                    "SELECT username FROM users WHERE username = ?"
                );
                check.setString(1, username);
                ResultSet rs = check.executeQuery();

                if (rs.next()) {
                    msgLabel.setForeground(Color.RED);
                    msgLabel.setText("Username already taken.");
                    return;
                }

                // Insert new user
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users (username, password) VALUES (?, ?)"
                );
                ps.setString(1, username);
                ps.setString(2, password);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame,
                        "Account created! You can now log in.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    frame.dispose();
                    new LoginScreen(); // Go back to login
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                msgLabel.setForeground(Color.RED);
                msgLabel.setText("Database error. Try again.");
            }
        });

        // ===== BACK ACTION =====
        backBtn.addActionListener(e -> {
            frame.dispose();
            new LoginScreen();
        });

        frame.setVisible(true);
    }
}
