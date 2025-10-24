package com.canteen.view;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;

public class CanteenFrame extends JFrame {
    private JTextArea billArea;
    private JButton placeOrderButton;
    private JPanel menuPanel;
    
    
    private JCheckBox item1, item2, item3;
    private JTextField qty1, qty2, qty3;

    public CanteenFrame() {
        setTitle("Canteen Menu & Ordering");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        
        setLayout(new BorderLayout(10, 10)); 

       
        billArea = new JTextArea(25, 20);
        billArea.setEditable(false);
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        billArea.setText("--- Your Bill ---\n\n");
        JScrollPane billScrollPane = new JScrollPane(billArea);
        add(billScrollPane, BorderLayout.EAST);

       
        placeOrderButton = new JButton("Place Order");
        JPanel southPanel = new JPanel(); // Use panel for padding/centering
        southPanel.add(placeOrderButton);
        add(southPanel, BorderLayout.SOUTH);

        
        menuPanel = new JPanel();
        
        menuPanel.setLayout(new GridLayout(0, 4, 10, 10)); 
        
       
        menuPanel.add(new JLabel("Select"));
        menuPanel.add(new JLabel("Item Name", SwingConstants.CENTER));
        menuPanel.add(new JLabel("Quantity", SwingConstants.CENTER));
        menuPanel.add(new JLabel("Price (INR)"));

       
        item1 = new JCheckBox();
        qty1 = new JTextField("0", 3);
        menuPanel.add(item1);
        menuPanel.add(new JLabel("Chicken Sandwich"));
        menuPanel.add(qty1);
        menuPanel.add(new JLabel("50.00"));

        item2 = new JCheckBox();
        qty2 = new JTextField("0", 3);
        menuPanel.add(item2);
        menuPanel.add(new JLabel("Veg Puff"));
        menuPanel.add(qty2);
        menuPanel.add(new JLabel("20.00"));
        
        item3 = new JCheckBox();
        qty3 = new JTextField("0", 3);
        menuPanel.add(item3);
        menuPanel.add(new JLabel("Filter Coffee"));
        menuPanel.add(qty3);
        menuPanel.add(new JLabel("15.00"));

        JScrollPane menuScrollPane = new JScrollPane(menuPanel);
        add(menuScrollPane, BorderLayout.CENTER);
    }

   
    public JTextArea getBillArea() { return billArea; }
    public JButton getPlaceOrderButton() { return placeOrderButton; }
    public JPanel getMenuPanel() { return menuPanel; }
   
    public JCheckBox getItem1() { return item1; }
    public JTextField getQty1() { return qty1; }
}
