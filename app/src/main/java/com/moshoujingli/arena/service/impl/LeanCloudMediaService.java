package com.moshoujingli.arena.service.impl;

import android.content.pm.PackageManager;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.moshoujingli.arena.model.Media;
import com.moshoujingli.arena.model.User;
import com.moshoujingli.arena.service.IMediaService;
import com.moshoujingli.arena.utils.LogHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by bixiaopeng on 2017/1/28.
 */

public class LeanCloudMediaService implements IMediaService {
    private static final String MEDIA_OBJ_NAME = "media";
    private static final String FIELD_MEDIA_FILE = "media_file";
    private static final String FIELD_MEDIA_THUMBNAIL = "media_tbl";
    private static final String TAG = "IMediaService";

    @Override
    public void uploadMedia(Media media, User user) {
        AVObject mediaObject = new AVObject("media");

        mediaObject.put(FIELD_MEDIA_FILE, new AVFile("media_content", readByte(media.mediaUrl)));
        mediaObject.put(FIELD_MEDIA_THUMBNAIL, new AVFile("tbl_content", readByte(media.thumbnailUrl)));
        mediaObject.saveInBackground();
//        media.thumbnailUrl;
//        media.mediaUrl;
    }

    private byte[] readByte(String filePath) {
        File file = new File(filePath);
        long fileLength = file.length();
        LogHelper.i(TAG, filePath + ":" + fileLength);
        if (fileLength > Integer.MAX_VALUE || fileLength > 100 * 1000 * 1000) {
            LogHelper.errorArg("file too big :" + filePath + " size:" + fileLength);
        }
        byte[] content = new byte[(int) fileLength];
        try {
            FileInputStream fis = new FileInputStream(file);
            int result = fis.read(content);
            if (result != fileLength) {
                LogHelper.errorState("read failed result:" + result);
            }
            return content;
        } catch (IOException e) {
            LogHelper.errorState("IOException +" + e.getMessage());
        }

        return null;
    }

    @Override
    public void challengeMedia(Media target, Media challenger) {

    }

    @Override
    public List<Media> getMedia(EnumSet<SortType> sort, EnumMap<Option, String> option) {
        return null;
    }

    @Override
    public void getMediaAsync(EnumSet<SortType> sort, EnumMap<Option, String> option, final ResultCallback<List<Media>> callback) {

        if (option.containsKey(Option.MEDIA_ID)) {
            AVObject avObject = AVObject.createWithoutData("media", option.get(Option.MEDIA_ID));
            avObject.fetchIfNeededInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject object, AVException e) {
                    if (e != null) {
                        e.printStackTrace();
                        LogHelper.e(TAG, "error for fetch by id", e);
                    }
                    callback.onReceiveResult(Collections.singletonList(toMedia(object)));
                }
            });
            return;
        }

        AVQuery<AVObject> query = new AVQuery<>("media");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                ArrayList<Media> mediaList = new ArrayList<>(list.size());

                for (AVObject avObject : list) {
                    mediaList.add(toMedia(avObject));
                }

                callback.onReceiveResult(mediaList);
            }
        });
    }

    @Override
    public void voteUpMedia(Media media, User user) {

    }

    @Override
    public void voteDownMedia(Media media, User user) {

    }

    private Media toMedia(AVObject object) {
        Media media = new Media();
        media.thumbnailUrl = object.getAVFile(FIELD_MEDIA_THUMBNAIL).getUrl();
        media.mediaUrl = object.getAVFile(FIELD_MEDIA_FILE).getUrl();
        media.id = object.getObjectId();
        return media;
    }
}
