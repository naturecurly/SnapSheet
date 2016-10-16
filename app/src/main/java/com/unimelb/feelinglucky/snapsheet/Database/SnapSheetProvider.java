package com.unimelb.feelinglucky.snapsheet.Database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.unimelb.feelinglucky.snapsheet.Bean.Message;

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
            case SnapSheetDataStoreUtils.TABLE_ID_CHATMESSAGE_WITH_FROM_USER: {
                    String username = uri.getLastPathSegment();
                    Log.i(LOG_TAG, username);
                    final Cursor c = mOpenHelper.getReadableDatabase().query(
                            table,
                            projection,
                            SnapSeetDataStore.ChatMessage.FROM + " = ?",
                            new String[]{username},
                            null,
                            null,
                            null);
                    setNotificationUri(c, uri);
                    return c;
                }

            case SnapSheetDataStoreUtils.TABLE_ID_CHATMESSAGE_WITH_TO_USER: {
                    String username = uri.getLastPathSegment();
                    final Cursor c = mOpenHelper.getReadableDatabase().query(
                            table,
                            projection,
                            SnapSeetDataStore.ChatMessage.TO + " = ?",
                            new String[]{username},
                            null,
                            null,
                            null);
                    setNotificationUri(c, uri);

                    return c;
                }

            case SnapSheetDataStoreUtils.TABLE_ID_CHATFRIENDLIST: {
                final Cursor c = mOpenHelper.getReadableDatabase().query(
                        table,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                setNotificationUri(c, uri);
                return c;
            }
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
        final int tableId = SnapSheetDataStoreUtils.getTableId(uri);
        final String table = SnapSheetDataStoreUtils.getTableNameById(tableId);
        switch (tableId) {
            case SnapSheetDataStoreUtils.TABLE_ID_CHATMESSAGE_WITH_FROM_USER_MSG: {
                String username = uri.getLastPathSegment();
                Log.i(LOG_TAG, "delete msg type messages from " + username);
                selection = SnapSeetDataStore.ChatMessage.FROM + "= ? and " +
                SnapSeetDataStore.ChatMessage.TYPE + " = ?";
                selectionArgs = new String[]{username, SnapSeetDataStore.ChatMessage.TYPE_MSG};

                break;
            }
            case SnapSheetDataStoreUtils.TABLE_ID_CHATMESSAGE_WITH_TO_USER_MSG: {
                String username = uri.getLastPathSegment();
                Log.i(LOG_TAG, "delete msg type message to " + username);
                selection = SnapSeetDataStore.ChatMessage.TO + " = ? and " +
                        SnapSeetDataStore.ChatMessage.TYPE + " = ?";
                selectionArgs = new String[] {username, SnapSeetDataStore.ChatMessage.TYPE_MSG};
                break;
            }

            case SnapSheetDataStoreUtils.TABLE_ID_CHATMESSAGE_WITH_FROM_USER_IMG: {
                String username = uri.getLastPathSegment();
                Log.i(LOG_TAG, "delete img type messages from " + username);
                selection = SnapSeetDataStore.ChatMessage.FROM + "= ? and " +
                        SnapSeetDataStore.ChatMessage.TYPE + " = ? ";
                selectionArgs = new String[]{username, SnapSeetDataStore.ChatMessage.TYPE_IMG};

                break;
            }

            case SnapSheetDataStoreUtils.TABLE_ID_CHATMESSAGE_WITH_TO_USER_IMG: {
                String username = uri.getLastPathSegment();
                Log.i(LOG_TAG, "delete img messages from " + username);
                selection = SnapSeetDataStore.ChatMessage.FROM + "= ? and " +
                        SnapSeetDataStore.ChatMessage.TYPE + " = ?";
                selectionArgs = new String[]{username, SnapSeetDataStore.ChatMessage.TYPE_IMG};

                break;
            }

            case SnapSheetDataStoreUtils.TABLE_ID_CHATMESSAGE_WITH_IMG_ID: {
                String id = uri.getLastPathSegment();
                Log.i(LOG_TAG, "delete img messages from " + id);
                selection = SnapSeetDataStore.ChatMessage._ID + "= ?";
                selectionArgs = new String[]{id};

                break;
            }

            case SnapSheetDataStoreUtils.TABLE_ID_CHATMESSAGE_WITH_IMG_RND: {
                Log.i(LOG_TAG, "delete img messages where status > 0 ");
                selection = SnapSeetDataStore.ChatMessage.STATUS + "> 0";
                break;
            }

        }
        if (table == null) return 0;
        int deleted = mOpenHelper.getWritableDatabase().delete(table, selection, selectionArgs);

        // do not notify change when deleted
        return deleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int tableId = SnapSheetDataStoreUtils.getTableId(uri);
        final String table = SnapSheetDataStoreUtils.getTableNameById(tableId);
        switch (tableId) {
            case SnapSheetDataStoreUtils.TABLE_ID_CHATMESSAGE_WITH_IMG_ID: {
                String id = uri.getLastPathSegment();
                Log.i(LOG_TAG, "update img messages where id =  " + id);
                selection = SnapSeetDataStore.ChatMessage._ID + "= ?";
                selectionArgs = new String[]{id};

                break;
            }

        }
        if (table == null) return 0;
        int updated = mOpenHelper.getWritableDatabase().update(table, values, selection, selectionArgs);
        // how to notify change
        getContext().getContentResolver().notifyChange(uri, null);

        return updated;
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
