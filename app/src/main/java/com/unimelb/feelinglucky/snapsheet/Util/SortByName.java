package com.unimelb.feelinglucky.snapsheet.Util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mac on 16/9/20.
 */
public class SortByName {
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
                final String hearder = "##" + head;
                if(!copyOfLetter.contains(hearder)) {
                    copyOfLetter.add(copyOfLetter.size(),hearder);
                }
                copyOfLetter.add(names.get(i));
            } else{
                final String header = "###";
                if (!copyOfOther.contains(header)) {
                    copyOfOther.add(0, header);
                }
                copyOfOther.add(names.get(i));
            }
        }
        copyOfLetter.addAll(copyOfOther);

        return copyOfLetter;
    }
}
