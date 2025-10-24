package com.canteen.controller;

import com.canteen.db.DatabaseManager;
import com.canteen.model.Bill;
import com.canteen.model.MenuItem;
import com.canteen.model.Order;
import com.canteen.model.OrderItem;
import com.canteen.model.Student;
import com.canteen.view.CanteenFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanteenController implements ActionListener {

    private CanteenFrame canteenFrame;
    private DatabaseManager dbManager;
    private Student currentStudent;
    private Map<MenuItem, JSpinner> menuItemsMap;

    public CanteenController(CanteenFrame canteenFrame, Student student) {
        this.canteenFrame = canteenFrame;
        this.currentStudent = student;
        this.dbManager = new DatabaseManager();
        this.menuItemsMap = new HashMap<>();
        this.canteenFrame.getPlaceOrderButton().addActionListener(this);
        
        loadStudentDetails();
        loadMenuItems();
    }

    private void loadStudentDetails() {
        canteenFrame.getWelcomeLabel().setText("Welcome, " + currentStudent.getEmail());
    }

    private void loadMenuItems() {
        JPanel menuPanel = canteenFrame.getMenuPanel();
        menuPanel.removeAll(); 

        try {
            List<MenuItem> items = dbManager.getMenuItems();
            for (MenuItem item : items) {
                JPanel itemPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
                JCheckBox checkBox = new JCheckBox(item.getName() + " (Rs. " + item.getPrice() + ")");
                JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
                
                itemPanel.add(checkBox);
                itemPanel.add(spinner);
                menuPanel.add(itemPanel);
                
                menuItemsMap.put(item, spinner);
                
                checkBox.addActionListener(e -> updateBill());
                spinner.addChangeListener(e -> updateBill());
            }
            menuPanel.revalidate();
            menuPanel.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(canteenFrame, "Failed to load menu.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Order createOrderFromSelection() {
        Order order = new Order();
        for (Map.Entry<MenuItem, JSpinner> entry : menuItemsMap.entrySet()) {
            JCheckBox checkBox = (JCheckBox)((JPanel)entry.getValue().getParent()).getComponent(0);
            if (checkBox.isSelected()) {
                MenuItem item = entry.getKey();
                int quantity = (int) entry.getValue().getValue();
                order.addItem(new OrderItem(item, quantity));
            }
        }
        return order;
    }

    private void updateBill() {
        Order order = createOrderFromSelection();
        if (order.getItems().isEmpty()) {
            canteenFrame.getBillArea().setText("Select items to see your bill.");
            return;
        }
        Bill bill = new Bill(order);
        canteenFrame.getBillArea().setText(bill.generateBillText());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == canteenFrame.getPlaceOrderButton()) {
            handlePlaceOrder();
        }
    }

    private void handlePlaceOrder() {
        Order order = createOrderFromSelection();
        
        if (order.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(canteenFrame, "Please select at least one item to order.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean success = dbManager.saveOrder(order, currentStudent.getStudentId());
            if (success) {
                JOptionPane.showMessageDialog(canteenFrame, "Order placed successfully!");
                canteenFrame.getBillArea().setText("");
                loadMenuItems(); 
            } else {
                JOptionPane.showMessageDialog(canteenFrame, "Failed to place order.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(canteenFrame, "Database error while placing order.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
