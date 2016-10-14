package com.unimelb.feelinglucky.snapsheet.Chat.widget;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;
import com.unimelb.feelinglucky.snapsheet.Util.SortByName;

import java.util.ArrayList;

import static com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance.database;

/**
 * Created by mac on 16/9/19.
 */
public class FriendNameAdapter extends RecyclerView.Adapter<FriendNameAdapter.ViewHolder> {
    private ArrayList<String> mDataset;
    private static final String HEADER = SortByName.HAEDERSYB;
    private Activity mContext;

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
    public FriendNameAdapter(Activity context, ArrayList<String> myDataset) {
        this.mDataset = myDataset;
        mContext = context;
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String content = mDataset.get(position);
        TextView textView = (TextView) holder.mItem.findViewById(R.id.search_friend_textview);
        TextView header = (TextView) holder.mItem.findViewById(R.id.search_friend_header);
        if (content.startsWith(HEADER)) {
            textView.setVisibility(View.GONE);
            header.setVisibility(View.VISIBLE);
            header.setText(content.substring(2));
        } else {
            textView.setText(mDataset.get(position));
            textView.setVisibility(View.VISIBLE);
            header.setVisibility(View.GONE);

            holder.mItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseUtils.updateChatPriority(database,
                            mDataset.get(position).toLowerCase());
                    Intent mIntent = new Intent();

                    mIntent.putExtra("id", mDataset.get(position).toLowerCase());
                    DatabaseUtils.updateFriendChatDb(database, mDataset.get(position).toLowerCase());

                    mContext.setResult(Activity.RESULT_OK, mIntent);
                    mContext.finish();
                }
            });
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
