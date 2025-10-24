package com.canteen.view;

import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {
    private JTextArea orderDisplayArea;
    private JPanel controlPanel;
    private JButton addItemButton;
    private JButton viewUsersButton;
    private JButton refreshOrdersButton;

    public AdminFrame() {
        setTitle("Canteen Admin Panel");
        
        setLayout(new BorderLayout(10, 10)); 
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
       
        orderDisplayArea = new JTextArea();
        orderDisplayArea.setEditable(false);
        orderDisplayArea.setText("--- Pending Orders ---\n\n" +
                                   "Order #123: 1x Biryani, 2x Coffee\n" +
                                   "Order #124: 1x Veg Sandwich");
        JScrollPane orderScrollPane = new JScrollPane(orderDisplayArea);
        orderScrollPane.setBorder(BorderFactory.createTitledBorder("Live Order Feed"));
        add(orderScrollPane, BorderLayout.CENTER);

        
        controlPanel = new JPanel();
        
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10)); 
        
        addItemButton = new JButton("Add Item");
        viewUsersButton = new JButton("View Users");
        refreshOrdersButton = new JButton("Refresh Orders");
        
        controlPanel.add(addItemButton);
        controlPanel.add(viewUsersButton);
        controlPanel.add(refreshOrdersButton);
        
        add(controlPanel, BorderLayout.SOUTH);
    }

    
    public JTextArea getOrderDisplayArea() { return orderDisplayArea; }
    public JButton getAddItemButton() { return addItemButton; }
    public JButton getViewUsersButton() { return viewUsersButton; }
    public JButton getRefreshOrdersButton() { return refreshOrdersButton; }
}
