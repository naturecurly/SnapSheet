package com.unimelb.feelinglucky.snapsheet.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by leveyleonhardt on 8/31/16.
 */
public class CustomizedViewPager extends ViewPager {
    private boolean isCanScroll = true;

    public CustomizedViewPager(Context context) {
        super(context);
    }

    public CustomizedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }
}
