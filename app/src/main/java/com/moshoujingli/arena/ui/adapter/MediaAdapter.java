package com.moshoujingli.arena.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moshoujingli.arena.R;

/**
 * Created by bixiaopeng on 2017/1/26.
 */

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaListItemViewHolder> {

    static class MediaListItemViewHolder extends RecyclerView.ViewHolder{


        private final ImageView imageView;

        public MediaListItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.media_thumbnail);
        }
    }

    @Override
    public MediaListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MediaListItemViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }
}
