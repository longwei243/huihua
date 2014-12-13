package com.saiman.smcall.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LandedDate {
	private SharedPreferences sharedPreferences;
	private Editor edit;
	private Context context;

	public LandedDate(Context context) {
		super();
		this.context = context;
		sharedPreferences = context.getSharedPreferences("saiman",
				Context.MODE_WORLD_READABLE);
		edit = sharedPreferences.edit();
	}

	public String getUserName() {
		String name = sharedPreferences.getString("UserName", null);
		return name;
	}

	public String getPassWord() {
		String pass = sharedPreferences.getString("PassWord", null);
		return pass;
	}

	public void setUserName(String name) {
		edit.putString("UserName", name);
	}

	public void setPassWord(String num) {
		edit.putString("PassWord", num);
	}
}
