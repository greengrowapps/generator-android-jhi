package com.greengrowapps.jhiusers;



public class JhiUsersApi {

    private static final String LOGIN_ENDPOINT = "/api/authenticate";
    private static final String REGISTER_ENDPOINT = "/api/register";
    private static final String ACCOUNT_ENDPOINT = "/api/account";
    private static final String CHANGE_PASSWORD_ENDPOINT = "/api/account/change-password";
    private static final String RECOVER_PASSWORD_ENDPOINT = "/api/account/reset-password/init";
    private static final String GOOGLE_SIGN_UP_ENDPOINT = "/api/authenticate/appGoogle";
    private static final String FACEBOOK_SIGN_UP_ENDPOINT = "/api/authenticate/appFacebook";


    private final String serverUrl;

    public static JhiUsersApi FromServerUrl(String serverUrl){
        return new JhiUsersApi(serverUrl);
    }

    private JhiUsersApi(String serverUrl){
        this.serverUrl = serverUrl;
    }

    public String getLoginEndpoint(){
        return serverUrl+LOGIN_ENDPOINT;
    }

    public String getRegisterEndpoint(){
        return serverUrl+REGISTER_ENDPOINT;
    }
    public String getAccountEndpoint(){
        return serverUrl+ACCOUNT_ENDPOINT;
    }
    public String getChangePasswordEndpoint() {
        return serverUrl+ CHANGE_PASSWORD_ENDPOINT;
    }
    public String getRecoverPasswordEndpoint() {
        return serverUrl+RECOVER_PASSWORD_ENDPOINT;
    }
    public String getGoogleSignUpEndpoint() {
            return serverUrl+GOOGLE_SIGN_UP_ENDPOINT;
    }

    public String getFacebookSignUpEndpoint() {
        return serverUrl+FACEBOOK_SIGN_UP_ENDPOINT;
    }
}
