package com.greengrowapps.jhiusers.listeners;

import com.greengrowapps.jhiusers.dto.User;

public interface OnUpdateUserListener {
    void onUpdateUserSuccess(User user);
    void onUpdateUserError(String error);
}
