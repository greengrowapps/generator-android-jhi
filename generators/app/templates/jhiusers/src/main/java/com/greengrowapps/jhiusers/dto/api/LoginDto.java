package com.greengrowapps.jhiusers.dto.api;

public class LoginDto {

    private String password;
    private String username;

    public LoginDto(){
        //Needed for jackson
    }
    public LoginDto(String username,String password){
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
