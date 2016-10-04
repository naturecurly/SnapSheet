package com.unimelb.feelinglucky.snapsheet.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Created by mac on 16/9/20.
 */
public class SortByName {
    public static final String HAEDERSYB = "##";
    public static ArrayList<String> sortByName(ArrayList<String> names) {
        for (int i = 0; i < names.size(); i ++) {
            String name = names.get(i);
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            names.set(i,name);
        }
        //Sort
        Collections.sort(names);
        ArrayList<String> copyOfLetter = new ArrayList<>();
        ArrayList<String> copyOfOther = new ArrayList<>();

        //Insert
        for (int i = 0; i < names.size(); i ++) {
            char head = names.get(i).charAt(0);
            if(head >= 'A' && head <= 'Z') {
                final String hearder = HAEDERSYB + head;
                if(!copyOfLetter.contains(hearder)) {
                    copyOfLetter.add(copyOfLetter.size(),hearder);
                }
                copyOfLetter.add(names.get(i));
            } else{
                final String header = HAEDERSYB + "#";
                if (!copyOfOther.contains(header)) {
                    copyOfOther.add(0, header);
                }
                copyOfOther.add(names.get(i));
            }
        }
        copyOfLetter.addAll(copyOfOther);

        return copyOfLetter;
    }

    public static void insertHeader(ArrayList<String> data) {
        if (data == null || data.size() == 0)
            return;
        String str = data.get(0);
        char head = str.toUpperCase().charAt(0);
        if(head >= 'A' && head <= 'Z') {
            data.add(0, HAEDERSYB + head);
        } else {
            data.add(0, HAEDERSYB + "#");
        }
    }

    public static void sortByPriority(Map<String, Integer> users) {

    }
}
