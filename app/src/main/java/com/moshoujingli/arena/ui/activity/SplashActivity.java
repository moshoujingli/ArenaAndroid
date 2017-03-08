package com.moshoujingli.arena.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.moshoujingli.arena.ArenaApp;
import com.moshoujingli.arena.pref.UserPreference;

/**
 * Created by bixiaopeng on 2017/1/28.
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    private void initialize() {
        if (UserPreference.getCurrentUser()==null){
            UserPreference.attachUser(ArenaApp.getUserService().generateTemporaryUser());
        }
        MainActivity.launch(this);
    }

    @Override
    protected void setOnClick() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void findView() {

    }
}
