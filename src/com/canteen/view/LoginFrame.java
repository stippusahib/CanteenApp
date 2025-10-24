package com.canteen.view;

import javax.swing.*;
import java.awt.GridLayout;


public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JRadioButton studentRadio;
    private JRadioButton adminRadio;
    private ButtonGroup roleGroup;
    private JButton loginButton;
    private JButton signUpButton;

    public LoginFrame() {
        setTitle("Canteen Login");
       
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        
        setLayout(new GridLayout(5, 2, 10, 10));

        
        emailField = new JTextField();
        passwordField = new JPasswordField();
        
        studentRadio = new JRadioButton("Student", true);
        adminRadio = new JRadioButton("Admin");
        roleGroup = new ButtonGroup();
        roleGroup.add(studentRadio);
        roleGroup.add(adminRadio);
        
        loginButton = new JButton("Login");
        signUpButton = new JButton("Sign Up");

        
        add(new JLabel("Email:"));
        add(emailField);
        
        add(new JLabel("Password:"));
        add(passwordField);
        
        
        add(studentRadio);
        add(adminRadio);
        
       
        add(new JLabel("")); 
        add(new JLabel("")); 

        
        add(loginButton);
        add(signUpButton);
        
        
        pack();
        setSize(400, 300); 
    }

    
    public JTextField getEmailField() { return emailField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JRadioButton getStudentRadio() { return studentRadio; }
    public JButton getLoginButton() { return loginButton; }
    public JButton getSignUpButton() { return signUpButton; }
}
