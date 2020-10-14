package com.xmexe.exe.ScreenShot;
/**
 * Created by Abay on 2015/8/24.
 */
import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ScreenShot extends CordovaPlugin {
    private static final String LOG_TAG = "ScreenShot";
    protected final static String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final int PERMISSION_DENIED_ERROR = 20;
    public static final int SAVE_SCREENSHOT_SEC = 0;
    public static final int SAVE_SCREENSHOT_URI_SEC = 1;
    private ScreenShot instance = null;
    public ScreenShot() {
        instance = this;
    }
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
//        final CordovaWebView wv = webView;

        Log.d(LOG_TAG, "No Method In This Plugin" + webView);
        super.initialize(cordova, webView);
        if(!PermissionHelper.hasPermission(this, PERMISSIONS[0])) {
            PermissionHelper.requestPermissions(this, SAVE_SCREENSHOT_SEC, PERMISSIONS);
        }
        CallbackContext callbackContext = new CallbackContext("1234", webView);
        RxScreenshotDetector.start(callbackContext, webView, instance, webView.getContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {


                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String path) {
                        Log.e("MainActivity", "path ： " + path);
                    }
                });

    }




    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(LOG_TAG, "No Method In This Plugin: " + action);
        if("screenshot".equals(action)) {
            Log.d(LOG_TAG, "I kicked off a screenshot: " + action + " " + callbackContext.getCallbackId());
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    PluginResult result = new PluginResult(PluginResult.Status.OK, "something else");
                    webView.loadUrl("javascript:receiveScreenShotCallback()");
                }
            });
            return true;
        } else {
            Log.d(LOG_TAG, "Something happened: " + action);
            return false;
        }

    }

}