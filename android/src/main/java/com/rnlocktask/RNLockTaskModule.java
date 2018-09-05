
package com.rnlocktask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

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

        /* mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); */

            if (android.os.Build.VERSION.SDK_INT >= 21 &&
                    myDevicePolicyManager.isDeviceOwnerApp(mActivity.getPackageName())) {
                String[] packages = {mActivity.getPackageName()};
                myDevicePolicyManager.setLockTaskPackages(mDPM, packages);
                mActivity.startLockTask();
            }
        }
    }

    @ReactMethod
    public Boolean isPinned() {
        Activity mActivity = getCurrentActivity();
        ActivityManager am = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        return android.os.Build.VERSION.SDK_INT >= 23 && am.getLockTaskModeState() ==
                ActivityManager.LOCK_TASK_MODE_PINNED;
    }

    @ReactMethod
    public Boolean isLocked() {
        Activity mActivity = getCurrentActivity();
        ActivityManager am = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        return android.os.Build.VERSION.SDK_INT >= 23 && am.getLockTaskModeState() ==
                ActivityManager.LOCK_TASK_MODE_LOCKED;
    }

    @ReactMethod
    public void removeDPM() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Activity mActivity = getCurrentActivity();
            DevicePolicyManager dpm = (DevicePolicyManager) mActivity
                    .getSystemService(Context.DEVICE_POLICY_SERVICE);
            dpm.clearDeviceOwnerApp(mActivity.getPackageName());
        }
    }

    @ReactMethod
    public void stopLockTask() {
        Activity mActivity = getCurrentActivity();
        if (mActivity != null && android.os.Build.VERSION.SDK_INT >= 21) {
            mActivity.stopLockTask();
        }
    }
}
