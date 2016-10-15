package com.unimelb.feelinglucky.snapsheet.Util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.unimelb.feelinglucky.snapsheet.Bean.Message;
import com.unimelb.feelinglucky.snapsheet.Bean.User;
import com.unimelb.feelinglucky.snapsheet.Database.FriendChatDbSchema;
import com.unimelb.feelinglucky.snapsheet.Database.FriendDbSchema.FriendTable;
import com.unimelb.feelinglucky.snapsheet.Database.ImgDbSchema;
import com.unimelb.feelinglucky.snapsheet.Database.UserDbSchema.UserTable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.unimelb.feelinglucky.snapsheet.Database.SnapSeetDataStore.ChatMessage;

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

    public static ContentValues getFriendContentValues(User friend) {
        ContentValues values = new ContentValues();
        values.put(FriendTable.Cols.USERNAME, friend.getUsername());
        values.put(FriendTable.Cols.MOBILE, friend.getMobile());
        values.put(FriendTable.Cols.EMAIL, friend.getEmail());
        return values;
    }

    public static ContentValues getFriendChatContentValues(String username) {
        ContentValues values = new ContentValues();
        values.put(FriendChatDbSchema.FriendChatTable.NAME, username);
        return values;
    }

    public static void refreshUserDb(SQLiteDatabase database, User user) {
        ContentValues values = getUserContentValues(user);
        if (database != null) {
            database.delete(UserTable.NAME, null, null);
            database.insert(UserTable.NAME, null, values);
        }
    }

    public static void refreshFriendDb(SQLiteDatabase database, User[] friends) {
        database.delete(FriendTable.NAME, null, null);
        for (int i = 0; i < friends.length; ++i) {
            ContentValues values = getFriendContentValues(friends[i]);
            database.insert(FriendTable.NAME, null, values);

            updateFriendChatDb(database, friends[i].getUsername());
        }
    }


    public static void updateFriendChatDb(SQLiteDatabase database, String username) {
        Cursor cursor = database.query(FriendChatDbSchema.FriendChatTable.NAME,
                new String[]{FriendChatDbSchema.FriendChatTable.Cols.USERNAME},
                FriendChatDbSchema.FriendChatTable.Cols.USERNAME + "=?", new String[]{username}, null, null, null);
        if (!cursor.moveToNext()) {
            ContentValues values = getFriendChatContentValues(username);
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

    public static void insertFriendDb(SQLiteDatabase database, User friend) {
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
        boolean result = false;
        Cursor cursor = database.query(FriendChatDbSchema.FriendChatTable.NAME,
                new String[]{FriendChatDbSchema.FriendChatTable.Cols.USERNAME},
                FriendChatDbSchema.FriendChatTable.Cols.USERNAME + "=?", new String[]{username}, null, null, null);
        if (cursor.moveToNext()) {
            result = true;
        }
        return result;

    }


    public static ContentValues buildChatMessage(Map<String, String> data) {
        Message message = new Message();
        message.setFrom(data.get("fromUsername"));
        message.setTo(data.get("toUser"));
        message.setType(data.get("type"));
        message.setContent(data.get("message"));

        return buildChatMessage(message);
    }
    public static ContentValues buildChatMessage(Message message) {
        ContentValues values = new ContentValues();
        values.put(ChatMessage.FROM, message.getFrom());
        values.put(ChatMessage.TO, message.getTo());
        values.put(ChatMessage.MESSAGE, message.getContent());
        values.put(ChatMessage.TYPE, message.getType());
        values.put(ChatMessage.STATUS, 0);
        values.put(ChatMessage.EXPIRE_TIME, 5);

        return values;
    }

    public static Message buildMessageFromCursor(Cursor cursor) {
        Message message = new Message();
        message.setFrom(cursor.getString(cursor.getColumnIndex(ChatMessage.FROM)));
        message.setType(cursor.getString(cursor.getColumnIndex(ChatMessage.TYPE)));
        message.setContent(cursor.getString(cursor.getColumnIndex(ChatMessage.MESSAGE)));
        // status
        // expiration time
        return message;
    }
}
