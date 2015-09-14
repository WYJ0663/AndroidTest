package com.example.disKillableService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button btnJni;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnJni = (Button) findViewById(R.id.btn_jni);
		btnJni.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				NativeTest.test();
			}
		});
	}

}
