package com.unimelb.feelinglucky.snapsheet.Story;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unimelb.feelinglucky.snapsheet.Discover.DensityUtil;
import com.unimelb.feelinglucky.snapsheet.R;

import java.io.File;
import java.util.List;

/**
 * Created by yuhaoliu on 1/10/16.
 */
public class StoryView extends RelativeLayout {
    private static final String TAG = "SnapSheet";
    Context context;

    ImageView profileImgView;
    TextView textView;
    LinearLayout imgContainer;
    TextView timeView;

    StoryInterface story;

    OnImageClickListener onImageClickListener;

    int imageViewPadding;
    int textViewPadding;

    int imgContainerWidth;
    int imgContainerImgCountPerRow;
    int imgWidth;

    public StoryView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public StoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        imgContainerWidth = imgContainer.getMeasuredWidth();
        imgWidth = imgContainerWidth/imgContainerImgCountPerRow;

        int imgContainerCount = this.imgContainer.getChildCount();

        for (int imgContainerChildIndex = 0; imgContainerChildIndex < imgContainerCount; imgContainerChildIndex++) {
            if (this.imgContainer.getChildAt(imgContainerChildIndex) instanceof LinearLayout){
                LinearLayout imgInnerContainer = (LinearLayout) this.imgContainer.getChildAt(imgContainerChildIndex);
                int imgInnerContainerChildCount = imgInnerContainer.getChildCount();

                for (int imgInnerContainerChildIndex = 0; imgInnerContainerChildIndex < imgInnerContainerChildCount; imgInnerContainerChildIndex++) {
                    if (imgInnerContainer.getChildAt(imgInnerContainerChildIndex) instanceof ImageView){
                        ImageView imageView = (ImageView) imgInnerContainer.getChildAt(imgInnerContainerChildIndex);
                        imageView.getLayoutParams().width = imgWidth;
                        imageView.getLayoutParams().height = imgWidth;
                    }
                }
                imgInnerContainer.requestLayout();
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        int imgContainerCount = this.imgContainer.getChildCount();
//
//        for (int imgContainerChildIndex = 0; imgContainerChildIndex < imgContainerCount; imgContainerChildIndex++) {
//            if (this.imgContainer.getChildAt(imgContainerChildIndex) instanceof LinearLayout){
//                LinearLayout imgInnerContainer = (LinearLayout) this.imgContainer.getChildAt(imgContainerChildIndex);
//                imgInnerContainer.requestLayout();
//            }
//        }
//    }

    private void init(){
        //init params
        imgContainerImgCountPerRow = 3;
        imageViewPadding = DensityUtil.dip2px(context,3);
        textViewPadding = DensityUtil.dip2px(context,5);

        //profileImgView
        int profileViewWidth = DensityUtil.dip2px(context,50);
        int profileViewHeight = profileViewWidth;
        profileImgView = new ImageView(context);
        profileImgView.setPadding(imageViewPadding, imageViewPadding, imageViewPadding, imageViewPadding);
        profileImgView.setId(View.generateViewId());
        RelativeLayout.LayoutParams profileImgViewParams = new RelativeLayout.LayoutParams(profileViewWidth, profileViewHeight);
        profileImgViewParams.addRule(ALIGN_PARENT_LEFT);
        profileImgViewParams.addRule(ALIGN_PARENT_TOP);
        profileImgView.setLayoutParams(profileImgViewParams);
        this.addView(profileImgView);

        textView = new TextView(context);
        textView.setTextSize(15);
        textView.setPadding(textViewPadding,3 * textViewPadding,textViewPadding,textViewPadding);
        textView.setId(View.generateViewId());
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewParams.addRule(RIGHT_OF,profileImgView.getId());
        textViewParams.addRule(ALIGN_PARENT_RIGHT);
        textView.setLayoutParams(textViewParams);
        this.addView(textView);

        imgContainer = new LinearLayout(context);
        imgContainer.setId(generateViewId());
        imgContainer.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams imgContainerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgContainerParams.addRule(BELOW, textView.getId());
        imgContainerParams.addRule(RIGHT_OF, profileImgView.getId());
        imgContainer.setLayoutParams(imgContainerParams);
        this.addView(imgContainer);

        timeView = new TextView(context);
        timeView.setId(generateViewId());
        timeView.setPadding(0,0,textViewPadding,textViewPadding);
        RelativeLayout.LayoutParams timeViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        timeViewParams.addRule(ALIGN_PARENT_RIGHT);
        timeViewParams.addRule(BELOW,imgContainer.getId());
        timeView.setLayoutParams(timeViewParams);
        this.addView(timeView);

        ImageView dividerImgView = new ImageView(context);
        dividerImgView.setImageDrawable(getResources().getDrawable(R.drawable.story_divider_line));
        RelativeLayout.LayoutParams dividerImgViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dividerImgViewParams.addRule(BELOW,timeView.getId());
        dividerImgView.setLayoutParams(dividerImgViewParams);
        this.addView(dividerImgView);

    }

    public void setStory(StoryInterface story){
        this.story = story;
        this.setProfileImg(story.getProfileUrl());
        this.setText(story.getStoryText());
        this.setImgs(story.getStoryImgUrls());
        this.setTimeText(story.getTimePassedString());
    }

    public void setProfileImg(String url){
        Picasso.with(context).load(url).fit().transform(new RoundedTransformation(50, 4)).into(this.profileImgView);
    }

    public void setImgs(List<String> urls){
        this.imgContainer.removeAllViews();
        int index = 0;
        for (String url:urls
             ) {
            int containerCount = this.imgContainer.getChildCount();

            if (containerCount != 0){
                LinearLayout currentContainer = (LinearLayout) this.imgContainer.getChildAt(containerCount-1);
                int imgCountInCurrentContainer = currentContainer.getChildCount();

                if (imgCountInCurrentContainer >= imgContainerImgCountPerRow){

                    LinearLayout newInnerContainer = this.getNewInnerImgContainer();
                    ImageView imgView = getNewImgView(url);
                    imgView.setTag(index);
                    newInnerContainer.addView(imgView);
                    this.imgContainer.addView(newInnerContainer);
                }else {
                    ImageView imgView = getNewImgView(url);
                    imgView.setTag(index);
                    currentContainer.addView(imgView);
                }
            }else {
                LinearLayout newInnerContainer = this.getNewInnerImgContainer();
                ImageView imgView = getNewImgView(url);
                imgView.setTag(index);
                newInnerContainer.addView(imgView);
                this.imgContainer.addView(newInnerContainer);
            }
            index += 1;
        }
        this.requestLayout();
    }

    private LinearLayout getNewInnerImgContainer(){
        LinearLayout imgInnerContainer = new LinearLayout(context);
        imgInnerContainer.setId(generateViewId());
        imgInnerContainer.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams imgInnerContainerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgInnerContainer.setLayoutParams(imgInnerContainerParams);

        return imgInnerContainer;
    }

    private ImageView getNewImgView(String url){
        final ImageView imgView = new ImageView(context);
        imgView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onImageClickListener != null){
                    if (story != null){
                        onImageClickListener.onStoryImageClick(v,(int) v.getTag(), story.getStoryImgUrls());
                    }
                }
            }
        });
        imgView.setAdjustViewBounds(true);
        imgView.setPadding(0, 0, imageViewPadding, imageViewPadding);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
        params.gravity = Gravity.CENTER_VERTICAL;
        imgView.setLayoutParams(params);
        Log.i(TAG, "url: " + url);
        if (url.contains("http")){
            Picasso.with(context).load(url).placeholder(R.drawable.img_placeholder).resize(500, 500).centerCrop().into(imgView);
            Log.i(TAG, "normal one: ");
        }else{
            File f = new File(url);
            Picasso.with(context).load(f).placeholder(R.drawable.img_placeholder).resize(500, 500).centerCrop().into(imgView);
            Log.i(TAG, "unnormal one: ");
        }

        return imgView;
    }

    public void setText(String storyText){
        this.textView.setText(storyText);
    }

    public void setTimeText(String timeString){
        this.timeView.setText(timeString);
    }

    public interface OnImageClickListener{
        void onStoryImageClick(View view, int index, List<String> urls);
        void onProfileImageClick(View view);
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }
}
