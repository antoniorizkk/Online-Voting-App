package com.example.myproject;

public class User {
    String userId;
    String userEmail;
    private boolean hasVoted = false;

    public User(){
    }
    public User(String userId, String userEmail, Boolean hasVoted){
        this.userId = userId;
        this.userEmail = userEmail;
        this.hasVoted = hasVoted;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public boolean hasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
}
