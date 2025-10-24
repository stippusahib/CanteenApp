package com.canteen.controller;

import com.canteen.db.DatabaseManager;
import com.canteen.view.LoginFrame;
import com.canteen.view.SignUpFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class SignUpController implements ActionListener {

    private SignUpFrame signUpFrame;
    private DatabaseManager dbManager;

    public SignUpController(SignUpFrame signUpFrame) {
        this.signUpFrame = signUpFrame;
        this.dbManager = new DatabaseManager();
        this.signUpFrame.getSignUpButton().addActionListener(this);
        this.signUpFrame.getBackButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signUpFrame.getSignUpButton()) {
            handleSignUp();
        } else if (e.getSource() == signUpFrame.getBackButton()) {
            openLoginFrame();
        }
    }

    private void openLoginFrame() {
        signUpFrame.dispose();
        LoginFrame loginFrame = new LoginFrame();
        new LoginController(loginFrame);
        loginFrame.setVisible(true);
    }

    private void handleSignUp() {
        String email = signUpFrame.getEmailField().getText();
        String password = new String(signUpFrame.getPasswordField().getPassword());
        String confirmPassword = new String(signUpFrame.getConfirmPasswordField().getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(signUpFrame, "Fields cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.toLowerCase().endsWith("@ktu.edu")) {
            JOptionPane.showMessageDialog(signUpFrame, "Email must be a valid @ktu.edu address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(signUpFrame, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean success = dbManager.createStudent(email, password);
            if (success) {
                JOptionPane.showMessageDialog(signUpFrame, "Registration Successful! Please log in.");
                openLoginFrame();
            } else {
                JOptionPane.showMessageDialog(signUpFrame, "Email already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(signUpFrame, "Database error during sign up.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
