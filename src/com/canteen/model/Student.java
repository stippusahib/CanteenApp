package com.canteen.model;

public class Student {
    private int studentId;
    private String email;

    public Student(int studentId, String email) {
        this.studentId = studentId;
        this.email = email;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getEmail() {
        return email;
    }
}
