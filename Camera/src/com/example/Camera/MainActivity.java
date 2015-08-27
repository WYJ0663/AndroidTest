package com.example.Camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.ptwyj.camera.CameraPreview;
import com.ptwyj.camera.view.FocusBoxView;

public class MainActivity extends Activity implements View.OnClickListener, Camera.PictureCallback {
    private final static String TAG = "MainActivity";


    private CameraPreview cameraPreview;
    private FrameLayout preview;
    private Button captureButton;
    private ImageView preViewImage;
    private FocusBoxView focusBoxView;
    private TextView infoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);




        initUI(this);
    }

    private void initUI(Context context) {
        captureButton = (Button) findViewById(R.id.button_capture);
        infoView = (TextView) findViewById(R.id.info);
        preViewImage = (ImageView) findViewById(R.id.pre_image);
        focusBoxView = (FocusBoxView) findViewById(R.id.focus_box);

        cameraPreview = new CameraPreview(this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(cameraPreview);

        captureButton.setOnClickListener(this);
    }

    Rect rect;

    @Override
    public void onClick(View v) {
        if (captureButton == v) {

            rect = focusBoxView.getBox();
            cameraPreview.tack(this);
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bitmap =  cameraPreview.Bytes2Bimap(data, rect);

        if (bitmap!=null){
            preViewImage.setImageBitmap(bitmap);
        }

        camera.startPreview();
    }
}
