package com.unimelb.feelinglucky.snapsheet.Chat.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.R;

import java.util.ArrayList;

/**
 * Created by mac on 16/9/19.
 */
public class FriendNameAdapter extends RecyclerView.Adapter<FriendNameAdapter.ViewHolder> {
    private ArrayList<String> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout mItem;

        public ViewHolder(View v) {
            super(v);
            mItem = (LinearLayout) v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FriendNameAdapter(ArrayList<String> myDataset) {
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FriendNameAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
//         create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_friend_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String content = mDataset.get(position);
        TextView textView = (TextView) holder.mItem.findViewById(R.id.search_friend_textview);
        TextView header = (TextView) holder.mItem.findViewById(R.id.search_friend_header);
        if(content.startsWith("##")){
            textView.setVisibility(View.GONE);
            header.setVisibility(View.VISIBLE);
            header.setText(content.substring(2));
        }else {
            textView.setText(mDataset.get(position));
            textView.setVisibility(View.VISIBLE);
            header.setVisibility(View.GONE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
