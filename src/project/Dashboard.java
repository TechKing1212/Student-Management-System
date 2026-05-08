package project;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

import org.jfree.chart.*;
import org.jfree.data.category.DefaultCategoryDataset;

public class Dashboard {

    JFrame frame;
    JTable table;
    DefaultTableModel model;
    CardLayout cardLayout;
    JPanel contentPanel;

    public Dashboard() {

        frame = new JFrame("Student Management System");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Center on screen

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel(new GridLayout(3, 1));
        sidebar.setBackground(new Color(25, 25, 25));
        sidebar.setPreferredSize(new Dimension(200, 0));

        JButton dashBtn     = createSidebarButton("Dashboard");
        JButton studentsBtn = createSidebarButton("Students");
        JButton logoutBtn   = createSidebarButton("Logout");

        sidebar.add(dashBtn);
        sidebar.add(studentsBtn);
        sidebar.add(logoutBtn);

        // ===== CARD LAYOUT CONTENT =====
        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createDashboardPanel(), "Dashboard");
        contentPanel.add(createStudentsPanel(),  "Students");

        // ===== SIDEBAR BUTTON ACTIONS =====
        dashBtn.addActionListener(e -> cardLayout.show(contentPanel, "Dashboard"));
        studentsBtn.addActionListener(e -> cardLayout.show(contentPanel, "Students"));

