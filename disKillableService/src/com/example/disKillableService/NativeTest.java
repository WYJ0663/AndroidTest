package com.example.disKillableService;

public class NativeTest {

	static {
		System.loadLibrary("com_example_jnitest_NativeTest");
	}

	public static native void test();
}
