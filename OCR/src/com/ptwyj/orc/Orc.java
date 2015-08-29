package com.ptwyj.orc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;

/**
 * Created by WYJ on 2015-08-26.
 */
public class Orc {

    private static final String TAG = "Orc";
    private static final String TESSBASE_PATH = "/mnt/sdcard/tesseract/";
    private static final String DEFAULT_LANGUAGE = "eng";
    //  private static final String EXPECTED_FILE = tessbase_path + "tessdata/" + DEFAULT_LANGUAGE + ".traineddata";


    public static Bitmap getBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return getBitmap(bitmap, path);
    }

    public static Bitmap getBitmap(Bitmap bitmap, String path) {

        Log.d("nimei", "---in ocr()  before try--");
        try {
            Log.v(TAG, "not in the exception");
            ExifInterface exif = new ExifInterface(path);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            Log.v(TAG, "Orient: " + exifOrientation);

            int rotate = 0;
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            Log.v(TAG, "Rotation: " + rotate);

            // if (rotate != 0) { //这儿必须要注释掉，否则会报错 什么 ARGB_8888之类的

            // Getting width & height of the given image.
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            // Setting pre rotate
            Matrix mtx = new Matrix();
            mtx.preRotate(rotate);

            // Rotating Bitmap
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
            // tesseract req. ARGB_8888
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            // } //这儿必须要注释掉，否则会报错 什么 ARGB_8888之类的

        } catch (IOException e) {
            Log.e(TAG, "Rotate or coversion failed: " + e.toString());
            Log.v(TAG, "in the exception");
        }

        return bitmap;

    }

    // 识别
    public String ocr(Bitmap bitmap, Context context) {
        TessDataManager.initTessTrainedData(context);
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        String path = TessDataManager.getTesseractFolder();

        Log.v(TAG, "Before baseApi");

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(path, DEFAULT_LANGUAGE);
        baseApi.setImage(bitmap);
        String recognizedText = baseApi.getUTF8Text();
        baseApi.end();

        Log.v(TAG, "OCR Result: " + recognizedText);

        // clean up and show
        if (DEFAULT_LANGUAGE.equalsIgnoreCase("eng")) {
            recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
        }
        if (recognizedText.length() != 0) {

            return recognizedText;
        }

        return "";
    }


}
