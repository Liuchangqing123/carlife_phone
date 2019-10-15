package com.didi365.carlife.video;

/**
 * Create by zheng on 2019/1/17
 */
public interface IBasePlayEngine {
	public void play();
	public void pause();
	public void stop();
	public void skipTo(int time);
}
