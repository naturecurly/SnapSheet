package com.unimelb.feelinglucky.snapsheet.Story;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.unimelb.feelinglucky.snapsheet.Discover.DensityUtil;
import com.unimelb.feelinglucky.snapsheet.Discover.ImgBrowserView;
import com.unimelb.feelinglucky.snapsheet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhaoliu on 1/10/16.
 */
public class StoriesFragment extends Fragment {
    private static final String TAG = "SnapSheet";

    private ImgBrowserView browserView;

    private int cellPadding;

    ArrayList<StoryInterface> stories = new ArrayList<>();
    RelativeLayout container;
    LinearLayout storyLinearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stories, container, false);
        this.storyLinearLayout = (LinearLayout) view.findViewById(R.id.stories_linear_layout);
        this.container = (RelativeLayout) view.findViewById(R.id.container);
        this.browserView = new ImgBrowserView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.browserView.setLayoutParams(params);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cellPadding = DensityUtil.dip2px(getContext(),5);
        setStories(stories);
    }

    public <E extends StoryInterface> void setStories(List<E> stories){
        this.stories.addAll(stories);
        if (this.storyLinearLayout != null) {
            this.storyLinearLayout.removeAllViews();
            for (StoryInterface story : stories
                    ) {
                StoryView newStoryView = this.createStoryViewByStory(story);
                newStoryView.setPadding(0, 0, 0, cellPadding);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                newStoryView.setLayoutParams(params);
                this.storyLinearLayout.addView(newStoryView);
            }
        }
    }

    private StoryView createStoryViewByStory(StoryInterface story){
        final StoryView storyView = new StoryView(getContext());
        storyView.setStory(story);
        storyView.setProfileImg(story.getProfileUrl());
        storyView.setText(story.getStoryText());
        storyView.setImgs(story.getStoryImgUrls());
        storyView.setTimeText(story.getTimePassedString());

        storyView.setOnImageClickListener(new StoryView.OnImageClickListener() {
            @Override
            public void onStoryImageClick(View view, int index, List<String> urls) {
                ScaleAnimation anim = new ScaleAnimation(0,1,0,1
                , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setDuration(300);
                anim.setFillAfter(false);
                browserView.setUrls(urls);
                browserView.getImgBrowserViewPager().setCurrentItem(index);
                browserView.setOnDisappearAnimationEnd(new ImgBrowserView.OnDisappearAnimationEnd() {
                    @Override
                    public void OnDisappearAnimationEnd() {
                        container.removeView(browserView);
                    }
                });
                container.addView(browserView);
                browserView.startAnimation(anim);
            }

            @Override
            public void onProfileImageClick(View view) {

            }
        });

        return storyView;
    }
}
