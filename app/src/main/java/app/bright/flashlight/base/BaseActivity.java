package app.bright.flashlight.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */

public class BaseActivity extends AppCompatActivity {
    public static List<BaseActivity> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(0);
        }
        addList(this);
    }

    public void addList(BaseActivity baseActivity) {
        mList.add(baseActivity);
    }

    public static void reMoveList() {
        for (BaseActivity activity : mList) {
            activity.finish();
        }
        mList.clear();
    }

    public View getContatiner() {
        return findViewById(android.R.id.content);
    }
}
