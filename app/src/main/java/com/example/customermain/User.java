package com.example.customermain;

public class User {
    String name;
    String email;
    String mobile_number;
    String token;
    String uid;
    String address;

    public User() {
    }

    public User(String name, String email, String mobile_number, String token, String uid, String address) {
        this.name = name;
        this.email = email;
        this.mobile_number = mobile_number;
        this.token = token;
        this.uid = uid;
        this.address = address;
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

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
