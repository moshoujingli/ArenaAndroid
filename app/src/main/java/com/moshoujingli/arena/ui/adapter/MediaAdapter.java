package com.moshoujingli.arena.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moshoujingli.arena.R;
import com.moshoujingli.arena.model.Media;
import com.moshoujingli.arena.ui.activity.MediaUploadActivity;
import com.moshoujingli.arena.ui.event.MediaItemClickEvent;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by bixiaopeng on 2017/1/26.
 */

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaListItemViewHolder> {

    private final Picasso picasso;
    private List<Media> medias;

    static class MediaListItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        MediaListItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.media_thumbnail);
        }
    }

    public void setMedias(List<Media> medias){
        this.medias = medias;
    }

    public MediaAdapter(Context context) {
        picasso = Picasso.with(context);
    }

    @Override
    public MediaListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_media_list, parent, false);
        return new MediaListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MediaListItemViewHolder holder, int position) {
        final Media media = medias.get(position);
        picasso.load(media.thumbnailUrl).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaItemClickEvent e = new MediaItemClickEvent();
                e.media = media;
                EventBus.getDefault().post(e);
            }
        });
    }


    @Override
    public int getItemCount() {
        return medias.size();
    }
}
