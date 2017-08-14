package app.bright.flashlight.util;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.solid.analytics.util.AndroidUtil;
import com.solid.common.Common;

import app.bright.flashlight.constant.Config;

/**
 * Created by Administrator on 2017/7/17.
 */

public class Utils {
    public static boolean getFlightState() {
        try {
            if (camera == null) {
                camera = Camera.open();
            }
            Camera.Parameters parameters = camera.getParameters();
            String mode = parameters.getFlashMode();
            if (Camera.Parameters.FLASH_MODE_TORCH.equals(mode)) {
                camera.release();
                camera = null;
                return true;
            }
            camera.release();
            camera = null;
            return false;
        } catch (Exception e) {
            Log.i("wwq", "获取闪光灯状态出错了" + e.getMessage());
        }
        return false;
    }

    private static Camera camera;

    public static void initCamera() {
        camera = Camera.open();
    }

    public static boolean setFlightState(boolean isOpen) {
        try {
            if (camera == null) {
                camera = Camera.open();
            }
            Camera.Parameters parameters = camera.getParameters();
            if (isOpen) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                camera.setParameters(parameters);
                camera.setPreviewTexture(new SurfaceTexture(0));
                camera.startPreview();
            } else {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//关闭
                camera.setParameters(parameters);
                camera.stopPreview();
                camera.release();
                camera = null;
            }

            return true;
        } catch (Exception e) {
            Log.i("wwq", e.getLocalizedMessage());
            Log.i("wwq", "设置闪光灯状态出错了" + e.getMessage());
        }
        return false;
    }

    public static boolean setFlightSOSState(boolean isOpen) {
        try {
            if (camera == null) {
                Log.d("wwq", "camera is null");
                return false;
            }
            Camera.Parameters parameters = camera.getParameters();
            if (isOpen) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                camera.setParameters(parameters);
                camera.setPreviewTexture(new SurfaceTexture(0));
                camera.startPreview();
            } else {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//关闭
                camera.setParameters(parameters);
            }

            return true;
        } catch (Exception e) {
            Log.i("wwq", "----  " + e.getLocalizedMessage());
            Log.i("wwq", "出错了" + e.getMessage());
        }
        return false;
    }

    public static boolean isSupportCameraLedFlash(Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm != null) {
            FeatureInfo[] features = pm.getSystemAvailableFeatures();
            if (features != null) {
                for (FeatureInfo f : features) {
                    if (f != null && PackageManager.FEATURE_CAMERA_FLASH.equals(f.name))
                        return true;
                }
            }
        }
        return false;
    }

    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static boolean isNetConnect() {
        if (GlobalContext.getAppContext() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) GlobalContext.getAppContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int getScreenWidht() {
        WindowManager windowManager = (WindowManager) GlobalContext.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager) GlobalContext.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static boolean isMainThread(Thread thread) {
        return thread != null && thread == Looper.getMainLooper().getThread();
    }

    public static int getNetStatus() {
        Context mCtx = GlobalContext.getAppContext();
        ConnectivityManager mConnMgr = (ConnectivityManager) mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnMgr.getActiveNetworkInfo();
        int type = 0;
        if (info != null && info.isAvailable()) {
            type = info.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                type = 1;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                type = 2;
            }
        }
        return type;
    }

    public static int getPackSign() {
        boolean fb = AndroidUtil.isAppInstalled(GlobalContext.getAppContext(), Config.FACEBOOK);
        boolean gg = AndroidUtil.isAppInstalled(GlobalContext.getAppContext(), Config.GOOGLEPLAY);
        if (fb) {
            if (gg) {
                return 1;
            } else {
                return 3;
            }
        } else if (fb) {
            return 2;
        } else {
            return 0;
        }
    }
}
