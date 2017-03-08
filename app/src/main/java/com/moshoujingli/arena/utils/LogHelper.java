package com.moshoujingli.arena.utils;

import android.util.Log;

import com.moshoujingli.arena.ArenaApp;
import com.moshoujingli.arena.BuildConfig;

/**
 * Created by bixiaopeng on 2017/1/28.
 */

public class LogHelper {
    public static final String TAG = "Arena";
    public static final String FATAL_ERROR = "FATAL_ERROR";
    private static boolean mLogEnabled = BuildConfig.DEBUG_LOG;
    private static boolean shouldThrowException = BuildConfig.DEBUG;

    public static void setLogEnabled(boolean enabled) {
        mLogEnabled = enabled;
    }

    public static boolean isLogEnabled() {
        return mLogEnabled;
    }

    public static void i(String subTag, String msg) {
        if (mLogEnabled) {
            Log.i(TAG, getLogMsg(subTag, msg));
        }
    }

    public static void i(String subTag, String msg, Throwable tr) {
        if (mLogEnabled) {
            Log.i(TAG, getLogMsg(subTag, msg), tr);
        }
    }

    public static void w(String subTag, String msg) {
        if (mLogEnabled) {
            Log.w(TAG, getLogMsg(subTag, msg));
        }

    }

    public static void w(String subTag, String msg, Throwable tr) {
        if (mLogEnabled) {
            Log.w(TAG, getLogMsg(subTag, msg), tr);
        }

    }

    public static void d(String subTag, String msg) {
        if (mLogEnabled) {
            Log.d(TAG, getLogMsg(subTag, msg));
        }

    }

    public static void d(String subTag, String msg, Throwable tr) {
        if (mLogEnabled) {
            Log.d(TAG, getLogMsg(subTag, msg), tr);
        }

    }

    public static void e(String subTag, String msg) {
        Log.e(TAG, getLogMsg(subTag, msg));
    }

    public static void e(String subTag, String msg, Throwable tr) {
        Log.e(TAG, getLogMsg(subTag, msg), tr);
    }

    private static String getLogMsg(String subTag, String msg) {
        StringBuffer sb = new StringBuffer()
                .append("{").append(Thread.currentThread().getName()).append("}")
                .append("[").append(subTag).append("] ")
                .append(msg);

        return sb.toString();
    }

    public static void printStaceTrace(String tag) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : elements) {
            d(tag, element.toString());
        }
    }

    public static void errorState(String msg){
        if (shouldThrowException) {
            throw new IllegalStateException(msg);
        } else {
            printStaceTrace(FATAL_ERROR);
            e(FATAL_ERROR,msg);
            ArenaApp.getKVReporter().report(FATAL_ERROR,msg);
        }
    }
    public static void errorArg(String msg){
        if (shouldThrowException) {
            throw new IllegalArgumentException(msg);
        } else {
            printStaceTrace(FATAL_ERROR);
            e(FATAL_ERROR,msg);
            ArenaApp.getKVReporter().report(FATAL_ERROR,msg);
        }
    }
}
