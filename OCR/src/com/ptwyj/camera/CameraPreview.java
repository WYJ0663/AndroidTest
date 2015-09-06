package com.ptwyj.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * WYJ 2015-8-31
 */
public class CameraPreview extends SurfaceView implements
        SurfaceHolder.Callback {
    private final static String TAG = "CameraPreview";

    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Camera.Parameters cameraParameters;

    public CameraPreview(Activity activity) {
        super(activity);

        init(activity);
        initCamera(activity);


    }

    private void init(Activity activity) {
        surfaceHolder = getHolder();
        surfaceHolder.setKeepScreenOn(true);      // 屏幕常亮

        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    private void initCamera(Activity activity) {
        camera = Camera.open(); // 打开摄像头\
        camera.setDisplayOrientation(getPreviewDegree(activity));
        surfaceHolder.addCallback(this);
        cameraParameters = camera.getParameters(); // 获取各项参数
        cameraParameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
        List<Camera.Size> list = cameraParameters.getSupportedPictureSizes();
        int i = list.size() / 2;//取中间像素值
        cameraParameters.setPictureSize(list.get(i).width, list.get(i).height); // 设置保存的图片尺寸
        surfaceHolder.setFixedSize(list.get(i).width, list.get(i).height);
        camera.setParameters(cameraParameters);
        camera.startPreview(); // 开始预览
    }

    // 创建相机预览
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
        }
    }

    // 销毁相机预览
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.release(); // 释放照相机
            camera = null;
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        //  If your preview can change or rotate,take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (surfaceHolder.getSurface() == null) {
            // 预览不存在
            return;
        }


        // 停止相机预览
        try {
            camera.stopPreview();
        } catch (Exception e) {
        }


        // 开始相机预览
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    public static int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }


//    //保存图片
//    @Override
//    public void onPictureTaken(byte[] data, Camera camera) {
//           savePrcture(data);
//    }


    //保存图片到SD
    private void savePrcture(byte[] data) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            camera.startPreview();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    //保存忘记名
    private static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/26WYJ/");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }


    //照相
    public void tack(Camera.PictureCallback callback) {
        camera.takePicture(null, null, callback);
    }

    public Bitmap Bytes2Bimap(byte[] data, Rect rect) {
        if (data.length != 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmap = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height());
            return bitmap;
        } else {
            return null;
        }
    }

    //打开关闭闪光灯
    public boolean turnLight() {

        if (camera == null) {
            return false;
        }
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            return false;
        }

        List<String> flashModes = parameters.getSupportedFlashModes();
        // Check if camera flash exists
        if (flashModes == null) {
            // Use the screen as a flashlight (next best thing)
            return false;
        }
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
                return true;
            } else {

            }
        } else if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            // Turn off the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                return false;
            } else {
                Log.e(TAG, "FLASH_MODE_OFF not supported");
            }
        }
        return false;
    }

}