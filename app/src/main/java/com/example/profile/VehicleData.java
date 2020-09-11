package com.example.profile;

public class VehicleData {
    private String name;
 private String number;
    private String model ;
 private String year;
 private String mobile;
 private String email;

    private String img;


    public VehicleData() {
    }

    public VehicleData(String name, String number, String model, String year, String mobile, String email, String img) {
        this.name = name;
        this.number = number;
        this.model = model;
        this.year = year;
        this.mobile = mobile;
        this.email = email;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
