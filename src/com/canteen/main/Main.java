package com.canteen.main;

import com.canteen.controller.LoginController;
import com.canteen.view.LoginFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        
        // This ensures the GUI runs on the correct thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            
            // This is new: it creates the controller and
            // attaches it to the view right at the start.
            new LoginController(loginFrame); 
            
            loginFrame.setVisible(true);
        });
    }
}
