package com.unimelb.feelinglucky.snapsheet.Bean;

/**
 * Created by mac on 16/10/9.
 */

public class Contact {
    private String username;
    private String mobile;

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        mobile = mobile.replace(" ","");
        mobile = mobile.replace("-","");
        return mobile;
    }

    public String getUsername() {
        return username;
    }
}
