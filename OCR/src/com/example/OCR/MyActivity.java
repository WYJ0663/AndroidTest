package com.example.OCR;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ptwyj.camera.OCRCameraActivity;
import com.ptwyj.orc.CameraActivity;
import com.ptwyj.orc.Orc;

public class MyActivity extends Activity {
    private static final String TAG = "";
    private static final String IMAGE_PATH = "/mnt/sdcard/tesseract/ocr.jpg";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        textView = ((TextView) findViewById(R.id.field));

        final Bitmap bitmap = Orc.getBitmap(IMAGE_PATH);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "begin>>>>>>>");
//                String text = Orc.ocr(bitmap);
//                Message msg = new Message();
//                msg.obj = text;
//                msg.what = 1;
//                mHandler.sendMessage(msg);
//            }
//        }).start();

        ImageView iv = (ImageView) findViewById(R.id.image);
        iv.setImageBitmap(bitmap);
        iv.setVisibility(View.VISIBLE);

    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            textView.setText((String) msg.obj);
        }

        ;
    };


    public void open(View view) {
       startActivity(new Intent(this, OCRCameraActivity.class));
    }
}
