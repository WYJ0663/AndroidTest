package com.example.Download;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    public static final String LOG_TAG = "test";

    private ProgressDialog mProgressDialog;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

    File rootDir = Environment.getExternalStorageDirectory();

    //定义要下载的文件名
    public String fileName = "test.jpg";
    public String fileURL = "http://img.shendu.com/forum/201108/23/123108h3dqhpj0bphzs9hp.jpg";

    public String folder = "mydownloads";
    File file;

    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        imageView = (ImageView) findViewById(R.id.image);
        //        TextView tv = new TextView(this);
        //        tv.setText("Android Download File With Progress Bar");

        //检查下载目录是否存在
        checkAndCreateDirectory(fileName);
        file = new File(rootDir + "/mydownloads/", fileName);

        //执行asynctask
        new DownloadFileAsync().execute(fileURL);
    }
    byte[] bytes;
    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... aurl) {
            //dismiss the dialog after the file was downloaded
            try {
                HttpUtil httpUtil = new HttpUtil();
                bytes = httpUtil.doGetBytes(fileURL);


                FileOutputStream fileOutputStream = new FileOutputStream(file);

                fileOutputStream.write(bytes);

                fileOutputStream.flush();
                fileOutputStream.close();




            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            Log.d(LOG_TAG, progress[0]);
        }

        @Override
        protected void onPostExecute(String unused) {
            Bitmap bitmap = HttpUtil.Bytes2Bimap(bytes);
            imageView.setImageBitmap(bitmap);

        }
    }


    public void checkAndCreateDirectory(String dirName) {
        File new_dir = new File(rootDir + dirName);
        if (!new_dir.exists()) {
            new_dir.mkdirs();
        }
    }


}
