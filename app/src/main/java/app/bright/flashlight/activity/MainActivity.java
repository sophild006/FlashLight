package app.bright.flashlight.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.provider.Settings.System;
import android.widget.Toast;

import com.eval.EvalSdk;
import com.solid.ad.Ad;
import com.solid.ad.AdInterstitial;
import com.solid.ad.AdListener;
import com.solid.ad.AdListenerBase;
import com.solid.ad.AdSdk;
import com.solid.ad.clink.AdNativeClink;
import com.solid.ad.protocol.ClinkNativeOffer;
import com.solid.ad.view.AdNativeViewIdBuilder;
import com.solid.analytics.AnalyticsUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

import app.bright.flashlight.R;
import app.bright.flashlight.base.BaseActivity;
import app.bright.flashlight.base.HomeWatcherReceiver;
import app.bright.flashlight.constant.Config;
import app.bright.flashlight.constant.Event;
import app.bright.flashlight.util.CameraInit;
import app.bright.flashlight.util.D;
import app.bright.flashlight.util.PreferenceHelper;
import app.bright.flashlight.util.ThreadManager;
import app.bright.flashlight.util.Utils;
import app.bright.flashlight.view.SlideLightView;
import app.bright.flashlight.view.ViewManager;
import okhttp3.internal.Util;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivOpen;
    private ImageView ivLightShow;
    private ImageView ivSlide;
    private ImageView ivColorView, ivSettingView;
    private RelativeLayout rlWhiteView, rlFlashView, rlStrobeView;
    private Window window;
    private WindowManager.LayoutParams attributes;
    private View colorRed, colorOrigin, colorYellow, colorGreen, colorBlue;
    private ProgressBar colorWhite,colorPurple;
    private ImageView ivFlashOpen, ivFlashClose;
    private boolean isMainViw = true;
    private LinearLayout llColorView;
    private boolean btnOpen = false;
    private long currentFrequence = 100;
    private boolean isSetClick = false;
    private SurfaceView surface;
    private int colorFlag = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    hideToolsView();
                    break;
                case 2:
                    CameraInit.getInstance().turnOff();
                    PreferenceHelper.getBoolean("is_delay_closed", true);
                    PreferenceHelper.setBoolean("isOpen", false);
                    ivOpen.setImageResource(R.drawable.close_btn_b_199);
                    ivLightShow.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    CameraInit.getInstance().turnOn();
                    break;
                case 4:
                    CameraInit.getInstance().turnOff();
                    break;

            }
        }
    };
    private static MainActivity mActivity;

    public static MainActivity instance() {
        return mActivity;
    }

    private ViewManager viewManager;
    private TextView tvFrequence;
    private SlideLightView slideLightView;
    private MyBroad broad;
    private int brightNess;
    private RelativeLayout mainAdContainer;
    private HomeWatcherReceiver homeWatcherReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mActivity = this;
        window = getWindow();
        attributes = getWindow().getAttributes();
        viewManager = new ViewManager();
        initSoundPool();
        intiView();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.delay.close.flash");
        broad = new MyBroad();
        registerReceiver(broad, intentFilter);
        homeWatcherReceiver = new HomeWatcherReceiver();
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeWatcherReceiver, homeFilter);
        AnalyticsUtil.sendEventSimple(Event.LIGHT_HOME_SHOW);
        if (!Utils.isSupportCameraLedFlash(this)) {
            showWhiteView();
        }
        D.i("eventLog", Event.LIGHT_HOME_SHOW + "  ");
    }

    public class MyBroad extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            D.d("delat start....");
            if (intent != null) {
                int type = intent.getIntExtra("type", 0);
                switch (type) {
                    case 0:
                        long delay_light_off = PreferenceHelper.getLong("delay_light_off", Config.TEN_MINUTES);
                        if (delay_light_off != -1) {
                            delayClose(delay_light_off);
                        }
                        break;
                    case 1:
//                        finish();
//                        PreferenceHelper.setLong("frequence", 0);
//                        PreferenceHelper.setBoolean("threadFlag", true);
//                        PreferenceHelper.setBoolean("isOpen", false);
//                        handler.removeCallbacksAndMessages(null);
//                        CameraInit.getInstance().turnOff();
//                        CameraInit.getInstance().releasea();
//                        reOpenLight();
                        break;
                }
            }

        }
    }

    public void delayClose(long delay) {
        handler.removeMessages(2);
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessageDelayed(2, delay);
    }

    private SurfaceHolder previewHolder;

    private void intiView() {
        PreferenceHelper.setLong("frequence", 0);
        PreferenceHelper.setBoolean("threadFlag", true);

        CameraInit.getInstance();
        surface = (SurfaceView) findViewById(R.id.surface);
        previewHolder=surface.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        previewHolder.setFixedSize(getWindow().getWindowManager()
                .getDefaultDisplay().getWidth(), getWindow().getWindowManager()
                .getDefaultDisplay().getHeight());
        CameraInit.getInstance().setSurfaceViewHolder(previewHolder);
        ivOpen = (ImageView) findViewById(R.id.iv_open);
        ivLightShow = (ImageView) findViewById(R.id.iv_light_show);
        ivSlide = (ImageView) findViewById(R.id.iv_slide);
        ivColorView = (ImageView) findViewById(R.id.iv_color_view);
        ivSettingView = (ImageView) findViewById(R.id.iv_setting);

        tvFrequence = (TextView) findViewById(R.id.tv_frequence);
        slideLightView = (SlideLightView) findViewById(R.id.view_slide);

        rlWhiteView = (RelativeLayout) findViewById(R.id.rl_white_light);
        rlFlashView = (RelativeLayout) findViewById(R.id.rl_flash_light);
        rlStrobeView = (RelativeLayout) findViewById(R.id.rl_strobe_light);


        colorWhite = (ProgressBar) findViewById(R.id.color_white);
        colorRed = findViewById(R.id.color_red);
        colorOrigin = findViewById(R.id.color_origin);
        colorYellow = findViewById(R.id.color_yellow);
        colorGreen = findViewById(R.id.color_green);
        colorBlue = findViewById(R.id.color_blue);
        colorPurple = (ProgressBar)findViewById(R.id.color_purple);

        llColorView = (LinearLayout) findViewById(R.id.ll_color_view);

        ivFlashOpen = (ImageView) findViewById(R.id.iv_flash_open);
        ivFlashClose = (ImageView) findViewById(R.id.iv_white_close);
        ivOpen = (ImageView) findViewById(R.id.iv_open);
        ivLightShow = (ImageView) findViewById(R.id.iv_light_show);


        mainAdContainer = (RelativeLayout) findViewById(R.id.rl_ad_container);
        initEvent();
    }

    private void initEvent() {
        ivOpen.setOnClickListener(this);
        ivColorView.setOnClickListener(this);
        ivSettingView.setOnClickListener(this);
        slideLightView.setOnFrequenceChangedListener(new SlideLightView.onFrequenceChangedListener() {
            @Override
            public void onChanged(int flag) {
                tvFrequence.setText(flag + "");
                D.d("flag: " + flag);
                if (flag != 0 && flag < 10) {

                    if (flag == 9) {
                        play(2);
                    } else {
                        play(1);
                    }
                    PreferenceHelper.setBoolean("resetFlag", true);
                    startShow(flag);
                    if (btnOpen && PreferenceHelper.getBoolean("threadFlag", false)) {
                        PreferenceHelper.setBoolean("threadFlag", false);
                        new MainThread().start();
                    }
                    long delay_light_off = PreferenceHelper.getLong("delay_light_off", Config.TEN_MINUTES);
                    if (delay_light_off != -1) {
                        delayClose(delay_light_off);
                    }
                    AnalyticsUtil.sendEventSimple(Event.LIGHT_FM_SLIDE);
                    D.i("eventLog", Event.LIGHT_FM_SLIDE + "  ");
                } else if (flag == 0) {
                    play(2);
                    startShow(flag);
                    PreferenceHelper.setBoolean("threadFlag", true);
                    boolean isOpen = PreferenceHelper.getBoolean("isOpen", false);
                    if (isOpen) {
                        CameraInit.getInstance().turnOn();
                        if (Utils.isSupportCameraLedFlash(MainActivity.this)) {
                            handler.sendEmptyMessageDelayed(3, 1000);
                        }
                    }
                    D.d("flag = " + flag);
                }
            }
        });
        colorWhite.setOnClickListener(this);
        colorRed.setOnClickListener(this);
        colorOrigin.setOnClickListener(this);
        colorYellow.setOnClickListener(this);
        colorGreen.setOnClickListener(this);
        colorBlue.setOnClickListener(this);
        colorPurple.setOnClickListener(this);

        ivFlashClose.setOnClickListener(this);
        ivFlashOpen.setOnClickListener(this);
        brightNess = Settings.System.getInt(getContentResolver(), "screen_brightness", 12);
        PreferenceHelper.setInt("screen_brightness", brightNess);
        //第一次打开开启闪光灯
        btnOpen = true;
        ThreadManager.execute(new Runnable() {
            @Override
            public void run() {
                CameraInit.getInstance().turnOff();
                CameraInit.getInstance().turnOn();
            }
        });
        play(0);
        ivOpen.setImageResource(R.drawable.open_btn_b_199);
        ivLightShow.setVisibility(View.VISIBLE);
        PreferenceHelper.setBoolean("isOpen", true);
        PreferenceHelper.setBoolean("delay_is_open", false);
        CameraInit.getInstance().enableWakeLoack();
    }


    private void startShow(int flag) {
        currentFrequence = viewManager.getFrequence(flag);
        PreferenceHelper.setLong("frequence", currentFrequence);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open:
                showIntivinal("torch_int");
                if (Utils.isSupportCameraLedFlash(this)) {
                    D.d("threadFlag  " + PreferenceHelper.getBoolean("threadFlag", false));
                    if (PreferenceHelper.getBoolean("threadFlag", false)) {
                        PreferenceHelper.setBoolean("threadFlag", false);
                        new MainThread().start();
                    }
                    D.d("isOpen:  " + !PreferenceHelper.getBoolean("isOpen", false));
                    if (!PreferenceHelper.getBoolean("isOpen", false)) {
                        handler.removeMessages(4);
                        btnOpen = true;
                        PreferenceHelper.setBoolean("isOpen", true);
                        PreferenceHelper.setBoolean("delay_is_open", false);
                        ivOpen.setImageResource(R.drawable.open_btn_b_199);
                        ivLightShow.setVisibility(View.VISIBLE);
                        CameraInit.getInstance().turnOn();
                        long delay_light_off = PreferenceHelper.getLong("delay_light_off", Config.TEN_MINUTES);
                        if (delay_light_off != -1) {
                            delayClose(delay_light_off);
                        }
                    } else {
                        handler.removeMessages(3);
                        CameraInit.getInstance().releaseWakeLoack();
                        btnOpen = false;
                        PreferenceHelper.setBoolean("isOpen", false);
                        ivOpen.setImageResource(R.drawable.close_btn_b_199);
                        ivLightShow.setVisibility(View.INVISIBLE);
                        CameraInit.getInstance().turnOff();
                        PreferenceHelper.setBoolean("threadFlag", true);
                        handler.sendEmptyMessageDelayed(4, 1000);
                        D.d("turn off");
                    }
                    play(0);
                } else {
                    showWhiteView();
                }
                AnalyticsUtil.sendEventSimple(Event.LIGHT_HOME_SWITCH_CLICK);
                D.i("eventLog", Event.LIGHT_HOME_SWITCH_CLICK + "  ");
                break;
            case R.id.iv_color_view:
                showIntivinal("switch_int");
                if (isMainViw) {
                    isMainViw = false;
                    showWhiteView();
                    CameraInit.getInstance().turnOff();
                } else {
                    if (mainAdContainer.getVisibility() == View.VISIBLE) {
                        mainAdContainer.setBackgroundColor(Color.parseColor("#171717"));
                    }
                    ivOpen.setEnabled(true);
                    PreferenceHelper.setBoolean("isOpen", false);
                    boolean resetFlag = PreferenceHelper.getBoolean("resetFlag", false);
                    if (!resetFlag) {
                        PreferenceHelper.setBoolean("resetFlag", true);
                    }
                    isMainViw = true;
                    handler.removeMessages(1);
                    ivColorView.setImageResource(R.drawable.forwardlight_btn_b_98);
                    ivSettingView.setImageResource(R.drawable.set_btn_b_98);
                    rlWhiteView.setVisibility(View.GONE);
                    rlStrobeView.setVisibility(View.VISIBLE);
                    rlFlashView.setVisibility(View.GONE);
                    int bright = PreferenceHelper.getInt("screen_brightness", 12);
                    Settings.System.putInt(getContentResolver(), "screen_brightness", bright);
                    attributes.screenBrightness = bright;
                    window.setAttributes(attributes);
                }
                AnalyticsUtil.sendEventSimple(Event.LIGHT_TOGGLE_BUTTON_CLICK);
                D.i("eventLog", Event.LIGHT_TOGGLE_BUTTON_CLICK + "  ");
                break;
            case R.id.iv_setting:
                isSetClick = true;
                startActivity(new Intent(this, SetActivity.class));
                break;
            case R.id.color_white:
                colorFlag = 0;
                rlWhiteView.setBackgroundColor(Color.parseColor("#FFFFFF"));

                break;
            case R.id.color_red:
                colorFlag = 1;
                rlWhiteView.setBackgroundColor(Color.parseColor("#ff0e21"));
                break;
            case R.id.color_origin:
                colorFlag = 2;
                rlWhiteView.setBackgroundColor(Color.parseColor("#faba1e"));
                break;
            case R.id.color_yellow:
                colorFlag = 3;
                rlWhiteView.setBackgroundColor(Color.parseColor("#eff82f"));
                break;
            case R.id.color_blue:
                colorFlag = 4;
                rlWhiteView.setBackgroundColor(Color.parseColor("#1f49fd"));
                break;
            case R.id.color_green:
                colorFlag = 5;
                rlWhiteView.setBackgroundColor(Color.parseColor("#56e41e"));
                break;
            case R.id.color_purple:
                colorFlag = 6;
                rlWhiteView.setBackgroundColor(Color.parseColor("#9320ff"));
                break;
            case R.id.iv_flash_open:
                showWhiteView();
                AnalyticsUtil.sendEventSimple(Event.LIGHT_SCREEN_SWITCH_CLICK, "", 1);
                D.i("eventLog", Event.LIGHT_SCREEN_SWITCH_CLICK + "  ");
                break;
            case R.id.iv_white_close:
                rlFlashView.setVisibility(View.VISIBLE);
                rlStrobeView.setVisibility(View.GONE);
                rlWhiteView.setVisibility(View.GONE);
                AnalyticsUtil.sendEventSimple(Event.LIGHT_SCREEN_SWITCH_CLICK, "", 0);
                D.i("eventLog", Event.LIGHT_SCREEN_SWITCH_CLICK + "  ");
                break;
        }
    }

    private SoundPool sp = null;//声明一个SoundPool的引用
    private HashMap<Integer, Integer> hm;//声明一个HashMap来存放声音资源

    public void initSoundPool() {
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);//创建SoundPool对象
        hm = new HashMap<Integer, Integer>();//创建HashMap对象
        hm.put(0, sp.load(this, R.raw.sound_toggle, 0));
        hm.put(1, sp.load(this, R.raw.adjustment_move, 0));
        hm.put(2, sp.load(this, R.raw.adjustment_end, 0));
    }

    private void play(int sound) {
        if (PreferenceHelper.getBoolean("sound", true)) {
            AudioManager am = (AudioManager) this.getSystemService(AUDIO_SERVICE);
            float currentStreamVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxStreamVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float setVolume = (float) currentStreamVolume / maxStreamVolume;
            sp.play(hm.get(sound), setVolume, setVolume, 1, 0, 1.0f);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isMainViw) {
                    if (!isToolsViewShowing) {
                        showToolsView();
                    } else {
                        hideToolsView();
                    }
                }
                break;
        }

        return true;
    }

    private boolean isToolsViewShowing = true;
    private boolean isScreenBrightDialogShowed = false;

    private void showWhiteView() {
//        if (!(Build.VERSION.SDK_INT < 23 || System.canWrite(this) || isScreenBrightDialogShowed)) {
//            isScreenBrightDialogShowed = true;
//            Intent grantIntent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
//            grantIntent.setData(Uri.parse("package:" + this.getApplication().getPackageName()));
//            startActivity(getIntent());
//        }

        btnOpen = false;
        if (mainAdContainer.getVisibility() == View.VISIBLE) {
            mainAdContainer.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        boolean resetFlag = PreferenceHelper.getBoolean("resetFlag", false);
        if (resetFlag) {
            PreferenceHelper.setBoolean("resetFlag", false);
        }
        Settings.System.putInt(getContentResolver(), "screen_brightness", 244);
        attributes.screenBrightness = 244;
        window.setAttributes(attributes);
        ivLightShow.setVisibility(View.INVISIBLE);
        ivOpen.setImageResource(R.drawable.close_btn_b_199);
        ivOpen.setEnabled(false);
        rlFlashView.setVisibility(View.GONE);
        rlStrobeView.setVisibility(View.GONE);
        rlWhiteView.setVisibility(View.VISIBLE);
        rlWhiteView.setBackgroundColor(Color.parseColor("#ffffff"));
        ivSettingView.setImageResource(R.drawable.set_btn_w_98);
        ivColorView.setImageResource(R.drawable.flashlight_btn_w_98);
        AnalyticsUtil.sendEventSimple(Event.LIGHT_SCREEN_PV);
        D.i("eventLog", Event.LIGHT_SCREEN_PV + "  ");
        handler.sendEmptyMessageDelayed(1, 3000);
        if (Utils.isSupportCameraLedFlash(this)) {
            handler.sendEmptyMessageDelayed(4, 1000);
        }
    }

    private void hideToolsView() {
        isToolsViewShowing = false;
        ivFlashClose.setVisibility(View.GONE);
        ivSettingView.setVisibility(View.GONE);
        ivColorView.setVisibility(View.GONE);
        llColorView.setVisibility(View.GONE);

    }


    private void showToolsView() {
        isToolsViewShowing = true;
        ivFlashClose.setVisibility(View.VISIBLE);
        ivSettingView.setVisibility(View.VISIBLE);
        ivColorView.setVisibility(View.VISIBLE);
        llColorView.setVisibility(View.VISIBLE);

    }

    public class MainThread extends Thread {
        public MainThread() {
            PreferenceHelper.setBoolean("threadFlag", false);
        }

        @Override
        public void run() {
            super.run();
            while (!PreferenceHelper.getBoolean("threadFlag", false)) {
                boolean isOpen = PreferenceHelper.getBoolean("isOpen", false);
                boolean resetFlag = PreferenceHelper.getBoolean("resetFlag", false);
                boolean isDelayClosed = PreferenceHelper.getBoolean("is_delay_closed", false);
                D.d("---------isOpen----" + isOpen + "  resetFlag: " + resetFlag);
                try {
                    if (isOpen && !isDelayClosed) {
                        long current = PreferenceHelper.getLong("frequence", 0);
                        if (current != 0 && resetFlag) {
                            D.d("-------------" + current);
                            CameraInit.getInstance().turnOn();
//                                setFlightSOSState(true);
                            Thread.sleep(current);
//                                setFlightSOSState(false);
                            CameraInit.getInstance().turnOff();
                            Thread.sleep(100);
                        } else {
                            Thread.sleep(100);
                        }
                    } else {
                        if (PreferenceHelper.getBoolean("thread_open", false)) {
                            CameraInit.getInstance().turnOff();
                        }
                        Thread.sleep(100);
                    }
                } catch (Exception e) {
                    PreferenceHelper.setBoolean("threadFlag", true);
                }
            }
        }
    }

    private void loadMainAd() {
        AdSdk.shared(this).loadAd(this, new AdSdk.AdRequest.Builder(this, "main_page")
                        .setContainer(mainAdContainer)
                        .setClinkNativeOfferer(new AdSdk.ClinkNativeOfferer() {
                            @Override
                            public ClinkNativeOffer offer() {
                                try {
                                    return null;
                                } catch (Exception e) {
                                    return null;
                                }
                            }
                        })
                        .setClinkCustomOnClick(true)
                        .setAdNativeViewBuilder(new AdNativeViewIdBuilder(this)
                                .setLayoutId(R.layout.layout_main_ad)
                                .setIconViewId(R.id.iv_ad_icon)
                                .setTitleViewId(R.id.tv_ad_title)
                                .setBodyViewId(R.id.tv_subtitle)
                                .setCallToActionViewId(R.id.tv_ad_btn))
//                                .setImageViewId(R.id.iv3_ad_img1)
//                                .setAdmobMediaViewId(R.id.ad_media_view_admob))
//                                .setAdChoicesPanelId(R.id.ad_choices_panel)
//                                .setPrivacyViewId(R.id.ad_privacy_view)
//                                .setMopubPrivacyViewId(R.id.ad_mopub_privacy_view))
                        .build()
                , new AdListenerBase<Ad>() {
                    @Override
                    public void onLoaded(Ad ad) {
                        mainAdContainer.setVisibility(View.VISIBLE);
                        D.d("onLoaded");
                    }

                    @Override
                    public void onClicked(Ad ad) {
                    }

                    @Override
                    public void onFailed(Ad ad, int code, String msg, Object err) {
                        super.onFailed(ad, code, msg, err);
                        mainAdContainer.setVisibility(View.GONE);
                        D.d("  onFailed" + "   msg:  " + msg);
                    }

                    @Override
                    public void onShown(Ad ad) {
                        super.onShown(ad);
                    }

                    @Override
                    public void onDismissed(Ad ad) {
                        super.onDismissed(ad);
                    }
                });

    }

    private void PreloadAd() {
        AdSdk.shared(this).preloadAd(this, new AdSdk.AdRequest.Builder(this, "thread_list")
                .setDestroyOnDetach(false)
                .build(), null);
    }

    private void showIntivinal(String placement) {

        AdSdk.shared(this).loadAd(this, new AdSdk.AdRequest.Builder(this, placement)
                .setDestroyOnDetach(false)
                .setAddBannerToParentOnLoaded(false)
                .setClearParentOnAdd(false)
                .setAddNativeToParentOnLoaded(false)
                .setMakeParentVisibleOnAdd(false)
                .build(), new AdListenerBase<Ad>() {
            @Override
            public void onFailed(Ad ad, int code, String msg, Object err) {
                super.onFailed(ad, code, msg, err);
                D.d("err:" + err);
                D.d("ad:" + ad);
                D.d("code:" + code);
                D.d("msg:" + msg);
            }

            @Override
            public void onLoaded(Ad ad) {
                D.d("ad:" + ad);
                if (ad instanceof AdInterstitial) {
                    ((AdInterstitial) ad).show();
                }
            }

            @Override
            public void onClicked(Ad ad) {
            }

            @Override
            public void onDismissed(Ad ad) {
                super.onDismissed(ad);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        D.i("wwq", "onResume");
        isSetClick = false;
        ivSettingView.setVisibility(View.VISIBLE);
        ivColorView.setVisibility(View.VISIBLE);
//        PreloadAd();
        loadMainAd();
//        PreferenceHelper.setBoolean("isOpen", false);
//        PreferenceHelper.setBoolean("resetFlag", true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isSetClick) {
            Log.d("wwq", "onPause...");
            ivOpen.setImageResource(R.drawable.close_btn_b_199);
            ivLightShow.setVisibility(View.INVISIBLE);
            CameraInit.getInstance().turnOff();
            CameraInit.getInstance().releasea();
            PreferenceHelper.setBoolean("threadFlag", true);
            PreferenceHelper.setBoolean("isOpen", false);
            handler.removeCallbacksAndMessages(null);
        }

//        PreferenceHelper.setBoolean("threadFlag", true);
//        PreferenceHelper.setBoolean("resetFlag", false);
//        PreferenceHelper.setBoolean("isOpen", false);
//        ivLightShow.setVisibility(View.GONE);
//        ivOpen.setImageResource(R.drawable.close_btn_b_199);
//        CameraInit.getInstance().releasea();
    }

//    private void reOpenLight() {
//        if (btnOpen) {
//            ivOpen.setImageResource(R.drawable.open_btn_b_199);
//            ivLightShow.setVisibility(View.VISIBLE);
//            PreferenceHelper.setBoolean("isOpen", true);
//            CameraInit.getInstance().turnOn();
//            if (PreferenceHelper.getBoolean("threadFlag", false)) {
//                PreferenceHelper.setBoolean("threadFlag", false);
//                new MainThread().start();
//            }
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            isScreenBrightDialogShowed = false;
            brightNess = PreferenceHelper.getInt("screen_brightness", 12);
            Settings.System.putInt(getContentResolver(), "screen_brightness", brightNess);
            attributes.screenBrightness = brightNess;
            window.setAttributes(attributes);
            CameraInit.getInstance().releasea();
            PreferenceHelper.setLong("frequence", 0);
            PreferenceHelper.setBoolean("threadFlag", true);
            PreferenceHelper.setBoolean("isOpen", false);
            handler.removeCallbacksAndMessages(null);
            CameraInit.getInstance().releaseWakeLoack();
            unregisterReceiver(broad);
        } catch (Exception e) {
        }
    }

    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                CameraInit.getInstance().camera.setPreviewDisplay(holder);
            } catch (Throwable t) {
                Log.e("wwq",
                        "Exception in setPreviewDisplay():  " + t);
            }
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = CameraInit.getInstance().camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

}