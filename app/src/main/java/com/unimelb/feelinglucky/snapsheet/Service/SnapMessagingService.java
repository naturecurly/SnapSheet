package com.unimelb.feelinglucky.snapsheet.Service;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.unimelb.feelinglucky.snapsheet.Bean.Message;
import com.unimelb.feelinglucky.snapsheet.Database.SnapSeetDataStore;
import com.unimelb.feelinglucky.snapsheet.NetworkService.FileDownloadService;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.NetworkService.ServiceGenerator;
import com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        if (data.containsKey("type")) {
            String type = data.get("type");
            switch (type) {
                case Message.RND:
                    handleRNDTypeMessage(data);
                    break;

                case Message.MSG:
                    handleMSGTyeMessage(data);
                    break;

                case Message.IMG:
                    handleIMGTypeMessage(data);
            }
        }




    }

    private void handleRNDTypeMessage(Map<String, String> data ) {
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
    }

    private void handleMSGTyeMessage(Map<String, String> data) {
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

        String fromUser = data.get("fromUsername");
        DatabaseUtils.updateFriendChatDb(getApplicationContext(), DatabaseInstance.database, fromUser);
    }



    private void handleIMGTypeMessage(Map<String, String> data) {
        String image = data.get("message");  // get the image path from message content
        String imagePath = NetworkSettings.baseUrl + image;

        Uri uri = Uri.parse(image);
        image = uri.getLastPathSegment().toString();
        Log.i(LOG_TAG, "image-name: " + image);


        // download Image, move to SnapSheetMessagingService
        //Retrofit retrofit = new Retrofit.Builder().baseUrl("https://c.hime.io/").build();
        FileDownloadService fileDownloadService = ServiceGenerator.createService(FileDownloadService.class);
        Call<ResponseBody> call = fileDownloadService.downloadFileWithDynamicUrlSync(imagePath);
        String finalImage = image;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(LOG_TAG, "server contacted and has file");

                // TODO: change the test.png name with a dynamic name
                boolean writtenToDisk = writeResponseBodyToDisk(response.body(), getApplicationContext(), finalImage);

                Log.d(LOG_TAG, "file download was a success? " + writtenToDisk);

                if (writtenToDisk) {
                    // store into database by replacing the content with local image file path
                    // this message is for me
                    // TODO: change the test.png name with a dynamic name
                    data.put("message", finalImage);
                    // add a status
                    data.put("status", "1");
                    // if there is not a living time for this image, set a default 5 sec
                    if (!data.containsKey("live_time")) {
                        data.put("live_time", "5");
                    }
                    handleMSGTyeMessage(data);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(LOG_TAG, "error");
                if (t.getMessage() != null) {

                    Log.e(LOG_TAG, t.getMessage());
                }
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, Context context, String filename) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(context.getFilesDir(), filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(LOG_TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
