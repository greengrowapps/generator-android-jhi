package com.greengrowapps.jhiusers.dto;

public class User {

    private String login;
    private String email;
    private final String password;
    private String firstName;
    private String lastName;

    public User(String login, String email, String firstName, String lastName, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public User(String login, String email, String firstName, String lastName) {
        this(login,email,firstName,lastName,null);
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName+" "+lastName;
    }
}
