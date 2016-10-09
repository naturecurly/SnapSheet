package com.unimelb.feelinglucky.snapsheet.Story;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.unimelb.feelinglucky.snapsheet.R;

import java.util.ArrayList;

/**
 * Created by yuhaoliu on 3/10/16.
 */
public class StoryListViewAdapter extends BaseAdapter {
    private ArrayList<StoryView> storyViews = new ArrayList<>();

    public StoryListViewAdapter(ArrayList<StoryView> storyViews) {
        this.storyViews = storyViews;
    }

    @Override
    public int getCount() {
        return storyViews.size();
    }

    @Override
    public Object getItem(int position) {
        return storyViews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        storyViews.get(position).setAlpha(0);
        storyViews.get(position).animate().alpha(1).setDuration(500).start();
        return storyViews.get(position);
    }
}
