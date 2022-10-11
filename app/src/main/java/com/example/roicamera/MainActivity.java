package com.example.roicamera;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.CAMERA;


public class MainActivity extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2{

    private final int PICK_IMAGE = 1111;
    private static final int PICK_IMAGE_REQUEST = 0;
    private static final String TAG = "camera";
    private Mat matInput;
    private Mat m_matRoi;
    Bitmap bmp_result;
    Bitmap gallery_img;
    Button roi_capture;
    Button img_gallery;
    Rect rect;
    Rect roi_rect;

    private CameraBridgeViewBase mOpenCvCameraView;

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roi_capture = (Button)findViewById(R.id.btn_capture);
        img_gallery = (Button)findViewById(R.id.btn_gallery);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);


        mOpenCvCameraView = (CameraBridgeViewBase)findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(0); // front-camera(1),  back-camera(0)
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
//        Button  Btn = (Button) findViewById(R.id.btn_capture);
//        Btn.setEnabled(true);

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "onResume :: Internal OpenCV library not found.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "onResum :: OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    public void onDestroy() {
        super.onDestroy();

        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        matInput = inputFrame.rgba();

        // ROI size
        double m_dWscale = (double) 4/7;
        double m_dHscale = (double) 3/5;

        int mRoiWidth = (int)(matInput.size().width * m_dWscale);
        int mRoiHeight = (int)(matInput.size().height * m_dHscale);

        int mRoiX = (int) (matInput.size().width - mRoiWidth) / 2;
        int mRoiY = (int) (matInput.size().height - mRoiHeight) / 2;

        rect = new Rect(mRoiX,mRoiY,mRoiWidth,mRoiHeight);
        Imgproc.rectangle(matInput,rect,new Scalar(255, 255, 255, 255),5);

        roi_rect = new Rect(mRoiX+4,mRoiY+4,mRoiWidth-8,mRoiHeight-8);
        m_matRoi = matInput.submat(roi_rect);

        return matInput;
    }


    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }


    //여기서부턴 퍼미션 관련 메소드
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;


    protected void onCameraPermissionGranted() {
        List<? extends CameraBridgeViewBase> cameraViews = getCameraViewList();
        if (cameraViews == null) {
            return;
        }
        for (CameraBridgeViewBase cameraBridgeViewBase: cameraViews) {
            if (cameraBridgeViewBase != null) {
                cameraBridgeViewBase.setCameraPermissionGranted();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean havePermission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                havePermission = false;
            }
        }
        if (havePermission) {
            onCameraPermissionGranted();
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onCameraPermissionGranted();
        }else{
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id){
                requestPermissions(new String[]{CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.create().show();
    }

    private static final long MIN_CLICK_INTERVAL = 1000;
    private long mLastClickTime = 0;

    public void onClick(View view) {
        Button Btn = (Button) findViewById(R.id.btn_capture);
        Button Btn2 = (Button) findViewById(R.id.btn_gallery);
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;
        mLastClickTime = currentClickTime;
//        Btn.setEnabled(true);

        // 중복클릭 아닌 경우
        if (elapsedTime > MIN_CLICK_INTERVAL) {
            switch(view.getId()) {
                case R.id.btn_capture: {
                    bmp_result = Bitmap.createBitmap(m_matRoi.cols(),m_matRoi.rows(),Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(m_matRoi,bmp_result);

                    Intent intent = new Intent(getApplicationContext(),RoiActivity.class);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    bmp_result.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("roi",byteArray);

                    Btn.setEnabled(false);
                    startActivity(intent);
                }

                case R.id.btn_gallery: {
                    Btn2.setOnClickListener(new Button.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            pickFromGallery();
                        }
                    });
                }
            }
        }

    }

    private void pickFromGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent,PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri selectedImage = data.getData();

            gallery_img = null;
            try {
//                gallery_img = Bitmap.createBitmap(m_matRoi.cols(),m_matRoi.rows(),Bitmap.Config.ARGB_8888);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                    gallery_img = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), selectedImage));
                    bmp_result = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), selectedImage));
                    gallery_img = Bitmap.createScaledBitmap(bmp_result, m_matRoi.cols(), m_matRoi.rows(), true);
                } else {
//                    gallery_img = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    bmp_result = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    gallery_img = Bitmap.createScaledBitmap(bmp_result, m_matRoi.cols(), m_matRoi.rows(), true);
                }
//                gallery_img = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                Intent intent = new Intent(getApplicationContext(),RoiActivity.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                gallery_img.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                byte[] byteArray = stream.toByteArray();
                intent.putExtra("roi",byteArray);


//            Btn.setEnabled(false);
                startActivity(intent);

//                imageview1.setImageBitmap(bitmap);
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(getApplicationContext(),RoiActivity.class);
            intent.putExtra("roi",selectedImage);
            startActivity(intent);
        }
    }


}