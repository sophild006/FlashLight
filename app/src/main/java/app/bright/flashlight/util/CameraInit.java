package app.bright.flashlight.util;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.lang.reflect.Method;
import java.util.List;

public class CameraInit {
    private static CameraInit mInstance;
    public Camera camera;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    public SurfaceHolder holder;

    public synchronized void enableWakeLoack() {
        try {
            this.wakeLock.acquire();
        } catch (Exception E) {
        }

    }

    public synchronized void releaseWakeLoack() {

        try {
            if (wakeLock != null && wakeLock.isHeld()) {
                this.wakeLock.release();
            }
        } catch (Exception e) {

        }
    }

    public CameraInit() {
        this.powerManager = (PowerManager) GlobalContext.getAppContext()
                .getSystemService(Context.POWER_SERVICE);
        this.wakeLock = this.powerManager.newWakeLock(
                PowerManager.FULL_WAKE_LOCK, "FlashLock");
        try {
            Object invoke = Class.forName("android.os.ServiceManager").getMethod("getService", new Class[]{String.class}).invoke(null, new Object[]{"hardware"});
            this.f1247a = Class.forName("android.os.IHardwareService$Stub").getMethod("asInterface", new Class[]{IBinder.class}).invoke(null, new Object[]{(IBinder) invoke});
            Class cls = this.f1247a.getClass();
            this.f1248b = cls.getMethod("getFlashlightEnabled", new Class[0]);
            this.f1249c = cls.getMethod("setFlashlightEnabled", new Class[]{Boolean.TYPE});
        } catch (Exception e) {
            try {
                Log.d("wwq", "LED could not be initialized");
                throw new Exception("LED could not be initialized");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        init();
    }

    public static CameraInit getInstance() {
        if (mInstance == null) {
            synchronized (CameraInit.class) {
                mInstance = new CameraInit();
            }
        }
        return mInstance;
    }

    public synchronized void releasea() {
        try {
            if (this.camera != null) {
                Parameters parameters = this.camera.getParameters();
                parameters.setFlashMode("off");
                this.camera.setParameters(parameters);
                this.camera.cancelAutoFocus();
                this.camera.release();
                this.camera = null;
            }
        } catch (Exception e) {
            D.d("releasea  " + e.getLocalizedMessage());
            PreferenceHelper.setBoolean("threadFlag", true);
//            CameraInit.getInstance().releasea();
        }

    }

    public synchronized boolean init() {
        if (this.camera == null) {
            try {
                this.camera = Camera.open();
                this.camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
                PreferenceHelper.setBoolean("threadFlag", true);
//                CameraInit.getInstance().releasea();
                D.d("...:  " + e.getLocalizedMessage());
                return false;
            }
        }
        return true;
    }

    public synchronized boolean turnOff() {
        try {
            if (init()) {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode("off");
                this.camera.setParameters(parameters);
                PreferenceHelper.setBoolean("flash_open", false);
                PreferenceHelper.setBoolean("thread_open", false);
                return true;
            }
        } catch (Exception e) {
            PreferenceHelper.setBoolean("threadFlag", true);
            PreferenceHelper.setBoolean("thread_open", false);
            D.d("turnOff:  " + e.getLocalizedMessage());
            return false;
        }
        return false;
    }

    public synchronized boolean turnOn() {
        try {
            if (init()) {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode("torch");
                this.camera.setParameters(parameters);
//                camera.setPreviewDisplay(getSurfaceViewHolder());
                if (Build.BRAND.toLowerCase().contains("google")) {
                    camera.setPreviewTexture(new SurfaceTexture(0));
                }
//                this.camera.startPreview();
                PreferenceHelper.setBoolean("flash_open", true);
                PreferenceHelper.setBoolean("thread_open", true);
            }
        } catch (Exception e) {
            D.d("turnOn:  " + e.getLocalizedMessage());
            PreferenceHelper.setBoolean("threadFlag", true);
//            CameraInit.getInstance().releasea();
        }
        return true;
    }

    public synchronized void startPreView() {
        if (camera != null) {
            this.camera.startPreview();
        }
    }

    public boolean getFlightState() {
        try {
            if (init()) {
                Camera.Parameters parameters = camera.getParameters();
                String mode = parameters.getFlashMode();
                if (Camera.Parameters.FLASH_MODE_TORCH.equals(mode)) {
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                    return true;
                }
                camera.release();
                camera = null;
                return false;
            }

        } catch (Exception e) {
            PreferenceHelper.setBoolean("threadFlag", true);
            Log.i("wwq", "获取闪光灯状态出错了" + e.getMessage());
            CameraInit.getInstance().releasea();
        }
        return false;
    }

    public boolean turnOn_() {
        if (this.camera != null) {
            try {
                Camera.Parameters parameters = camera.getParameters();
                if (parameters != null) {
                    List supportedFlashModes = parameters.getSupportedFlashModes();
                    if (supportedFlashModes != null && supportedFlashModes.contains("on")) {
                        parameters.setFlashMode("on");
                        this.camera.setParameters(parameters);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private Object f1247a = null;
    private Method f1248b = null;
    private Method f1249c = null;

    public void turn(boolean z) {
        try {
            this.f1249c.invoke(this.f1247a, new Object[]{Boolean.valueOf(z)});
        } catch (Exception e) {
        }
    }

    public void setSurfaceViewHolder(SurfaceHolder surfaceViewHolder) {
        this.holder = surfaceViewHolder;
    }

    public SurfaceHolder getSurfaceViewHolder() {
        return this.holder;
    }

}