package com.unimelb.feelinglucky.snapsheet.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.unimelb.feelinglucky.snapsheet.Database.FriendDbSchema.FriendTable;
import com.unimelb.feelinglucky.snapsheet.Database.UserDbSchema.UserTable;

/**
 * Created by leveyleonhardt on 9/8/16.
 */
public class UserDataOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "users.db";

    public UserDataOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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
                FriendTable.Cols.USERNAME +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
