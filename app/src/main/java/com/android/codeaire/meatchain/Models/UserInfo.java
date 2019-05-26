package com.android.codeaire.meatchain.Models;

public class UserInfo {

    private String UserId;
    private String Name;
    private String Email;
    private String Password;
    private int Role;

    public UserInfo() {
    }

    public UserInfo(String userId, String name, String email, String password, int role) {
        UserId = userId;
        Name = name;
        Email = email;
        Password = password;
        Role = role;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getRole() {
        return Role;
    }

    public void setRole(int role) {
        Role = role;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
