package com.example.disKillableService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TestService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("start service");
		NativeTest.test();
	}

}
