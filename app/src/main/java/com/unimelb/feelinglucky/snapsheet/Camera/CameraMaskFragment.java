package com.unimelb.feelinglucky.snapsheet.Camera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SnapSheetActivity;
import com.unimelb.feelinglucky.snapsheet.View.ShutterButton;

/**
 * Created by leveyleonhardt on 9/7/16.
 */
public class CameraMaskFragment extends Fragment {
    private Button switchLensButton;
    private Button switchFlashButton;
    private SnapSheetActivity activity;
    private ShutterButton shutterButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (SnapSheetActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_mask, container, false);
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
            }
        });
        return view;
    }
}
