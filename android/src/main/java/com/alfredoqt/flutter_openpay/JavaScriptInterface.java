package com.alfredoqt.flutter_openpay;

import android.app.Activity;
import android.provider.Settings.Secure;
import android.webkit.JavascriptInterface;

public class JavaScriptInterface {
    private Activity activity;

    JavaScriptInterface(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public String getIdentifierForVendor() {
        return Secure.getString(this.activity.getContentResolver(), "android_id");
    }
}
