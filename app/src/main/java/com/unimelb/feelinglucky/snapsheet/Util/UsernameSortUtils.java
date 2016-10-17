package com.unimelb.feelinglucky.snapsheet.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by leveyleonhardt on 10/17/16.
 */

public class UsernameSortUtils {
    public static List<String> sortUsername(List<String> usernameList) {
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.toLowerCase().compareTo(rhs.toLowerCase());
            }
        };
        Collections.sort(usernameList, comparator);
        return usernameList;
    }
}
