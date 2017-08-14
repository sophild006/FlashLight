package app.bright.flashlight.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.solid.gamesdk.activity.GameActivity;

import app.bright.flashlight.R;
import app.bright.flashlight.activity.NewsActivity;
import app.bright.flashlight.constant.Config;

/**
 * Created by caosc on 2017/7/20.
 */

public class ShortcutNewsUtils {
    // Action 添加Shortcut
    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    // Action 移除Shortcut
    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";

    public static final String ShortcutName = "Top News";

    public static final Context context = GlobalContext.getAppContext();

    public static Intent shortcutActionIntent;


    /**
     * 添加快捷方式
     */
    public static void addShortcut() {

        Intent intent = new Intent(ACTION_ADD_SHORTCUT);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.mipmap.icon_img_b));
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, getShortcutActionIntent());
        intent.putExtra("duplicate", false);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, ShortcutName);
        context.sendBroadcast(intent);

    }

    public static boolean isShortCutExist() {
        boolean result = false;
        try {
            ContentResolver cr = context.getContentResolver();
            Uri uri = getUriFromLauncher();
            Cursor c = cr.query(uri, new String[]{"title", "intent"}, "title=?  and intent=?",
                    new String[]{ShortcutName, getShortcutActionIntent().toUri(0)}, null);
            if (c != null && c.getCount() > 0) {
                result = true;
            }
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (Exception ex) {
            result = false;
            ex.printStackTrace();
        }
        return result;
    }

    private static Uri getUriFromLauncher() {
        StringBuilder uriStr = new StringBuilder();
        String authority = LauncherUtil.getAuthorityFromPermissionDefault(ShortcutNewsUtils.context);
        if (authority == null || authority.trim().equals("")) {
            authority = LauncherUtil.getAuthorityFromPermission(ShortcutNewsUtils.context, LauncherUtil.getCurrentLauncherPackageName(ShortcutNewsUtils.context) + ".permission.READ_SETTINGS");
        }
        uriStr.append("content://");
        if (TextUtils.isEmpty(authority)) {
            int sdkInt = android.os.Build.VERSION.SDK_INT;
            if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
                uriStr.append("com.android.launcher.settings");
            } else if (sdkInt < 19) {// Android 4.4以下
                uriStr.append("com.android.launcher2.settings");
            } else {// 4.4以及以上
                uriStr.append("com.android.launcher3.settings");
            }
        } else {
            uriStr.append(authority);
        }
        uriStr.append("/favorites?notify=true");
        return Uri.parse(uriStr.toString());
    }

    /**
     * 移除快捷方式
     */
    public static void removeShortcut() {
        Intent intent = new Intent(ACTION_REMOVE_SHORTCUT);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, ShortcutName);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.putExtra("duplicate", false);
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, getShortcutActionIntent());
        GlobalContext.getAppContext().sendBroadcast(intent);
    }

    public static Intent getShortcutActionIntent() {
        if (shortcutActionIntent == null) {
            shortcutActionIntent = new Intent();
            shortcutActionIntent.setClassName(context, NewsActivity.class.getName());
            shortcutActionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shortcutActionIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            shortcutActionIntent.setAction("com.top.news.action");
        }
        return shortcutActionIntent;
    }

}
