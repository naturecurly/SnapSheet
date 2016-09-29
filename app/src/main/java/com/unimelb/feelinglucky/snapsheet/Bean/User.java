package com.unimelb.feelinglucky.snapsheet.Bean;

import java.util.Date;

/**
 * Created by leveyleonhardt on 8/28/16.
 */
public class User {
    private String username;
    private Date birthday;
    private String mobile;
    private String email;
    private String password;
    private String avatar;
    private String[] friend;
    private String[] subscribeIds;
    private String device_id;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String[] getFriend() {
        return friend;
    }

    public void setFriend(String[] friend) {
        this.friend = friend;
    }

    public String[] getSubscribeIds() {
        return subscribeIds;
    }

    public void setSubscribeIds(String[] subscribeIds) {
        this.subscribeIds = subscribeIds;
    }
}
