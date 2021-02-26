package com.example.fitnessapplication;

import java.util.List;

public class User {
    private String userName = "";
    private String email = "";
    private String password = "";
    private List<String> my_plans;

    public User() {
    }

    public User(String userName, String email, String password, List<String> my_plans) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.my_plans = my_plans;
    }

    public List<String> getMy_plans() {
        return my_plans;
    }

    public void setMy_plans(List<String> my_plans) {
        this.my_plans = my_plans;
    }

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
