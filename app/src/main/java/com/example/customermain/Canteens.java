package com.example.customermain;

public class Canteens {
    String name;
    String image;
    String key;
    String phone,token;
    public Canteens()
    {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Canteens(String name, String image, String key, String phone, String token) {
        this.name = name;
        this.image = image;
        this.key = key;
        this.phone = phone;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
