package com.unimelb.feelinglucky.snapsheet.Discover;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhaoliu on 9/09/2016.
 */
public class ImgBrowserView extends RelativeLayout {
    private static final String TAG = "SnapSheet";
    private Context context;
    private LinearLayout root;
    private ImgBrowserViewPager browserPager;
    private ImgBrowserPagerAdapter browserAdapter;
    private List<String> urls = new ArrayList<>();
    private OnDisappearAnimationEnd onDisappearAnimationEnd;

    public ImgBrowserView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ImgBrowserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init(){
        root = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LayoutTransition lt = new LayoutTransition();
        lt.disableTransitionType(LayoutTransition.DISAPPEARING);
        root.setLayoutTransition(lt);
        root.setLayoutParams(params);

        browserPager = new ImgBrowserViewPager(context);
        browserPager.setLongClickable(true);
        browserPager.setOnPagerSlide(new ImgBrowserViewPager.OnPagerSlide() {
            @Override
            public void onAnimateEnd() {
                if (onDisappearAnimationEnd != null){
                    onDisappearAnimationEnd.OnDisappearAnimationEnd();
                }
                browserPager.setY(0);
                browserPager.setCurrentItem(0);
            }
        });
        this.setUpPager();
        root.addView(browserPager);
        this.addView(root);
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
        if (browserAdapter != null){
            browserAdapter.getUrls().clear();
            browserAdapter.getUrls().addAll(urls);
            browserAdapter.notifyDataSetChanged();
        }
    }
    public ImgBrowserPagerAdapter getBrowserAdapter(){
        return browserAdapter;
    }

    public ImgBrowserViewPager getImgBrowserViewPager(){
        return browserPager;
    }

    private void setUpPager(){
        browserAdapter = new ImgBrowserPagerAdapter(context);
        if (this.urls != null){
            browserAdapter.setUrls(urls);
            browserAdapter.notifyDataSetChanged();
        }
        browserPager.setAdapter(browserAdapter);
    }

    public interface OnDisappearAnimationEnd{
        void OnDisappearAnimationEnd();
    }


    public void setOnDisappearAnimationEnd(OnDisappearAnimationEnd onDisappearAnimationEnd){
        this.onDisappearAnimationEnd = onDisappearAnimationEnd;
    }
}
