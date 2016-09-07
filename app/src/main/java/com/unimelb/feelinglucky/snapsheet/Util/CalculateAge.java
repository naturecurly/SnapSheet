package com.unimelb.feelinglucky.snapsheet.Util;


import java.util.Calendar;

/**
 * Created by leveyleonhardt on 8/31/16.
 */
public class CalculateAge {

    public static boolean isOver18(Calendar dob) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        if (age >= 18 && dob.before(today)) {
            return true;
        }

        return false;
    }
}
