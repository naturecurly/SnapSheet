package com.unimelb.feelinglucky.snapsheet.Camera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.unimelb.feelinglucky.snapsheet.Adapter.SettingAdapter;
import com.unimelb.feelinglucky.snapsheet.R;

/**
 * Created by leveyleonhardt on 10/12/16.
 */

public class CameraSettingFragment extends Fragment {
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_settings, container, false);
        listView = (ListView) view.findViewById(R.id.settings_listview);
        listView.setAdapter(new SettingAdapter(getActivity()));
        return view;
    }
}
