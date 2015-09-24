package mobi.shoumeng.judge.util;

import android.content.Context;
import android.os.Environment;
import mobi.shoumeng.judge.DebugSetting;

import java.io.File;
import java.io.FileOutputStream;

public class PhoneJudgeUtilMain {
    private static final String fileName = "mode";


    /**
     * 获取sdcard的路径,传入包名
     *
     * @return
     */
    public static String getChannelFilePath2(Context context, String fileName, String packageName) {
        try {
            File sdDir = null;
            boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
            if (sdCardExist) {
                sdDir = Environment.getExternalStorageDirectory();// 获取根目录
                return sdDir.getAbsolutePath() + "/shoumeng/" + packageName + "/" + fileName;
            }
        } catch (Exception e) {
            if (DebugSetting.IS_DEBUG_MODE)
                e.printStackTrace();
        }
        return null;
    }


    /**
     * 写渠道信息到sdcard
     *
     * @param activity
     * @param str
     */
    public static boolean writeChannelInfo(Context context, String str, String packageName, String fileName2) {
        boolean success = false;
        try {
            String path = getChannelFilePath2(context, fileName2, packageName);
            if (path != null) {
                success = writeToFile(path, str);
            } else {
                DebugSetting.toLog("the path is null");
            }
        } catch (Exception e) {
        }
        return success;
    }

    /**
     * 将str写进去
     *
     * @param fileName
     * @param str
     * @return
     */
    public static boolean writeToFile(String fileName, String str) {
        FileOutputStream fos = null;
        try {
            File file = new File(fileName);
            File filepath = file.getParentFile();
            if (filepath.exists()) {
                DebugSetting.toLog("channle directory is already exists : " + filepath.getAbsolutePath());
            } else {
                filepath.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            } else {
                DebugSetting.toLog("channle file is already exists : " + file.getAbsolutePath());
            }
            fos = new FileOutputStream(file);
            fos.write(str.getBytes());
            fos.flush();
            return true;
        } catch (Exception e) {
            DebugSetting.toLog("fail to write channel infomation to sdcard ");
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }
        return false;
    }


}
