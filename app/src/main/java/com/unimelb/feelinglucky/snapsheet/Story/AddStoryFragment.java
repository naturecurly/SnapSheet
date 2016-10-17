package com.unimelb.feelinglucky.snapsheet.Story;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.unimelb.feelinglucky.snapsheet.Discover.DensityUtil;
import com.unimelb.feelinglucky.snapsheet.PhotoPickerActivity;
import com.unimelb.feelinglucky.snapsheet.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by yuhaoliu on 17/10/16.
 */
public class AddStoryFragment extends Fragment {
    private static final String TAG = "SnapSheet";
    private final int ACTIVITY_PHOTOPICKER = 2;

    private Context context;

    private ImageButton backBtn;
    private ImageButton addPicBtn;
    private ImageButton doneBtn;
    private EditText editText;
    private HorizontalScrollView scrollView;
    private LinearLayout imageContainer;

    private AddStoryFragment self= this;

    ArrayList<String> imageUrl = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_story, container, false);
        context = getContext();
        scrollView = (HorizontalScrollView) view.findViewById(R.id.pictures);
        editText = (EditText) view.findViewById(R.id.edit_text_story);
        imageContainer = (LinearLayout) view.findViewById(R.id.pictures_container);
        backBtn = (ImageButton) view.findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.hide(self).commit();
            }
        });

        addPicBtn = (ImageButton) view.findViewById(R.id.add_photo);
        addPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhotoPickerActivity.class);
                int selectedMode = PhotoPickerActivity.MODE_MULTI;
                intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, selectedMode);
                int maxNum = 3;
                intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum);
                intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                startActivityForResult(intent, ACTIVITY_PHOTOPICKER);
            }
        });

        doneBtn = (ImageButton) view.findViewById(R.id.done);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Story> temp = new ArrayList<Story>();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd hh:mm");
                Story story = new Story();
                story.setText(editText.getText().toString());
                story.setImgUrls(imageUrl);
                story.setTimePassedText(dateFormat.format(cal.getTime()));
                story.setProfileUrl("http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg");
                temp.add(story);
                DBManager.getInstance(context).insertStoryList(temp);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.hide(self).commit();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            Bundle bundle = data.getExtras();
            ArrayList<String> images = (ArrayList<String>) bundle.get(PhotoPickerActivity.KEY_RESULT);
            imageContainer.removeAllViews();
            imageUrl.clear();
            for (int i = 0; i < images.size(); i++) {
                addImages(images.get(i));
                imageUrl.add(images.get(i));
            }
        }
    }

    private void addImages(String path){
        ImageView imgV = new ImageView(context);
        int width = DensityUtil.dip2px(context,150);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
        params.leftMargin = width/5;
        imgV.setLayoutParams(params);
        File f = new File(path);
        Picasso.with(getActivity()).load(f).into(imgV);
        imageContainer.addView(imgV);
    }
}
