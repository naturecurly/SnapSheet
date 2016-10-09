package com.unimelb.feelinglucky.snapsheet.Discover;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.unimelb.feelinglucky.snapsheet.R;


/**
 * Created by yuhaoliu on 12/08/16.
 */
public class RecyclerDivider extends RecyclerView.ItemDecoration {
    private int space;

    public RecyclerDivider(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = space;
        }
    }
}
