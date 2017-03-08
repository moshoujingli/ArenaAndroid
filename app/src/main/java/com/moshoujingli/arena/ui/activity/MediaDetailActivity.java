package com.moshoujingli.arena.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.moshoujingli.arena.ArenaApp;
import com.moshoujingli.arena.R;
import com.moshoujingli.arena.model.Media;
import com.moshoujingli.arena.pref.UserPreference;
import com.moshoujingli.arena.service.IMediaService;
import com.moshoujingli.arena.utils.LogHelper;

import java.util.EnumMap;
import java.util.List;

/**
 * Created by bixiaopeng on 2017/1/28.
 */

public class MediaDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String MEDIA_ID = "media_id";
    public static final String VOTE_DOWN = "vote down";
    public static final String VOTE_UP = "vote up";
    private VideoView videoPlayer;
    private Button challengeBtn;
    private Button voteUpBtn;
    private Button playBtn;
    private String mediaId;
    private Media curMedia;


    public static void launch(Activity activity, String mediaId) {
        Intent i = new Intent(activity, MediaDetailActivity.class);
        if (mediaId != null) {
            i.putExtra(MEDIA_ID, mediaId);
        } else {
            LogHelper.errorState("media id error");
        }
        activity.startActivity(i);
    }

    @Override
    protected void setOnClick() {
        playBtn.setOnClickListener(this);
        voteUpBtn.setOnClickListener(this);
        challengeBtn.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        EnumMap<IMediaService.Option, String> options = new EnumMap<>(IMediaService.Option.class);
        mediaId = getIntent().getStringExtra(MEDIA_ID);
        options.put(IMediaService.Option.MEDIA_ID, String.valueOf(mediaId));
        ArenaApp.getMediaService().getMediaAsync(null, options, new IMediaService.ResultCallback<List<Media>>() {
            @Override
            public void onReceiveResult(List<Media> result) {
                if (result == null || result.size() != 1) {
                    LogHelper.errorState("medias large then 1 " + result.size());
                } else {
                    curMedia = result.get(0);
                }
                videoPlayer.setVideoURI(Uri.parse(curMedia.mediaUrl));
            }
        });

    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_media_detail);
        videoPlayer = (VideoView) findViewById(R.id.video_player);
        playBtn = (Button) findViewById(R.id.play_btn);
        voteUpBtn = (Button) findViewById(R.id.voteup_btn);
        challengeBtn = (Button) findViewById(R.id.challenge_btn);
    }

    @Override
    public void onClick(View view) {
        if (view == playBtn) {
            if (videoPlayer.isPlaying()) {
                videoPlayer.pause();
            } else {
                videoPlayer.start();
            }
        } else if (view == voteUpBtn) {
            ArenaApp.getMediaService().voteUpMedia(curMedia, UserPreference.getCurrentUser());
        } else if (view == challengeBtn) {
            MediaUploadActivity.launch(this, curMedia.id);
        }
    }
}
