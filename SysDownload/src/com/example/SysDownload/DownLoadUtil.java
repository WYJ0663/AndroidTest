package com.example.SysDownload;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * 万精游下载
 * 
 * @author yuxiaohua 日期：2015-4-3
 */
public class DownLoadUtil {
	/**
	 * 根据提供的url调用系统下载器下载文件，优先使用谷歌浏览器下载
	 * 
	 * @param context
	 * @param downLoadUrl
	 */
	public static void downLoad(Context context, String downLoadUrl) {
		// DebugSetting.toLog("下载地址：" + downLoadUrl);
		try {
			// DebugSetting.toLog("优先使用谷歌流量器下载....");
			downLoadWithGoogleBrowser(context, downLoadUrl);
			return;
		} catch (Exception e) {

		//	DebugSetting.printException(e);
			// DebugSetting.toLog("使用谷歌流量器下载失败...");
		}
		try {
			// DebugSetting.toLog("使用系统其它下载器下载...");
			downLoadWithAnyOne(context, downLoadUrl);
		} catch (Exception e) {
			Toast.makeText(context, "下载失败:未知错误", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 定向使用com.google.android.browser下载 防止如应用宝之类的下载器拦截下载文件 导致下载的文件不一定是从手盟下载
	 * 
	 * @param context
	 * @param downLoadUrl
	 */
	public static void downLoadWithGoogleBrowser(Context context, String downLoadUrl) {
		Uri uri = Uri.parse(downLoadUrl);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setPackage("com.google.android.browser");
		intent.addCategory("android.intent.category.BROWSABLE");
		intent.setComponent(new ComponentName("com.android.browser",
				"com.android.browser.BrowserActivity"));
		// 如果不为空则说明是放在悬浮窗容器中的,
		if (!(context instanceof Activity)) {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}

		context.startActivity(intent);
	}

	/**
	 * 不定向 调用系统下载器下载
	 * 
	 * @param context
	 * @param downLoadUrl
	 */
	public static void downLoadWithAnyOne(Context context, String downLoadUrl) {
		Uri uri = Uri.parse(downLoadUrl);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		// 如果不为空则说明是放在悬浮窗容器中的,
		if (!(context instanceof Activity)) {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		context.startActivity(intent);
	}
}
