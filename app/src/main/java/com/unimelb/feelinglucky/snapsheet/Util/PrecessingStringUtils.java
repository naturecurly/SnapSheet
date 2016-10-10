package com.unimelb.feelinglucky.snapsheet.Util;

/**
 * Created by mac on 16/10/9.
 */

public class PrecessingStringUtils {
    public static String getFriendName (String username) {
        username = username.replaceAll("\\[.*\\]", "").trim();
        username = username.replaceAll("\\#.*\\#", "").trim();
        return username;
    }
}
