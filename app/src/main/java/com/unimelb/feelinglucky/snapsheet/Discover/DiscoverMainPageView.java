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
import com.unimelb.feelinglucky.snapsheet.Story.DBManager;
import com.unimelb.feelinglucky.snapsheet.Story.Story;

import java.util.ArrayList;
import java.util.Collections;
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
//        DBManager.getInstance(context).clearItems();
//        initItems();

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

    public void initItems(){
        List<DiscoverItem> items = new ArrayList<>();

        List<String> coverURLs = new ArrayList<>();
        coverURLs.add("http://image.shutterstock.com/display_pic_with_logo/2971852/288918572/stock-vector-background-abstract-orange-black-sport-basketball-ball-frame-vertical-gold-ribbon-illustration-288918572.jpg");
        coverURLs.add("http://previews.123rf.com/images/matthewephotography/matthewephotography1311/matthewephotography131101064/23774188-Young-woman-smiling-with-shopping-bags-while-walking-on-the-street-Vertical-Shot--Stock-Photo.jpg");
        coverURLs.add("https://cdn.vectorstock.com/i/composite/70,81/cartoon-vertical-night-landscape-vector-6107081.jpg");
        coverURLs.add("https://cdn.colorlib.com/wp/wp-content/uploads/sites/2/food-recipe-sharing-themes.jpg");
        coverURLs.add("http://fuze.lu/wp-content/uploads/2013/10/Singer-wiki-commons.jpg");
        coverURLs.add("http://images.clipartpanda.com/movie-night-clipart-9cp4q9xcE.jpeg");

        List<String> contents = new ArrayList<>();
        contents.add("http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg");
        contents.add("https://pmcdeadline2.files.wordpress.com/2016/06/angelababy.jpg?w=970");
        contents.add("http://img.ixiumei.com/uploadfile/2016/0819/20160819105642918.png");
        contents.add("http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg");
        contents.add("http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg");

        for (int i = 0; i < coverURLs.size(); i++) {
            DiscoverItem item = new DiscoverItem();
            item.clickCount = 0;
            item.setCoverURL(coverURLs.get(i));
            item.setContentURLs(contents);
            items.add(item);
        }

        DBManager.getInstance(context).insertDiscoveryItemList(items);
    }

    public List<DiscoverItem> loadItems(){
        return DBManager.getInstance(context).getAllItems();
    }

    public void refresh(){
        ArrayList<DiscoverItemInterface> newItems = new ArrayList<>();
        List<DiscoverItem> items = DBManager.getInstance(context).getAllItems();
        Collections.sort(items);

        newItems.addAll(items);
        discoverRecyclerViewAdapter.setItems(newItems);
        discoverRecyclerViewAdapter.notifyDataSetChanged();
    }

    public interface OnClickImage{
        void onClickImage(DiscoverItemInterface item, List<String> urls);
    }

    private class RefreshTask extends AsyncTask<String, Integer, List<DiscoverItemInterface>> {
        ArrayList<DiscoverItemInterface> newItems = new ArrayList<>();

        @Override
        protected List<DiscoverItemInterface> doInBackground(String... params) {

            List<DiscoverItem> items = DBManager.getInstance(context).getAllItems();
            Collections.sort(items);

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
