package com.moshoujingli.arena.statistic;

import org.json.JSONObject;

/**
 * Created by bixiaopeng on 2017/1/28.
 */

public interface IKVReporter {
    void report(String key,String message);
    void report(String key, JSONObject jsonObject);
}
