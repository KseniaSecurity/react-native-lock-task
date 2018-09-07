
package com.rnlocktask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;

public class RNLockTaskModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNLockTaskModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNLockTask";
    }

    @ReactMethod
    public void startLockTask() {
        Activity mActivity = getCurrentActivity();
        if (mActivity != null) {
            DevicePolicyManager myDevicePolicyManager = (DevicePolicyManager) mActivity
                    .getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName mDPM = new ComponentName(mActivity, MyAdmin.class);

            if (android.os.Build.VERSION.SDK_INT >= 21 &&
                    myDevicePolicyManager.isDeviceOwnerApp(mActivity.getPackageName())) {
                String[] packages = {mActivity.getPackageName()};
                myDevicePolicyManager.setLockTaskPackages(mDPM, packages);
                mActivity.startLockTask();
            }
        }
    }

    @ReactMethod
    public void isLocked(Promise promise) {
        Activity mActivity = getCurrentActivity();
        ActivityManager am = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        promise.resolve(android.os.Build.VERSION.SDK_INT >= 23 && am.getLockTaskModeState() ==
                ActivityManager.LOCK_TASK_MODE_LOCKED);
    }

    @ReactMethod
    public void isPinned(Promise promise) {
        Activity mActivity = getCurrentActivity();
        ActivityManager am = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        promise.resolve(android.os.Build.VERSION.SDK_INT >= 23 && am.getLockTaskModeState() ==
                ActivityManager.LOCK_TASK_MODE_PINNED);
    }

    @ReactMethod
    public void stopLockTask() {
        Activity mActivity = getCurrentActivity();
        if (mActivity != null && android.os.Build.VERSION.SDK_INT >= 21) {
            mActivity.stopLockTask();
        }
    }
}
