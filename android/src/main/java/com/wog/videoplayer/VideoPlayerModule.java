package com.wog.videoplayer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.File;

/**
 * Created by nhbao on 9/7/2016.
 */
public class VideoPlayerModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    public final int VIDEO_CODE = 1;

    public VideoPlayerModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "VideoPlayerManager";
    }

    @ReactMethod
    public void showVideoPlayer(String url) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity != null) {
            Intent videoIntent = new Intent(Intent.ACTION_VIEW);
            if (url.indexOf("http") != 0 && Build.VERSION.SDK_INT >= 24) {
                String path = url.replace("file://", "");
                Uri uriForFile = FileProvider.getUriForFile(currentActivity,
                        this.getReactApplicationContext().getPackageName() + ".provider", new File(path));

                videoIntent.setDataAndType(uriForFile, "video/*");

                // Set flag to give temporary permission to external app to use FileProvider
                videoIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Validate that the device can open the file
                PackageManager pm = currentActivity.getPackageManager();
                if (videoIntent.resolveActivity(pm) != null) {
                    currentActivity.startActivityForResult(videoIntent, VIDEO_CODE);
                }
            } else {
                videoIntent.setDataAndType(Uri.parse(url), "video/*");

                currentActivity.startActivityForResult(videoIntent, VIDEO_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_CODE) {
            getCurrentActivity().finish();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
