package com.unimelb.feelinglucky.snapsheet.Story;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yuhaoliu on 1/10/16.
 */
public class Story implements StoryInterface, Serializable {
    private String profileUrl;
    private String text;
    private List<String> imgUrls;
    private String timePassedText;

    public void setTimePassedText(String timePassedText) {
        this.timePassedText = timePassedText;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @Override
    public String getProfileUrl() {
        return profileUrl;
    }

    @Override
    public String getStoryText() {
        return text;
    }

    @Override
    public List<String> getStoryImgUrls() {
        return imgUrls;
    }

    @Override
    public String getTimePassedString() {
        return timePassedText;
    }
}
