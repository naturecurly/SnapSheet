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

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.unimelb.feelinglucky.snapsheet.data.User;

/**
 * Modified by Team: You're feeling lucky on 16/8/16.
 */
public class UserLoader extends AsyncTaskLoader<User> implements UserRepository.UserRepositoryObserver{

    private UserRepository mRepository;
    private String mUserId;

    public UserLoader(String userID, Context context) {
        super(context);
        mUserId = userID;
        mRepository = Injection.provideUserRepository(context);
    }

    @Override
    public User loadInBackground() {
        return mRepository.getUuser(mUserId);
    }

    @Override
    protected void onStartLoading() {
        if (mRepository.cachedUserAvailable()) {
            deliverResult(mRepository.getUuser(mUserId));
        }

        // Begin monitoring the underlying data source.
        mRepository.addContentObserver(this);

        if (takeContentChanged() || !mRepository.cachedUserAvailable()) {
            // When a change has  been delivered or the repository cache isn't available, we force
            // a load.
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mRepository.removeContentObserver(this);
    }

    @Override
    public void deliverResult(User data) {
        if (isReset()) {
            return;
        }

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    public void onUserChanged() {
        if (isStarted()) {
            forceLoad();
        }
    }
}
