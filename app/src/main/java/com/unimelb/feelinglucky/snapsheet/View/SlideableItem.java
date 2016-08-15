package com.unimelb.feelinglucky.snapsheet.View;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
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
    LinearLayout mLinearLayout;
    private PointF origin;
    private final int pullLimit = DensityUtil.dip2px(getContext(), 65);
    private TextView mTextView;
    private PullToLimitListener mListener;

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
        PointF current = new PointF(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("Event", "Down");
                origin = current;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("Event", "Move");
                float distance = current.x - origin.x;
                if (distance > 0) {
                    LayoutParams params = (LayoutParams) mLinearLayout.getLayoutParams();
                    if (distance < pullLimit) {
                        params.setMargins((int) distance, 0, 0, 0);
                        Log.i("TTT", distance + " " + pullLimit);
                        requestLayout();
                        if (pullLimit - distance < 20) {
                            mListener.openChat();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i("Event", "Up");
                LayoutParams params = (LayoutParams) mLinearLayout.getLayoutParams();
                final ValueAnimator animator = ValueAnimator.ofInt(params.leftMargin, 0);
                animator.setDuration(100);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        LayoutParams params = (LayoutParams) mLinearLayout.getLayoutParams();
                        params.setMargins((Integer) animator.getAnimatedValue(), 0, 0, 0);
                        mLinearLayout.setLayoutParams(params);
                    }
                });
                animator.start();


                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    public void setUsername(String username) {
        mTextView.setText(username);
    }

    public void setOnPullToLimitListener(PullToLimitListener listener) {
        mListener = listener;
    }

    public interface PullToLimitListener {
        void openChat();
    }

}
