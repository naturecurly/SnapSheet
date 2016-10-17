package com.unimelb.feelinglucky.snapsheet.Database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Xuhui Chen (yorkfine) on 14/10/2016.
 */

public interface SnapSeetDataStore {
    String AUTHORITY = "snapsheet";

    String TYPE_PRIMARY_KEY = "INTEGER PRIMARY KEY AUTOINCREMENT";
    String TYPE_INT = "INTEGER";
    String TYPE_INT_UNIQUE = "INTEGER UNIQUE";
    String TYPE_BOOLEAN = "INTEGER(1)";
    String TYPE_BOOLEAN_DEFAULT_TRUE = "INTEGER(1) DEFAULT 1";
    String TYPE_BOOLEAN_DEFAULT_FALSE = "INTEGER(1) DEFAULT 0";
    String TYPE_TEXT = "TEXT";
    String TYPE_DOUBLE_NOT_NULL = "DOUBLE NOT NULL";
    String TYPE_TEXT_NOT_NULL = "TEXT NOT NULL";
    String TYPE_TEXT_NOT_NULL_UNIQUE = "TEXT NOT NULL UNIQUE";

    Uri BASE_CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY).build();

    interface ChatMessage extends BaseColumns {
        String TABLE_NAME = "chat_message";
        String CONTENT_PATH = TABLE_NAME;
        String FROM_USER = "from_user";
        String TO_USER = "to_user";
        String IMG_ID = "img_id";
        String IMG_RND = "img_rnd";

        String TYPE_ANY = "any";
        String TYPE_MSG = "msg";
        String TYPE_IMG = "img";
        String USER = "user"; // from or to user
        Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, CONTENT_PATH);
        Uri CONTENT_URI_TYPE_ANY = Uri.withAppendedPath(CONTENT_URI, TYPE_ANY);
        Uri CONTENT_URI_FROM_USER = Uri.withAppendedPath(CONTENT_URI_TYPE_ANY, FROM_USER);
        Uri CONTENT_URI_TO_USER = Uri.withAppendedPath(CONTENT_URI_TYPE_ANY, TO_USER);
        Uri CONTENT_URI_TYPE_MSG = Uri.withAppendedPath(CONTENT_URI, TYPE_MSG);
        Uri CONTENT_URI_FROM_USER_MSG = Uri.withAppendedPath(CONTENT_URI_TYPE_MSG, FROM_USER);
        Uri CONTENT_URI_TO_USER_MSG = Uri.withAppendedPath(CONTENT_URI_TYPE_MSG, TO_USER);
        Uri CONTENT_URI_TYPE_IMG = Uri.withAppendedPath(CONTENT_URI, TYPE_IMG);
        Uri CONTENT_URI_FROM_USER_IMG = Uri.withAppendedPath(CONTENT_URI_TYPE_IMG, FROM_USER);
        Uri CONTENT_URI_IMG_ID = Uri.withAppendedPath(CONTENT_URI_TYPE_IMG, IMG_ID);
        Uri CONTENT_URI_IMG_RND = Uri.withAppendedPath(CONTENT_URI_TYPE_IMG, IMG_RND);

        Uri CONTENT_URI_TO_USER_IMG = Uri.withAppendedPath(CONTENT_URI_TYPE_IMG, TO_USER);
        Uri CONTENT_URI_USER = Uri.withAppendedPath(CONTENT_URI, USER);

        String FROM = "fromUser";
        String TO = "toUser";
        String MESSAGE = "message";
        String TYPE = "type";
        String EXPIRE_TIME = "expire_time";
        String STATUS = "status";  // read or unread
        String REMOTE_ID = "remote_id";

        String [] COLUMNS = {_ID, FROM, TO, MESSAGE, TYPE, EXPIRE_TIME, STATUS, REMOTE_ID};
        String [] TYPES = {TYPE_PRIMARY_KEY, TYPE_TEXT_NOT_NULL, TYPE_TEXT_NOT_NULL, TYPE_TEXT_NOT_NULL, TYPE_TEXT_NOT_NULL, TYPE_INT, TYPE_INT, TYPE_TEXT};
    }

    interface ChatFriendList extends BaseColumns {
        String TABLE_NAME = "friend_chat";
        String CONTENT_PATH = TABLE_NAME;

        Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, CONTENT_PATH);
        String USERNAME = "username";
        String CHAT_PRIORITY = "chat_priority";


        String [] COLUMNS = {_ID, USERNAME, CHAT_PRIORITY};
        String [] TYPES = {TYPE_PRIMARY_KEY, TYPE_TEXT_NOT_NULL, TYPE_INT};
    }
}
