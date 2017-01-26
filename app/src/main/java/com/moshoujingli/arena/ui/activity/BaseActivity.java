package com.moshoujingli.arena.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * Created by bixiaopeng on 2017/1/26.
 */

abstract public class BaseActivity extends FragmentActivity {
    protected Bundle currentSavedInstanceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentSavedInstanceState = savedInstanceState;
        findView();
        initView();
        setOnClick();
    }

    abstract protected void setOnClick();

    abstract protected void initView();

    abstract protected void findView();
}