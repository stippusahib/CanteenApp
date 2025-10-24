package com.canteen.view;

import javax.swing.*;
import java.awt.*; 

public class SignUpFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton signUpButton;

    public SignUpFrame() {
        setTitle("Canteen Sign Up");
        setSize(400, 250); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null); 
        setLayout(new GridLayout(4, 2, 10, 10)); 

        
        emailField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        signUpButton = new JButton("Sign Up");

       
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("Confirm Password:"));
        add(confirmPasswordField);
        add(new JLabel(""));
        add(signUpButton);
    }

  
    public JTextField getEmailField() { return emailField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JPasswordField getConfirmPasswordField() { return confirmPasswordField; }
    public JButton getSignUpButton() { return signUpButton; }
}

