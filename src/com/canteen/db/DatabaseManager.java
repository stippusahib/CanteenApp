package com.canteen.db;

import com.canteen.model.Admin;
import com.canteen.model.AdminOrderView;
import com.canteen.model.MenuItem;
import com.canteen.model.Order;
import com.canteen.model.OrderItem;
import com.canteen.model.Student;
import com.canteen.model.TransactionHistoryView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/canteen";
    private static final String USER = "root";
    private static final String PASS = "";

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
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
        } catch (SQLException | ClassNotFoundException e) {
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createStudent(String email, String password) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO students (email, password) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<MenuItem> getMenuItems() {
        List<MenuItem> menu = new ArrayList<>();
        String sql = "SELECT * FROM menu_items WHERE is_available = TRUE ORDER BY item_id";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                menu.add(new MenuItem(
                    rs.getInt("item_id"), rs.getString("name"),
                    rs.getDouble("price"), rs.getBoolean("is_available")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return menu;
    }

    public int saveOrder(Order order, int studentId) {
        String sqlInsertOrder = "INSERT INTO orders (student_id, total_amount) VALUES (?, ?)";
        String sqlInsertDetails = "INSERT INTO order_details (order_id, item_id, quantity, item_price) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        int newOrderId = -1;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmtOrder = conn.prepareStatement(sqlInsertOrder, Statement.RETURN_GENERATED_KEYS)) {
                pstmtOrder.setInt(1, studentId);
                pstmtOrder.setDouble(2, order.getTotalAmount());
                pstmtOrder.executeUpdate();
                try (ResultSet generatedKeys = pstmtOrder.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newOrderId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }
            try (PreparedStatement pstmtDetails = conn.prepareStatement(sqlInsertDetails)) {
                for (OrderItem item : order.getItems()) {
                    pstmtDetails.setInt(1, newOrderId);
                    pstmtDetails.setInt(2, item.getItem().getItemId());
                    pstmtDetails.setInt(3, item.getQuantity());
                    pstmtDetails.setDouble(4, item.getItem().getPrice());
                    pstmtDetails.addBatch();
                }
                pstmtDetails.executeBatch();
            }
            conn.commit();
            return newOrderId;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            return -1;
        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    public boolean completeOrder(int orderId) {
        String sql = "UPDATE orders SET is_completed = TRUE WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<AdminOrderView> getAllOrders() {
        List<AdminOrderView> allOrders = new ArrayList<>();
        String sql = "SELECT o.order_id, s.email, o.total_amount, o.order_date, o.is_completed, " +
                     "IFNULL(GROUP_CONCAT(CONCAT(od.quantity, 'x ', mi.name) SEPARATOR ', '), 'Items no longer available') AS items_summary " +
                     "FROM orders o " +
                     "JOIN students s ON o.student_id = s.student_id " +
                     "LEFT JOIN order_details od ON o.order_id = od.order_id " +
                     "LEFT JOIN menu_items mi ON od.item_id = mi.item_id " +
                     "GROUP BY o.order_id, s.email, o.total_amount, o.order_date, o.is_completed " +
                     "ORDER BY o.order_date DESC";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                allOrders.add(new AdminOrderView(
                    rs.getInt("order_id"), rs.getString("email"), rs.getDouble("total_amount"),
                    rs.getTimestamp("order_date"), rs.getBoolean("is_completed"), rs.getString("items_summary")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return allOrders;
    }

    public List<TransactionHistoryView> getTransactionHistory() {
        List<TransactionHistoryView> transactions = new ArrayList<>();
        String sql = "SELECT o.order_id, s.email, o.total_amount, o.order_date " +
                     "FROM orders o " +
                     "JOIN students s ON o.student_id = s.student_id " +
                     "ORDER BY o.order_date DESC";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                transactions.add(new TransactionHistoryView(
                    rs.getInt("order_id"), rs.getString("email"),
                    rs.getDouble("total_amount"), rs.getTimestamp("order_date")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menu = new ArrayList<>();
        String sql = "SELECT * FROM menu_items ORDER BY item_id";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                menu.add(new MenuItem(
                    rs.getInt("item_id"), rs.getString("name"),
                    rs.getDouble("price"), rs.getBoolean("is_available")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return menu;
    }

    public boolean toggleItemAvailability(int itemId, boolean makeAvailable) {
        String sql = "UPDATE menu_items SET is_available = ? WHERE item_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, makeAvailable);
            pstmt.setInt(2, itemId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addMenuItem(String name, double price) {
        String sql = "INSERT INTO menu_items (name, price, is_available) VALUES (?, ?, TRUE)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isItemInAnyOrder(int itemId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM order_details WHERE item_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public int deleteMenuItem(int itemId) {
        try {
            if (isItemInAnyOrder(itemId)) {
                return -3;
            }
            String sql = "DELETE FROM menu_items WHERE item_id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, itemId);
                int rowsAffected = pstmt.executeUpdate();
                return (rowsAffected > 0) ? 1 : 0;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
