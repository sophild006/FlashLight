package app.bright.flashlight.util;

import android.util.Log;

import app.bright.flashlight.BuildConfig;
import app.bright.flashlight.constant.Config;

/**
 * Created by Administrator on 2017/7/19.
 */

public class D {

    public static void d(Object object) {
        if (Config.DEBUG && object != null) {
            Log.d("wwq", "" + object.toString());
        }
    }

    public static void i(String eventLog, String s) {
        if (Config.DEBUG) {
            Log.i(eventLog, "" + s);
        }
    }
}
