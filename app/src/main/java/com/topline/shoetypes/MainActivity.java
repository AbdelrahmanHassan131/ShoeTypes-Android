package com.topline.shoetypes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WebView m_Webview;
    private String m_WebUrl = "https://www.shoetypes.co/";
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 9; Redmi Note 7 Build/PKQ1.180904.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/83.0.4103.106 Mobile Safari/537.36 drive2client/3.101.830 (Android 9;Smartphone;Xiaomi Redmi Note 7 lavender;Features:203919)";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        m_Webview = (WebView) findViewById((R.id.webview));
        setupAppWebviewSettings(m_Webview);
    }

    public class m_WebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);}

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url)
        {
            WebSettings webSettings = webView.getSettings();
            if (Build.VERSION.SDK_INT >= 21) {
                CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
            } else {
                CookieManager.getInstance().setAcceptCookie(true);
            }
            if (url != null && url.startsWith("fb://profile"))
            {
                try {
                    webView.getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    webView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } catch (Exception e) {
                    Log.e("testing","ex" + e);
                    Log.d("testing", "url is " + url);
                    Log.d("testing", "url parse is  " + Uri.parse(url));
                    Toast.makeText(MainActivity.this, "FaceBook Not Installed in that Device", Toast.LENGTH_SHORT).show();
                    openInChrome(webView, url);
                    return true;
                }
            }

            if (url != null && !url.startsWith(m_WebUrl))
            {
                try
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    finish();
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "App Not Installed in that Device", Toast.LENGTH_SHORT).show();
                    openInChrome(webView, url);
                }

                return true;
            }

            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
            webView.getSettings().setUserAgentString(USER_AGENT);
            webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString().replace("; wv", ""));
            webView.loadUrl(url);
            return true;
        }

    }
    @Override
    public void onBackPressed() {
        if (m_Webview.canGoBack()) {
            m_Webview.goBack();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onPause() {
        m_Webview.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        m_Webview.onResume();
        super.onResume();
    }

    private void openInChrome(WebView view, String url) {
        try {

            Uri uri = Uri.parse(url.replace("fb://profile/","https://www.facebook.com/"));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.android.chrome");
            PackageManager packageManager = getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
            view.getContext().startActivity(intent);
        } catch (Exception e) {
            Log.e("testing","ex" + e);
            Log.d("testing", "url is " + url);
            Log.d("testing", "url parse is  " + Uri.parse(url));
        }
    }

    private void setupAppWebviewSettings(WebView m_Webview)
    {
        m_Webview.setWebViewClient(new m_WebClient());
        m_Webview.loadUrl(m_WebUrl);
        WebSettings webSettings = m_Webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        m_Webview.setWebChromeClient(new WebChromeClient());
        m_Webview.setFocusable(true);
        m_Webview.setFocusableInTouchMode(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDatabaseEnabled(true);
        //webSettings.setAppCacheEnabled(true);
        m_Webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(m_Webview, true);
            webSettings.setMixedContentMode(0);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }
        m_Webview.getSettings().setUserAgentString(USER_AGENT);
        webSettings.setUserAgentString(webSettings.getUserAgentString().replace("; wv", ""));
        m_Webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

}