package com.greengrowapps.jhiusers.listeners;

public interface OnLoginListener {
    void onLoginSuccess();
    void onLoginError(String error);
}
