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

import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;

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

    private Bitmap mBitmap;
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

        read();
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
        DatabaseUtils.storeImg(DatabaseInstance.database, null);
    }

    private void shareImg() {
        if (mImgUri == null){
            String pathofBmp = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), mBitmap,"title", null);
            Uri bmpUri = Uri.parse(pathofBmp);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, "a"));
        } else {
            ArrayList<Uri> imageUris = new ArrayList<>();
            imageUris.add(mImgUri);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "Share images to.."));
        }
    }


    private void read() {
        try {
            mBitmap = DatabaseUtils.getImg(DatabaseInstance.database);
        } catch (Exception e){}
        if (mBitmap != null) {
            mImg.setImageBitmap(mBitmap);
            mGrid.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_SELECT: {
                if (data != null) {
                    mBitmap = null;
                    try {
                        mImgUri = data.getData();
                        mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImgUri);
                        mImg.setImageBitmap(mBitmap);
                        mGrid.setVisibility(View.VISIBLE);
                        DatabaseUtils.storeImg(DatabaseInstance.database, mBitmap);

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
                deleteImg();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        mDialog = builder.create();
    }
}
