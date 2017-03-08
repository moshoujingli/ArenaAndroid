package com.moshoujingli.arena.ui.fragment;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.moshoujingli.arena.ArenaApp;
import com.moshoujingli.arena.R;
import com.moshoujingli.arena.model.Media;
import com.moshoujingli.arena.service.IMediaService;
import com.moshoujingli.arena.ui.adapter.MediaAdapter;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

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
        videoListView = (RecyclerView) root.findViewById(R.id.media_list);
        videoListView.setHasFixedSize(true);
        videoListView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        EnumMap<IMediaService.Option, String> conditions = new EnumMap<>(IMediaService.Option.class);
        EnumSet<IMediaService.SortType> sortType = EnumSet.of(IMediaService.SortType.RECOMMEND);
        adapter = new MediaAdapter(getActivity().getApplicationContext());
        adapter.setMedias(Collections.<Media>emptyList());
        ArenaApp.getMediaService().getMediaAsync(sortType, conditions, new IMediaService.ResultCallback<List<Media>>() {
            @Override
            public void onReceiveResult(List<Media> result) {
                adapter.setMedias(result);
                adapter.notifyDataSetChanged();
            }
        });
        videoListView.setAdapter(adapter);

    }
}
