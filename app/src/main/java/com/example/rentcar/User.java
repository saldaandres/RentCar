package com.example.rentcar;

public class User {
    private String name;
    private String email;
    private String password;
    // el rol es 1 para admin y 0 para usuarios
    private int role;
    private String reserveword;

    public User() {
    }

    public User(String name, String email, String password, int role, String reserveword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.reserveword = reserveword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getReserveword() {
        return reserveword;
    }

    public void setReserveword(String reserveword) {
        this.reserveword = reserveword;
    }
}
