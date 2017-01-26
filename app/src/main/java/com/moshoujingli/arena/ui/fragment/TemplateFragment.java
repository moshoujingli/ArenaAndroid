package com.moshoujingli.arena.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bixiaopeng on 2017/1/26.
 */

public abstract class TemplateFragment extends BaseFragment {
    protected View mainView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        beforeCreateView();
        mainView = inflater.inflate(getMainLayout(), container, false);
        initialView(mainView);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialData();
    }

    protected void beforeCreateView() {

    }

    protected View findViewById(int resId) {
        return mainView.findViewById(resId);
    }

    protected abstract int getMainLayout();

    protected abstract void initialView(@Nullable View root);

    protected void initialData() {
    }
}
