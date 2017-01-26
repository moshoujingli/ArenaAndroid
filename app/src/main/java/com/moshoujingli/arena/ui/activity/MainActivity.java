package com.moshoujingli.arena.ui.activity;

import android.view.View;
import android.view.ViewGroup;

import com.moshoujingli.arena.R;
import com.moshoujingli.arena.ui.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    private ViewGroup containerView;
    private View videoTabBtn;
    private View captureTabBtn;

    @Override
    protected void setOnClick() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_main);
        containerView = (ViewGroup)findViewById(R.id.fragment_area);
        videoTabBtn = findViewById(R.id.tab_video_btn);
        captureTabBtn = findViewById(R.id.tab_capture_btn);
    }
}
