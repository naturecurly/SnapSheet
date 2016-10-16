
package com.unimelb.feelinglucky.snapsheet.Memory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unimelb.feelinglucky.snapsheet.R;

/**
 * Created by leveyleonhardt on 9/7/16.
 */
public class MemoryContainerFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memory_container, container, false);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_memory_container);
        if (fragment == null) {
            fragmentManager.beginTransaction().add(R.id.fragment_memory_container, new MemoryFragment()).commit();
        }
        return view;
    }
}
