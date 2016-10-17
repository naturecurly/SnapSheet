package com.unimelb.feelinglucky.snapsheet.Discover;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.unimelb.feelinglucky.snapsheet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhaoliu on 7/09/16.
 */
public class DiscoverRecyclerViewAdapter extends  RecyclerView.Adapter<DiscoverRecyclerViewAdapter.MyViewHolder> {
    private static final String TAG = "SnapSheet";
    List<DiscoverItemInterface> contentURLs = new ArrayList<>();
    Context context;

    DiscoverMainPageView.OnClickImage onClickImage;

    int selectedPst;

    public DiscoverRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<DiscoverItemInterface> contentURLs){
        this.contentURLs = contentURLs;
    }

    public void setOnClickImage(DiscoverMainPageView.OnClickImage onClickImage) {
        this.onClickImage = onClickImage;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        MyViewHolder holder = new MyViewHolder(view);
        holder.coverImageView.getLayoutParams().height = parent.getMeasuredWidth();

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.coverImageView.setTag(position);
        Picasso.with(context).load(contentURLs.get(position).getCoverURL()).placeholder(R.drawable.invitee_selected_default_picture).resize(700,700).centerCrop().into(holder.coverImageView);
    }

    @Override
    public int getItemCount() {
        return contentURLs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView coverImageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            int padding = DensityUtil.dip2px(context,2);

            final Animation onClickAnim = AnimationUtils.loadAnimation(context, R.anim.image_click);
            onClickAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                        //prepare for images
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (onClickImage != null){
                        if (contentURLs.get(selectedPst).getContentURLs() != null){
                            onClickImage.onClickImage(contentURLs.get(selectedPst) ,contentURLs.get(selectedPst).getContentURLs());
                        }else{
                            Log.i(TAG, "url null: ");
                        }
                    }else {
                        Log.i(TAG, "onAnimationEnd: null listener");
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            coverImageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            coverImageView.setLayoutParams(params);
            coverImageView.setAdjustViewBounds(true);
            coverImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            coverImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(onClickAnim);
                    selectedPst = (int) v.getTag();
                }
            });
            ((LinearLayout)itemView).addView(coverImageView);
        }
    }
}
