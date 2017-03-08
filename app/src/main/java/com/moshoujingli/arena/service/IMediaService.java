package com.moshoujingli.arena.service;

import com.moshoujingli.arena.model.Media;
import com.moshoujingli.arena.model.User;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by bixiaopeng on 2017/1/28.
 */

public interface IMediaService {
    interface ResultCallback<T> {
        void onReceiveResult(T result);
    }

    enum SortType {
        TIME,
        SHOW,
        RECOMMEND
    }

    enum Option {
        MEDIA_ID,
    }

    void uploadMedia(Media media, User user);

    void challengeMedia(Media target, Media challenger);

    List<Media> getMedia(EnumSet<SortType> sort, EnumMap<Option, String> option);
    void getMediaAsync(EnumSet<SortType> sort, EnumMap<Option, String> option,ResultCallback<List<Media>> callback);

    void voteUpMedia(Media media, User user);

    void voteDownMedia(Media media, User user);


}
