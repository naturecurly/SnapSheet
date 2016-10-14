package com.unimelb.feelinglucky.snapsheet.Database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Xuhui Chen (yorkfine) on 14/10/2016.
 */

public class SnapSheetProvider extends ContentProvider {
    public static final String LOG_TAG = SnapSheetProvider.class.getSimpleName();

    private UserDataOpenHelper mOpenHelper;
    private ContentResolver mContentResolver;


    @Override
    public boolean onCreate() {
        mOpenHelper = new UserDataOpenHelper(getContext());  // DataBaseInstance also get a static database instance
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int tableId = SnapSheetDataStoreUtils.getTableId(uri);
        final String table = SnapSheetDataStoreUtils.getTableNameById(tableId);
        switch (tableId) {
            case SnapSheetDataStoreUtils.TABLE_ID_CHATMESSAGE_WITH_USERNAME:
                String username = uri.getLastPathSegment();
                Log.i(LOG_TAG, username);
                final Cursor c = mOpenHelper.getReadableDatabase().query(
                        table,
                        projection,
                        SnapSeetDataStore.ChatMessage.USERNAME + " = ?",
                        new String[]{username},
                        null,
                        null,
                        null);
                setNotificationUri(c, uri);
                return c;
        }
        if (table == null) return null;
        final Cursor c = mOpenHelper.getReadableDatabase().query(table, projection, selection,
                selectionArgs, null, null, sortOrder);

        setNotificationUri(c, uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int tableId = SnapSheetDataStoreUtils.getTableId(uri);
        final String table = SnapSheetDataStoreUtils.getTableNameById(tableId);
        final long rowId;
        switch (tableId) {

        }
        if (table == null) return null;
        rowId = mOpenHelper.getWritableDatabase().insert(table, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(uri, String.valueOf(rowId));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private void setNotificationUri(final Cursor c, final Uri uri) {
        final ContentResolver cr = getContentResolver();
        if (cr == null || c == null || uri == null) return;
        c.setNotificationUri(cr, uri);
    }

    private ContentResolver getContentResolver() {
        if (mContentResolver != null) return mContentResolver;
        final Context context = getContext();
        assert context != null;
        return mContentResolver = context.getContentResolver();
    }
}
