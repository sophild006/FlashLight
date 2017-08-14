package app.bright.flashlight.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.solid.news.sdk.NewsSdk;

import app.bright.flashlight.R;
import app.bright.flashlight.base.BaseActivity;

/**
 * Created by caosc on 2017/7/20.
 */

public class NewsActivity extends BaseActivity implements NewsSdk.JumpDetailListener, NewsSdk.LoadNewsListener {

    private ImageView ivBack;
    private RelativeLayout rlNews;
    private ImageView ivNoConnected;
    private long startTimes;
    private boolean isNewsSuccess = true;
    private View newsView;
    private boolean isJumpDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        isJumpDetail = false;
        initView();
        initData();
        setOnclick();
    }

    private void setOnclick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivNoConnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    private void initData() {
        newsView = NewsSdk.getInstance().getNewsView(this, this, null);
        if (rlNews != null) {
            rlNews.removeAllViews();
            rlNews.addView(newsView);
        }
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        rlNews = (RelativeLayout) findViewById(R.id.rl_news);
        ivNoConnected = (ImageView) findViewById(R.id.iv_no_connected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isJumpDetail = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isJumpDetail) {
            if (isNewsSuccess) {
                long times = (System.currentTimeMillis() - startTimes) / 1000;
//                MyApp.onSendEvent(Constants.DESKTOP_NEWS_LIST_DURATION, "duration", times);
            }
            NewsSdk.getInstance().removeAdsCache();
        }
    }

    @Override
    public void jumpDetail() {
        isJumpDetail = true;
    }

    @Override
    public void loadNewsSucc() {
        isNewsSuccess = true;
        startTimes = System.currentTimeMillis();
        ivNoConnected.setVisibility(View.GONE);
        rlNews.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadNewsError() {
        isNewsSuccess = false;
        ivNoConnected.setVisibility(View.VISIBLE);
        rlNews.setVisibility(View.GONE);
    }
}
