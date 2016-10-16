package com.unimelb.feelinglucky.snapsheet.Bean;

/**
 * Created by leveyleonhardt on 10/8/16.
 */

public class Message {
    private int localId;

    private String from;
    private String to;
    private String type;
    private String content;
    private String live_time;   // time that can be viewed
    private String status;      // status of the read of the image


    public final static String MSG = "msg";
    public final static String IMG = "img";
    public final static String RND = "read";

    // Image message status
    public static final String FST = "0";  // the first time
    public static final String SND = "1"; // the second time
    public static final String FST_TEXT = "Tap to View";
    public static final String FST_OPENED_TEXT = "Opened";
    public static final String SND_TEXT = "Press and hold to replay";
    public static final String SND_OPENED_TEXT = "Replayed!";
    public static final String RND_IMG = "2";
    public static final String RND_IMG_TEXT = "Opened";


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLive_time() {
        return live_time;
    }

    public void setLive_time(String live_time) {
        this.live_time = live_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

}
