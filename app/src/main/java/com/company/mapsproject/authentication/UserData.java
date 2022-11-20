package com.company.mapsproject.authentication;

public class UserData {
    String email,password;

    public UserData() {
    }

    public UserData( String email, String password) {

        this.email = email;

        this.password = password;
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

    @Override
    public String toString() {
        return "UserData{" +

                ", email='" + email + '\'' +

                ", password='" + password + '\'' +
                '}';
    }
}
