package com.canteen.controller;

import com.canteen.model.Admin;
import com.canteen.view.AdminFrame;
import com.canteen.view.LoginFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class AdminController implements ActionListener {

    private AdminFrame adminFrame;
    private Admin currentAdmin;

    public AdminController(AdminFrame adminFrame, Admin admin) {
        this.adminFrame = adminFrame;
        this.currentAdmin = admin;
        
        this.adminFrame.getLogoutButton().addActionListener(this);
        this.adminFrame.getAddItemButton().addActionListener(this);
        this.adminFrame.getRemoveItemButton().addActionListener(this);
        
        loadAdminDetails();
    }

    private void loadAdminDetails() {
        adminFrame.getOrdersArea().setText("Welcome, " + currentAdmin.getEmail() + "\n\n"
            + "Order management and item management features are pending implementation.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == adminFrame.getLogoutButton()) {
            adminFrame.dispose();
            LoginFrame loginFrame = new LoginFrame();
            new LoginController(loginFrame);
            loginFrame.setVisible(true);
        } else if (e.getSource() == adminFrame.getAddItemButton()) {
            // Future logic:
             JOptionPane.showMessageDialog(adminFrame, "Add Item feature not yet implemented.");
        } else if (e.getSource() == adminFrame.getRemoveItemButton()) {
            // Future logic:
            JOptionPane.showMessageDialog(adminFrame, "Remove Item feature not yet implemented.");
        }
    }
}
