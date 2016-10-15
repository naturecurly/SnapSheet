package com.unimelb.feelinglucky.snapsheet.Database;

/**
 * Created by leveyleonhardt on 9/9/16.
 */
public class FriendChatDbSchema {


    public static final class FriendChatTable {
        public static final String NAME = "friend_chat";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String USERNAME = "username";
            public static final String CHAT_PRIORITY = "chat_priority";
        }
    }
}
