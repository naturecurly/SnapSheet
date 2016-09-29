package com.unimelb.feelinglucky.snapsheet.Discover;

import java.util.List;

/**
 * Created by yuhaoliu on 7/09/16.
 */
public class DiscoverItem implements DiscoverItemInterface {
    String coverURL;
    List<String> contentURLs;

    @Override
    public String getCoverURL() {
        return this.coverURL;
    }

    @Override
    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    @Override
    public List<String> getContentURLs() {
        return this.contentURLs;
    }

    @Override
    public void setContentURLs(List<String> contentURLs) {
        this.contentURLs = contentURLs;
    }
}
