package com.unimelb.feelinglucky.snapsheet.data.source;

import android.support.annotation.NonNull;

import com.unimelb.feelinglucky.snapsheet.data.User;

/**
 * Modified by Team: You're feeling lucky on 16/8/16.
 */
public interface UserDataSource {
    void changeUserName (@NonNull User user);
    User getUuser(@NonNull String id);
}
