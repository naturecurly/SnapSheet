package com.unimelb.feelinglucky.snapsheet.Camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.unimelb.feelinglucky.snapsheet.ImageSendActivity;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.Util.StatusBarUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by leveyleonhardt on 10/7/16.
 */

public class ImageViewFragment extends Fragment {
    private ImageView imageView;
    private Button sendButton;
    private String imagePath;
    private Button editButton;

    public static ImageViewFragment newInstance(String imagePath) {

        Bundle args = new Bundle();
        args.putString("imagePath", imagePath);
        ImageViewFragment fragment = new ImageViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_view, container, false);
        imagePath = getArguments().getString("imagePath");
        imageView = (ImageView) view.findViewById(R.id.activity_send_imageview);
        editButton = (Button) view.findViewById(R.id.image_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri imageUri = Uri.fromFile(new File(imagePath));
                Intent intent = new AdobeImageIntent.Builder(getActivity()).setData(imageUri).build();
                getActivity().startActivityForResult(intent, 1);
            }
        });
        sendButton = (Button) view.findViewById(R.id.image_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.activity_image_send_container, new SendImageFriendListFragment()).addToBackStack(null).commit();
            }
        });
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            rotateBitmap(orientation, bitmap);
            Log.i("local", orientation + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void rotateBitmap(int orientation, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case 6:
                matrix.postRotate(90);
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                imageView.setImageBitmap(rotatedBitmap);
                break;
            case 3:
                matrix.postRotate(180);
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                imageView.setImageBitmap(rotatedBitmap);
                break;
            case 8:
                matrix.postRotate(270);
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                imageView.setImageBitmap(rotatedBitmap);
                break;
            default:
                imageView.setImageBitmap(bitmap);

        }
    }

    public void refreshImage(Uri imageUri) {
        imageView.setImageURI(imageUri);
    }
}
