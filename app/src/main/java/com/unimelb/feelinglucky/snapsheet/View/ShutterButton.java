package com.unimelb.feelinglucky.snapsheet.View;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.unimelb.feelinglucky.snapsheet.Util.DensityUtil;

/**
 * Created by leveyleonhardt on 8/14/16.
 */
public class ShutterButton extends View {
    private Context mContext;
    private Paint circlePaint;
    private static final int CIRCLE_RADIUS = 40;
    private static final int CIRCLE_STROKE_WIDTH = 4;
    private static final int CIRCLE_EXPAND_WIDTH = 10;
    private float radius;
    private boolean isPressed;


    public ShutterButton(Context context) {
        super(context);
    }

    public ShutterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        radius = DensityUtil.dip2px_f(context, CIRCLE_RADIUS);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStrokeWidth(DensityUtil.dip2px_f(context, CIRCLE_STROKE_WIDTH));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle((float) DensityUtil.dip2px(mContext, 2 * CIRCLE_RADIUS + 2 * CIRCLE_STROKE_WIDTH + 2 * CIRCLE_EXPAND_WIDTH) / 2, (float) DensityUtil.dip2px(mContext, 2 * CIRCLE_RADIUS + 2 * CIRCLE_STROKE_WIDTH + 2 * CIRCLE_EXPAND_WIDTH) / 2, radius, circlePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isPressed) {

        } else {
            setMeasuredDimension(DensityUtil.dip2px(mContext, 2 * CIRCLE_RADIUS + 2 * CIRCLE_STROKE_WIDTH + 2 * CIRCLE_EXPAND_WIDTH), DensityUtil.dip2px(mContext, 2 * CIRCLE_RADIUS + 2 * CIRCLE_STROKE_WIDTH + 2 * CIRCLE_EXPAND_WIDTH));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                final ValueAnimator animator_expand = ValueAnimator.ofFloat(CIRCLE_RADIUS, CIRCLE_RADIUS + CIRCLE_EXPAND_WIDTH);
                animator_expand.setDuration(100);
                animator_expand.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        radius = DensityUtil.dip2px(mContext, (float) animator_expand.getAnimatedValue());
                        invalidate();
                    }
                });
                animator_expand.start();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isPressed = false;
                final ValueAnimator animator_shrink = ValueAnimator.ofFloat(CIRCLE_RADIUS + CIRCLE_EXPAND_WIDTH, CIRCLE_RADIUS);
                animator_shrink.setDuration(100);
                animator_shrink.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        radius = DensityUtil.dip2px(mContext, (float) animator_shrink.getAnimatedValue());
                        invalidate();
                    }
                });
                animator_shrink.start();
                break;
        }

        return true;

    }
}
