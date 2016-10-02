package com.unimelb.feelinglucky.snapsheet;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unimelb.feelinglucky.snapsheet.Camera.CameraPageViewerFragment;
import com.unimelb.feelinglucky.snapsheet.Chat.ChatFragment;
import com.unimelb.feelinglucky.snapsheet.Chatroom.ChatRoomFragment;
import com.unimelb.feelinglucky.snapsheet.Discover.DiscoverFragment;
import com.unimelb.feelinglucky.snapsheet.Story.SimulateStory;
import com.unimelb.feelinglucky.snapsheet.Story.StoriesFragment;
import com.unimelb.feelinglucky.snapsheet.Util.StatusBarUtils;
import com.unimelb.feelinglucky.snapsheet.View.CustomizedViewPager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by leveyleonhardt on 8/11/16.
 */
public class SnapSheetActivity extends AppCompatActivity {

    private static final String TAG = "SnapSheetActivity";
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private static final int REQUEST_CAMERA_CODE = 0;
    private CustomizedViewPager mViewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private TextureView mTextureView;
    private DisplayMetrics mRealMetrics = new DisplayMetrics();
    private AppCompatActivity context;
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera(mRealMetrics.widthPixels, mRealMetrics.heightPixels, mFacingLens);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private Integer mSensorOrientation;
    private Size mPreviewSize;
    private String mCameraId;
    private CameraDevice mCameraDevice;
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {

        }

        @Override
        public void onError(CameraDevice camera, int error) {

        }
    };
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest mPreviewRequest;
    private int mFacingLens = CameraCharacteristics.LENS_FACING_BACK;
    private boolean mIsFlashOn;
    private ImageReader mImageReader;
    private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Log.i(TAG, "photo has been taken");
        }
    };
    private Size mImageSize;


    CameraCaptureSession.CaptureCallback stillCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if (mTextureView.isAvailable()) {
            Log.i(TAG, "sssss" + mTextureView.getWidth() + " " + mTextureView.getHeight());
            openCamera(mRealMetrics.widthPixels, mRealMetrics.heightPixels, mFacingLens);
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }


    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_fragment);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        getWindowManager().getDefaultDisplay().getRealMetrics(mRealMetrics);
        mTextureView = (TextureView) findViewById(R.id.activity_camera_texture_view);
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);


        mViewPager = (CustomizedViewPager) findViewById(R.id.activity_fragment_view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        loadFragments();
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                if (fragments.get(position) instanceof StoriesFragment){
                    StoriesFragment storiesFragment = (StoriesFragment) fragments.get(position);
                    storiesFragment.setStories(SimulateStory.simulateStories());
                }

                return super.instantiateItem(container, position);
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        mViewPager.setCurrentItem(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 2) {
                    StatusBarUtils.setStatusBarVisable(context);
                } else if (position == 2) {
                    StatusBarUtils.setStatusBarInvisable(context);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void startBackgroundThread() {
        mHandlerThread = new HandlerThread("CameraBackground");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    private void stopBackgroundThread() {
        mHandlerThread.quitSafely();
        try {
            mHandlerThread.join();
            mHandlerThread = null;
            mHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void openCamera(int width, int height, int facingLens) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestCameraPermission();
                return;
            }
        }
        setupCamera(width, height, facingLens);
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            cameraManager.openCamera(mCameraId, mStateCallback, mHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (mCaptureSession != null) {
            mCaptureSession.close();
            mCaptureSession = null;
        }
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(this, "This app needs to access camera", Toast.LENGTH_SHORT).show();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
            }
        }
    }


    private void setupCamera(int i, int i1, int facingLens) {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) != facingLens) {
                    continue;
                }
                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                mImageReader = ImageReader.newInstance(i, i1, ImageFormat.JPEG, 1);
                mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mHandler);
                int displayRotation = getWindowManager().getDefaultDisplay().getRotation();
                Log.i(TAG, displayRotation + "");
                mSensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                Log.i(TAG, mSensorOrientation + "");
                boolean swappedDimensions = false;
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                            swappedDimensions = true;
                        }
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                            swappedDimensions = true;
                        }
                        break;
                    default:
                        Log.i(TAG, "rotation is invalid");
                }
                int rotatedWidth = i;
                int rotatedHeight = i1;
                if (swappedDimensions) {
                    rotatedWidth = i1;
                    rotatedHeight = i;
                }
                Log.i(TAG, "rotated" + rotatedWidth + " " + rotatedHeight);
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), rotatedWidth, rotatedHeight);
                Log.i(TAG, "preview" + mPreviewSize.getWidth() + " " + mPreviewSize.getHeight());
                mImageSize = chooseOptimalSize(map.getOutputSizes(ImageFormat.JPEG), rotatedWidth, rotatedHeight);
                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private static Size chooseOptimalSize(Size[] choices, int width, int height) {
        List<Size> bigEnough = new ArrayList<Size>();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * height / width &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizeByArea());
        } else {
            return choices[0];
        }

    }

    private void createCameraPreviewSession() {
        try {


            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface surface = new Surface(texture);

            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuilder.addTarget(surface);


            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    if (null == mCameraDevice) {
                        return;
                    }
                    mCaptureSession = session;
                    try {
                        mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        mPreviewRequest = mCaptureRequestBuilder.build();
                        mCaptureSession.setRepeatingRequest(mPreviewRequest, null, mHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    public void takePicture() {
        if (mCameraDevice == null) {
            return;
        }
        try {
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            mCaptureRequestBuilder.addTarget(mImageReader.getSurface());
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            mCaptureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            mCaptureSession.capture(mCaptureRequestBuilder.build(), stillCaptureCallback, mHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


    }

    private void loadFragments() {
        if (fragments.size() == 0) {
            fragments.add(new ChatRoomFragment());
            fragments.add(new ChatFragment());
            fragments.add(new CameraPageViewerFragment());
            fragments.add(new StoriesFragment());
            fragments.add(new DiscoverFragment());
        }
    }

    private static class CompareSizeByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() / (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    public void switchLens() {
        closeCamera();
        if (mFacingLens == CameraCharacteristics.LENS_FACING_BACK) {
            mFacingLens = CameraCharacteristics.LENS_FACING_FRONT;
        } else if (mFacingLens == CameraCharacteristics.LENS_FACING_FRONT) {
            mFacingLens = CameraCharacteristics.LENS_FACING_BACK;
        }
        openCamera(mRealMetrics.widthPixels, mRealMetrics.heightPixels, mFacingLens);
    }

    public void switchFlash() {
        if (mIsFlashOn) {
            mIsFlashOn = false;
        } else {
            mIsFlashOn = true;
        }
    }

    public boolean ismIsFlashOn() {
        return mIsFlashOn;
    }
}
