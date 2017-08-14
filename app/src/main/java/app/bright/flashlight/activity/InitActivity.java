package app.bright.flashlight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.solid.ad.Ad;
import com.solid.ad.AdListenerBase;
import com.solid.ad.AdNative;
import com.solid.ad.AdSdk;
import com.solid.ad.AdUnit;
import com.solid.ad.view.AdNativeViewIdBuilder;
import com.solid.analytics.AnalyticsUtil;
import com.solid.news.util.L;
import com.solid.news.util.Util;

import java.lang.ref.WeakReference;

import app.bright.flashlight.MyApp;
import app.bright.flashlight.R;
import app.bright.flashlight.base.BaseActivity;
import app.bright.flashlight.constant.Config;
import app.bright.flashlight.constant.Event;
import app.bright.flashlight.util.D;
import app.bright.flashlight.util.PreferenceHelper;
import app.bright.flashlight.util.Utils;
import app.bright.flashlight.view.CountDownView;

/**
 * Created by caosc on 2017/7/20.
 */

public class InitActivity extends BaseActivity {

    private static final int SHOW_TIME = 500;// 最小显示时间
    private static final int SHOW_WITH_AD_TIME = 5000;// 最小显示时间

    private RelativeLayout rlAds;
    private CountDownView countDownView;
    private boolean isImageLoaded;
    private boolean isEndCallRemue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        initView();
        long lastAdTime = PreferenceHelper.getLong(Config.LAST_START_PAGE_AD_SHOW_TIME, 0);

        if ((System.currentTimeMillis() - lastAdTime) < 3 * 60 * 1000) {
            mMainHandler = new MyHandler(this);
            mMainHandler.sendEmptyMessageDelayed(0, SHOW_TIME);
        } else {
            mMainHandler = new MyHandler(this);
            mMainHandler.postDelayed(finishRun, SHOW_WITH_AD_TIME);
            initData();
        }
    }


    private Runnable finishRun = new Runnable() {
        @Override
        public void run() {
            goHomeActivity();
        }
    };

    private void removeHandler() {
        try {
            mMainHandler.removeCallbacks(finishRun);
        } catch (Exception e) {

        }
    }

    private void initData() {
        L.d("ywc", "load start page");

        countDownView.setCallBack(new CountDownView.EndCallBack() {
            @Override
            public void endCallBack() {
                L.d("ywc", "endCallBack");
                if (isEndCallRemue) {
                    goHomeActivity();
                }

            }
        });
        countDownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                L.d("ywc", "onClick:");
                if (!countDownView.isCloseDisabled()) {
                    goHomeActivity();
                }
            }
        });
        L.d("ywc", "loadNativeAdView");

        loadAd();
    }

    void loadAd() {
        L.d("load ad");
        AdSdk.shared(this).loadAd(this, createAdRequest(), new AdListenerBase<Ad>() {
            @Override
            public void onFailed(Ad ad, int code, String msg, Object err) {
                super.onFailed(ad, code, msg, err);
            }

            @Override
            public void onLoaded(Ad ad) {
                L.d("init onLoaded");
                AdUnit a = AdSdk.fetchAd(ad);
                if (a instanceof AdNative && rlAds != null) {
                    try {
                        Button closeButton = (Button) rlAds.findViewById(R.id.no_btn);
                        closeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                goHomeActivity();
                            }
                        });
                    } catch (Exception e) {

                    }
                }

                removeHandler();
                PreferenceHelper.setLong(Config.LAST_START_PAGE_AD_SHOW_TIME, System.currentTimeMillis());
                countDownView.start();
                countDownView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onClicked(Ad ad) {
                L.d("init onClicked");
                goHomeActivity();
            }


        });
    }

    AdSdk.AdRequest createAdRequest() {
        final ViewGroup parent = rlAds;
        AdSdk.AdRequest adRequest = new AdSdk.AdRequest.Builder(this, Config.Placement.START_PAGE_AD)
                .setContainer(parent)
                .setSize(Util.getScreenWidthDP() - 30, 250)
                .setAdNativeViewBuilder(new AdNativeViewIdBuilder(this)
                        .setLayoutId(R.layout.layout_start_page_ad)
                        .setIconViewId(R.id.ad_icon_img)
                        .setTitleViewId(R.id.ad_title_text)
                        .setBodyViewId(R.id.ad_subtitle_text)
                        .setCallToActionViewId(R.id.ad_cta_text)
                        .setImageViewId(R.id.ad_big_img)
                        .setAdmobMediaViewId(R.id.ad_fb_privacy_icon)
                        .setFacebookMediaViewId(R.id.ad_fb_media_view)
                        .setPrivacyViewId(R.id.ad_fb_privacy_icon))
                .build();

        return adRequest;
    }


    private void initView() {
        rlAds = (RelativeLayout) findViewById(R.id.rl_Ads);
        countDownView = (CountDownView) findViewById(R.id.countdown_view);
        countDownView.setProperty(Color.BLACK, Color.parseColor("#FFCE13"), Util.dip2px(10), 7000);
        countDownView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        isEndCallRemue = true;
        int netStatus = Utils.getNetStatus();
        AnalyticsUtil.sendEventSimple(Event.USER_NETWORK_STATUS, "", netStatus);
        D.i("eventLog", Event.USER_NETWORK_STATUS + "  " + netStatus);
        int packFlag = Utils.getPackSign();
        AnalyticsUtil.sendEventSimple(Event.USER_PHONE_STATUS, "", packFlag);
        D.i("eventLog", Event.USER_PHONE_STATUS + "  " + packFlag);
    }


    @Override
    public void onPause() {
        super.onPause();
        isEndCallRemue = false;
    }

    @Override
    protected void onDestroy() {
        try {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        } catch (Exception e) {

        }
        super.onDestroy();
    }

    private Handler mMainHandler;

    private static class MyHandler extends Handler {
        WeakReference<InitActivity> out;

        MyHandler(InitActivity target) {
            out = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            InitActivity target = out.get();
            if (target == null) return;
            target.goHomeActivity();
        }
    }

    private void goHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
