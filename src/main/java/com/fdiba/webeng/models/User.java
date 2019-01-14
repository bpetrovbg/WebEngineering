package com.fdiba.webeng.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userID;

    @Size(min = 1)
    @Column(name = "username", unique = true, nullable = false)
    private String userName;

    @Size(min = 6)
    @Column(name = "password", nullable = false)
    private String userPassword;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "userphone", nullable = true)
    private String userPhone;

    @Column(name = "usersocialnetwork", nullable = true)
    private String userSocialNetwork;

    @ManyToMany
    private List<Interest> userInterestsList;

    public User() {
    }

    public User(String userName, String userPassword, String firstName, String lastName, String userPhone, String userSocialNetwork) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userPhone = userPhone;
        this.userSocialNetwork = userSocialNetwork;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String password) {
        this.userPassword = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserSocialNetwork(String userSocialNetwork) {
        this.userSocialNetwork = userSocialNetwork;
    }

    public void setUserInterests(List<Interest> interestsList) {
        this.userInterestsList = interestsList;
    }


    public Integer getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserFirstName() {
        return firstName;
    }

    public String getUserLastName() {
        return lastName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserSocialNetwork() {
        return userSocialNetwork;
    }

    public List<Interest> getUserInterestsList() {
        return userInterestsList;
    }

}