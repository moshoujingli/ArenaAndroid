package com.moshoujingli.arena;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.moshoujingli.arena.service.IMediaService;
import com.moshoujingli.arena.service.IUserService;
import com.moshoujingli.arena.service.impl.LeanCloudMediaService;
import com.moshoujingli.arena.service.impl.LeanCloudUserService;
import com.moshoujingli.arena.statistic.IKVReporter;
import com.moshoujingli.arena.statistic.impl.LeanCloudReporter;
import com.moshoujingli.arena.utils.LogHelper;

/**
 * Created by bixiaopeng on 2017/1/28.
 */

public class ArenaApp extends Application {
    private static ArenaApp sInstance;
    private static IKVReporter sKVReporter;
    private static IMediaService sMediaService;
    private static IUserService sUserService;

    @Override
    public void onCreate() {
        super.onCreate();

        AVOSCloud.initialize(this,BuildConfig.leanCloudAppId, BuildConfig.leanCloudAppClientSecret);
        AVOSCloud.setDebugLogEnabled(true);
        AVAnalytics.enableCrashReport(this, true);

        sKVReporter = new LeanCloudReporter(getApplicationContext());
        sInstance = this;
    }

    public static ArenaApp getApp() {
        return sInstance;
    }

    public static IMediaService getMediaService() {
        if (sInstance == null) {
            LogHelper.errorState("media service get on app not init.");
        }

        if (sMediaService == null) {
            synchronized (ArenaApp.class) {
                if (sMediaService == null) {
                    sMediaService = new LeanCloudMediaService();
                }
            }
        }

        return sMediaService;
    }

    public static IUserService getUserService() {
        if (sInstance == null) {
            LogHelper.errorState("user service get on app not init.");
        }

        if (sUserService == null) {
            synchronized (ArenaApp.class) {
                if (sUserService == null) {
                    sUserService = new LeanCloudUserService();
                }
            }
        }

        return sUserService;

    }

    public static IKVReporter getKVReporter() {
        return sKVReporter;
    }
}
