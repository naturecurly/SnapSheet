/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * Modified by Team: You're feeling lucky on 16/8/16.
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

    public boolean cachedUserAvailable() {
        if (mCachedUser != null && mCachedUser.size() > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void changeUserName(@NonNull User user) {

    }

    @Override
    public User getUuser(@NonNull String id) {
        return null;
    }




    // *********************** Observation ***********************

    public interface UserRepositoryObserver {
        void onUserChanged();
    }

    public void addContentObserver(UserRepositoryObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void removeContentObserver(UserRepositoryObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    private void notifyContentObserver() {
        for (UserRepositoryObserver observer : mObservers) {
            observer.onUserChanged();
        }
    }
}
