package br.com.motus.models;

import androidx.annotation.NonNull;

public class User {
    private String phone;
    private String email;
    private String password;

    public User(String phone, String email, String password) {
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public boolean isValidPhone() {
        return phone != null && phone.matches("\\d{10,15}");
    }

    public boolean isValidEmail() {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    public boolean isValidPassword() {
        return password != null && password.length() >= 6;
    }

    public boolean isValid() {
        return isValidPhone() && isValidEmail() && isValidPassword();
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}