package app.bright.flashlight.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import app.bright.flashlight.R;
import app.bright.flashlight.base.BaseActivity;
import app.bright.flashlight.util.Utils;
import app.bright.flashlight.view.LoadingView;


public class TermsActivity extends BaseActivity {

    private WebView web;
    private RelativeLayout llnoConnect;
    private static final String url = "http://flashlight.adsformob.com/privacy.html";
    private LoadingView tremLoading;
    private Button btnInternet;
    private ImageView ivBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trems);
        initview();
    }

    private void initview() {
        tremLoading = (LoadingView) findViewById(R.id.trem_loading);
        llnoConnect = (RelativeLayout) findViewById(R.id.rl_trems);
        btnInternet = (Button) findViewById(R.id.btn_trem_internet);
        web = (WebView) findViewById(R.id.trems_web);

        ivBack= (ImageView) findViewById(R.id.iv_back);
        boolean networkConnected = Utils.isNetConnect();
        if (networkConnected) {
            llnoConnect.setVisibility(View.GONE);
            setwebView();

        } else {
            web.setVisibility(View.GONE);
            llnoConnect.setVisibility(View.VISIBLE);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setwebView() {
        tremLoading.setVisibility(View.VISIBLE);
        web.setVisibility(View.GONE);

        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        web.loadUrl(url);
        web.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
//                intent.putExtra("url", url);
//                startActivity(intent);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                tremLoading.setVisibility(View.GONE);
                web.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                llnoConnect.setVisibility(View.VISIBLE);
                web.setVisibility(View.GONE);
            }
        });

    }

}
