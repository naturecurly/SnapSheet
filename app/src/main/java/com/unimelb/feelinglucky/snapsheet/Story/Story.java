package com.unimelb.feelinglucky.snapsheet.Story;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yuhaoliu on 1/10/16.
 */
@Entity
public class Story implements StoryInterface, Serializable {
    private String profileUrl;
    private String text;
    @Convert(columnType = String.class, converter = Story.StringConverter.class)
    private List<String> imgUrls;
    private String timePassedText;

    @Generated(hash = 1100848824)
    public Story(String profileUrl, String text, List<String> imgUrls, String timePassedText) {
        this.profileUrl = profileUrl;
        this.text = text;
        this.imgUrls = imgUrls;
        this.timePassedText = timePassedText;
    }

    @Generated(hash = 922655990)
    public Story() {
    }

    public static class StringConverter implements PropertyConverter<List<String>, String> {
        @Override
        public List<String> convertToEntityProperty(String databaseValue) {
            Gson gson = new Gson();
            List<String> rslt = gson.fromJson(databaseValue, new TypeToken<List<String>>(){}.getType());
            return rslt;
        }

        @Override
        public String convertToDatabaseValue(List<String> entityProperty) {
            Gson gson = new Gson();
            String rslt = gson.toJson(entityProperty);
            return rslt;
        }
    }

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

    public String getTimePassedText() {
        return this.timePassedText;
    }

    public List<String> getImgUrls() {
        return this.imgUrls;
    }

    public String getText() {
        return this.text;
    }
}
