package app.bright.flashlight.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.bright.flashlight.R;
import app.bright.flashlight.base.BaseActivity;
import app.bright.flashlight.bean.VersionBean;
import app.bright.flashlight.constant.Config;
import app.bright.flashlight.constant.Event;
import app.bright.flashlight.util.D;
import app.bright.flashlight.util.OkHttpHelper;
import app.bright.flashlight.util.PreferenceHelper;
import app.bright.flashlight.util.ThreadManager;
import app.bright.flashlight.view.DelayDialog;
import app.bright.flashlight.view.UpdateDialog;

import com.google.gson.Gson;
import com.solid.analytics.AnalyticsUtil;
import com.solid.lock.init.ScreenLock;
import com.solid.lock.util.L;

/**
 * Created by Administrator on 2017/7/17.
 */

public class SetActivity extends BaseActivity implements View.OnClickListener {

    public boolean isSetlayout;
    private RelativeLayout rlAutoTurnOff, rlFeedback, rlRate, rlUpdate, rlPrivacy,rkAdvanced;
    private ImageView ivSound;
    private ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_set);
        initView();
        try {
            versionCode=getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        rlAutoTurnOff = (RelativeLayout) findViewById(R.id.rl_auto_turn_off);
        rlFeedback = (RelativeLayout) findViewById(R.id.rl_feedback);
        rlRate = (RelativeLayout) findViewById(R.id.rl_rate);
        rlUpdate = (RelativeLayout) findViewById(R.id.rl_update);
        rlPrivacy = (RelativeLayout) findViewById(R.id.rl_privacy);
        rkAdvanced= (RelativeLayout) findViewById(R.id.rl_advanced);

        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivSound = (ImageView) findViewById(R.id.iv_sound_choice);

        initEvent();
    }

    private void initEvent() {
        rlAutoTurnOff.setOnClickListener(this);
        rlFeedback.setOnClickListener(this);
        rlRate.setOnClickListener(this);
        rlUpdate.setOnClickListener(this);
        rlPrivacy.setOnClickListener(this);
        rkAdvanced.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivSound.setOnClickListener(this);


        if (PreferenceHelper.getBoolean("sound", true)) {
            ivSound.setImageResource(R.drawable.open_btn_b_71);
        } else {
            ivSound.setImageResource(R.drawable.close_btn_g_71);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_auto_turn_off:
                showAudoDialog();
                break;
            case R.id.iv_sound_choice:
                if (PreferenceHelper.getBoolean("sound", true)) {
                    PreferenceHelper.setBoolean("sound", false);
                    ivSound.setImageResource(R.drawable.close_btn_g_71);
                    AnalyticsUtil.sendEventSimple(Event.LIGHT_AUTO_CLOSE);
                    D.i("eventLog", Event.LIGHT_AUTO_CLOSE + "  " );
                } else {
                    PreferenceHelper.setBoolean("sound", true);
                    ivSound.setImageResource(R.drawable.open_btn_b_71);
                    AnalyticsUtil.sendEventSimple(Event.LIGHT_VOICE_OPEN);
                    D.i("eventLog", Event.LIGHT_VOICE_OPEN + "  " );
                }
                break;

            case R.id.rl_feedback:
                startActivity(new Intent(this, FeedActivity.class));
                break;
            case R.id.rl_rate:
                goRateStar();
                break;
            case R.id.rl_update:
                upadteVersion();
                break;
            case R.id.rl_privacy:
                startActivity(new Intent(this, TermsActivity.class));
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_advanced:
                startActivity(new Intent(this,AdvanceActivity.class));
                break;
        }
    }

    private void goRateStar() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));
            return;
        } catch (ActivityNotFoundException e2) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            return;
        }
    }

    private void showAudoDialog() {

        final DelayDialog backdialog = new DelayDialog(this);
        backdialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backdialog.dismiss();
                finish();
            }
        });
        backdialog.getWindow().setLayout((int) getResources().getDimension(R.dimen.with),
                (int) getResources().getDimension(R.dimen.medium_hight));
        backdialog.show();
    }

    private void upadteVersion() {
        rlUpdate.setEnabled(false);
        Runnable updateRun = new Runnable() {
            @Override
            public void run() {
                OkHttpHelper.getInstance().getAsync(Config.url + getPackageName(), new OkHttpHelper.IResultCallBack() {
                    @Override
                    public void getResult(int code, String ret) {
                        rlUpdate.setEnabled(true);
                        if (code == OkHttpHelper.HTTP_FAILURE || TextUtils.isEmpty(ret)) {
                            Snackbar.make(getContatiner(), getResources().getString(R.string.netfailed), Snackbar.LENGTH_SHORT).show();

                        } else {
                            try {
                                Gson gson = new Gson();
                                VersionBean bean = gson.fromJson(ret, VersionBean.class);
                                if (bean.ret != 200) {
                                    Snackbar make = Snackbar.make(getContatiner(), getResources().getString(R.string.netfailed), Snackbar.LENGTH_SHORT);
                                    View view = make.getView();
                                    TextView mesage = (TextView) view.findViewById(R.id.snackbar_text);
                                    mesage.setTextColor(getResources().getColor(R.color.white));
                                    mesage.setGravity(Gravity.CENTER);
                                    make.show();
                                    return;
                                }
                                compareallVersions(bean.msg);
                            } catch (Exception e) {
                            }
                        }
                    }
                });
            }
        };
        ThreadManager.execute(updateRun);
    }

    private int versionCode;

    private void compareallVersions(int serverVersion) {
        if (versionCode >= serverVersion) {
            Snackbar make = Snackbar.make(getContatiner(), getResources().getString(R.string.your_current_version_is_up_to_date), Snackbar.LENGTH_SHORT);
            View view = make.getView();
            TextView mesage = (TextView) view.findViewById(R.id.snackbar_text);
            mesage.setTextColor(getResources().getColor(R.color.white));
            mesage.setGravity(Gravity.CENTER);
            make.show();
        } else {
            final UpdateDialog dialog = new UpdateDialog(this);
            dialog.setOnPositiveListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    goRateStar();
                }
            });
            dialog.setOnNegativeListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //访问网络下载更新
                    dialog.dismiss();

                }
            });
            dialog.getWindow().setLayout((int) getResources().getDimension(R.dimen.with), (int) getResources().getDimension(R.dimen.hight));
            dialog.show();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Intent intent=new Intent("com.delay.close.flash");
//        intent.putExtra("type",1);
//        sendBroadcast(intent);
//        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
