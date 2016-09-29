package com.unimelb.feelinglucky.snapsheet.Util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unimelb.feelinglucky.snapsheet.Bean.User;
import com.unimelb.feelinglucky.snapsheet.Database.FriendDbSchema;
import com.unimelb.feelinglucky.snapsheet.Database.FriendDbSchema.FriendTable;
import com.unimelb.feelinglucky.snapsheet.Database.UserDbSchema;
import com.unimelb.feelinglucky.snapsheet.Database.UserDbSchema.UserTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leveyleonhardt on 9/9/16.
 */
public class DatabaseUtils {
    public static ContentValues getUserContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(UserTable.Cols.USERNAME, user.getUsername());
        values.put(UserTable.Cols.BIRTHDAY, user.getBirthday().getTime());
        values.put(UserTable.Cols.MOBILE, user.getMobile());
        values.put(UserTable.Cols.EMAIL, user.getEmail());
        values.put(UserTable.Cols.PASSWORD, user.getPassword());
        values.put(UserTable.Cols.AVATAR, user.getAvatar());
        values.put(UserTable.Cols.DEVICE, user.getDevice_id());
        return values;
    }

    public static ContentValues getFriendContentValues(String username) {
        ContentValues values = new ContentValues();
        values.put(FriendTable.Cols.USERNAME, username);
        return values;
    }

    public static void refreshUserDb(SQLiteDatabase database, User user) {
        ContentValues values = getUserContentValues(user);
        database.delete(UserTable.NAME, null, null);
        database.insert(UserTable.NAME, null, values);
    }

    public static void refreshFriendDb(SQLiteDatabase database, String[] friends) {
        database.delete(FriendTable.NAME, null, null);
        for (int i = 0; i < friends.length; ++i) {
            ContentValues values = getFriendContentValues(friends[i]);
            database.insert(FriendTable.NAME, null, values);
        }
    }

    public static List<String> fetchFriends(SQLiteDatabase database) {
        Cursor cursor = database.query(FriendTable.NAME, new String[]{FriendTable.Cols.USERNAME}, null, null, null, null, null);
        int columnIndex = cursor.getColumnIndex(FriendTable.Cols.USERNAME);
//        String[] friendArray = new String[cursor.getCount()];
        List<String> friendList = new ArrayList<>();
        while (cursor.moveToNext()) {
            friendList.add(cursor.getString(columnIndex));
        }
        return friendList;
    }
}
