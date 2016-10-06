package com.unimelb.feelinglucky.snapsheet.Camera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SnapSheetActivity;

/**
 * Created by mac on 16/10/6.
 */

public class AddFriendNearByFragment extends Fragment {
    private Button mScan;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_nearby, container, false);
        mScan = (Button) view.findViewById(R.id.near_by_scan);
        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SnapSheetActivity)getActivity()).scanUser();
            }
        });

        return view;
    }
}
