package com.canteen.db;

import com.canteen.model.Student;
import com.canteen.model.Admin;
import com.canteen.model.MenuItem;
import com.canteen.model.Order;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    
    // Connection details for canteen_db
    private static final String DB_URL = "jdbc:mysql://localhost:3306/canteen_db";
    private static final String USER = "root";
    private static final String PASS = "";

    /**
     * Loads the JDBC driver and establishes a connection to the database.
     */
    private Connection getConnection() throws SQLException {
        try {
            // Explicitly load the driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Handle driver not found exception
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            throw new SQLException("JDBC Driver not found", e);
        }
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * Validates student login credentials.
     * Returns a Student object on success, null on failure.
     */
    public Student validateStudentLogin(String email, String password) {
        String sql = "SELECT * FROM students WHERE email = ? AND password = ?";
        
        // try-with-resources ensures Connection and PreparedStatement are auto-closed
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            // try-with-resources for the ResultSet
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Login successful, return new Student object
                    return new Student(rs.getInt("student_id"), rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            // Log the database error
            e.printStackTrace();
        }
        return null; // Login failed
    }

    /**
     * Validates admin login credentials.
     * Returns an Admin object on success, null on failure.
     */
    public Admin validateAdminLogin(String email, String password) {
        String sql = "SELECT * FROM admins WHERE email = ? AND password = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Admin login successful
                    return new Admin(rs.getInt("admin_id"), rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Admin login failed
    }

    /**
     * Creates a new student account.
     * Returns true on success, false on failure (e.g., duplicate email).
     */
    public boolean createStudent(String email, String password) {
        String sql = "INSERT INTO students (email, password) VALUES (?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            int rowsAffected = pstmt.executeUpdate();
            
            // executeUpdate() returns the number of rows affected. 
            // If it's > 0, the insert was successful.
            return rowsAffected > 0;

        } catch (SQLException e) {
            // This will catch errors, including UNIQUE constraint violations (duplicate email)
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all menu items from the database.
     * Returns a List of MenuItem objects.
     */
    public List<MenuItem> getMenuItems() {
        List<MenuItem> menu = new ArrayList<>();
        String sql = "SELECT * FROM menu_items";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Loop through all rows in the result set
            while (rs.next()) {
                int id = rs.getInt("item_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                
                // Add new MenuItem to the list
                menu.add(new MenuItem(id, name, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menu; // Returns the list (will be empty if an error occurred)
    }

    /**
     * Saves a new order to the database.
     * Assumes Order object has a getSubtotal() method.
     */
    public boolean saveOrder(Order order, int studentId) {
        String sql = "INSERT INTO orders (student_id, total_amount) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setDouble(2, order.getSubtotal()); // Getting total from the Order object
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
