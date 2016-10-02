package com.unimelb.feelinglucky.snapsheet.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhaoliu on 1/10/16.
 */
public class SimulateStory {
    public static List<Story> simulateStories(){
        List<Story> stories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Story story = new Story();
            story.setText("" + i);
            List<String> urls = new ArrayList<>();

            urls.add("https://pmcdeadline2.files.wordpress.com/2016/06/angelababy.jpg?w=970");
            urls.add("http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg");
            urls.add("http://img.ixiumei.com/uploadfile/2016/0819/20160819105642918.png");
            urls.add("http://img.ixiumei.com/uploadfile/2016/0819/20160819105642918.png");
            urls.add("https://pmcdeadline2.files.wordpress.com/2016/06/angelababy.jpg?w=970");
            urls.add("http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg");
            urls.add("http://img.ixiumei.com/uploadfile/2016/0819/20160819105642918.png");
            urls.add("http://img.ixiumei.com/uploadfile/2016/0819/20160819105642918.png");
            urls.add("https://pmcdeadline2.files.wordpress.com/2016/06/angelababy.jpg?w=970");
            urls.add("http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg");
//            for (int j = 0; j < 4; j++) {
//            }
            story.setImgUrls(urls);
            story.setTimePassedText("Not yet");
            story.setProfileUrl("http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg");
            stories.add(story);
        }

        return stories;
    }
}
