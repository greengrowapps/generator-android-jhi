package com.greengrowapps.jhiusers;

import com.greengrowapps.jhiusers.dto.User;
import com.greengrowapps.jhiusers.listeners.OnChangePasswordListener;
import com.greengrowapps.jhiusers.listeners.OnLoginListener;
import com.greengrowapps.jhiusers.listeners.OnLoginStatusListener;
import com.greengrowapps.jhiusers.listeners.OnRecoverPasswordRequestListener;
import com.greengrowapps.jhiusers.listeners.OnRegisterListener;
import com.greengrowapps.jhiusers.listeners.OnUpdateUserListener;
import com.greengrowapps.jhiusers.listeners.OnUserAvailableListener;

public interface JhiUsers {

    boolean isLoginSaved();

    boolean isGoogleLoginSaved();

    boolean isFacebookLoginSaved();

    void autoLogin(OnLoginListener listener);

    void login(String login, String password, OnLoginListener listener);
    void register(String email, String firstName, String lastName, String password, OnRegisterListener listener, String phoneNumber);
    void logout();

    void update(User user, OnUpdateUserListener listener);
    void changePassword(String newPassword, OnChangePasswordListener listener);
    void recoverPassword(String email, OnRecoverPasswordRequestListener listener);

    void getLogedUser(OnUserAvailableListener listener);
    String getAuthToken();

    void loginWithFacebook(String token, OnLoginListener listener);

     void loginWithGoogle(String token, OnLoginListener listener);

     void setOnLoginStatusListener(OnLoginStatusListener listener);
}
