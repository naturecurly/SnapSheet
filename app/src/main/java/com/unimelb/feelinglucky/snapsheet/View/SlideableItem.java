package com.unimelb.feelinglucky.snapsheet.View;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.Util.DensityUtil;

/**
 * Created by leveyleonhardt on 8/11/16.
 */
public class SlideableItem extends FrameLayout {
    private final String TAG = this.getClass().getSimpleName();
    LinearLayout mLinearLayout;
    private PointF origin;
    private final int pullLimit = DensityUtil.dip2px(getContext(), 65);
    private TextView mTextView;
    private PullToLimitListener mListener;
    private CustomizedViewPager pager;
    private PointF last;
    private PointF current;
    private boolean flag;
    private boolean skip;

    public SlideableItem(Context context) {
        super(context);
    }

    public SlideableItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(ContextCompat.getColor(context, R.color.lower_chat_entrance_blue));
        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setBackgroundColor(Color.WHITE);
        mLinearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 65)));

        ImageView lowerImage = new ImageView(context);
        lowerImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_chat_entrance));
        LayoutParams lowerImageParams = new LayoutParams(DensityUtil.dip2px(context, 30), DensityUtil.dip2px(context, 30));
        lowerImageParams.setMargins(DensityUtil.dip2px(context, 15), 0, 0, 0);
        lowerImageParams.gravity = Gravity.CENTER_VERTICAL;
        lowerImage.setLayoutParams(lowerImageParams);

        ImageView mImageView = new ImageView(context);
        mImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_chat_indication));
        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 30), DensityUtil.dip2px(context, 30));
        imageViewParams.gravity = Gravity.CENTER_VERTICAL;
        imageViewParams.setMargins(DensityUtil.dip2px(context, 10), 0, 0, 0);
        mImageView.setLayoutParams(imageViewParams);

        mTextView = new TextView(context);
        mTextView.setText("Leon Wu");
        LinearLayout.LayoutParams mTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTextViewParams.gravity = Gravity.CENTER_VERTICAL;
        mTextViewParams.setMargins(DensityUtil.dip2px(context, 10), 0, 0, 0);
        mTextView.setLayoutParams(mTextViewParams);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


        addView(lowerImage);
        mLinearLayout.addView(mImageView);
        mLinearLayout.addView(mTextView);
        addView(mLinearLayout);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        current = new PointF(event.getRawX(), event.getRawY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.i("Event", "Down");
                origin = current;
                last = current;
                pager = (CustomizedViewPager) getRootView().findViewById(R.id.activity_fragment_view_pager);
                pager.requestDisallowInterceptTouchEvent(true);
                pager.beginFakeDrag();
                skip = false;
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.i("Event", "Move");
                float distance = current.x - origin.x;
                float distanceY = current.y - origin.y;
                if (distance - 20 > 0 && Math.abs(distance) > Math.abs(distanceY)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    Log.i(TAG, "Horizon");
                    LayoutParams params = (LayoutParams) mLinearLayout.getLayoutParams();
                    if (flag) {
                        pager.fakeDragBy(current.x - last.x);
                    }
                    if (distance < pullLimit) {
                        if (!flag) {
                            params.setMargins((int) distance, 0, 0, 0);
                            requestLayout();
                            if (pullLimit - distance < 30) {
                                flag = true;
                                if (mListener != null) {
                                    mListener.openChat();
                                }
                                break;
                            }
                        }
                    }
                    if (distance > pullLimit) {
                        flag = true;
                    }
                } else if (distance + 20 < 0 && Math.abs(distance) > Math.abs(distanceY)) {
                    pager.fakeDragBy(current.x - last.x);
                    getParent().requestDisallowInterceptTouchEvent(true);

                }
//                } else if (distance < Math.abs(distanceY)) {
//                    Log.i(TAG, "verticle");
////                    ((RecyclerView) (getRootView().findViewById(R.id.fragment_chat_recyclerview))).requestDisallowInterceptTouchEvent(false);
////                    getParent().requestDisallowInterceptTouchEvent(false);
//                    skip = true;
////                    return false;
//                    break;
//                }
                last = current;

                break;
            case MotionEvent.ACTION_UP:
                pager.endFakeDrag();
                flag = false;
                fingerUpEvent();
                break;
            case MotionEvent.ACTION_CANCEL:
                pager.endFakeDrag();

                fingerUpEvent();
                break;

        }
        Log.i(TAG, "skip" + !skip);
        return true;
    }


    public void setUsername(String username) {
        mTextView.setText(username);
    }

    public void fingerUpEvent() {
        LayoutParams params = (LayoutParams) mLinearLayout.getLayoutParams();
        final ValueAnimator animator = ValueAnimator.ofInt(params.leftMargin, 0);
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LayoutParams params = (LayoutParams) mLinearLayout.getLayoutParams();
                params.setMargins((Integer) animator.getAnimatedValue(), 0, 0, 0);
                mLinearLayout.setLayoutParams(params);
            }
        });
        animator.start();
//        getParent().requestDisallowInterceptTouchEvent(true);
        Log.i(TAG, this.toString());
    }

    public void setOnPullToLimitListener(PullToLimitListener listener) {
        mListener = listener;
    }


    public interface PullToLimitListener {
        void openChat();
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
////        return super.dispatchTouchEvent(ev);
//
//        return false;
//    }


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.i(TAG, "intercept");
//        current = new PointF(ev.getRawX(), ev.getRawY());
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                origin = current;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float distanceX = ev.getRawX() - origin.x;
//                float distanceY = ev.getRawY() - origin.y;
//                if (Math.abs(distanceX) < Math.abs(distanceY)) {
//                    Log.i(TAG, "dispatch move");
////                    getParent().requestDisallowInterceptTouchEvent(false);
//                    return false;
//                } else {
//                    break;
//                }
//        }
//        return super.onInterceptTouchEvent(ev);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}


