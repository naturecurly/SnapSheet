package com.unimelb.feelinglucky.snapsheet.Thread;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.unimelb.feelinglucky.snapsheet.ImageSendActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by leveyleonhardt on 10/5/16.
 */

public class ImageSaver implements Runnable {
    private final Image mImage;
    private final String mImageFilename;
    private Context context;


    public ImageSaver(Image mImage, String filename, Context mContext) {
        this.mImageFilename = filename;
        this.mImage = mImage;
        this.context = mContext;
    }

    @Override
    public void run() {
        ByteBuffer byteBuffer = mImage.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(mImageFilename);
            fileOutputStream.write(bytes);
            Log.i("local", "image saved");
            jumpToImageView(context, mImageFilename);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mImage.close();
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void jumpToImageView(Context context, String filename) {
        Intent intent = new Intent(context, ImageSendActivity.class);
        intent.putExtra("image", filename);
        context.startActivity(intent);
    }
}
