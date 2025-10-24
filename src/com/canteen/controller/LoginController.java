package com.canteen.controller;

import com.canteen.db.DatabaseManager;
import com.canteen.model.Admin;
import com.canteen.model.Student;
import com.canteen.view.CanteenFrame;
import com.canteen.view.LoginFrame;
import com.canteen.view.SignUpFrame;
import com.canteen.view.AdminFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class LoginController implements ActionListener {

    private LoginFrame loginFrame;
    private DatabaseManager dbManager;

    public LoginController(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        this.dbManager = new DatabaseManager();
        this.loginFrame.getLoginButton().addActionListener(this);
        this.loginFrame.getSignUpButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginFrame.getSignUpButton()) {
            openSignUpFrame();
        } else if (e.getSource() == loginFrame.getLoginButton()) {
            handleLogin();
        }
    }

    private void openSignUpFrame() {
        loginFrame.dispose();
        SignUpFrame signUpFrame = new SignUpFrame();
        new SignUpController(signUpFrame); 
        signUpFrame.setVisible(true);
    }

    private void handleLogin() {
        String email = loginFrame.getEmailField().getText();
        String password = new String(loginFrame.getPasswordField().getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginFrame, "Email and Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (loginFrame.getStudentRadio().isSelected()) {
                Student student = dbManager.validateStudentLogin(email, password);
                if (student != null) {
                    loginFrame.dispose();
                    CanteenFrame canteenFrame = new CanteenFrame();
                    new CanteenController(canteenFrame, student); 
                    canteenFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid Student Email or Password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } else if (loginFrame.getAdminRadio().isSelected()) {
                Admin admin = dbManager.validateAdminLogin(email, password);
                if (admin != null) {
                    loginFrame.dispose();
                    AdminFrame adminFrame = new AdminFrame();
                    new AdminController(adminFrame, admin);
                    adminFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid Admin Email or Password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(loginFrame, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
