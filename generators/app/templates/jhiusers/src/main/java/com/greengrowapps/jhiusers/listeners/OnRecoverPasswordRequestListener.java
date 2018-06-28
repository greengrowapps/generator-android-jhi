package com.greengrowapps.jhiusers.listeners;

public interface OnRecoverPasswordRequestListener {
    void onRecoverPasswordSuccess();
    void onRecoverPasswordError(String error);
}
