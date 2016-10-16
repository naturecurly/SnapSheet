package com.unimelb.feelinglucky.snapsheet.Chatroom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unimelb.feelinglucky.snapsheet.Bean.Message;
import com.unimelb.feelinglucky.snapsheet.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by asahui on 10/10/2016.
 */

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    private static final String LOG_TAG = ChatRecyclerViewAdapter.class.getSimpleName();
    private Context mContext;
    private List<Message> mChatMessages;
    private final static DateFormat df = new SimpleDateFormat("HH:mm");


    public static final int MSG_VIEW_TYPE = 1;
    public static final int IMG_VIEW_TYPE = 2;


    public ChatRecyclerViewAdapter(Context context, List<Message> mChatMessages) {
        this.mContext = context;
        this.mChatMessages = mChatMessages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        ViewHolder vh = null;
        switch (viewType) {
            case MSG_VIEW_TYPE: {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_item, parent, false);

                vh = new ViewHolder(v);
                break;
            }

            case IMG_VIEW_TYPE: {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_item, parent, false);
                vh = new ViewHolder(v);
                break;
            }

            default:


        }
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case MSG_VIEW_TYPE:
                Log.d("messageAdapter", mChatMessages.get(position).getType() + " : " + mChatMessages.get(position).getContent());
                holder.mItem.setId(mChatMessages.get(position).getFrom());
                String time = df.format(Calendar.getInstance().getTime());
                holder.mItem.setTimeStamp(time);
                holder.mItem.setMessage(mChatMessages.get(position).getContent());
                break;

            case IMG_VIEW_TYPE:
                Log.d("messageAdapter", mChatMessages.get(position).getType() + " : " + mChatMessages.get(position).getContent());
                handleImageMessage(holder, position);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return mChatMessages.size();
    }



    @Override
    public int getItemViewType(int position) {
        Message message = mChatMessages.get(position);
        if (message != null) {
            String type = message.getType();
            switch (type) {
                case Message.MSG:
                    return MSG_VIEW_TYPE;
                case Message.IMG:
                    return IMG_VIEW_TYPE;
                default:
                    return MSG_VIEW_TYPE;
            }
        }
        return MSG_VIEW_TYPE;
    }


    private void handleImageMessage(ViewHolder holder, int position) {
        Message message = mChatMessages.get(position);
        String status = message.getStatus();

        switch (status) {
            case Message.FST:
                holder.mItem.setId(mChatMessages.get(position).getFrom());
                String time = df.format(Calendar.getInstance().getTime());
                holder.mItem.setTimeStamp(time);
                holder.mItem.setMessage(Message.FST_TEXT);
                String image  = message.getContent();

                holder.mItem.setOnPullToLimitListener(new MessageSlideableItem.PullToLimitListener() {
                    @Override
                    public void openMessage() {
                        Log.i(LOG_TAG, Message.FST + " Clicked");

                        Toast.makeText(mContext, LOG_TAG + ": " + Message.FST + " Clicked", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void openMessageByLongPress() {

                    }
                });


                break;

            case Message.SND:
                holder.mItem.setId(mChatMessages.get(position).getFrom());
                String time1 = df.format(Calendar.getInstance().getTime());
                holder.mItem.setTimeStamp(time1);
                holder.mItem.setMessage(Message.SND_TEXT);
                String image1  = message.getContent();
                holder.mItem.setOnPullToLimitListener(new MessageSlideableItem.PullToLimitListener() {
                    @Override
                    public void openMessage() {
                    }

                    @Override
                    public void openMessageByLongPress() {
                        Log.i(LOG_TAG, Message.SND + " Clicked");

                        Toast.makeText(mContext, LOG_TAG + ": " + Message.SND + " Clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                /*holder.mItem.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // start an Activity to view the image
                        Toast.makeText(mContext, LOG_TAG + ": " + Message.SND + " Clicked", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });*/
                break;

            default:

        }
    }



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public MessageSlideableItem mItem;

        public ViewHolder(View v) {
            super(v);
            mItem = (MessageSlideableItem) v;
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public MessageSlideableItem mItem;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mItem = (ImageMessageSlideableItem) itemView;
        }
    }
}
