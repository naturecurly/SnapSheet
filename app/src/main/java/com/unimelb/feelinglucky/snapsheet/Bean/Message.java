package com.unimelb.feelinglucky.snapsheet.Bean;

/**
 * Created by leveyleonhardt on 10/8/16.
 */

public class Message {
    private String from;
    private String to;
    private String type;
    private String content;

    public final static String MSG = "msg";
    public final static String IMG = "img";
    public final static String RND = "read";

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
}
