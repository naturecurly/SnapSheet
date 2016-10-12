package com.unimelb.feelinglucky.snapsheet.Discover;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.Story.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhaoliu on 7/09/16.
 */
public class DiscoverMainPageView extends RelativeLayout {
    private final String TAG = "SnapSheet";
    private Context context;
    private View root;

    private RecyclerView discoveryRecyclerView;
    private DiscoverRecyclerViewAdapter discoverRecyclerViewAdapter;
    private GridLayoutManager gridLayoutManager;

    private OnClickImage onClickImage;
    private RefreshTask refreshTask;

    public DiscoverMainPageView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DiscoverMainPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init(){
        discoveryRecyclerView = new RecyclerView(context);
        discoverRecyclerViewAdapter = new DiscoverRecyclerViewAdapter(context);
        refreshTask = new RefreshTask();
        refreshTask.execute();
        if (this.onClickImage != null){
            discoverRecyclerViewAdapter.setOnClickImage(this.onClickImage);
        }
        discoveryRecyclerView.setHasFixedSize(false);
        discoveryRecyclerView.setAdapter(discoverRecyclerViewAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        discoveryRecyclerView.addItemDecoration(new RecyclerDivider(spacingInPixels));
        gridLayoutManager = new GridLayoutManager(context,2);
        discoveryRecyclerView.setLayoutManager(gridLayoutManager);

        this.addView(discoveryRecyclerView,new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setOnClickImage(OnClickImage onClickImage) {
        this.onClickImage = onClickImage;
        if (discoverRecyclerViewAdapter != null){
            discoverRecyclerViewAdapter.setOnClickImage(onClickImage);
        }
    }

    public List<DiscoverItemInterface> simulateData(){
        List<DiscoverItemInterface> items = new ArrayList<>();

        String coverURL = "http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg";
        List<String> contents = new ArrayList<>();
        contents.add("http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg");
        contents.add("https://pmcdeadline2.files.wordpress.com/2016/06/angelababy.jpg?w=970");
        contents.add("http://img.ixiumei.com/uploadfile/2016/0819/20160819105642918.png");
        contents.add("http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg");
        contents.add("http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg");

        for (int i = 0; i < 10; i++) {
            DiscoverItem item = new DiscoverItem();
            item.setCoverURL(coverURL);
            item.setContentURLs(contents);
            items.add(item);
        }

        return items;
    }

    public interface OnClickImage{
        void onClickImage(List<String> urls);
    }

    private class RefreshTask extends AsyncTask<String, Integer, List<DiscoverItemInterface>> {
        ArrayList<DiscoverItemInterface> newItems = new ArrayList<>();

        @Override
        protected List<DiscoverItemInterface> doInBackground(String... params) {
            List<DiscoverItemInterface> items = new ArrayList<>();

            String coverURL = "http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg";
            List<String> contents = new ArrayList<>();
            contents.add("http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg");
//            contents.add("https://pmcdeadline2.files.wordpress.com/2016/06/angelababy.jpg?w=970");
//            contents.add("http://img.ixiumei.com/uploadfile/2016/0819/20160819105642918.png");
//            contents.add("http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg");
//            contents.add("http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg");

            for (int i = 0; i < 1; i++) {
                DiscoverItem item = new DiscoverItem();
                item.setCoverURL(coverURL);
                item.setContentURLs(contents);
                items.add(item);
            }
            newItems.addAll(items);
            return newItems;
        }

        @Override
        protected void onPostExecute(List<DiscoverItemInterface> items) {
            //insert new stories

            refreshTask = null;
            if (discoverRecyclerViewAdapter != null){
                discoverRecyclerViewAdapter.setItems(newItems);
                discoverRecyclerViewAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(items);
        }
    }

}
