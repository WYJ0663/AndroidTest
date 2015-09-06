package com.example.Download;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends Activity {
    public String fileURL = "http://img.shendu.com/forum/201108/23/123108h3dqhpj0bphzs9hp.jpg";
    public String folder = "/mydownloads/";
    String path = Environment.getExternalStorageDirectory() + folder;

    /**
     * Called when the activity is first created.
     */
    ProgressBar pb;
    TextView tv;
    int fileSize;
    int downLoadFileSize;
    String fileEx, fileNa, filename;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {//定义一个Handler，用于处理下载线程与UI间通讯
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 0:
                        pb.setMax(fileSize);
                    case 1:
                        pb.setProgress(downLoadFileSize);
                        int result = downLoadFileSize * 100 / fileSize;
                        tv.setText(result + "%");
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "文件下载完成", Toast.LENGTH_SHORT).show();
                        break;

                    case -1:
                        String error = msg.getData().getString("error");
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = (ProgressBar) findViewById(R.id.down_pb);
        tv = (TextView) findViewById(R.id.tv);
        new Thread() {
            public void run() {
                try {
                    down_file(fileURL, path);
                    //下载文件，参数：第一个URL，第二个存放路径  
                } catch (IOException e) {
                    // TODO Auto-generated catch block  
                    e.printStackTrace();
                }
            }
        }.start();


    }

    public void down_file(String url, String path) throws IOException {
        //下载函数        
        filename = url.substring(url.lastIndexOf("/") + 1);
        //获取文件名  
        URL myURL = new URL(url);
        URLConnection conn = myURL.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();
        this.fileSize = conn.getContentLength();//根据响应获取文件大小  
        if (this.fileSize <= 0) throw new RuntimeException("无法获知文件大小 ");
        if (is == null) throw new RuntimeException("stream is null");
        FileOutputStream fos = new FileOutputStream(path + filename);
        //把数据存入路径+文件名  
        byte buf[] = new byte[1024];
        downLoadFileSize = 0;
        sendMsg(0);
        do {
            //循环读取  
            int numread = is.read(buf);
            if (numread == -1) {
                break;
            }
            fos.write(buf, 0, numread);
            downLoadFileSize += numread;

            sendMsg(1);//更新进度条  
        } while (true);
        sendMsg(2);//通知下载完成  
        try {
            is.close();
        } catch (Exception ex) {
            Log.e("tag", "error: " + ex.getMessage(), ex);
        }

    }

    private void sendMsg(int flag) {
        Message msg = new Message();
        msg.what = flag;
        handler.sendMessage(msg);
    }


}  