package app.bright.flashlight;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.support.multidex.MultiDexApplication;

import com.google.android.gms.cover.CoverSdk;
import com.probe.ProbeSdk;
import com.solid.ad.AdSdk;
import com.solid.analytics.Analytics;
import com.solid.analytics.AnalyticsUtil;
import com.solid.common.CommonSdk;
import com.solid.gamesdk.activity.GameActivity;
import com.solid.gamesdk.init.GameSdk;
import com.solid.gamesdk.logic.SettingMgr;
import com.solid.gamesdk.util.L;
import com.solid.lock.init.ScreenLock;
import com.solid.lock.util.DevicesUtils;
import com.solid.news.sdk.NewsSdk;
import com.solid.news.util.Util;
import com.solid.resident.ResidentSdk;
import com.solid.util.LoggerUtil;

import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import app.bright.flashlight.activity.NewsActivity;
import app.bright.flashlight.constant.Config;
import app.bright.flashlight.constant.Event;
import app.bright.flashlight.util.D;
import app.bright.flashlight.util.GlobalContext;
import app.bright.flashlight.util.PreferenceHelper;
import app.bright.flashlight.util.ShortcutGifUtils;
import app.bright.flashlight.util.ShortcutNewsUtils;
import app.bright.flashlight.util.ThreadManager;
import app.bright.flashlight.util.Utils;

/**
 * Created by Administrator on 2017/7/17.
 */

public class MyApp extends MultiDexApplication implements Analytics.Interceptor {


    List<String> mWhiteList;

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalContext.setAppContext(this);
        setDefaultUncaughtExceptionHandler();
        initTracking();
        initResident();
        initLockSdk();
        initNewsSdk();
        initAds();
        initCoverSdk();
        initGameSdk();
        ShortcutNewsUtils.removeShortcut();
        ShortcutNewsUtils.addShortcut();
        ShortcutGifUtils.removeShortcut();
        ShortcutGifUtils.addShortcut();

