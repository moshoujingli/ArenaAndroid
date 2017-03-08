package com.moshoujingli.arena.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by bixiaopeng on 2017/2/4.
 */

public class LocalStorageHelper {
    private static final String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/arena/";
    private static final String internalPath = Environment.getDataDirectory().getAbsolutePath();
    private static final String TAG = "LocalStorageHelper";

    public static String saveInExternalStorage(Bitmap bitmap) {
        return saveInExternalStorage(bitmap, System.currentTimeMillis() + ".png");
    }

    public static String saveInExternalStorage(Bitmap bitmap, String fileName) {
        return save(bitmap,externalPath+"/tbl/",fileName);
    }

    public static String saveInAppDataStorage(Bitmap bitmap) {
        return saveInExternalStorage(bitmap, System.currentTimeMillis() + ".png");
    }

    public static String saveInAppDataStorage(Bitmap bitmap, String fileName) {
        return save(bitmap,internalPath+"/tbl/",fileName);
    }

    private static String save(Bitmap bitmap, String filePath, String fileName) {
        File dir = new File(filePath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fOut);
            fOut.flush();
            return file.getAbsolutePath();
        } catch (IOException e) {
            LogHelper.e(TAG, "save file error " + file.getAbsolutePath(), e);
        }
        return null;
    }
}
