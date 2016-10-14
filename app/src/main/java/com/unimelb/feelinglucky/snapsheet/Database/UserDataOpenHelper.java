package com.unimelb.feelinglucky.snapsheet.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.unimelb.feelinglucky.snapsheet.Database.FriendDbSchema.FriendTable;
import com.unimelb.feelinglucky.snapsheet.Database.UserDbSchema.UserTable;

import static com.unimelb.feelinglucky.snapsheet.Database.SnapSeetDataStore.*;

/**
 * Created by leveyleonhardt on 9/8/16.
 */
public class UserDataOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 2;
    public static final String DATABASE_NAME = "users.db";

    public UserDataOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDB(db);
        Log.i("database", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropDB(db);
        createDB(db);
        Log.i("database", "onUpgrade");
    }

    private void dropDB(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + DATABASE_NAME);
    }

    private void createDB(SQLiteDatabase db) {
//        db.beginTransaction();
        db.execSQL("create table " + UserTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                UserTable.Cols.USERNAME + ", " +
                UserTable.Cols.BIRTHDAY + ", " +
                UserTable.Cols.MOBILE + ", " +
                UserTable.Cols.EMAIL + ", " +
                UserTable.Cols.PASSWORD + ", " +
                UserTable.Cols.AVATAR + ", " +
                UserTable.Cols.DEVICE +
                ")"
        );
        db.execSQL("create table " + FriendTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                FriendTable.Cols.USERNAME + "," +
                FriendTable.Cols.EMAIL + "," +
                FriendTable.Cols.MOBILE +
                ")"
        );
        db.execSQL("create table " + FriendChatDbSchema.FriendChatTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                FriendChatDbSchema.FriendChatTable.Cols.USERNAME + "," +
                FriendChatDbSchema.FriendChatTable.Cols.CHAT_PRIORITY + " INTEGER DEFAULT 0" +
                ")"
        );


        db.execSQL("CREATE TABLE " + ImgDbSchema.ImgTable.NAME + "(" +
                ImgDbSchema.ImgTable.Cols.IMGRTEXT + " TEXT," +
                ImgDbSchema.ImgTable.Cols.IMG + " BLOB);");

        db.execSQL(SnapSheetDataStoreUtils.createTable(
                ChatMessage.TABLE_NAME,
                ChatMessage.COLUMNS,
                ChatMessage.TYPES));

//        db.endTransaction();
    }

}
