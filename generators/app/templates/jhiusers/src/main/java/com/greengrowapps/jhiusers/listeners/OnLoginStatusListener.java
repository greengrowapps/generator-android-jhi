package com.greengrowapps.jhiusers.listeners;

public interface OnLoginStatusListener {
    void onLogin(String authToken);
    void onLogout();
}
