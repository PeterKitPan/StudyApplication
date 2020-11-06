package com.test.mvp.models;

/**
 * 登陆用户
 */
public class User {
    private String userName;
    private String workNO;

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", workNO='" + workNO + '\'' +
                '}';
    }

    public String getWorkNO() {
        return workNO;
    }

    public void setWorkNO(String workNO) {
        this.workNO = workNO;
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User(String userName, String workNO) {

        this.userName = userName;
        this.workNO = workNO;
    }
}
