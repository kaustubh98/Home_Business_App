package com.example.admin.homebusiness;

public class UserData {
    private String name,email,number,address;

    public UserData(String name, String email, String number, String address) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.address = address;
    }

    public UserData() {
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
