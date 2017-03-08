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

import static com.moshoujingli.arena.service.IMediaService.CHALLENGE_MEDIA_ID_NONE;

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
    private int mediaId;
    private Media curMedia;


    public static void launch(Activity activity, int mediaId) {
        Intent i = new Intent(activity, MediaDetailActivity.class);
        if (mediaId!=CHALLENGE_MEDIA_ID_NONE){
            i.putExtra(MEDIA_ID,mediaId);
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
        EnumMap<IMediaService.Option,String> options = new EnumMap<>(IMediaService.Option.class);
        mediaId = getIntent().getIntExtra(MEDIA_ID,CHALLENGE_MEDIA_ID_NONE);
        options.put(IMediaService.Option.MEDIA_ID,String.valueOf(mediaId));
        List<Media> medias = ArenaApp.getMediaService().getMedia(null, options);
        if (medias.size()!=1){
            LogHelper.errorState("medias large then 1 "+medias.size());
        } else {
            curMedia = medias.get(0);
        }
        videoPlayer.setVideoURI(Uri.parse(curMedia.mediaUrl));
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
        if (view==playBtn){
            if (videoPlayer.isPlaying()){
                videoPlayer.pause();
            } else {
                videoPlayer.start();
            }
        } else if(view==voteUpBtn) {
            ArenaApp.getMediaService().voteUpMedia(curMedia, UserPreference.getCurrentUser());
        } else if(view == challengeBtn) {
            MediaUploadActivity.launch(this,curMedia.id);
        }
    }
}
