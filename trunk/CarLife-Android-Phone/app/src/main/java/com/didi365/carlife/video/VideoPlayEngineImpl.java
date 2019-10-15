package com.didi365.carlife.video;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;

/**
 * Create by zheng on 2019/1/17
 */

public class VideoPlayEngineImpl extends AbstractMediaPlayEngine {

    private SurfaceHolder mHolder = null;
    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    private MediaPlayer.OnSeekCompleteListener mSeekCompleteListener;
    private MediaPlayer.OnErrorListener mOnErrorListener;

    public VideoPlayEngineImpl(Context context, SurfaceHolder holder) {
        super(context);
        setHolder(holder);
    }

    public void setHolder(SurfaceHolder holder) {
        mHolder = holder;
    }

    public void setOnBuffUpdateListener(MediaPlayer.OnBufferingUpdateListener listener) {
        mBufferingUpdateListener = listener;
    }

    public void setOnSeekCompleteListener(MediaPlayer.OnSeekCompleteListener listener) {
        mSeekCompleteListener = listener;
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener listener) {
        mOnErrorListener = listener;
    }

    @Override
    protected boolean prepareSelf() {
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(mMediaInfo.getUrl());
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if (mHolder != null) {
                mMediaPlayer.setDisplay(mHolder);
            }
            if (mBufferingUpdateListener != null) {
                mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            }
            if (mSeekCompleteListener != null) {
                mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
            }
            if (mOnErrorListener != null) {
                mMediaPlayer.setOnErrorListener(mOnErrorListener);
            }
            mMediaPlayer.prepareAsync();
            mPlayState = PlayState.MPS_PARESYNC;
            performPlayListener(mPlayState);
        } catch (Exception e) {
            e.printStackTrace();
            mPlayState = PlayState.MPS_INVALID;
            performPlayListener(mPlayState);
            return false;
        }
        return true;
    }

    @Override
    protected boolean prepareComplete(MediaPlayer mp) {
        mPlayState = PlayState.MPS_PARECOMPLETE;
        if (mPlayerEngineListener != null) {
            mPlayerEngineListener.onTrackPrepareComplete(mMediaInfo);
        }
        if (mHolder != null) {
            CommonUtil.ViewSize viewSize = CommonUtil.getFitSize(mContext, mp);
            mHolder.setFixedSize(viewSize.width, viewSize.height);
        }
        mMediaPlayer.start();
        mPlayState = PlayState.MPS_PLAYING;
        performPlayListener(mPlayState);
        return true;
    }
}
