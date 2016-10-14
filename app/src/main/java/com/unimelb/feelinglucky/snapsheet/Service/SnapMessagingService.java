package com.unimelb.feelinglucky.snapsheet.Service;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.unimelb.feelinglucky.snapsheet.Database.SnapSeetDataStore;
import com.unimelb.feelinglucky.snapsheet.Database.SnapSheetDataStoreUtils;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;

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

        Uri chatMessageUri = SnapSeetDataStore.ChatMessage.CONTENT_URI.buildUpon().build();

        ContentValues values = DatabaseUtils.buildChatMessage(data);
        getContentResolver().insert(chatMessageUri, values);
        Set<Map.Entry<String, String>> dataSet = data.entrySet();
        for (Map.Entry<String, String> e : dataSet) {
            Log.i(LOG_TAG, e.getKey() + " : " + e.getValue());
        }

    }
}
