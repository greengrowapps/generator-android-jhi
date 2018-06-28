package com.greengrowapps.jhiusers.dto.api;

public class AccountRequestDto {

    private boolean activated;
    private String email;
    private String firstName;
    private String imageUrl;
    private String langKey;
    private String lastName;
    private String login;

    public AccountRequestDto(boolean activated, String email, String firstName, String imageUrl, String langKey, String lastName, String login) {
        this.activated = activated;
        this.email = email;
        this.firstName = firstName;
        this.imageUrl = imageUrl;
        this.langKey = langKey;
        this.lastName = lastName;
        this.login = login;
    }

    public AccountRequestDto() {
        //NEEDED FOR JACKSON
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
