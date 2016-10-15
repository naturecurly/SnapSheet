package com.unimelb.feelinglucky.snapsheet.Service;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.unimelb.feelinglucky.snapsheet.Bean.Message;
import com.unimelb.feelinglucky.snapsheet.Database.SnapSeetDataStore;
import com.unimelb.feelinglucky.snapsheet.Database.SnapSheetDataStoreUtils;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by leveyleonhardt on 10/7/16.
 */

public class SnapMessagingService extends FirebaseMessagingService {

    public static final String LOG_TAG = SnapMessagingService.class.getSimpleName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();

        // log
        Set<Map.Entry<String, String>> dataSet = data.entrySet();
        for (Map.Entry<String, String> e : dataSet) {
            Log.i(LOG_TAG, e.getKey() + " : " + e.getValue());
        }

        // first, handle the `read` message
        if (data.containsKey("type") && data.get("type").equalsIgnoreCase(Message.RND)) {
            // delete all the message sent to this user

            String toUser = data.get("fromUsername"); // delete all the message I sent to this `from` user
            if (toUser != null) {
                Uri chatMessageWithUserUri = SnapSeetDataStore.ChatMessage.CONTENT_URI_TO_USER.buildUpon().appendEncodedPath(toUser).build();
                getContentResolver().delete(chatMessageWithUserUri, null, null);
            }
            return;
        }

        // second, handle the `msg` or `img` message
        // store these message into database
        String username = SharedPreferencesUtils.getSharedPreferences(getApplicationContext()).getString(SharedPreferencesUtils.USERNAME, "");
        if (username.isEmpty()) {
            Log.e(LOG_TAG, "current user owns an empty username, weird");
            return;
        }

        // this message is for me
        data.put("toUser", username);
        Uri chatMessageUri = SnapSeetDataStore.ChatMessage.CONTENT_URI.buildUpon().build();
        ContentValues values = DatabaseUtils.buildChatMessage(data);
        getContentResolver().insert(chatMessageUri, values);

    }
}
