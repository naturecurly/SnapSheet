package com.unimelb.feelinglucky.snapsheet.Discover;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yuhaoliu on 19/09/16.
 */
public class ImgBrowserViewPager extends ViewPager {
    private static final String TAG = "SnapSheet";
    private int downY = 0;
    private boolean isAnimating = false;
    private OnPagerSlide onPagerSlide;

    public ImgBrowserViewPager(Context context) {
        this(context,null);
    }

    public ImgBrowserViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:

                downY = (int)ev.getY();

                getParent().requestDisallowInterceptTouchEvent(true);

                break;

            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                int offset = moveY - downY;

                if (offset > 200 && !isAnimating) {
                    isAnimating = true;
                    this.animate()
                            .translationY(this.getHeight())
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    isAnimating = false;
                                    if (onPagerSlide != null){
                                        onPagerSlide.onAnimateEnd();
                                    }
                                }
                            });
                }else{
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    public void setOnPagerSlide(OnPagerSlide onPagerSlide) {
        this.onPagerSlide = onPagerSlide;
    }

    public interface OnPagerSlide{
        void onAnimateEnd();
    }

}
