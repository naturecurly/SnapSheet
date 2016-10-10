package com.unimelb.feelinglucky.snapsheet.Memory;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.unimelb.feelinglucky.snapsheet.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mac on 16/10/9.
 */

public class MemoryFragment extends Fragment implements View.OnClickListener{
    private Button mImport;
    private ImageView mImg;
    private final int REQUEST_IMAGE_SELECT = 1;
    private GridLayout mGrid;
    private Uri mImgUri;
    private AlertDialog mDialog;

    private Button mDelete;
    private Button mSend;
    private Button mShare;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memory, container, false);
        mImport = (Button) view.findViewById(R.id.memory_import);
        mImg = (ImageView) view.findViewById(R.id.memory_img);
        mImport.setOnClickListener(this);
        mGrid = (GridLayout) view.findViewById(R.id.memory_grid);

        mDelete = (Button) view.findViewById(R.id.memory_delete);
        mSend = (Button) view.findViewById(R.id.memory_send);
        mShare = (Button) view.findViewById(R.id.memory_share);
        mDelete.setOnClickListener(this);
        mSend.setOnClickListener(this);
        mShare.setOnClickListener(this);
        initDialog();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mImport.getId()) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_IMAGE_SELECT);
        } else if(v.getId() == mShare.getId()) {
            shareImg();
        } else if (v.getId() == mDelete.getId()) {
            mDialog.show();

        }
    }

    private void deleteImg () {
        mImg.setImageBitmap(null);
        mGrid.setVisibility(View.GONE);
    }
    private void shareImg() {
        if (mImgUri == null)
            return;;
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        imageUris.add(mImgUri);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Share images to.."));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_SELECT: {
                if (data != null) {
                    Bitmap mBitmap = null;
                    try {
                        mImgUri = data.getData();
                        mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImgUri);
                        mImg.setImageBitmap(mBitmap);
                        mGrid.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            }
        }
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Do you want to delete the image from your memory?");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                deleteImg();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        mDialog = builder.create();
    }
}
