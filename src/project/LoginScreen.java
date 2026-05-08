package project;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginScreen {

    public LoginScreen() {

        JFrame frame = new JFrame("Login - Student Management System");
        frame.setSize(420, 300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen

        // ===== TITLE =====
        JLabel title = new JLabel("Login");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(170, 25, 120, 30);
        frame.add(title);

        // ===== USERNAME =====
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(60, 80, 100, 28);
        frame.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(160, 80, 180, 28);
        frame.add(userField);

        // ===== PASSWORD =====
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(60, 125, 100, 28);
        frame.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(160, 125, 180, 28);
        frame.add(passField);

        // ===== MESSAGE LABEL (for errors) =====
        JLabel msgLabel = new JLabel("");
        msgLabel.setForeground(Color.RED);
        msgLabel.setBounds(60, 165, 300, 25);
        frame.add(msgLabel);

        // ===== LOGIN BUTTON =====
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(100, 200, 100, 32);
        frame.add(loginBtn);

        // ===== REGISTER BUTTON =====
        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(215, 200, 100, 32);
        frame.add(registerBtn);

        // ===== LOGIN ACTION =====
        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                msgLabel.setText("Please enter both fields.");
                return;
            }

            if (checkLogin(username, password)) {
                frame.dispose();
                new Dashboard(); // Login successful, open dashboard
            } else {
                msgLabel.setText("Invalid username or password.");
                passField.setText("");
            }
        });

        // ===== REGISTER ACTION =====
        registerBtn.addActionListener(e -> {
            frame.dispose();
            new RegisterForm(); // Go to registration
        });

        frame.setVisible(true);
    }

    // ===== LOGIN CHECK: queries users table =====
    private boolean checkLogin(String username, String password) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // Returns true if a matching row is found

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
