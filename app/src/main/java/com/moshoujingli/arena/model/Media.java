package com.moshoujingli.arena.model;

import java.util.List;

/**
 * Created by bixiaopeng on 2017/1/28.
 *
 *  id
 *	user id
 *	获得缩略图（地址）
 *	获得视频（地址）
 *	获得挑战视频ID
 *	观看数
 *	点赞数
 *	视频时长，宽高，文件大小
 *	标签
 */

public class Media {
    public int id;
    public int userId;
    public String thumbnailUrl;
    public String mediaUrl;
    public int challengeMediaId;
    public int viewCount;
    public int voteUpCount;
    public int durationTimeMillis;
    public int width;
    public int height;
    public int sizeInByte;
    public List<String> tags;
    public boolean voted;
}
