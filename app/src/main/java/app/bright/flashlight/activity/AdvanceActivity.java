package app.bright.flashlight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.solid.lock.init.ScreenLock;
import com.solid.news.sdk.NewsSdk;

import app.bright.flashlight.R;
import app.bright.flashlight.base.BaseActivity;
import app.bright.flashlight.util.ShortcutGifUtils;
import app.bright.flashlight.util.ShortcutNewsUtils;

/**
 * Created by Administrator on 2017/7/21.
 */

public class AdvanceActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivMemory, ivCharge, ivWifiOptimzer, ivWifiQuick;
    private RelativeLayout rlNewsShort, rlGameShort;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layou_advanced_set);
        initView();
    }

    private void initView() {
        ivMemory = (ImageView) findViewById(R.id.iv_memory_choice);
        ivCharge = (ImageView) findViewById(R.id.iv_charge_choice);
        ivWifiOptimzer = (ImageView) findViewById(R.id.iv_wifi_optimizer);
        ivWifiQuick = (ImageView) findViewById(R.id.iv_wifi_quick_choice);
        rlNewsShort = (RelativeLayout) findViewById(R.id.rl_create_news_short);
        rlGameShort = (RelativeLayout) findViewById(R.id.rl_create_game_short);
        initEvent();
    }

    private void initEvent() {
        ivMemory.setOnClickListener(this);
        ivCharge.setOnClickListener(this);
        ivWifiOptimzer.setOnClickListener(this);
        ivWifiQuick.setOnClickListener(this);
        rlNewsShort.setOnClickListener(this);
        rlGameShort.setOnClickListener(this);
        if (ScreenLock.getInstance().getIsUsingClean()) {
            ivMemory.setImageResource(R.drawable.open_btn_b_71);
        } else {
            ivMemory.setImageResource(R.drawable.close_btn_g_71);
        }
        if (ScreenLock.getInstance().getIsUsingLock()) {
            ivCharge.setImageResource(R.drawable.open_btn_b_71);
        } else {
            ivCharge.setImageResource(R.drawable.close_btn_g_71);
        }
        if (ScreenLock.getInstance().getIsUsingWifiCheck()) {
            ivWifiOptimzer.setImageResource(R.drawable.open_btn_b_71);
        } else {
            ivWifiOptimzer.setImageResource(R.drawable.close_btn_g_71);
        }
        boolean isUsingQuickView = NewsSdk.getInstance().getIsUsingQuickView();
        if (isUsingQuickView) {
            ivWifiQuick.setImageResource(R.drawable.open_btn_b_71);
        } else {
            ivWifiQuick.setImageResource(R.drawable.close_btn_g_71);
        }
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_memory_choice:
                boolean isUseClean = ScreenLock.getInstance().getIsUsingClean();
                if (isUseClean) {
                    ivMemory.setImageResource(R.drawable.close_btn_g_71);
                    ScreenLock.getInstance().setIsUsingClean(false);
                } else {
                    ivMemory.setImageResource(R.drawable.open_btn_b_71);
                    ScreenLock.getInstance().setIsUsingClean(true);
                }
                break;
            case R.id.iv_charge_choice:
                boolean isUsingLock = ScreenLock.getInstance().getIsUsingLock();
                if (isUsingLock) {
                    ScreenLock.getInstance().setIsUsingLock(false);
                    ivCharge.setImageResource(R.drawable.close_btn_g_71);
                } else {
                    ivCharge.setImageResource(R.drawable.open_btn_b_71);
                    ScreenLock.getInstance().setIsUsingLock(true);
                }
                break;
            case R.id.iv_wifi_optimizer:
                boolean isUsingWifiCheck = ScreenLock.getInstance().getIsUsingWifiCheck();
                if (isUsingWifiCheck) {
                    ScreenLock.getInstance().setIsUsingWifiCheck(false);
                    ivWifiOptimzer.setImageResource(R.drawable.close_btn_g_71);
                } else {
                    ivWifiOptimzer.setImageResource(R.drawable.open_btn_b_71);
                    ScreenLock.getInstance().setIsUsingWifiCheck(true);
                }
                break;
            case R.id.iv_wifi_quick_choice:
                boolean isUsingQuickView = NewsSdk.getInstance().getIsUsingQuickView();
                NewsSdk.getInstance().setIsUsingQuickView(!isUsingQuickView);
                if (isUsingQuickView) {
                    ivWifiQuick.setImageResource(R.drawable.close_btn_g_71);
                } else {
                    ivWifiQuick.setImageResource(R.drawable.open_btn_b_71);
                }
                break;
            case R.id.rl_create_news_short:
                ShortcutNewsUtils.removeShortcut();
                ShortcutNewsUtils.addShortcut();
                showSnackBar("Create Top News ShortCut Success");
                break;
            case R.id.rl_create_game_short:
                ShortcutGifUtils.removeShortcut();
                ShortcutGifUtils.addShortcut();
                showSnackBar("Create Slot ShortCut Success");
                break;
        }

    }
    private void showSnackBar(String msg) {
        Snackbar make = Snackbar.make(getContatiner(), msg, Snackbar.LENGTH_LONG);
        View view = make.getView();
        TextView mesage = (TextView) view.findViewById(R.id.snackbar_text);
        mesage.setTextColor(getResources().getColor(R.color.white));
        mesage.setGravity(Gravity.CENTER);
        make.show();
    }
    @Override
    protected void onPause() {
        super.onPause();
//        Intent intent = new Intent("com.delay.close.flash");
//        intent.putExtra("type", 1);
//        sendBroadcast(intent);
//        finish();
    }
}
