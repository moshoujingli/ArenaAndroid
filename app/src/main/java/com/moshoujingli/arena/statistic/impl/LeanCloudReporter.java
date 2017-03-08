package com.moshoujingli.arena.statistic.impl;

import android.content.Context;

import com.moshoujingli.arena.statistic.IKVReporter;

import org.json.JSONObject;

/**
 * Created by bixiaopeng on 2017/1/28.
 */

public class LeanCloudReporter implements IKVReporter{
    public LeanCloudReporter(Context context) {
    }

    @Override
    public void report(String key, String message) {

    }

    @Override
    public void report(String key, JSONObject jsonObject) {

    }
}
