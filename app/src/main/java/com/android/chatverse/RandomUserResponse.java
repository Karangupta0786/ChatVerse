package com.android.chatverse;

public class RandomUserResponse {
    private String email;

    public RandomUserResponse() {
        // Default constructor
    }

    public RandomUserResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "email='" + email + '\'' +
                '}';
    }
}
