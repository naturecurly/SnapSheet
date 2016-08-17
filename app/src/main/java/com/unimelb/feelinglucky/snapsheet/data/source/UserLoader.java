package com.unimelb.feelinglucky.snapsheet.data.source;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.unimelb.feelinglucky.snapsheet.data.User;

/**
 * Created by mac on 16/8/16.
 */
public class UserLoader extends AsyncTaskLoader<User> implements UserRepository.UserRepositoryObserver{

    private UserRepository mRepository;
    private String mUserId;
    public UserLoader(String userID, Context context) {
        super(context);
        mUserId = userID;
    }

    @Override
    public User loadInBackground() {
        return mRepository.getUuser(mUserId);
    }

    @Override
    public void deliverResult(User data) {
        super.deliverResult(data);
    }

    @Override
    public void onUserChanged() {

    }
}