        final String TAG = "MyApp";


    }

    private void initTracking() {
        mWhiteList = Arrays.asList(Config.TRACKING_WHITE_LIST);
        try {
            Analytics.shared(this)
                    .init(new Analytics.Configuration.Builder()
                            .setAnalyticsUrl("http://api.solidtracking.com")
                            .setChannel("gp")
                            .setUmengAppKey("5971f214ae1bf84373000e10")
                            .setBuglyAppId("576136db4e")
                            .setFirebaseEnable(true)
                            .setBuglyDebugMode(false)
                            .setAnalyticInterceptor(this)
                            .setUploadAppsInfo(false)
                            .setCategoryCanBeEmpty(true)
                            .setActionCanBeEmpty(false)
                            .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initResident() {
        ResidentSdk.init(this, new ResidentSdk.Configuration.Builder()
                .setScheduleResidentServiceInterval(1000 * 40L)
                .setScheduleResidentReceiverInterval(1000 * 30L)
                .setScheduleResidentDaemonInterval(1000 * 30L)
                .setScheduleResidentJobServiceInterval(5000L)
                .build());
    }

    private void initLockSdk() {
        try {
            ScreenLock.getInstance().setSocket(new DatagramSocket());
        } catch (Exception e) {

        }
        ScreenLock.getInstance()
                .initContext(this)
                .log(false)
                .token(Config.NEWS_TOKEN)
                .pubid(Config.PUBID)
                .lockName("FlashLight")
                .isHaveSlots(true)
                .slotClass(GameActivity.class)
                .exitNewsDetail(new ScreenLock.ExitNewsDetailListener() {
                    @Override
                    public void exitNewsDetail() {
                        Intent intent = new Intent(MyApp.this, NewsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .report(new ScreenLock.ReportListener() {
                    @Override
                    public void sendEvent(String eventId, String key, Long value) {
                        onSendEvent(eventId, key, value);
                    }

                })
                .videoRewarded(new ScreenLock.VideoRewardedListener() {
                    @Override
                    public void videoRewarded() {
                        long currCoins = SettingMgr.getInstance().getMyCoins();
                        SettingMgr.getInstance().setMyCoins(currCoins + 20);
                    }
                })
                .build();

        try {
            ThreadManager.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        DevicesUtils.ScanArp(ScreenLock.getInstance().getSocket());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
        }
    }

    private void initNewsSdk() {
        NewsSdk.getInstance()
                .initContext(this)
                .log(false)
                .pubid(Config.PUBID)
                .token(Config.NEWS_TOKEN)
                .className(NewsActivity.class)
                .themeColor(Color.parseColor("#00b385"))
                .lineColor(Color.parseColor("#00b385"))
                .bundleKey("0")
                .bundle(new Bundle())
                .reportListener(new NewsSdk.ReportListener() {
                    @Override
                    public void sendEvent(String event, String label, Long value) {
                        onSendEvent(event, label, value);
                    }
                })
                .loadAdsCache(true);
    }


    public static void onSendEvent(String event, String label, Long value) {
        if (value == null) {
            AnalyticsUtil.sendEventSimple(event);
            L.d("tracking_test", "send event:" + event);
        } else {
            L.d("tracking_test", "send event:" + event + "      value:" + value);
            AnalyticsUtil.sendEventSimple(event, label, value);
        }
    }

    private void initCoverSdk() {
        LoggerUtil.configureLogbackDirectly(this);

        CommonSdk.init(this);


        CoverSdk.init(this, new CoverSdk.Configuration.Builder()
                .setConfigUrl(Config.CONFIG_URL)
                .setPubid(Config.PUBID)
                .build());

        ProbeSdk.init(this, new ProbeSdk.Configuration.Builder()
                .setApiUrl("http://check.22adz.com")
                .setPubId(Config.PUBID)
                .setChannel(Config.PUBID)
                .build(), new ProbeSdk.AnalyticsProvider() {
            @Override
            public void onEvent(String eventId, String label, Long value, Map<String, Object> params) {
                AnalyticsUtil.sendEventSimple(eventId, label, value, params);
            }
        });

    }

    private void initAds() {
        AdSdk.shared(this).init(new AdSdk.Configuration.Builder()
                .setAnalyticsProvider(new AdSdk.AnalyticsProvider() {
                    @Override
                    public void onEvent(String action, Map<String, Object> params) {
                        L.d("tracking_test", action);
                        AnalyticsUtil.sendEventSimple(action, null, null, params);
                    }
                })
                .setConfigUrl(Config.CONFIG_URL)
                .setConfigPubid(Config.PUBID)
                .build());

        AdSdk.shared(this).setPlacementAutoCacheAdRequest(new AdSdk.AdRequest.Builder(this,
                Config.Placement.START_PAGE_AD)
                .setSize(Util.getScreenWidthDP() - 30, 250)
                .build()
        );

        AdSdk.shared(this).setPlacementAutoCacheAdRequest(new AdSdk.AdRequest.Builder(this,
                Config.Placement.UNLOCK_CLEAN_AD)
                .setSize(Util.getScreenWidthDP() >= 324 ? 324 : 320, 250)
                .build()
        );

        AdSdk.shared(this).setPlacementAutoCacheAdRequest(new AdSdk.AdRequest.Builder(this,
                Config.Placement.NEWS_LOCK_AD)
                .setSize(Util.getScreenWidthDP() >= 324 ? 324 : 320, 250)
                .build()
        );

        AdSdk.shared(this).setPlacementAutoCacheAdRequest(new AdSdk.AdRequest.Builder(this,
                Config.Placement.WIFI_OPT_AD)
                .setSize(Util.getScreenWidthDP(), 250)
                .build()
        );
    }

    //
    @Override
    public boolean sendEvent(String category, String eventId, Map<String, Object> map) {
        Log.d("wwq", "eventId: " + eventId);
        if (mWhiteList != null && mWhiteList.size() > 0) {
            if (mWhiteList.contains(eventId)) {
                L.d("tracking_test", "send to analytics server:" + eventId);
                return true;
            }
        }

        if (eventId.equals("ad_clean_adm_banner_show") &&
                PreferenceHelper.getBoolean(Config.PERMISSION_BUTTON_CLICK, false)) {
            AnalyticsUtil.sendEventSimple(Event.GUIDE_CLICK_LOCKCLEAN_SHOW);
            D.i("eventLog", Event.GUIDE_CLICK_LOCKCLEAN_SHOW + "  ");
        }


        if (eventId.contains("_click") && eventId.contains("ad")) {
            L.d("tracking_test", "send to analytics server:" + eventId);
            return true;
        }

        if (eventId.contains("_loaded") && eventId.contains("ad")) {
            L.d("tracking_test", "send to analytics server:" + eventId);
            return true;
        }

        if (eventId.contains("_show") && eventId.contains("ad")) {
            L.d("tracking_test", "send to analytics server:" + eventId);
            return true;
        }

        return false;
    }

    @Override
    public boolean onPageBegin(Object o, String s) {
        return false;
    }

    @Override
    public boolean onPageEnd(Object o) {
        return false;
    }

    @Override
    public boolean onInterfaceBegin(String s) {
        return false;
    }

    private void initGameSdk() {
        GameSdk.getInstance()
                .initContext(this)
                .log(false)
                .isHaveVip(false)
                .reportListener(new GameSdk.ReportListener() {
                    @Override
                    public void sendEvent(String eventId, String key, Long value) {
                        onSendEvent(eventId, key, value);
                    }
                });
    }

    public static void setDefaultUncaughtExceptionHandler() {
        final Thread.UncaughtExceptionHandler old = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {

                if (old != null && Config.DEBUG) {
                    old.uncaughtException(thread, ex);
                    return;
                }

                ex.printStackTrace();
                final boolean isMainThread = Utils.isMainThread(thread);
                if (isMainThread) {
                    // exit if crash on main
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(10);
                } else {
                    // do nothing if crash not on main
                }
            }
        });
    }

}
