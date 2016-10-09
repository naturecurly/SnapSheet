package com.unimelb.feelinglucky.snapsheet.Chat.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SnapSheetActivity;
import com.unimelb.feelinglucky.snapsheet.View.SlideableItem;

/**
 * Created by mac on 16/8/17.
 */
public class FriendInfoAdapter extends RecyclerView.Adapter<FriendInfoAdapter.ViewHolder> {
    private String[] mDataset;
    private Context mContext;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public SlideableItem mItem;

        public ViewHolder(View v) {
            super(v);
            mItem = (SlideableItem) v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FriendInfoAdapter(Context context, String[] myDataset) {
        mDataset = myDataset;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FriendInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
//         create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mItem.setUsername(mDataset[position]);
        holder.mItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((SnapSheetActivity) mContext).setmChatWith(mDataset[position]);
                return false;
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}



