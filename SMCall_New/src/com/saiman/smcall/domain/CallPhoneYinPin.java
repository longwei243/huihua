package com.saiman.smcall.domain;

import android.content.Context;
import android.media.MediaPlayer;

import com.saiman.smcall.R;

public class CallPhoneYinPin {
	public CallPhoneYinPin(Context context) {
		super();
		mp = MediaPlayer.create(context, R.raw.callphoneyinpinii);
	}

	private MediaPlayer mp = null;

	public MediaPlayer getMp() {
		return mp;
	}
}
