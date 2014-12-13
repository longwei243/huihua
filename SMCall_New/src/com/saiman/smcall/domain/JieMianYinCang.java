package com.saiman.smcall.domain;

import com.saiman.smcall.options.dial.CallPhone;

public class JieMianYinCang {
	private CallPhone callingPhoneActivity;
	private static JieMianYinCang jieMianYinCang;

	private JieMianYinCang() {
		super();
	}

	public static JieMianYinCang getJieMianYinCang() {
		if (jieMianYinCang == null) {
			jieMianYinCang = new JieMianYinCang();
		}
		return jieMianYinCang;
	}

	public CallPhone getCallingPhoneActivity() {
		return callingPhoneActivity;
	}

	public void setCallingPhoneActivity(CallPhone callingPhoneActivity) {
		this.callingPhoneActivity = callingPhoneActivity;
	}

}
