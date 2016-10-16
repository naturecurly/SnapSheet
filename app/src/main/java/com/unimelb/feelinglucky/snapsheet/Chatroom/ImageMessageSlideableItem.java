package com.unimelb.feelinglucky.snapsheet.Chatroom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.Util.DensityUtil;
import com.unimelb.feelinglucky.snapsheet.View.CustomizedViewPager;
import com.unimelb.feelinglucky.snapsheet.View.SlideableItem;

/**
 * Created by asahui on 10/10/2016.
 */

public class ImageMessageSlideableItem extends MessageSlideableItem {

    private String image;  // image path


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public ImageMessageSlideableItem(Context context) {
        super(context);
    }

    public ImageMessageSlideableItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


}
