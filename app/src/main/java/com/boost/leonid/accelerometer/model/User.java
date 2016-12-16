package com.boost.leonid.accelerometer.model;

public class User {
    private String mUserName;
    private String mUserEmail;

    public User(String userName, String userEmail) {
        mUserName = userName;
        mUserEmail = userEmail;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }
}
