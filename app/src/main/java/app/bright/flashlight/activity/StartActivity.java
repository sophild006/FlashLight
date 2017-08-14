package app.bright.flashlight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.solid.analytics.AnalyticsUtil;
import com.solid.lock.init.ScreenLock;
import com.solid.news.util.Util;

import app.bright.flashlight.MyApp;
import app.bright.flashlight.R;
import app.bright.flashlight.base.BaseActivity;
import app.bright.flashlight.constant.Config;
import app.bright.flashlight.constant.Event;
import app.bright.flashlight.util.D;
import app.bright.flashlight.util.PreferenceHelper;

/**
 * Created by caosc on 2017/7/20.
 */

public class StartActivity extends BaseActivity {
    private TextView tvOpen;
    private CheckBox cbAgree;
    private TextView tvPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_open_permission);
        tvOpen = (TextView) findViewById(R.id.tv_start);
        cbAgree = (CheckBox) findViewById(R.id.checkbox_agree);
        tvPrivacy = (TextView) findViewById(R.id.tv_privacy);

        AnalyticsUtil.sendEventSimple(Event.GUIDE_PAGE_SHOW);
        D.i("eventLog", Event.GUIDE_PAGE_SHOW + "  " );
        cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbAgree.setChecked(isChecked);
            }
        });

        tvOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceHelper.setBoolean(Config.PERMISSION_BUTTON_CLICK, true);
                sendUnMeng();
                PreferenceHelper.setBoolean(Config.START_OPEN_PERMISSION_FIRST, true);
                if (cbAgree.isChecked()) {
                    ScreenLock.getInstance().setIsUsingClean(true);
                    ScreenLock.getInstance().setIsUsingLock(true);
                    ScreenLock.getInstance().setIsUsingWifiCheck(true);
                    AnalyticsUtil.sendEventSimple(Event.GUIDE_PAGE_SHOW_ALL);
                    D.i("eventLog", Event.GUIDE_PAGE_SHOW_ALL + "  " );
                } else {
                    ScreenLock.getInstance().setIsUsingClean(false);
                    ScreenLock.getInstance().setIsUsingLock(false);
                    ScreenLock.getInstance().setIsUsingWifiCheck(false);
                    AnalyticsUtil.sendEventSimple(Event.GUIDE_PAGE_SHOW_NONE);
                    D.i("eventLog", Event.GUIDE_PAGE_SHOW_NONE + "  " );

                }
                startMainAc();
            }
        });

        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, TermsActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        boolean aBoolean = PreferenceHelper.getBoolean(Config.START_OPEN_PERMISSION_FIRST, false);
        if (aBoolean) {
            startInitAc();
            return;
        }
        MyApp.onSendEvent(Config.Event.CALL_GUIDE_SHOW, null, null);
    }

    public void startMainAc() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void startInitAc() {
        Intent intent = new Intent(this, InitActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendUnMeng() {
        if (cbAgree.isChecked()) {
            MyApp.onSendEvent(Config.Event.CALL_GUIDE_ALL, null, null);
        } else {
            MyApp.onSendEvent(Config.Event.CALL_GUIDE_NONE, null, null);
        }
    }
}
