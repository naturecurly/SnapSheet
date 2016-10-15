package com.unimelb.feelinglucky.snapsheet.Util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.unimelb.feelinglucky.snapsheet.Bean.User;
import com.unimelb.feelinglucky.snapsheet.Database.FriendChatDbSchema;
import com.unimelb.feelinglucky.snapsheet.Database.FriendDbSchema;
import com.unimelb.feelinglucky.snapsheet.Database.FriendDbSchema.FriendTable;
import com.unimelb.feelinglucky.snapsheet.Database.ImgDbSchema;
import com.unimelb.feelinglucky.snapsheet.Database.UserDbSchema.UserTable;

import java.io.ByteArrayOutputStream;
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
        if (database != null) {
            database.delete(UserTable.NAME, null, null);
            database.insert(UserTable.NAME, null, values);
        }
    }

    public static void refreshFriendDb(SQLiteDatabase database, String[] friends) {
        database.delete(FriendTable.NAME, null, null);
        for (int i = 0; i < friends.length; ++i) {
            ContentValues values = getFriendContentValues(friends[i]);
            database.insert(FriendTable.NAME, null, values);

        }
    }


    public static void updateFriendChatDb(SQLiteDatabase database, String username) {
        Cursor cursor = database.query(FriendChatDbSchema.FriendChatTable.NAME,
                new String[]{FriendChatDbSchema.FriendChatTable.Cols.USERNAME},
                FriendChatDbSchema.FriendChatTable.Cols.USERNAME + "=?", new String[]{username}, null, null, null);
        if (!cursor.moveToNext()) {
            ContentValues values = getFriendContentValues(username);
            database.insert(FriendChatDbSchema.FriendChatTable.NAME, null, values);
        }

    }

    public static void updateChatPriority(SQLiteDatabase database, String userName) {

        String search = "MAX(" + FriendChatDbSchema.FriendChatTable.Cols.CHAT_PRIORITY + ")";
        Cursor cursor = database.query(FriendChatDbSchema.FriendChatTable.NAME, new String[]{search}, null, null, null, null, null);
        Integer max = 0;
        if (cursor.moveToNext()) {
            // Zero means the index of the column.
            max = cursor.getInt(0);
        }

        ContentValues values = new ContentValues();
        values.put(FriendChatDbSchema.FriendChatTable.Cols.CHAT_PRIORITY, max + 1);
        database.update(FriendChatDbSchema.FriendChatTable.NAME, values, FriendChatDbSchema.FriendChatTable.Cols.USERNAME + "=?", new String[]{userName});
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

    public static void insertFriendDb(SQLiteDatabase database, String friend) {
        ContentValues value = getFriendContentValues(friend);
        database.insert(FriendTable.NAME, null, value);
    }

    public static String[] loadFriendsWithPriority(SQLiteDatabase database) {
        String orderBy = FriendChatDbSchema.FriendChatTable.Cols.CHAT_PRIORITY + " DESC";
        List<String> userList = new ArrayList<>();

        Cursor cursor = database.query(FriendChatDbSchema.FriendChatTable.NAME, new String[]{FriendChatDbSchema.FriendChatTable.Cols.USERNAME,
                FriendChatDbSchema.FriendChatTable.Cols.CHAT_PRIORITY}, null, null, null, null, orderBy);
        int usernameIndex = cursor.getColumnIndex(FriendChatDbSchema.FriendChatTable.Cols.USERNAME);
//        int priorityIndex = cursor.getColumnIndex(FriendChatDbSchema.FriendChatTable.Cols.CHAT_PRIORITY);
        while (cursor.moveToNext()) {
            userList.add(cursor.getString(usernameIndex));
        }

        String[] result = new String[userList.size()];
        result = userList.toArray(result);
        return result;
    }

    public static Bitmap getImg(SQLiteDatabase database) {
        Cursor cursor = database.query(ImgDbSchema.ImgTable.NAME,
                new String[]{ImgDbSchema.ImgTable.Cols.IMG}, null, null, null, null, null);
        int columnIndex = cursor.getColumnIndex(ImgDbSchema.ImgTable.Cols.IMG);
        Bitmap mBitmap = null;
        while (cursor.moveToNext()) {
            byte[] image = cursor.getBlob(columnIndex);
            mBitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        }
        return mBitmap;
    }

    public static void storeImg(SQLiteDatabase database, Bitmap mBitmap) {
        if (mBitmap == null) {
            database.delete(ImgDbSchema.ImgTable.NAME, null, null);
        } else {
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            ContentValues cv = new ContentValues();
            cv.put(ImgDbSchema.ImgTable.Cols.IMGRTEXT, "temp");
            cv.put(ImgDbSchema.ImgTable.Cols.IMG, os.toByteArray());
            database.insert(ImgDbSchema.ImgTable.NAME, null, cv);
        }

    }

    public static boolean isFriend(SQLiteDatabase database, String username) {
        Cursor cursor = database.query(FriendDbSchema.FriendTable.NAME,
                new String[]{FriendDbSchema.FriendTable.Cols.USERNAME},
                FriendDbSchema.FriendTable.Cols.USERNAME + "=?", new String[]{username}, null, null, null);

        return cursor.moveToNext();
    }

    public static boolean isImgLocked(SQLiteDatabase database) {
        Cursor cursor = database.query(ImgDbSchema.ImgTable.NAME,
                new String[]{ImgDbSchema.ImgTable.Cols.ISLOCKED},
                ImgDbSchema.ImgTable.Cols.ISLOCKED + "=?", new String[]{}, null, null, null);
        int columnIndex = cursor.getColumnIndex(ImgDbSchema.ImgTable.Cols.ISLOCKED);
        if (cursor.moveToNext()) {
            Integer lock = cursor.getInt(columnIndex);
            if (lock == 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
