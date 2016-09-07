package com.unimelb.feelinglucky.snapsheet.Bean;

/**
 * Created by leveyleonhardt on 8/30/16.
 */
public class ReturnMessage {
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
