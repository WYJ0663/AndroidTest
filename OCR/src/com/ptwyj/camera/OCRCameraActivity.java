package com.ptwyj.camera;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.OCR.R;
import com.ptwyj.camera.view.FocusBoxView;
import com.ptwyj.orc.Orc;

public class OCRCameraActivity extends Activity implements View.OnClickListener, Camera.PictureCallback {
    private final static String TAG = "OCRCameraActivity";

    private CameraPreview cameraPreview;
    private FrameLayout preview;
    private Button flashButton;
    private Button captureButton;
    private Button clearButton;
    private ImageView preViewImage;
    private FocusBoxView focusBoxView;
    private TextView infoView;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orc_camera);


        initUI(this);
    }

    private void initUI(Context context) {
        flashButton = (Button) findViewById(R.id.button_flash);
        captureButton = (Button) findViewById(R.id.button_capture);
        clearButton = (Button) findViewById(R.id.button_clear);
        infoView = (TextView) findViewById(R.id.info);
        preViewImage = (ImageView) findViewById(R.id.pre_image);
        focusBoxView = (FocusBoxView) findViewById(R.id.focus_box);

        cameraPreview = new CameraPreview(this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(cameraPreview);

        flashButton.setOnClickListener(this);
        captureButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("正在解析...");
    }

    Rect rect;

    @Override
    public void onClick(View v) {
        if (captureButton == v) {
            progressDialog.show();
            rect = focusBoxView.getBox();
            cameraPreview.tack(this);
        } else if (clearButton == v) {
            preViewImage.setImageBitmap(null);
            infoView.setText("");
        } else if (flashButton == v) {
            if (cameraPreview.turnLight()) {
                flashButton.setText("关闭灯光");
            } else {
                flashButton.setText("打开灯光");
            }
        }

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        final Bitmap bitmap = cameraPreview.Bytes2Bimap(data, rect);

        if (bitmap != null) {
            preViewImage.setImageBitmap(bitmap);
        }

        camera.startPreview();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "begin>>>>>>>");
                Orc orc = new Orc();
                String text = orc.ocr(bitmap, OCRCameraActivity.this);
                Message msg = new Message();
                msg.obj = text;
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            infoView.setText((String) msg.obj);
            progressDialog.dismiss();
        }
    };

}
