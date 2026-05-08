-- ============================================================
--  OOP PROJECT - Student Management System
--  Run this script in MySQL Workbench or the MySQL CLI
--  before launching the application.
-- ============================================================

-- Step 1: Create the database
CREATE DATABASE IF NOT EXISTS oopproject;

USE oopproject;

-- Step 2: Create the students table
CREATE TABLE IF NOT EXISTS students (
    id         INT           AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100)  NOT NULL,
    attendance DOUBLE        DEFAULT 0,
    sub1       DOUBLE        DEFAULT 0,
    sub2       DOUBLE        DEFAULT 0,
    sub3       DOUBLE        DEFAULT 0,
    sub4       DOUBLE        DEFAULT 0,
    sub5       DOUBLE        DEFAULT 0
);

-- Step 3: Create the users table (for login)
CREATE TABLE IF NOT EXISTS users (
    id       INT          AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- Step 4: Insert a default admin user so you can log in right away
--  Username: admin   |   Password: admin123
INSERT INTO users (username, password)
VALUES ('admin', 'admin123');

-- Step 5: (Optional) Insert some sample students for the chart demo
INSERT INTO students (name, attendance, sub1, sub2, sub3, sub4, sub5) VALUES
('Vidushi Rawat',  92, 88, 76, 91, 85, 79),
('Rachit Singhal', 85, 72, 68, 80, 77, 83),
('Sarthak Jain',   78, 65, 70, 74, 69, 72),
('Sample Student', 90, 95, 92, 88, 97, 90);

-- Done! You can now run the Java application.
