package com.unimelb.feelinglucky.snapsheet.data.source;

import android.support.annotation.NonNull;

import com.unimelb.feelinglucky.snapsheet.data.User;

/**
 * Created by mac on 16/8/15.
 */
public interface UserDataSource {
    void changeUserName (@NonNull User user);
}
