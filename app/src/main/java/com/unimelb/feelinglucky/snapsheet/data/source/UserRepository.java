package com.unimelb.feelinglucky.snapsheet.data.source;

import android.support.annotation.NonNull;

import com.unimelb.feelinglucky.snapsheet.data.User;
import com.unimelb.feelinglucky.snapsheet.data.source.local.UserLocalDataSource;
import com.unimelb.feelinglucky.snapsheet.data.source.remote.UserRemoteDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mac on 16/8/15.
 */
public class UserRepository implements UserDataSource{
    private static UserRepository INSTANCE = null;
    private final UserLocalDataSource mUserLocalDataSource;
    private final UserRemoteDataSource mUserRemoteDataSource;
    private List<UserRepositoryObserver> mObservers = new ArrayList<>();
    Map<String, User> mCachedUser;

    public static UserRepository getInstance(UserLocalDataSource mUserLocalDataSource, UserRemoteDataSource mUserRemoteDataSource) {
        if (INSTANCE == null) {
            new UserRepository(mUserLocalDataSource, mUserRemoteDataSource);
        }
        return INSTANCE;
    }

    private UserRepository(@NonNull UserLocalDataSource UserLocalDataSource,
                           @NonNull UserRemoteDataSource UserRemoteDataSource) {
        mUserLocalDataSource = checkNotNull(UserLocalDataSource);
        mUserRemoteDataSource = checkNotNull(UserRemoteDataSource);

    }


    @Override
    public void changeUserName(@NonNull User user) {

    }

    @Override
    public User getUuser(@NonNull String id) {
        return null;
    }

    private void notifyContentObserver() {
        for (UserRepositoryObserver observer : mObservers) {
            observer.onUserChanged();
        }
    }

    public interface UserRepositoryObserver {
        void onUserChanged();
    }
}
