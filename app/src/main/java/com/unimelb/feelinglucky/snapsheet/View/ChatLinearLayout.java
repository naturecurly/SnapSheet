package com.unimelb.feelinglucky.snapsheet.View;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by leveyleonhardt on 10/11/16.
 */

public class ChatLinearLayout extends LinearLayout {
    private PointF current;
    private PointF origin;
    private CustomizedViewPager pager;

    public ChatLinearLayout(Context context) {
        super(context);
    }

    public ChatLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        current = new PointF(event.getRawX(), event.getRawY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                origin = current;
                getParent().requestDisallowInterceptTouchEvent(true);

                break;
            case MotionEvent.ACTION_MOVE:
                float distanceX = current.x - origin.x;
                float distanceY = current.y - origin.y;
                if (distanceX > 0) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);

                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                pager.requestDisallowInterceptTouchEvent(false);

        }
        return super.onTouchEvent(event);

    }
}
