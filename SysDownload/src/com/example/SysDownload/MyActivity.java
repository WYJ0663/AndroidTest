package com.example.SysDownload;

import android.app.Activity;
import android.os.Bundle;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        DownLoadUtil.downLoad(this,"http://down.19meng.com/apk/download/web/id/1384/name/%E7%83%AD%E8%A1%80%E5%A5%A5%E7%89%B9%E6%9B%BC");
    }
}
