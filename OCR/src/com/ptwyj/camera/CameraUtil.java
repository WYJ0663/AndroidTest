package com.ptwyj.camera;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2015-08-26.
 */
public class CameraUtil {
    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

}