package com.example.profile;

public class MyData {
   private String  Email;
   private String password;
   private String Mobile_NO;
   private String Licence;

    public MyData() {
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile_NO() {
        return Mobile_NO;
    }

    public void setMobile_NO(String mobile_NO) {
        Mobile_NO = mobile_NO;
    }

    public String getLicence() {
        return Licence;
    }

    public void setLicence(String licence) {
        Licence = licence;
    }
}
