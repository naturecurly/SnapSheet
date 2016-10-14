package com.unimelb.feelinglucky.snapsheet.Story;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ListView;
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

    private ArrayList<StoryInterface> stories = new ArrayList<>();
    private ArrayList<StoryView> storyViewArrayList = new ArrayList<>();

    private RelativeLayout container;
//    LinearLayout storyLinearLayout;
    private SwipeRefreshLayout mSwipeLayout;

    private ListView mListView;
    private StoryListViewAdapter adapter;

    private RefreshTask refreshTask;

    private Context context;

    private OnRefreshListener onRefreshListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stories, container, false);
        context = getContext();
        this.container = (RelativeLayout) view.findViewById(R.id.container);
        this.mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        this.mListView = (ListView) view.findViewById(R.id.listview);
        this.browserView = new ImgBrowserView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.browserView.setLayoutParams(params);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cellPadding = DensityUtil.dip2px(getContext(),5);
        initView();
    }

    private void initView(){
        mSwipeLayout.setOnRefreshListener(() -> {
           if(refreshTask == null){
               refreshTask = new RefreshTask();
               refreshTask.execute();
           }
        });


        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setDistanceToTriggerSync(400);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setProgressBackgroundColor(R.color.colorAccent); // 设定下拉圆圈的背景
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小

        adapter = new StoryListViewAdapter(this.storyViewArrayList);
        this.mListView.setAdapter(adapter);
    }

    public <E extends StoryInterface> void addStories(List<E> stories){
        if (getContext() != null){
            this.stories.clear();
            this.storyViewArrayList.clear();
            for (int i = 0; i < stories.size(); i++) {
                StoryInterface story = stories.get(stories.size()-1 - i);
                this.stories.add(0,story);
                StoryView newStoryView = createStoryViewByStory(story);
                this.storyViewArrayList.add(0,newStoryView);
            }

            if (adapter != null){
                adapter.notifyDataSetChanged();
            }
        }

    }

    private StoryView createStoryViewByStory(StoryInterface story){
        final StoryView storyView = new StoryView(context);
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

    public interface OnRefreshListener{
        void startRefresh(ArrayList<StoryInterface> nowStories);
        void endRefresh(ArrayList<StoryInterface> nowStories);
    }

    private class RefreshTask extends AsyncTask<String, Integer, ArrayList<Story>>{
        ArrayList<Story> newStories = new ArrayList<>();

        @Override
        protected ArrayList<Story> doInBackground(String... params) {
            newStories = (ArrayList<Story>) SimulateStory.simulateStories();

            return newStories;
        }

        @Override
        protected void onPostExecute(ArrayList<Story> newStories) {
            //insert new stories

            refreshTask = null;
            if (mSwipeLayout!=null){
                mSwipeLayout.setRefreshing(false);
            }

            addStories(newStories);
            super.onPostExecute(newStories);
        }
    }
}
