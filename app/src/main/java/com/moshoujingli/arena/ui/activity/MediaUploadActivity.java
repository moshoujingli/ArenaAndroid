package com.moshoujingli.arena.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.moshoujingli.arena.ArenaApp;
import com.moshoujingli.arena.R;
import com.moshoujingli.arena.model.Media;
import com.moshoujingli.arena.pref.UserPreference;
import com.moshoujingli.arena.utils.LocalStorageHelper;
import com.moshoujingli.arena.utils.LogHelper;

import static com.moshoujingli.arena.service.IMediaService.CHALLENGE_MEDIA_ID_NONE;

/**
 * Created by bixiaopeng on 2017/1/28.
 */

public class MediaUploadActivity extends BaseActivity implements View.OnClickListener {
    private static final int VIDEO_CAPTURE = 10001;
    public static final String CHALLENGE_MEDIA_ID = "ChallengeMediaId";
    public static final String FROM_CHALLENGE_MEDIA = "FromChallengeMedia";
    private Button captureBtn;
    private Button uploadBtn;
    private ImageView previewImg;
    private String curFilePath;
    private String curTblPath;

    public static void launch(Activity activity) {
        launch(activity, CHALLENGE_MEDIA_ID_NONE);
    }

    public static void launch(Activity activity,int challengeMediaId) {
        Intent i = new Intent(activity, MediaUploadActivity.class);
        if (challengeMediaId!=CHALLENGE_MEDIA_ID_NONE){
            i.putExtra(CHALLENGE_MEDIA_ID,challengeMediaId);
            i.putExtra(FROM_CHALLENGE_MEDIA,true);
        }
        activity.startActivity(i);
    }

    @Override
    protected void setOnClick() {
        captureBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_media_upload);
        captureBtn = (Button) findViewById(R.id.capture_btn);
        uploadBtn = (Button) findViewById(R.id.upload_btn);
        previewImg = (ImageView) findViewById(R.id.preview);
    }

    @Override
    public void onClick(View view) {
        if (view == captureBtn) {
            startMediaCapture();
        } else if (view == uploadBtn) {
            Media media = new Media();
            media.userId = UserPreference.getCurrentUser().id;
            media.mediaUrl = curFilePath;
            media.thumbnailUrl = curTblPath;
            if (getIntent().getBooleanExtra(FROM_CHALLENGE_MEDIA,false)){
                int challengeId = getIntent().getIntExtra(CHALLENGE_MEDIA_ID, CHALLENGE_MEDIA_ID_NONE);
                if (challengeId==CHALLENGE_MEDIA_ID_NONE){
                    LogHelper.errorState("FROM_CHALLENGE_MEDIA is true but no CHALLENGE_MEDIA_ID");
                }
                media.challengeMediaId = challengeId;
            }
            ArenaApp.getMediaService().uploadMedia(media,UserPreference.getCurrentUser());
            finish();
        }
    }

    private void startMediaCapture() {
//        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.8);
//        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
//        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 100*1000*1000);
//        startActivityForResult(intent, VIDEO_CAPTURE);
        Intent i = new Intent(this,MediaCaptureActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == VIDEO_CAPTURE) {
            Uri uri = data.getData();
            Cursor cursor = this.getContentResolver().query(uri, null, null,
                    null, null);
            if (cursor != null && cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                curFilePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(curFilePath, MediaStore.Video.Thumbnails.MINI_KIND);
                curTblPath = LocalStorageHelper.saveInAppDataStorage(bitmap);
                previewImg.setImageBitmap(bitmap);
                cursor.close();
            }
        }
    }
}
