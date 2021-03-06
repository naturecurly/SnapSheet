package com.unimelb.feelinglucky.snapsheet.Discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unimelb.feelinglucky.snapsheet.R;

import java.util.List;

public class DiscoverFragment extends Fragment {

    private static final String TAG = "SnapSheet";

    private DiscoverMainPageView mainPageView;
    private ImgBrowserView imgBrowserView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        mainPageView = (DiscoverMainPageView) view.findViewById(R.id.main_page_view);
        imgBrowserView = (ImgBrowserView) view.findViewById(R.id.img_browser_view);
        this.init();

        return view;
    }

    private void init(){
        mainPageView.setOnClickImage((itemInterface, urls) -> {
            DiscoverItem item = (DiscoverItem) itemInterface;
            item.clickCount += 1;
            item.update();

            imgBrowserView.setVisibility(View.VISIBLE);
            imgBrowserView.setUrls(urls);
            imgBrowserView.bringToFront();
            mainPageView.refresh();

            if (imgBrowserView.getY() == imgBrowserView.getHeight()){
                imgBrowserView.animate()
                        .translationY(0)
                        .setDuration(200);
            }

            imgBrowserView.setOnDisappearAnimationEnd(() -> {
                imgBrowserView.setY(imgBrowserView.getHeight());
                mainPageView.bringToFront();
            });
        });
    }
}
