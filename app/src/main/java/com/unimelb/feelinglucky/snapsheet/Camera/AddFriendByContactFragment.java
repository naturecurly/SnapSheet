package com.unimelb.feelinglucky.snapsheet.Camera;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.unimelb.feelinglucky.snapsheet.Bean.Contact;
import com.unimelb.feelinglucky.snapsheet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 16/10/8.
 */

public class AddFriendByContactFragment extends Fragment{
    private List<Contact> mContact;
    private ListView mListView;
    private PhotoContactsAdapter mListViewAdapter;
    private ProgressDialog dialog;

    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    private static final int PHONES_NUMBER_INDEX = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        mContact = new ArrayList<>();
        mListView = (ListView) view.findViewById(R.id.contact_list_view);
        dialog = ProgressDialog.show(getActivity(), null, "Loading", true, false);
        new LoadingThread().start();
        return view;
    }


    public void setContacts() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(getActivity(),"Please get me the permission", Toast.LENGTH_LONG).show();
            }
        }else {
            getContact();
        }

    }

    private void getContact () {
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                Contact person = new Contact();
                person.setMobile(phoneNumber);
                person.setUsername(contactName);
                mContact.add(person);
            }
            mListViewAdapter = new PhotoContactsAdapter(getActivity(),mContact);
            mListView.setAdapter(mListViewAdapter);
            phoneCursor.close();
        }
    }
    private class LoadingThread extends Thread {
        public void run() {
            mHandler.sendEmptyMessage(0);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            setContacts();
            if (dialog.isShowing())
                dialog.dismiss();
        }
    };
}
