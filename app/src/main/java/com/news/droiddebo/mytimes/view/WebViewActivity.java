package com.news.droiddebo.mytimes.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.news.droiddebo.mytimes.R;
import com.news.droiddebo.mytimes.model.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private String url;
    private TextView mTitle;
    private Typeface montserratRegular;
    private float mDownX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        AssetManager assetManager = this.getApplicationContext().getAssets();
        montserratRegular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");

        url = getIntent().getStringExtra(Constants.INTENT_URL);

        // Custom Toolbar ( App Bar )
        createToolbar();

        webView = findViewById(R.id.webView_article);
        progressBar = findViewById(R.id.progressBar);

        if (savedInstanceState == null) {
            webView.loadUrl(url);
            initWebView();
        }
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_web_view);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mTitle = findViewById(R.id.toolbar_title_web_view);
        mTitle.setTypeface(montserratRegular);
        mTitle.setText(url);
    }

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    private void initWebView() {
        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                progressBar.setVisibility(View.GONE);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);

        webView.setOnTouchListener((v, event) -> {

            if (event.getPointerCount() > 1) {
                // Multi touch detected
                return true;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // save the x
                    mDownX = event.getX();
                    break;

                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    // set x so that it doesn't move
                    event.setLocation(mDownX, event.getY());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + event.getAction());
            }

            return false;
        });

    }

    private static class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Override the Up/Home Button
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(Constants.TITLE_WEBVIEW_KEY, url);
        webView.saveState(bundle);
    }

    @Override
    protected void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        createToolbar();
        webView.restoreState(savedInstanceState);
        mTitle.setText(savedInstanceState.getString(Constants.TITLE_WEBVIEW_KEY));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

}
