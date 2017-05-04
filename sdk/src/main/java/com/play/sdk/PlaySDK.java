package com.play.sdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;

import com.play.common.Config;
import com.play.floatmenu.SDKMenuManager;
import com.play.util.DialogUtil;
import com.play.util.ResourceUtil;

/**
 * Created by Tobin on 2017/4/21.
 */

public class PlaySDK {

    private Activity activity;
    private Context context;

    private static PlaySDK ourInstance  = null;

    private PlaySDK() {
    }

    public Context getContext() {
        if (context == null && activity != null)
            context = activity.getApplicationContext();
        return context;
    }

    public Activity getActivity() {
        return activity;
    }

    public static PlaySDK getInstance() {
        if( ourInstance == null){
            synchronized (PlaySDK.class){
                if( ourInstance == null){
                    ourInstance = new PlaySDK();
                }
            }
        }
        return ourInstance;
    }

    public void initSDK(Activity activity, String clientId, boolean isShowLog){
        this.activity = activity;
        Config.isShowLog = isShowLog;
        Config.clientId = clientId;
        checkSdkCallMethod();

        MobUserManager.initUserManager(activity, isShowLog);
        SDKMenuManager.getInstance(activity);
        SDKMenuManager.getInstance(activity).initParentView(null);
        SDKMenuManager.getInstance(activity).popupMenu();
//        SDKMenuManager.getInstance(null).updateMenuViewLoginToday();

    }

    public void sdkLogin() {
        if (activity.isFinishing())
            return;

    }


    private void checkSdkCallMethod() {
        StackTraceElement stack[] = new Throwable().getStackTrace();
        for (int i = 0; i < stack.length; i++) {
            if (stack[i].getClassName().equals(Activity.class.getName()) && stack[i].getMethodName().equals("performCreate")) {
                return;
            }
        }
        alertMessage("SDK.initSDK必须在主Activity的onCreate中调用", false);
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void alertMessage(String msg, boolean cancel) {
        Resources res = activity.getResources();
        String title = Config.sdkTitle;
        AlertDialog.Builder builder = DialogUtil.showDialog(activity, title, msg);
        if (cancel) {
            String done = res.getString(ResourceUtil.getStringId("tobin_string_btn_done"));
            builder.setPositiveButton(done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.setCancelable(false);
        builder.show();
    }



}
