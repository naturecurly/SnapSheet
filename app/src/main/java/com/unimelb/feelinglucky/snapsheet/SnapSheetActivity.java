package com.unimelb.feelinglucky.snapsheet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.unimelb.feelinglucky.snapsheet.Camera.CameraFragment;
import com.unimelb.feelinglucky.snapsheet.Chat.ChatFragment;
import com.unimelb.feelinglucky.snapsheet.Chatroom.ChatRoomFragment;
import com.unimelb.feelinglucky.snapsheet.Discover.DiscoverFragment;
import com.unimelb.feelinglucky.snapsheet.Story.StoryFragment;
import com.unimelb.feelinglucky.snapsheet.View.SlideableItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leveyleonhardt on 8/11/16.
 */
public class SnapSheetActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private SlideableItem mSlideableItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        mViewPager = (ViewPager) findViewById(R.id.activity_fragment_view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        loadFragments();
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        mViewPager.setCurrentItem(2);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSlideableItem = (SlideableItem) mViewPager.findViewById(R.id.friend_item_slideable_item);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (mSlideableItem != null) {
                            mSlideableItem.fingerUpEvent();
                        }
                        break;
                }
                return false;
            }
        });
    }
    public void up(viewPagerUp event) {
        event.doUp(this);

    }
    private void loadFragments() {
        if (fragments.size() == 0) {
            fragments.add(new ChatRoomFragment());
            fragments.add(new ChatFragment());
            fragments.add(new CameraFragment());
            fragments.add(new StoryFragment());
            fragments.add(new DiscoverFragment());
        }
    }



}
