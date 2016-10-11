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

public class MessageSlideableItem extends FrameLayout {
    LinearLayout mLinearLayoutH; // horizontal
    LinearLayout mLinearLayoutVL; // vertical layout on the left hand side
    LinearLayout mLinearLayoutVR; // vertical layout on the right hand side

    private TextView tvId; // Me for myself and `id` for opposite side
    private ChatMessageTextView mChatMessageTextView;
    private TextView tvTimeStamp;
    private String message;

    private final int pullLimit = DensityUtil.dip2px(getContext(), 65);
    private SlideableItem.PullToLimitListener mListener;
    private CustomizedViewPager pager;
    private PointF origin;
    private PointF last;
    private PointF current;
    private boolean flag;
    private boolean skip;

    public MessageSlideableItem(Context context) {
        super(context);
    }

    public MessageSlideableItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        mLinearLayoutH = new LinearLayout(context);
        mLinearLayoutH.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayoutH.setBackgroundColor(Color.WHITE);
        mLinearLayoutH.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 65)));

        mLinearLayoutVL = new LinearLayout(context);
        mLinearLayoutVL.setOrientation(LinearLayout.VERTICAL);
        mLinearLayoutVL.setBackgroundColor(Color.WHITE);
        mLinearLayoutVL.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 65)));

        mLinearLayoutVR = new LinearLayout(context);
        mLinearLayoutVR.setOrientation(LinearLayout.VERTICAL);
        mLinearLayoutVR.setBackgroundColor(Color.WHITE);
        mLinearLayoutVR.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 65)));

        LinearLayout.LayoutParams mTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTextViewParams.gravity = Gravity.CENTER_VERTICAL;

        // left hand side: dummy text view on the top and time stamp on the bottom
        TextView dummyTv  = new TextView(context);
        tvTimeStamp = new TextView(context);
        tvTimeStamp.setText("10:00");
        tvTimeStamp.setLayoutParams(mTextViewParams);

        // right hand side: id on the top and message text on the bottom
        tvId = new TextView(context);
        tvId.setText("Me");
        mChatMessageTextView = new ChatMessageTextView(context);
        mChatMessageTextView.setText("Test message");

        mChatMessageTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.left_border));
        mChatMessageTextView.setPadding(DensityUtil.dip2px(context, 5), 0, 0, 0); // left padding thanks to the left border
        LinearLayout.LayoutParams mTextViewParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTextViewParams1.gravity = Gravity.CENTER_VERTICAL;
        mChatMessageTextView.setLayoutParams(mTextViewParams1);
        mChatMessageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


        // add views and layouts
        mLinearLayoutVL.addView(dummyTv);
        mLinearLayoutVL.addView(tvTimeStamp);
        addView(mLinearLayoutVL);
        mLinearLayoutVR.addView(tvId);
        mLinearLayoutVR.addView(mChatMessageTextView);
        addView(mLinearLayoutVR);
    }

    public void setMessage(String message) {
        mChatMessageTextView.setText(message);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        current = new PointF(event.getRawX(), event.getRawY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                origin = current;
                last = current;
                pager = (CustomizedViewPager) getRootView().findViewById(R.id.activity_fragment_view_pager);
                pager.requestDisallowInterceptTouchEvent(true);
                pager.beginFakeDrag();
                skip = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float distance = current.x - origin.x;
                float distanceY = current.y - origin.y;
                if (distance - 20 > 0 && Math.abs(distance) > Math.abs(distanceY)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    LayoutParams params = (LayoutParams) mLinearLayoutVR.getLayoutParams();
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
                last = current;

                break;
            case MotionEvent.ACTION_UP:
                pager.endFakeDrag();
                flag = false;
                fingerUpEvent();
                break;
            case MotionEvent.ACTION_CANCEL:
                pager.endFakeDrag();
                flag = false;
                fingerUpEvent();
                break;

        }
        return true;
    }


    public void fingerUpEvent() {
        LayoutParams params = (LayoutParams) mLinearLayoutVR.getLayoutParams();
        final ValueAnimator animator = ValueAnimator.ofInt(params.leftMargin, 0);
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LayoutParams params = (LayoutParams) mLinearLayoutVR.getLayoutParams();
                params.setMargins((Integer) animator.getAnimatedValue(), 0, 0, 0);
                mLinearLayoutVR.setLayoutParams(params);
            }
        });
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

}
