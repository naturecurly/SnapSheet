/*
 * Copyright (C) 2015 The Android Open Source Project
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
import android.support.annotation.NonNull;

import com.unimelb.feelinglucky.snapsheet.data.source.local.UserLocalDataSource;
import com.unimelb.feelinglucky.snapsheet.data.source.remote.UserRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Modified by Team: You're feeling lucky on 16/8/16.
 */
public class Injection {
    public static UserRepository provideUserRepository(@NonNull Context context) {
        checkNotNull(context);
        return UserRepository.getInstance(UserLocalDataSource.getInstance(context),
                UserRemoteDataSource.getInstance());
    }
}
