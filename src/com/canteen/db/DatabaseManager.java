package com.canteen.db;

import com.canteen.model.Admin;
import com.canteen.model.MenuItem;
import com.canteen.model.Order;
import com.canteen.model.OrderItem;
import com.canteen.model.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/canteen_db";
    private static final String USER = "root";
    private static final String PASS = "";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            throw new SQLException("JDBC Driver not found", e);
        }
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public Student validateStudentLogin(String email, String password) {
        String sql = "SELECT * FROM students WHERE email = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(rs.getInt("student_id"), rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Admin validateAdminLogin(String email, String password) {
        String sql = "SELECT * FROM admins WHERE email = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Admin(rs.getInt("admin_id"), rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createStudent(String email, String password) {
        String sql = "INSERT INTO students (email, password) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<MenuItem> getMenuItems() {
        List<MenuItem> menu = new ArrayList<>();
        String sql = "SELECT * FROM menu_items";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                menu.add(new MenuItem(
                    rs.getInt("item_id"), 
                    rs.getString("name"), 
                    rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menu;
    }

    public boolean saveOrder(Order order, int studentId) {
        
        String sqlOrder = "INSERT INTO orders (student_id, total_amount) VALUES (?, ?)";
        String sqlDetails = "INSERT INTO order_details (order_id, item_id, quantity, item_price) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmtOrder = null;
        PreparedStatement pstmtDetails = null;
        ResultSet generatedKeys = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false); 

            pstmtOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            pstmtOrder.setInt(1, studentId);
            pstmtOrder.setDouble(2, order.getTotalAmount());
            
            int rowsAffected = pstmtOrder.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            int newOrderId = -1;
            generatedKeys = pstmtOrder.getGeneratedKeys();
            if (generatedKeys.next()) {
                newOrderId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }
            
            pstmtDetails = conn.prepareStatement(sqlDetails);
            
            for (OrderItem item : order.getItems()) {
                pstmtDetails.setInt(1, newOrderId);
                pstmtDetails.setInt(2, item.getItemId());
                pstmtDetails.setInt(3, item.getQuantity());
                pstmtDetails.setDouble(4, item.getPriceAtPurchase());
                pstmtDetails.addBatch();
            }
            
            pstmtDetails.executeBatch();

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (pstmtOrder != null) pstmtOrder.close();
                if (pstmtDetails != null) pstmtDetails.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
