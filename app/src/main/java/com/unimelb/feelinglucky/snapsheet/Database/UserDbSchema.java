package com.unimelb.feelinglucky.snapsheet.Database;

/**
 * Created by leveyleonhardt on 9/9/16.
 */
public class UserDbSchema {
    public static final class UserTable {
        public static final String NAME = "users";

        public static final class Cols {
            public static final String USERNAME = "username";
            public static final String BIRTHDAY = "birthday";
            public static final String MOBILE = "mobile";
            public static final String EMAIL = "email";
            public static final String PASSWORD = "password";
            public static final String AVATAR = "avatar";
            public static final String DEVICE = "device_id";
        }
    }
}
