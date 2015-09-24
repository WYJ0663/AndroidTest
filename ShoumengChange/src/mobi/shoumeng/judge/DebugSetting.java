package mobi.shoumeng.judge;

import android.util.Log;

public class DebugSetting {
	public static boolean IS_DEBUG_MODE = false;
	public final static boolean IS_TEST_PAY = true;
	public static void toLog(String msg) {
		if (IS_DEBUG_MODE) {
			Log.v("debug", msg);
		}
	}
}
