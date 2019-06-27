package com.baidu.carlife.video;

import android.content.Context;

public class SingleSecondTimer extends AbstractTimer{

	public SingleSecondTimer(Context context) {
		super(context);
		setTimeInterval(1000);
	}
}
