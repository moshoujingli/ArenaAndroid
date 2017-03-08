package com.moshoujingli.arena.ui.fragment;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.moshoujingli.arena.R;
import com.moshoujingli.arena.ui.adapter.MediaAdapter;

/**
 * Created by bixiaopeng on 2017/1/26.
 */

public class MediaListFragment extends TemplateFragment {
    private RecyclerView videoListView;
    private MediaAdapter adapter;

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_media_list;
    }

    @Override
    protected void initialView(@Nullable View root) {
        videoListView = (RecyclerView)root.findViewById(R.id.media_list);
        adapter = new MediaAdapter();
        videoListView.setAdapter(adapter);
    }
}
