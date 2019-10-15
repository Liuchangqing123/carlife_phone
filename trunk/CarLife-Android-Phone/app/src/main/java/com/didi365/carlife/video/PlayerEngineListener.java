package com.didi365.carlife.video;

/**
 * Create by zheng on 2019/1/17
 */
public interface PlayerEngineListener {
	
	public void onTrackPlay(MediaModel itemInfo);

	public void onTrackStop(MediaModel itemInfo);
	
	public void onTrackPause(MediaModel itemInfo);

	public void onTrackPrepareSync(MediaModel itemInfo);
	
	public void onTrackPrepareComplete(MediaModel itemInfo);
	
	public void onTrackStreamError(MediaModel itemInfo);
	
	public void onTrackPlayComplete(MediaModel itemInfo);
}
