package com.canteen.model;

public class Admin {
    private int adminId;
    private String email;

    public Admin(int adminId, String email) {
        this.adminId = adminId;
        this.email = email;
    }

    public int getAdminId() {
        return adminId;
    }

    public String getEmail() {
        return email;
    }
}
