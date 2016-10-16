package com.unimelb.feelinglucky.snapsheet.Database;

import android.content.UriMatcher;
import android.net.Uri;

import com.unimelb.feelinglucky.snapsheet.Database.SnapSeetDataStore.*;

/**
 * Created by Xuhui Chen (yorkfine) on 14/10/2016.
 */

public class SnapSheetDataStoreUtils {
    static final UriMatcher CONTENT_PROVIDER_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int TABLE_ID_CHATMESSAGE = 10;
    public static final int TABLE_ID_CHATMESSAGE_WITH_FROM_USER = 11;
    public static final int TABLE_ID_CHATMESSAGE_WITH_TO_USER = 12;
    public static final int TABLE_ID_CHATMESSAGE_WITH_USER = 13;
    public static final int TABLE_ID_CHATMESSAGE_WITH_FROM_USER_MSG = 14;
    public static final int TABLE_ID_CHATMESSAGE_WITH_TO_USER_MSG = 15;
    public static final int TABLE_ID_CHATMESSAGE_WITH_FROM_USER_IMG = 16;
    public static final int TABLE_ID_CHATMESSAGE_WITH_TO_USER_IMG = 17;
    public static final int TABLE_ID_CHATMESSAGE_WITH_IMG_ID = 18;

    public static final int TABLE_ID_CHATFRIENDLIST = 20;

    static {
        CONTENT_PROVIDER_URI_MATCHER.addURI(SnapSeetDataStore.AUTHORITY, ChatMessage.CONTENT_PATH, TABLE_ID_CHATMESSAGE);
        CONTENT_PROVIDER_URI_MATCHER.addURI(SnapSeetDataStore.AUTHORITY, ChatMessage.CONTENT_PATH + "/" + ChatMessage.TYPE_ANY + "/" + ChatMessage.FROM_USER + "/*", TABLE_ID_CHATMESSAGE_WITH_FROM_USER);
        CONTENT_PROVIDER_URI_MATCHER.addURI(SnapSeetDataStore.AUTHORITY, ChatMessage.CONTENT_PATH + "/" + ChatMessage.TYPE_ANY + "/" + ChatMessage.TO_USER + "/*", TABLE_ID_CHATMESSAGE_WITH_TO_USER);
        CONTENT_PROVIDER_URI_MATCHER.addURI(SnapSeetDataStore.AUTHORITY, ChatMessage.CONTENT_PATH + "/" + ChatMessage.USER + "/*", TABLE_ID_CHATMESSAGE_WITH_USER);
        CONTENT_PROVIDER_URI_MATCHER.addURI(SnapSeetDataStore.AUTHORITY, ChatMessage.CONTENT_PATH + "/" + ChatMessage.TYPE_MSG + "/" + ChatMessage.FROM_USER + "/*", TABLE_ID_CHATMESSAGE_WITH_FROM_USER_MSG);
        CONTENT_PROVIDER_URI_MATCHER.addURI(SnapSeetDataStore.AUTHORITY, ChatMessage.CONTENT_PATH + "/" + ChatMessage.TYPE_MSG + "/" + ChatMessage.TO_USER + "/*", TABLE_ID_CHATMESSAGE_WITH_TO_USER_MSG);

        CONTENT_PROVIDER_URI_MATCHER.addURI(SnapSeetDataStore.AUTHORITY, ChatMessage.CONTENT_PATH + "/" + ChatMessage.TYPE_IMG + "/" + ChatMessage.FROM_USER + "/*", TABLE_ID_CHATMESSAGE_WITH_FROM_USER_IMG);
        CONTENT_PROVIDER_URI_MATCHER.addURI(SnapSeetDataStore.AUTHORITY, ChatMessage.CONTENT_PATH + "/" + ChatMessage.TYPE_IMG + "/" + ChatMessage.TO_USER + "/*", TABLE_ID_CHATMESSAGE_WITH_TO_USER_IMG);
        CONTENT_PROVIDER_URI_MATCHER.addURI(SnapSeetDataStore.AUTHORITY, ChatMessage.CONTENT_PATH + "/" + ChatMessage.TYPE_IMG + "/" + ChatMessage.IMG_ID + "/*", TABLE_ID_CHATMESSAGE_WITH_IMG_ID);

        CONTENT_PROVIDER_URI_MATCHER.addURI(SnapSeetDataStore.AUTHORITY, ChatFriendList.CONTENT_PATH, TABLE_ID_CHATFRIENDLIST);
    }

    /**
     * get the table id. Here table means a view, a select result; id means the only indicator of this view.
     * @param uri
     * @return
     */
    public static int getTableId(final Uri uri) {
        if (uri == null) return -1;
        return CONTENT_PROVIDER_URI_MATCHER.match(uri);
    }

    public static String getTableNameById(final int id) {
        switch (id) {
            case TABLE_ID_CHATMESSAGE:
            case TABLE_ID_CHATMESSAGE_WITH_FROM_USER:
            case TABLE_ID_CHATMESSAGE_WITH_TO_USER:
            case TABLE_ID_CHATMESSAGE_WITH_FROM_USER_MSG:
            case TABLE_ID_CHATMESSAGE_WITH_TO_USER_MSG:
            case TABLE_ID_CHATMESSAGE_WITH_FROM_USER_IMG:
            case TABLE_ID_CHATMESSAGE_WITH_TO_USER_IMG:
            case TABLE_ID_CHATMESSAGE_WITH_USER:
            case TABLE_ID_CHATMESSAGE_WITH_IMG_ID:
                return ChatMessage.TABLE_NAME;


            case TABLE_ID_CHATFRIENDLIST:
                return ChatFriendList.TABLE_NAME;

            default:
                return null;
        }
    }

    // We had better put some specific database query methods here.
    // DatabaseUtils contains API that build content value

    public static String createTable(final String tableName, final String[] columns, final String[] types, final String... constraints) {
        if (tableName == null) {
            throw new NullPointerException("Name must not be null");
        }
        if (columns == null) {
            throw new NullPointerException("Columns must not be null");
        }
        if (types == null || columns.length != types.length) {
            throw new IllegalArgumentException("length of columns and types not match");
        }
        String newColumns[] = new String[columns.length];
        for (int i = 0; i < newColumns.length; i++) {
            newColumns[i] = String.format("%s %s", columns[i], types[i]);
        }
        final StringBuilder sb = new StringBuilder("CREATE ");
        sb.append("TABLE ");
        sb.append(tableName);
        sb.append(' ');
        if (newColumns != null && newColumns.length > 0) {
            sb.append('(');
            sb.append(joinArray(newColumns, ',', true));
            if (constraints != null && constraints.length > 0) {
                sb.append(", ");
                sb.append(joinArray(constraints, ',', true));
                sb.append(' ');
            }
            sb.append(')');
        }
        return sb.toString();
    }

    private static String joinArray(final String[] columns, final char token, final boolean includeSpace) {
        final StringBuilder sb = new StringBuilder();
        final int length = columns.length;
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                sb.append(includeSpace ? token + " " : token);
            }
            sb.append(columns[i]);
        }
        return sb.toString();
    }
}
