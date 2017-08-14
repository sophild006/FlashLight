package app.bright.flashlight.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.SoundPool;
import android.util.Log;

import app.bright.flashlight.R;

/**
 * Created by Administrator on 2017/7/18.
 */

public class ViewManager {
    public static float dips_1 = 1.33f;
    public static float dips_2 = 1.67f;
    public static float dips_3 = 1.8f;
    public static float dips_4 = 1.5f;
    public Bitmap slideBitmap;
    public Bitmap mainBg;
    public float translateX;
    public int widthTemp;
    public int itemWidth;
    public float picWidth, picHeight;
    private SoundPool soundPool = new SoundPool(2, 3, 0);

    public ViewManager() {
    }

    public void create_8(Resources resources, BitmapFactory.Options options) {
        this.mainBg = BitmapFactory.decodeResource(resources, R.drawable.panel_led_bg_8, options);
        this.slideBitmap = BitmapFactory.decodeResource(resources, R.drawable.panel_led_knob_8, options);
        translateX = -1095.0f;
        widthTemp = 511;
        itemWidth = 58;
        picWidth = 480;
        picHeight = 800;
    }

    public void create_9(Resources resources, BitmapFactory.Options options) {
        this.mainBg = BitmapFactory.decodeResource(resources, R.drawable.panel_led_bg_9, options);
        this.slideBitmap = BitmapFactory.decodeResource(resources, R.drawable.panel_led_knob_6, options);
        translateX = -1095.0f;
        widthTemp = 511;
        itemWidth = 58;
        picWidth = 480;
        picHeight = 854;
    }

    public void create_4(Resources resources, BitmapFactory.Options options) {
        this.mainBg = BitmapFactory.decodeResource(resources, R.drawable.panel_led_bg_4, options);
        this.slideBitmap = BitmapFactory.decodeResource(resources, R.drawable.panel_led_knob_4, options);
        translateX = -667.0f;
        Log.d("wwq", "-667.0f");
        widthTemp = 328;
        itemWidth = 38;
        picWidth = 320;
        picHeight = 480;
    }

    public void create_default(Resources resources, BitmapFactory.Options options) {
        this.mainBg = BitmapFactory.decodeResource(resources, R.drawable.panel_led_bg_6, options);
        this.slideBitmap = BitmapFactory.decodeResource(resources, R.drawable.panel_led_knob_6, options);
        translateX = -1095.0f;
        widthTemp = 511;
        itemWidth = 58;
        picWidth = 480;
        picHeight = 640;
    }

    public void play() {
    }

    public long getFrequence(int i) {
        switch (i) {
            case 1:
                return 900;
            case 2:
                return 800;
            case 3:
                return 700;
            case 4:
                return 600;
            case 5:
                return 500;
            case 6:
                return 400;
            case 7:
                return 300;
            case 8:
                return 200;
            case 9:
                return 100;
            default:
                return 0;
        }
    }
}
