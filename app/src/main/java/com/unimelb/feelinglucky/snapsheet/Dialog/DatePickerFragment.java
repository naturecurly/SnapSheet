package com.unimelb.feelinglucky.snapsheet.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by leveyleonhardt on 8/28/16.
 */
public class DatePickerFragment extends DialogFragment {


    private DatePickerDialog.OnDateSetListener listener;


    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listner) {

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listner);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

}
