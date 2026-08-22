package com.abhimanyu.jobportal.dto;

public class LoginResponseDTO {

    private String token;
    private String email;
    private String role;

    // =========================
    // CONSTRUCTOR
    // =========================

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(
            String token,
            String email,
            String role) {

        this.token = token;
        this.email = email;
        this.role = role;
    }

    // =========================
    // GETTERS AND SETTERS
    // =========================

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}