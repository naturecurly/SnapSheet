package com.unimelb.feelinglucky.snapsheet.Discover;

import java.util.Comparator;
import java.util.List;

/**
 * Created by yuhaoliu on 7/09/16.
 */
public interface DiscoverItemInterface<T> extends Comparable<T>{
    String getCoverURL();
    void setCoverURL(String coverURL);

    List<String> getContentURLs();
    void setContentURLs(List<String> contentURLs);
    int getclickCount();
}
