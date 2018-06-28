package com.greengrowapps.jhiusers.dto.api;

public class LoginResponseDto {
    public String id_token;

    public LoginResponseDto(){

    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }
}
