package project;

import javax.swing.*;
import java.awt.*;

public class OpeningScreen {

    public OpeningScreen() {

        JFrame frame = new JFrame("OOP Group Project");
        frame.setSize(500, 380);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen

        // ===== MAIN TITLE =====
        JLabel title = new JLabel("OOP Group Project");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(130, 30, 300, 35);
        frame.add(title);

        // ===== SUBTITLE =====
        JLabel subtitle = new JLabel("Student Management System");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setBounds(115, 70, 300, 25);
        frame.add(subtitle);

        // ===== DIVIDER LABEL =====
        JLabel membersLabel = new JLabel("Group Members:");
        membersLabel.setFont(new Font("Arial", Font.BOLD, 14));
        membersLabel.setBounds(165, 115, 200, 25);
        frame.add(membersLabel);

        // ===== GROUP MEMBERS (UPDATED NAMES) =====
        JLabel name1 = new JLabel("Vidushi Rawat");
        name1.setFont(new Font("Arial", Font.PLAIN, 14));
        name1.setBounds(175, 145, 200, 25);
        frame.add(name1);

        JLabel name2 = new JLabel("Shreyansh Jaiswal");
        name2.setFont(new Font("Arial", Font.PLAIN, 14));
        name2.setBounds(175, 170, 200, 25);
        frame.add(name2);

        JLabel name3 = new JLabel("Sarthak Jain");
        name3.setFont(new Font("Arial", Font.PLAIN, 14));
        name3.setBounds(175, 195, 200, 25);
        frame.add(name3);

        // ===== NEXT BUTTON =====
        JButton nextBtn = new JButton("Next →");
        nextBtn.setBounds(180, 270, 140, 38);
        nextBtn.setFont(new Font("Arial", Font.BOLD, 14));
        frame.add(nextBtn);

        // ===== BUTTON ACTION: go to Login =====
        nextBtn.addActionListener(e -> {
            frame.dispose();
            new LoginScreen(); // Go to login before dashboard
        });

        frame.setVisible(true);
    }
}
