package com.alfredoqt.flutter_openpay;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.UUID;

public class DeviceCollectorDefaultImpl {
    private String baseUrl;
    private String merchantId;
    private String errorMessage;

    public DeviceCollectorDefaultImpl(String baseUrl, String merchantId) {
        this.baseUrl = baseUrl;
        this.merchantId = merchantId;
    }

    public String setup(Activity activity) {
        try {
            String sessionId = UUID.randomUUID().toString();
            sessionId = sessionId.replace("-", "");
            char[] sessionIdChars = sessionId.toCharArray();
            sessionIdChars[12] = 'A';
            sessionId = String.valueOf(sessionIdChars);
            WebView webViewDF = new WebView(activity);
            webViewDF.setWebViewClient(new WebViewClient());
            webViewDF.getSettings().setJavaScriptEnabled(true);
            webViewDF.addJavascriptInterface(new JavaScriptInterface(activity), "Android");
            String urlAsString = String.format("%s/oa/logo.htm?m=%s&s=%s", this.baseUrl, this.merchantId, sessionId);
            webViewDF.loadUrl(urlAsString);
            return sessionId;
        } catch (Exception var6) {
            this.logError(this.getClass().getName(), var6.getMessage());
            this.errorMessage = var6.getMessage();
            return null;
        }
    }

    private void logError(String tag, String content) {
        if (content.length() > 4000) {
            Log.e(tag, content.substring(0, 4000));
            this.logError(tag, content.substring(4000));
        } else {
            Log.e(tag, content);
        }

    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
