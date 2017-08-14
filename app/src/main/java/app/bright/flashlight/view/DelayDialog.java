package app.bright.flashlight.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import app.bright.flashlight.R;
import app.bright.flashlight.constant.Config;
import app.bright.flashlight.util.PreferenceHelper;


/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    : mist
 * Author     : scotch
 * Date       : 2016/7/8 16:32
 */
public class DelayDialog extends Dialog implements View.OnClickListener {

    private ImageView ivTenSecends, ivTwentSecends;
    private ImageView ivOneM, ivthreeM, ivfiveM, ivtenM, ivthirtyM, ivNever;

    public DelayDialog(Context context) {
        super(context, R.style.CustomDialog);
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.layout_auto_choice, null);
        super.setContentView(mView);
        ivTenSecends = (ImageView) mView.findViewById(R.id.iv_ten_secends);
        ivTwentSecends = (ImageView) mView.findViewById(R.id.iv_twenty_secends);
        ivOneM = (ImageView) mView.findViewById(R.id.iv_one_minute);
        ivthreeM = (ImageView) mView.findViewById(R.id.iv_three_minute);
        ivfiveM = (ImageView) mView.findViewById(R.id.iv_five_minute);
        ivtenM = (ImageView) mView.findViewById(R.id.iv_ten_minute);
        ivthirtyM = (ImageView) mView.findViewById(R.id.iv_thirty_minute);
        ivNever = (ImageView) mView.findViewById(R.id.iv_never);

        ivTenSecends.setOnClickListener(this);
        ivTwentSecends.setOnClickListener(this);
        ivOneM.setOnClickListener(this);
        ivthreeM.setOnClickListener(this);
        ivfiveM.setOnClickListener(this);
        ivtenM.setOnClickListener(this);
        ivthirtyM.setOnClickListener(this);
        ivNever.setOnClickListener(this);
        initRes();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Intent intent=new Intent();
        intent.setAction("com.delay.close.flash");
        intent.putExtra("type",0);
        getContext().sendBroadcast(intent);
    }

    private void initRes() {
        int defaultFlag = PreferenceHelper.getInt("isChoosed", -1);
        resetImg();
        switch (defaultFlag) {
            case 0:
                ivTenSecends.setImageResource(R.drawable.choose_btn_b_50);
                break;
            case 1:
                ivTwentSecends.setImageResource(R.drawable.choose_btn_b_50);
                break;
            case 2:
                ivOneM.setImageResource(R.drawable.choose_btn_b_50);
                break;
            case 3:
                ivthreeM.setImageResource(R.drawable.choose_btn_b_50);
                break;
            case 4:
                ivfiveM.setImageResource(R.drawable.choose_btn_b_50);
                break;
            case 5:
                ivtenM.setImageResource(R.drawable.choose_btn_b_50);
                break;
            case 6:
                ivthirtyM.setImageResource(R.drawable.choose_btn_b_50);
                break;
            case 7:
                ivNever.setImageResource(R.drawable.choose_btn_b_50);
                break;
            default:
                PreferenceHelper.setLong("delay_light_off", -1);
                ivNever.setImageResource(R.drawable.choose_btn_b_50);
                break;

        }
    }


    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

    /**
     * 确定键监听器
     *
     * @param listener
     */
    public void setDismisListener(View.OnClickListener listener) {
    }

    /**
     * 取消键监听器
     *
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener) {
    }

    @Override
    public void onClick(View v) {
        resetImg();
        switch (v.getId()) {
            case R.id.iv_ten_secends:
                PreferenceHelper.setLong("delay_light_off", Config.TEN_SECENDS);
                PreferenceHelper.setInt("isChoosed", 0);
                ivTenSecends.setImageResource(R.drawable.choose_btn_b_50);
                break;
            case R.id.iv_twenty_secends:
                PreferenceHelper.setLong("delay_light_off", Config.TWENTY_SECENDS);
                ivTwentSecends.setImageResource(R.drawable.choose_btn_b_50);
                PreferenceHelper.setInt("isChoosed", 1);
                break;
            case R.id.iv_one_minute:
                PreferenceHelper.setLong("delay_light_off", Config.ONE_MINUTE);
                ivOneM.setImageResource(R.drawable.choose_btn_b_50);
                PreferenceHelper.setInt("isChoosed", 2);
                break;
            case R.id.iv_three_minute:
                PreferenceHelper.setLong("delay_light_off", Config.THREE_MINUTES);
                ivthreeM.setImageResource(R.drawable.choose_btn_b_50);
                PreferenceHelper.setInt("isChoosed", 3);
                break;
            case R.id.iv_five_minute:
                PreferenceHelper.setLong("delay_light_off", Config.FIVE_MINUTES);
                ivfiveM.setImageResource(R.drawable.choose_btn_b_50);
                PreferenceHelper.setInt("isChoosed", 4);
                break;
            case R.id.iv_ten_minute:
                PreferenceHelper.setLong("delay_light_off", Config.TEN_MINUTES);
                ivtenM.setImageResource(R.drawable.choose_btn_b_50);
                PreferenceHelper.setInt("isChoosed", 5);
                break;
            case R.id.iv_thirty_minute:
                PreferenceHelper.setLong("delay_light_off", Config.THIRTY_MINUTES);
                ivthirtyM.setImageResource(R.drawable.choose_btn_b_50);
                PreferenceHelper.setInt("isChoosed", 6);
                break;
            case R.id.iv_never:
                PreferenceHelper.setLong("delay_light_off", -1);
                ivNever.setImageResource(R.drawable.choose_btn_b_50);
                PreferenceHelper.setInt("isChoosed", 7);
                break;
        }
        this.dismiss();
    }

    private void resetImg() {
        ivTenSecends.setImageResource(R.drawable.choose_btn_g_50);
        ivTwentSecends.setImageResource(R.drawable.choose_btn_g_50);
        ivOneM.setImageResource(R.drawable.choose_btn_g_50);
        ivthreeM.setImageResource(R.drawable.choose_btn_g_50);
        ivfiveM.setImageResource(R.drawable.choose_btn_g_50);
        ivtenM.setImageResource(R.drawable.choose_btn_g_50);
        ivthirtyM.setImageResource(R.drawable.choose_btn_g_50);
        ivNever.setImageResource(R.drawable.choose_btn_g_50);
    }

}
