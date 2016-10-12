package com.unimelb.feelinglucky.snapsheet.Camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SnapSheetActivity;
import com.unimelb.feelinglucky.snapsheet.View.ShutterButton;

import java.io.File;

/**
 * Created by leveyleonhardt on 9/7/16.
 */
public class CameraMaskFragment extends Fragment {
    private Button switchLensButton;
    private Button switchFlashButton;
    private SnapSheetActivity activity;
    private ShutterButton shutterButton;
    private TextView faceCount;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (SnapSheetActivity) getActivity();
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter("updateFaceCount");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String number = intent.getExtras().getString("count");
                if (number != null)
                    Log.i("facecount", number);
                updateFaceCount(number);
            }
        };
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_mask, container, false);
        faceCount = (TextView) view.findViewById(R.id.face_count);
        faceCount.setText("0");
        switchLensButton = (Button) view.findViewById(R.id.fragment_switch_lens_button);
        switchLensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.switchLens();
            }
        });
        switchFlashButton = (Button) view.findViewById(R.id.fragment_switch_flash_button);
        switchFlashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.switchFlash();
                if (activity.ismIsFlashOn()) {
                    switchFlashButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_flash_on));
                } else {
                    switchFlashButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_flash_off));

                }
            }
        });
        shutterButton = (ShutterButton) view.findViewById(R.id.fragment_camera_shutter_button);
        shutterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.takePicture();
//                Uri imageUri = Uri.fromFile(new File("/sdcard/cats.jpg"));
//                Intent intent = new AdobeImageIntent.Builder(activity).setData(imageUri).build();
//
//                activity.startActivity(intent);
            }

        });
        return view;
    }

    public void updateFaceCount(String number) {
        faceCount.setText(number);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }
}
