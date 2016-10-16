package com.unimelb.feelinglucky.snapsheet.Chatroom;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.unimelb.feelinglucky.snapsheet.R;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by asahui on 16/10/2016.
 */

public class DisplayImageActivity extends AppCompatActivity {
    public static final String LOG_TAG = DisplayImageActivity.class.getSimpleName();

    public static final String ARG_IMAGE = "image";
    public static final String ARG_EXPIRE_TIME = "expire_time";

    private ImageView ivImage;
    private DonutProgress donutProgress;
    private Timer timer;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        Bundle args = getIntent().getExtras();
        String image = args.getString(ARG_IMAGE);
        int expire_time = args.getInt(ARG_EXPIRE_TIME);

        Uri imageUri = Uri.fromFile(new File(getApplicationContext().getFilesDir(), image));

        ivImage = (ImageView) findViewById(R.id.display_area);
        ivImage.setImageURI(imageUri);

        donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        //donutProgress.setText(Integer.toString(expire_time));

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        boolean a = true;
                        if (a) {
                            donutProgress.setMax(expire_time);
                            ObjectAnimator anim = ObjectAnimator.ofInt(donutProgress, "progress", 0, expire_time);
                            //anim.setInterpolator();
                            anim.setDuration(expire_time*1000);
                            anim.start();
                            anim.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    finish();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            //finish();
                        } else {
                            AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(DisplayImageActivity.this, R.animator.progress_anim);
                            set.setInterpolator(new DecelerateInterpolator());
                            set.setTarget(donutProgress);
                            set.start();
                        }
                    }
                });
            }
        }, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
