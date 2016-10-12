package com.unimelb.feelinglucky.snapsheet.Chatroom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.Util.DensityUtil;

public class ChatMessageTextView extends TextView {


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DensityUtil.dip2px(getContext(), 5));

        paint.setColor(ContextCompat.getColor(getContext(), R.color.chat_message_textview_left_border));

        canvas.drawLine(0, 0, 0, this.getHeight(), paint);
    }

    public ChatMessageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatMessageTextView(Context context) {
        super(context);
    }
}