package com.moshoujingli.arena.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.moshoujingli.arena.R;
import com.moshoujingli.arena.ui.fragment.MediaListFragment;
import com.moshoujingli.arena.utils.LogHelper;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String MEDIA_LIST_FRAGMENT = "MEDIA_LIST_FRAGMENT";
    private ViewGroup containerView;
    private View videoTabBtn;
    private View captureTabBtn;

    public static void launch(Activity activity){
        Intent i = new Intent(activity,MainActivity.class);
        activity.startActivity(i);
    }

    @Override
    protected void setOnClick() {
        videoTabBtn.setOnClickListener(this);
        captureTabBtn.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        switchFragment(MEDIA_LIST_FRAGMENT);
    }

    private void switchFragment(String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ftr = fm.beginTransaction();
        List<Fragment> fragments = fm.getFragments();
        Fragment targetFragment = fm.findFragmentByTag(fragmentTag);
        if (targetFragment == null) {
            switch(fragmentTag){
                case MEDIA_LIST_FRAGMENT:
                    targetFragment = new MediaListFragment();
                    break;
                default:
                    throw new IllegalArgumentException("not support tag:"+fragmentTag);
            }
            ftr.add(R.id.fragment_area,targetFragment,fragmentTag);
        }

        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f == null) {
                    continue;
                }
                if (f == targetFragment) {
                    ftr.show(f);
                } else {
                    ftr.hide(f);
                }
            }
        }

        ftr.commitAllowingStateLoss();
    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_main);
        containerView = (ViewGroup)findViewById(R.id.fragment_area);
        videoTabBtn = findViewById(R.id.tab_video_btn);
        captureTabBtn = findViewById(R.id.tab_capture_btn);
    }

    @Override
    public void onClick(View view) {

        if (view==videoTabBtn){

        } else if (view==captureTabBtn){
            MediaUploadActivity.launch(this);
        } else {
            LogHelper.errorArg("no such view bind click listener"+view);
        }
    }
}
