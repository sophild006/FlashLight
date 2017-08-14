package app.bright.flashlight.constant;

/**
 * Created by Administrator on 2017/7/17.
 */

public class Config {
    public static boolean DEBUG = false;

    public static long TEN_SECENDS = 1000 * 10;
    public static long TWENTY_SECENDS = 1000 * 20;
    public static long ONE_MINUTE = 1000 * 60;
    public static long THREE_MINUTES = 1000 * 60 * 3;
    public static long FIVE_MINUTES = 1000 * 60 * 5;
    public static long TEN_MINUTES = 1000 * 60 * 10;
    public static long THIRTY_MINUTES = 1000 * 60 * 30;
    public static final String FEEDBOOK_SEND_URL = "http://aconf.cloudzad.com/apps/feedback/add";
    public static final String url = "http://cloudzad.com/apps/update?chl=GP&pack=";//访问网络下载地址的url

    public class Placement {
        public static final String START_PAGE_AD = "start_page";
        public static final String UNLOCK_CLEAN_AD = "clean";
        public static final String NEWS_LOCK_AD = "news_lock";
        public static final String WIFI_OPT_AD = "wifi_optmize";
    }

    public class Event {
        public static final String CALL_GUIDE_SHOW = "Call_guide_show";//新手引导页展示次数
        public static final String CALL_GUIDE_ALL = "Call_guide_all";//引导页全选
        public static final String CALL_GUIDE_NONE = "Call_guide_None";//引导页全不选
    }

    public static final String CONFIG_URL = "http://config.cloudzad.com/v1/config";
    public static final String NEWS_TOKEN = "a7b3a013-42b9-25e3-4152-39dc6fb24aba";
    public static final String PUBID = "flash_light";

    public static final String START_OPEN_PERMISSION_FIRST = "start_open_permission_first";
    public static final String PERMISSION_BUTTON_CLICK = "permission_button_clicked";
    public static final String LAST_START_PAGE_AD_SHOW_TIME = "last_start_page_ad_show_time";

    public static final String[] TRACKING_WHITE_LIST = {
            "guide_click_lockclean_show",
            "show_sdk_ad_after_delay_time"
    };

    public static String FACEBOOK = "com.facebook.katana";
    public static String GOOGLEPLAY = "com.android.vending";

    public static String OPEN_TAG = "open_tag";
}
