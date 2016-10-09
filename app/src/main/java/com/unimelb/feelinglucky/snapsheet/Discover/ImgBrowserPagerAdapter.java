package com.unimelb.feelinglucky.snapsheet.Discover;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.unimelb.feelinglucky.snapsheet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhaoliu on 9/09/2016.
 */
public class ImgBrowserPagerAdapter extends PagerAdapter {
    private final String TAG = "SnapSheet";
    private List<String> urls = new ArrayList<>();
    private Context context;

    public ImgBrowserPagerAdapter(Context context) {
        this.context = context;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getUrls() {
        return urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgContainer = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imgContainer.setLayoutParams(params);
        imgContainer.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        imgContainer.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(context).load(urls.get(position)).placeholder(R.drawable.invitee_selected_default_picture).into(imgContainer);

        container.addView(imgContainer);

        return imgContainer;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