        // Logout: go back to login screen
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            new LoginScreen();
        });

        frame.add(sidebar,       BorderLayout.WEST);
        frame.add(contentPanel,  BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // ===== SIDEBAR BUTTON STYLE =====
    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(40, 40, 40));
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        return btn;
    }

    // ===== DASHBOARD PANEL =====
    private JPanel createDashboardPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));

        JLabel title = new JLabel("Student Performance Chart", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));

        panel.add(title,        BorderLayout.NORTH);
        panel.add(createChart(), BorderLayout.CENTER);

        return panel;
    }

    // ===== BAR CHART =====
    private JPanel createChart() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            Connection con = DBConnection.getConnection();
            if (con != null) {
                ResultSet rs = con.createStatement().executeQuery(
                    "SELECT name, (sub1+sub2+sub3+sub4+sub5) AS total FROM students"
                );
                while (rs.next()) {
                    dataset.addValue(rs.getDouble("total"), "Total Marks", rs.getString("name"));
                }
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "Student Performance",
            "Students",
            "Total Marks",
            dataset
        );

        return new ChartPanel(chart);
    }

    // ===== STUDENTS PANEL =====
    private JPanel createStudentsPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));

        JLabel title = new JLabel("Student Records", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(12, 0, 8, 0));

        // ===== TABLE =====
        String[] cols = {
            "ID", "Name", "Attendance",
            "Sub1", "Sub2", "Sub3", "Sub4", "Sub5",
            "Total", "Average", "Grade"
        };

        model = new DefaultTableModel(cols, 0) {
            // Make table cells non-editable directly (edit via Update button)
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(24);
        JScrollPane sp = new JScrollPane(table);

        loadData(); // Load from database on startup

        // ===== BUTTONS =====
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(30, 30, 30));

        JButton addBtn     = new JButton("Add Student");
        JButton updateBtn  = new JButton("Update");
        JButton deleteBtn  = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh Chart");

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        // ===== BUTTON ACTIONS =====
        addBtn.addActionListener(e    -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        refreshBtn.addActionListener(e -> {
            loadData();
            // Rebuild the dashboard chart panel with fresh data
            contentPanel.remove(contentPanel.getComponent(0));
            contentPanel.add(createDashboardPanel(), "Dashboard", 0);
            contentPanel.revalidate();
        });

        panel.add(title,    BorderLayout.NORTH);
        panel.add(sp,       BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ===== LOAD DATA FROM DATABASE =====
    private void loadData() {
        try {
            model.setRowCount(0); // Clear existing rows

            Connection con = DBConnection.getConnection();
            if (con == null) return;

            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM students");

            while (rs.next()) {
                double s1    = rs.getDouble("sub1");
                double s2    = rs.getDouble("sub2");
                double s3    = rs.getDouble("sub3");
                double s4    = rs.getDouble("sub4");
                double s5    = rs.getDouble("sub5");
                double total = s1 + s2 + s3 + s4 + s5;
                double avg   = total / 5.0;

                // Grade logic
                String grade;
                if      (avg >= 80) grade = "A";
                else if (avg >= 60) grade = "B";
                else                grade = "C";

                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("attendance"),
                    s1, s2, s3, s4, s5,
                    total,
                    String.format("%.1f", avg),
                    grade
                });
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== ADD STUDENT =====
    private void addStudent() {

        JTextField nameField = new JTextField();
        JTextField attField  = new JTextField();
        JTextField s1Field   = new JTextField();
        JTextField s2Field   = new JTextField();
        JTextField s3Field   = new JTextField();
        JTextField s4Field   = new JTextField();
        JTextField s5Field   = new JTextField();

        Object[] fields = {
            "Name:",       nameField,
            "Attendance:", attField,
            "Subject 1:",  s1Field,
            "Subject 2:",  s2Field,
            "Subject 3:",  s3Field,
            "Subject 4:",  s4Field,
            "Subject 5:",  s5Field
        };

        int result = JOptionPane.showConfirmDialog(
            frame, fields, "Add New Student", JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            // ===== INPUT VALIDATION =====
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Name cannot be empty.");
                return;
            }

            try {
                double att = Double.parseDouble(attField.getText().trim());
                double s1  = Double.parseDouble(s1Field.getText().trim());
                double s2  = Double.parseDouble(s2Field.getText().trim());
                double s3  = Double.parseDouble(s3Field.getText().trim());
                double s4  = Double.parseDouble(s4Field.getText().trim());
                double s5  = Double.parseDouble(s5Field.getText().trim());

                // Range check: marks should be 0-100
                if (s1 < 0 || s1 > 100 || s2 < 0 || s2 > 100 ||
                    s3 < 0 || s3 > 100 || s4 < 0 || s4 > 100 || s5 < 0 || s5 > 100) {
                    JOptionPane.showMessageDialog(frame, "Marks must be between 0 and 100.");
                    return;
                }

                if (att < 0 || att > 100) {
                    JOptionPane.showMessageDialog(frame, "Attendance must be between 0 and 100.");
                    return;
                }

                Connection con = DBConnection.getConnection();
                if (con == null) return;

                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO students (name, attendance, sub1, sub2, sub3, sub4, sub5) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)"
                );
                ps.setString(1, name);
                ps.setDouble(2, att);
                ps.setDouble(3, s1);
                ps.setDouble(4, s2);
                ps.setDouble(5, s3);
                ps.setDouble(6, s4);
                ps.setDouble(7, s5);

                ps.executeUpdate();
                con.close();
                loadData();

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter valid numbers for attendance and marks.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // ===== UPDATE STUDENT =====
    private void updateStudent() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a student to update.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        // Pre-fill fields with existing values
        JTextField nameField = new JTextField(model.getValueAt(row, 1).toString());
        JTextField attField  = new JTextField(model.getValueAt(row, 2).toString());
        JTextField s1Field   = new JTextField(model.getValueAt(row, 3).toString());
        JTextField s2Field   = new JTextField(model.getValueAt(row, 4).toString());
        JTextField s3Field   = new JTextField(model.getValueAt(row, 5).toString());
        JTextField s4Field   = new JTextField(model.getValueAt(row, 6).toString());
        JTextField s5Field   = new JTextField(model.getValueAt(row, 7).toString());

        Object[] fields = {
            "Name:",       nameField,
            "Attendance:", attField,
            "Subject 1:",  s1Field,
            "Subject 2:",  s2Field,
            "Subject 3:",  s3Field,
            "Subject 4:",  s4Field,
            "Subject 5:",  s5Field
        };

        int result = JOptionPane.showConfirmDialog(
            frame, fields, "Update Student (ID: " + id + ")", JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Name cannot be empty.");
                return;
            }

            try {
                double att = Double.parseDouble(attField.getText().trim());
                double s1  = Double.parseDouble(s1Field.getText().trim());
                double s2  = Double.parseDouble(s2Field.getText().trim());
                double s3  = Double.parseDouble(s3Field.getText().trim());
                double s4  = Double.parseDouble(s4Field.getText().trim());
                double s5  = Double.parseDouble(s5Field.getText().trim());

                if (s1 < 0 || s1 > 100 || s2 < 0 || s2 > 100 ||
                    s3 < 0 || s3 > 100 || s4 < 0 || s4 > 100 || s5 < 0 || s5 > 100) {
                    JOptionPane.showMessageDialog(frame, "Marks must be between 0 and 100.");
                    return;
                }

                if (att < 0 || att > 100) {
                    JOptionPane.showMessageDialog(frame, "Attendance must be between 0 and 100.");
                    return;
                }

                Connection con = DBConnection.getConnection();
                if (con == null) return;

                PreparedStatement ps = con.prepareStatement(
                    "UPDATE students SET name=?, attendance=?, sub1=?, sub2=?, sub3=?, sub4=?, sub5=? " +
                    "WHERE id=?"
                );
                ps.setString(1, name);
                ps.setDouble(2, att);
                ps.setDouble(3, s1);
                ps.setDouble(4, s2);
                ps.setDouble(5, s3);
                ps.setDouble(6, s4);
                ps.setDouble(7, s5);
                ps.setInt(8, id);

                ps.executeUpdate();
                con.close();
                loadData();

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter valid numbers for attendance and marks.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // ===== DELETE STUDENT =====
    private void deleteStudent() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a student to delete.");
            return;
        }

        String name = model.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(
            frame,
            "Are you sure you want to delete \"" + name + "\"?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) model.getValueAt(row, 0);

                Connection con = DBConnection.getConnection();
                if (con == null) return;

                PreparedStatement ps = con.prepareStatement("DELETE FROM students WHERE id=?");
                ps.setInt(1, id);
                ps.executeUpdate();
                con.close();
                loadData();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
