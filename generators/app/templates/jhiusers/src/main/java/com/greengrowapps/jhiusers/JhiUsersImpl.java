package com.greengrowapps.jhiusers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.greengrowapps.ggarest.GgaRest;
import com.greengrowapps.ggarest.Response;
import com.greengrowapps.ggarest.listeners.OnObjResponseListener;
import com.greengrowapps.ggarest.listeners.OnResponseListener;
import com.greengrowapps.jhiusers.dto.User;
import com.greengrowapps.jhiusers.dto.api.AccountRequestDto;
import com.greengrowapps.jhiusers.dto.api.AccountResponseDto;
import com.greengrowapps.jhiusers.dto.api.LoginDto;
import com.greengrowapps.jhiusers.dto.api.LoginResponseDto;
import com.greengrowapps.jhiusers.dto.api.RegisterDto;
import com.greengrowapps.jhiusers.listeners.OnChangePasswordListener;
import com.greengrowapps.jhiusers.listeners.OnLoginListener;
import com.greengrowapps.jhiusers.listeners.OnLoginStatusListener;
import com.greengrowapps.jhiusers.listeners.OnRecoverPasswordRequestListener;
import com.greengrowapps.jhiusers.listeners.OnRegisterListener;
import com.greengrowapps.jhiusers.listeners.OnUpdateUserListener;
import com.greengrowapps.jhiusers.listeners.OnUserAvailableListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JhiUsersImpl implements JhiUsers{

    private static final String USERNAME_PREF = "JhiUsername";
    private static final String PASSWORD_PREF = "JhiPassword";
    private static final String GOOGLE_SAVED_PREF = "JhiGoogle";
    private static final String FACEBOOK_SAVED_PREF = "JhiFacebook";

    private final Handler handler;
    private final JhiUsersApi jhiUsersApi;
    private final boolean keepLogedIn;
    private final SharedPreferences preferences;
    private String authToken="";
    private User user;

    private final Object userLock = new Object();
    private boolean gettingUser = false;
    private List<OnUserAvailableListener> userListeners = new ArrayList<>();
    private OnLoginStatusListener statusListener;

    public static JhiUsers with(Context context, String serverUrl, boolean keepLogedIn, SharedPreferences preferences){
        return new JhiUsersImpl(context, JhiUsersApi.FromServerUrl(serverUrl), keepLogedIn, preferences);
    }

    private JhiUsersImpl(Context context, JhiUsersApi jhiUsersApi, boolean keepLogedIn, SharedPreferences preferences){
        this.keepLogedIn = keepLogedIn;
        this.preferences = preferences;
        this.handler = new Handler();
        this.jhiUsersApi = jhiUsersApi;
        GgaRest.init(context);
    }

    @Override
    public void login(final String login, final String password, final OnLoginListener listener) {
        LoginDto loginDto = new LoginDto(login,password);
        GgaRest.ws()
                .post(jhiUsersApi.getLoginEndpoint())
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .withBody(loginDto)
                .onSuccess(LoginResponseDto.class,new OnObjResponseListener<LoginResponseDto>(){

                    @Override
                    public void onResponse(int code, LoginResponseDto object, Response fullResponse) {
                        authToken=object.getId_token();
                        listener.onLoginSuccess();
                        if(statusListener!=null){
                            statusListener.onLogin(authToken);
                        }
                        if(keepLogedIn){
                            saveLogin(login,password);
                        }
                        loadUser();
                    }
                })
                .onOther(new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        if(e!=null){
                            Log.e("Core","Login error",e);
                        }
                        listener.onLoginError(fullResponse.getBody());
                    }
                })
                .execute();
    }

    private void saveLogin(String login, String password) {
        preferences.edit()
                .putString(USERNAME_PREF,login)
                .putString(PASSWORD_PREF,password)
                .apply();
    }

    @Override
    public boolean isLoginSaved() {
        return preferences.getString(USERNAME_PREF,null)!=null;
    }
    @Override
    public boolean isGoogleLoginSaved(){
        return preferences.getBoolean(GOOGLE_SAVED_PREF,false);
    }
    @Override
    public boolean isFacebookLoginSaved(){
        return preferences.getBoolean(FACEBOOK_SAVED_PREF,false);
    }

    @Override
    public void autoLogin(OnLoginListener listener) {
        login(preferences.getString(USERNAME_PREF,null),preferences.getString(PASSWORD_PREF,null),listener);
    }

    private void loadUser() {
        synchronized (userLock) {
            if(gettingUser) {
                return;
            }
            gettingUser=true;
            GgaRest.ws()
                    .get(jhiUsersApi.getAccountEndpoint())
                    .addHeader("Authorization", "Bearer " + authToken)
                    .addHeader("Content-Type","application/json;charset=UTF-8")
                    .onSuccess(AccountResponseDto.class, new OnObjResponseListener<AccountResponseDto>() {
                                @Override
                                public void onResponse(int code, AccountResponseDto response, Response fullResponse) {
                                    onUserResponse(response);
                                }
                            }
                    )
                    .onOther(new OnResponseListener() {
                        @Override
                        public void onResponse(int code, Response fullResponse, Exception e) {
                            synchronized (userLock) {
                                gettingUser = false;
                            }
                        }
                    })
                    .execute();
        }
    }

    @Override
    public void register(String email, String firstName, String lastName, String password, final OnRegisterListener listener, String phoneNumber) {
        RegisterDto register = new RegisterDto(email,firstName,lastName,email, phoneNumber, password, Locale.getDefault().getLanguage());
        GgaRest.ws()
                .post(jhiUsersApi.getRegisterEndpoint())
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .withBody(register)
                .onResponse(201, new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        listener.onRegisterSuccess();
                    }
                })
                .onOther(new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        listener.onRegisterError(fullResponse.getBody());
                    }
                })
                .execute();
    }

    @Override
    public void logout() {
        synchronized (userLock){
            user = null;
            gettingUser = false;
        }
        authToken = "";
        if(statusListener!=null){
            statusListener.onLogout();
        }
        saveLogin(null,null);
        preferences.edit().putBoolean(GOOGLE_SAVED_PREF,false).putBoolean(FACEBOOK_SAVED_PREF,false).apply();
    }

    @Override
    public void update(final User user, final OnUpdateUserListener listener) {
        AccountRequestDto request = new AccountRequestDto(true,user.getEmail(),user.getFirstName(),null,Locale.getDefault().getLanguage(), user.getLastName(),user.getLogin());

        GgaRest.ws()
                .post(jhiUsersApi.getAccountEndpoint())
                .withBody(request)
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .addHeader("Authorization", "Bearer " + authToken)
                .onSuccess(new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        synchronized (userLock){
                            JhiUsersImpl.this.user = user;
                        }
                        listener.onUpdateUserSuccess(user);
                    }
                })
                .onOther(new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        listener.onUpdateUserError("");
                    }
                })
                .execute();
    }

    @Override
    public void changePassword(String newPassword, final OnChangePasswordListener listener) {
        GgaRest.ws()
                .post(jhiUsersApi.getChangePasswordEndpoint())
                .withPlainBody(newPassword)
                .addHeader("Authorization", "Bearer " + authToken)
                .addHeader("Content-Type","text/plain;charset=UTF-8")
                .onSuccess(new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        listener.onChangePasswordSuccess();
                    }
                })
                .onOther(new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        listener.onChangePasswordError("");
                    }
                })
                .execute();
    }

    @Override
    public void recoverPassword(String email, final OnRecoverPasswordRequestListener listener) {
        GgaRest.ws()
                .post(jhiUsersApi.getRecoverPasswordEndpoint())
                .withPlainBody(email)
                .addHeader("Content-Type","text/plain;charset=UTF-8")
                .onSuccess(new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        listener.onRecoverPasswordSuccess();
                    }
                })
                .onOther(new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        listener.onRecoverPasswordError("");
                    }
                })
                .execute();
    }

    @Override
    public void getLogedUser(OnUserAvailableListener listener) {
        synchronized (userLock){
            if(user!=null){
                listener.onUserAvailable(user);
            }
            else{
                loadUser();
                userListeners.add(listener);
            }
        }
    }

    @Override
    public String getAuthToken() {
        return authToken;
    }

    @Override
    public void loginWithFacebook(String token, final OnLoginListener listener) {
        String url = jhiUsersApi.getFacebookSignUpEndpoint();
        GgaRest.ws()
                .post(url)
                .withBody(token)
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .onSuccess(LoginResponseDto.class,new OnObjResponseListener<LoginResponseDto>(){
                    @Override
                    public void onResponse(int code, LoginResponseDto object, Response fullResponse) {
                        authToken=object.getId_token();
                        listener.onLoginSuccess();
                        if(statusListener!=null){
                            statusListener.onLogin(authToken);
                        }
                        if(keepLogedIn) {
                            saveFacebookLogin();
                        }
                        loadUser();
                    }
                })
                .onOther(new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        listener.onLoginError("");
                    }
                })
                .execute();
    }

    private void onUserResponse(AccountResponseDto response) {
        synchronized (userLock) {
            gettingUser = false;
            user = new User(response.getLogin(),response.getEmail(),response.getFirstName(),response.getLastName(),response.getPhoneNumber());
            for (OnUserAvailableListener listener : userListeners) {
                listener.onUserAvailable(user);
            }
            userListeners.clear();
        }
    }

    @Override
    public void loginWithGoogle(String idToken, final OnLoginListener listener) {
        String url = jhiUsersApi.getGoogleSignUpEndpoint();
        GgaRest.ws()
                .post(url)
                .withBody(idToken)
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .onSuccess(LoginResponseDto.class,new OnObjResponseListener<LoginResponseDto>(){
                    @Override
                    public void onResponse(int code, LoginResponseDto object, Response fullResponse) {
                        authToken=object.getId_token();
                        listener.onLoginSuccess();
                        if(statusListener!=null){
                            statusListener.onLogin(authToken);
                        }
                        if(keepLogedIn) {
                            saveGoogleLogin();
                        }
                        loadUser();
                    }
                })
                .onOther(new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        listener.onLoginError("");
                    }
                })
                .execute();
    }

    private void saveGoogleLogin() {
        preferences.edit().putBoolean(GOOGLE_SAVED_PREF,true).apply();
    }
    private void saveFacebookLogin() {
        preferences.edit().putBoolean(FACEBOOK_SAVED_PREF,true).apply();
    }
    @Override
    public void setOnLoginStatusListener(OnLoginStatusListener listener) {
        this.statusListener = listener;
    }
}
